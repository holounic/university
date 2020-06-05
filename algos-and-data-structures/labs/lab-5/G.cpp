#include <iostream>
#include <vector>
using namespace std;

#define ll long long

struct node
{
    node* left;
    node* right;
    node* parent;
    int size;
    ll d;
    ll my_d;
    node() : left(nullptr), right(nullptr), parent(nullptr), size(1), d(0), my_d(0) {};
};
vector<node*> ptr;
vector<vector<int>> edge;
vector<bool> used;

void init_family(node* x, node* p)
{
    if (!x) return;
    (*x).parent = p;
}

void upd_family(node* x)
{
    init_family((*x).left, x);
    init_family((*x).right, x);
}

void add(node* x, ll d)
{
    if (!x) return;
    (*x).d += d;
    (*x).my_d += d;
}

void update(node* x)
{
    if (x)
    {
        (*x).size = 1 ;
        if ((*x).left)
            (*x).size += (*(*x).left).size;
        if ((*x).right)
            (*x).size += (*(*x).right).size;
        (*x).d = 0;
    }
}

void push(node* x)
{
    if (!(*x).d) return;
    add((*x).left, (*x).d);
    add((*x).right, (*x).d);
    (*x).d = 0;
}

bool is_root(node* x)
{
    return x && ((*x).parent == nullptr || ((*(*x).parent).left != x && (*(*x).parent).right != x));
}

void rotate(node* x)
{
    auto p = (*x).parent, g = (*p).parent;
    if (g) push(g);
    push(p);
    push(x);
    if (!is_root(p))
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
    upd_family(x);
    upd_family(p);
    init_family(x, g);
    update(p);
    update(x);
    update(g);
}


node* splay(node* x)
{
    if (is_root(x))
    {
        push(x);
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

node* get_lca(node* x, node* y)
{
    expose(x);
    return expose(y);
}

void build(int u, int v, unsigned ll d)
{
    node* x = ptr[u];
    node* y = ptr[v];
    node* lca = get_lca(x, y);
    if (lca != x && lca != y)
    {
        splay(x);
        add(x, d);
    } else
    {
        expose((x == lca) ? y : x);
    }
    splay(lca);
    add((*lca).left, d);
    (*lca).my_d += d;
}

void dfs(int u)
{
    used[u] = true;
    for (int v : edge[u])
    {
        if (!used[v])
        {
            link(ptr[v], ptr[u]);
            dfs(v);
        }
    }
}

ll query(int x)
{
    splay(ptr[x]);
    return (*ptr[x]).my_d;
}

int main()
{
    ios_base::sync_with_stdio(0);
    cin.tie(0), cout.tie(0);

    int n;
    cin >> n;
    ptr.resize(n), edge.resize(n), used.resize(n);

    for (int i = 0; i < n; i++)
        ptr[i] = new node();

    for (int i = 0; i < n - 1; ++i)
    {
        int u, v;
        cin >> u >> v;
        edge[u - 1].push_back(v - 1);
        edge[v - 1].push_back(u - 1);
    }
    dfs(0);

    int m;
    cin >> m;
    for (int i = 0; i < m; ++i)
    {
        char c;
        cin >> c;
        if (c == '+')
        {
            int u, v;
            ll d;
            cin >> u >> v >> d;
            build(u - 1, v - 1, d);
        } else if (c == '?')
        {
            int u;
            cin >> u;
            cout << query(u - 1) << '\n';
        }
    }
    return 0;
}