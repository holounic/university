#include <iostream>
#include <vector>
using namespace std;

struct node
{
    node* left;
    node* right;
    node* parent;
    size_t size;
    node() : left(nullptr), right(nullptr), parent(nullptr), size(1) {};
};

vector<node*> ptr;
vector<size_t> groups;
int k;

void create_family(node* x, node* p)
{
    if (!x) return;
    (*x).parent = p;
}

void update_parent(node* x)
{
    create_family((*x).left, x);
    create_family((*x).right, x);
}

int size(node* x)
{
    return x ? (*x).size : 0;
}

void update(node * x)
{ 
    if (!x) return;
    (*x).size = size((*x).left) + size((*x).right) + 1;
}

bool is_root(node* x)
{
    return x && ((*x).parent == nullptr || ((*(*x).parent).left != x && (*(*x).parent).right != x));
}

void rotate(node* x)
{
    auto p = (*x).parent, g = (*p).parent;
    if (!is_root(p))
    {
        if ((*g).left == p)
            (*g).left = x;
        else (*g).right = x;
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
    create_family(x, g);
    update(p);
    update(x);
    update(g);
}


node * splay(node * x)
{
    if (is_root(x))
    {
        update(x);
        return x;
    }
    auto p = (*x).parent, g = (*p).parent;
    if (is_root(p))
    {
        rotate(x);
        return x;
    }
    rotate(((*p).left == x) == ((*g).left == p)  ?  p   :   x    );
    rotate(x);
    return splay(x);
}

node * get_splay_root(node* x)
{
    while (x && (*x).parent && ((*(*x).parent).left == x || (*(*x).parent).right == x))
        x = (*x).parent;
    return x;
}

node* expose(node* x)
{
    node* last = nullptr;
    for (auto y = x; y; y = (*y).parent)
    {
        splay(y);
        (*y).left = last;
        update(y);
        last = y;
    }
    splay(x);
    return last;
}

void link(node* x, node* y)
{
    expose(x);
    (*x).parent = y;
}

int get_index(node * v)
{
    if (!v) return 0;
    int ans = size((*v).left);

    while (!is_root(v))
    {
        if (v == (*(*v).parent).right)
            ans += size((*(*v).parent).left) + 1;
        v = (*v).parent;
    }
    return ans;
}

long long chiflexxxx()
{
    long long ans = 0;
    for (int i = 0; i < k; ++i) expose(ptr[groups[i]]);

    for (int i = 0; i < k; ++i)
        if (get_index(ptr[groups[i]]) == 0) ans += get_splay_root(ptr[groups[i]])->size;

    return ans;
}

int main(void)
{
    ios_base::sync_with_stdio(0);
    cin.tie(0), cout.tie(0);

    int n, g;
    cin >> n;
    ptr.resize(n), groups.resize(n);
    for (int i = 0; i < n; ++i)
        ptr[i] = new node();

    for (int i = 0; i < n; ++i)
    {
        int p;
        cin >> p;
        if (p == -1) continue;
        link(ptr[i], ptr[p - 1]);
    }

    cin >> g;
    for (int i = 0; i < g; ++i)
    {
        cin >> k;
        for (int j = 0; j < k; ++j)
        {
            int p;
            cin >> p;
            groups[j] = p - 1;
        }
        cout << chiflexxxx() << '\n';
        groups.clear();
    }

    return 0;
}