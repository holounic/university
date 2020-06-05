#include <iostream>
#include <vector>
using namespace std;

struct node
{
    node* left;
    node* right;
    node* parent;
    int size;
    bool set, my_set;
    node() : left(nullptr), right(nullptr), parent(nullptr), size(1), set(false), my_set(false) {};
};
vector <node *> ptrs;
vector <vector<int> > g;
vector <bool> used;
int n;

bool is_root(node * x)
{
    return x && ((*x).parent == nullptr || ((*(*x).parent).left != x && (*(*x).parent).right != x));
}

void update(node* x)
{
    if (x)
    {
        (*x).size = 1;
        (*x).set = false;
        if ((*x).left)
        {
            (*x).size += (*(*x).left).size;
        }
        if ((*x).right)
        {
            (*x).size += (*(*x).right).size;
        }
    }
}

void set_parent(node * x, node * p)
{
    if (x) (*x).parent = p;
}

void update_parent(node* x)
{
    set_parent((*x).left, x);
    set_parent((*x).right, x);
}

void set (node * x)
{
    (*x).set = ((*x).my_set = true);
}

void push(node* x)
{
    if ((*x).set)
    {
        (*x).set = false;
        (*x).my_set = true;
        if ((*x).left)
        {
            set((*x).left);
        }
        if ((*x).right)
        {
            set((*x).right);
        }
    }
}

void rotate(node* x)
{
    auto p = (*x).parent;
    auto g = (*p).parent;
    if (g) push(g);
    push(p);
    push(x);
    if (!is_root(p))
    {
        if ((*g).left == p) (*g).left = x;
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
    set_parent(x, g);
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
    rotate(((*p).left == x) == ((*g).left == p) ? p : x);
    rotate(x);
    return splay(x);
}

node* expose(node* x)
{
    node* last = nullptr;
    for (auto y = x; y; y = (*y).parent)
    {
        splay(y), (*y).left = last, update(y), last = y;
    }
    splay(x);
    return last;
}

void link(node* x, node* y)
{
    expose(x);
    (*x).parent = y;
}

node * LCA(node * x, node * y)
{
    expose(x);
    return expose(y);
}

void build(node * x, node * y)
{
    node* lca = LCA(x, y);
    if (lca != y && lca != x)
    {
        splay(x), set(x), splay(lca), set((*lca).left);
        return;
    }
    node *z = (x == lca) ? y : x;
    expose(z);
    splay(lca);
    set((*lca).left);
}

void not_dfs_at_all(int u)
{
    used[u] = true;
    for (int v : g[u])
    {
        if (!used[v])
        {
            link(ptrs[v], ptrs[u]);
            not_dfs_at_all(v);
        }
    }
}

void push_dfs(node* u)
{
    if (!u || !(*u).set)
        return;
    push(u);
    push_dfs((*u).left);
    push_dfs((*u).right);
}

int main(void)
{
    ios_base::sync_with_stdio(0);
    cin.tie(0), cout.tie(0);
    cin >> n;
    ptrs.resize(n), g.resize(n), used.resize(n);
    for (int i = 0; i < n; ++i) ptrs[i] = new node();

    for (int i = 0; i < n - 1; ++i)
    {
        int u, v;
        cin >> u >> v;
        g[u - 1].push_back(v - 1);
        g[v - 1].push_back(u - 1);
    }

    not_dfs_at_all(0);

    int m;
    cin >> m;
    for (int i = 0; i < m; i++)
    {
        int u, v;
        cin >> u >> v;
        build(ptrs[u - 1], ptrs[v - 1]);
    }

    for (int i = 0; i < n; i++) push_dfs(ptrs[i]);

    int res = 0;
    for (int i = 1; i < n; i++) {
        if (!(*ptrs[i]).my_set) ++res;
    }

    cout << res << '\n';
}