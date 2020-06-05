#include <iostream>
#include <vector>
#include <map>
using namespace std;

struct node
{
    node* left;
    node* right;
    node* parent;
    pair<int, int> edge;
    int size;
    node(int u, int z) : left(nullptr), right(nullptr), parent(nullptr), edge({u, z}), size(1) {};
};

map<pair<int, int>, node*> edge;
vector<node*> ptr;

int size(node* t)
{
    return t ? (*t).size : 0;
}

void update_size(node* t)
{
    if (!t) return;
    (*t).size = size((*t).left) + size((*t).right) + 1;
}

void make_family(node* x, node* p)
{
    if (!x) return;
    (*x).parent = p;
}

void update_parent(node * x)
{
    make_family((*x).left, x);
    make_family((*x).right, x);
}

void move_to_root(node* x)
{
    auto p = (*x).parent, g = (*p).parent;
    if (g)
    {
        if ((*g).left == p)
            (*g).left = x;
        else
            (*g).right = x;
    }
    if ((*p).left == x)
    {
        (*p).left = (*x).right;
        (*x).right = p;
    } else
    {
        (*p).right = (*x).left;
        (*x).left = p;
    }
    update_parent(x);
    update_parent(p);
    make_family(x, g);

    update_size(p);
    update_size(x);
    update_size(g);
}

node * splay(node* x)
{
    if (!(*x).parent) return x;
    auto p = (*x).parent, g = (*p).parent;
    if (!g)
    {
        move_to_root(x);
        return x;
    }
    move_to_root(((*p).left == x) == ((*g).left == p)   ?   p   :   x);
    move_to_root(x);
    return splay(x);
}


pair<node*, node*> split(node * t, int k)
{
    if(!t) return {nullptr, nullptr};
    if (size((*t).left) >= k)
    {
        auto [first, second] = split((*t).left, k);
        (*t).left = second;
        make_family(second, t);
        make_family(first, nullptr);
        update_size(t);
        return {first, t};
    }
    auto [first, second] = split((*t).right, k - size((*t).left) - 1);
    (*t).right = first;
    make_family(first, t);
    make_family(second, nullptr);
    update_size(t);
    return {t, second};
}

int get_index(node* z)
{
    if (!z) return 0;
    int ans = size((*z).left);
    while ((*z).parent)
    {
        if (z == (*(*z).parent).right) ans += size((*(*z).parent).left) + 1;
        z = (*z).parent;
    }
    return ans;
}

node* merge(node* a, node* b)
{
    if (!a) return b;
    if (!b) return a;
    splay(a);
    while ((*a).right) a = (*a).right;
    splay(a);
    (*a).right = b;
    make_family(b, a);
    update_size(a);
    return a;
}

node* get_root(node* z)
{
    if (!z) return z;
    while ((*z).parent) z = (*z).parent;
    return z;
}

node* head(node* z)
{
    if (!z) return z;
    while ((*z).left) z = (*z).left;
    return z;
}

node* tail(node* z)
{
    if (!z) return z;
    while ((*z).right) z = (*z).right;
    return z;
}

void cut(int i, int j)
{
    auto uv = edge[{i, j}];
    auto vu = edge[{j, i}];
    edge.erase({i, j}), edge.erase({j, i});

    if (get_index(vu) < get_index(uv)) swap(uv, vu);

    auto [A, BC] = split(get_root(uv), get_index(uv));
    node* au = tail(A);
    BC = split(BC, 1).second;

    auto [B, C] = split(BC, get_index(vu));
    node* vc = head(B);
    node* cv = (vc) ? edge[{(*vc).edge.second, (*vc).edge.first}] : nullptr;
    node* dv = tail(B);
    C = split(C, 1).second;
    node* ub = head(C);
    node* bu = (ub) ? edge[{(*ub).edge.second, (*ub).edge.first}] : nullptr;

    ptr[(*uv).edge.second] = dv ? dv : cv;
    ptr[(*uv).edge.first] = au ? au : bu;

    auto result = merge(A, C);
}


void link(int i, int j)
{
    node* u = ptr[i];
    node* z = ptr[j];

    auto [A, D] = split(get_root(u), get_index(u) + 1);
    auto [C, B] = split(get_root(z), get_index(z) + 1);
    auto uv = new node(i, j);
    auto vu = new node(j, i);

    if (!z) ptr[j] = uv;
    if (!u) ptr[i] = vu;

    edge.insert({{i, j}, uv}), edge.insert({{j, i}, vu});

    auto Auv = merge(A, uv), vuD = merge(vu, D), AuvB = merge(Auv, B), CvuD = merge(C, vuD);
    merge(AuvB, CvuD);
}

int get_size(int i)
{
    node* u = ptr[i];
    node* root = get_root(u);
    return size(root) / 2 + 1;
}

int main(void)
{
    ios_base::sync_with_stdio(0);
    cin.tie(0), cout.tie(0);
    int n, m;
    cin >> n >> m;
    ptr.resize(n);
    for (int i = 0; i < n; ++i) ptr[i] = nullptr;

    for (int i = 0; i < m; ++i)
    {
        string s;
        cin >> s;
        int u, v;
        cin >> u;
        if (s == "link")
        {
            cin >> v;
            link(u - 1, v - 1);
            continue;
        }
        if (s == "cut")
        {
            cin >> v;
            cut(u - 1, v - 1);
            continue;
        }
        if (s == "size")
        {
            cout << get_size(u - 1) << '\n';
        }
    }
}