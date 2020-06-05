#include <iostream>
#include <vector>
#include <set>
using namespace std;

#define ll long long

struct centre_data
{
    int color, w_size, b_size;
    ll w_sum_cur,  w_sum_next, b_sum_cur, b_sum_next;
};
vector <set<int> > g;
vector<int> sub_size, parent, d;
vector<vector<int> > dp;
vector<centre_data> inf;
int n, all_size, log_n;

int lca(int u, int v)
{
    if (d[v] > d[u])
        swap(u, v);

    for (int i = log_n; i >= 0; --i)
        if (d[u] - (1 << i) >= d[v]) u = dp[u][i];

    if (u == v) return u;

    for (int i = log_n; i >= 0; i--)
    {
        if (dp[v][i] != dp[u][i])
        {
            v = dp[v][i];
            u = dp[u][i];
        }
    }
    return dp[v][0];
}

ll distance(int u, int v)
{
    return d[u] + d[v] - 2 * d[lca(u, v)];
}

void fill(int u, int p)
{
    dp[u].push_back(p);
    for (int v : g[u])
    {
        if (v != p)
        {
            d[v] = 1 + d[u];
            fill(v, u);
        }
    }
}

void calculate()
{
    fill(0, 0);
    log_n = 0;
    for (int j = 1; 1 << (j - 1) < n; j++)
    {
        for (int i = 0; i < n; i++)
            dp[i].push_back(dp[dp[i][j - 1]][j - 1]);
        log_n++;
    }
}

int resize(int u, int p)
{
    sub_size[u] = 1;
    for (int v : g[u])
        if (v != p)
            sub_size[u] += resize(v, u);
    return sub_size[u];å
}

int calculate_centroid(int u, int p)
{
    for (int v : g[u])
        if (v != p && sub_size[v] > all_size / 2)
            return calculate_centroid(v, u);
    return u;
}

void decompose(int u, int p)
{
    resize(u, -1);
    all_size = sub_size[u];
    int centroid = calculate_centroid(u, -1);
    parent[centroid] = p;
    for (int v : g[centroid])
    {
        g[v].erase(centroid);
        decompose(v, centroid);
    }
    g[centroid].clear();
}

void paint()
{
    for (int i = 0; i < n; i++)
    {
        inf[i].color = 1;
        int v = i;
        while (true)
        {
            inf[v].b_size += 1;
            inf[v].b_sum_cur += distance(i, v);
            if (parent[v] == -1)
                break;
            inf[v].b_sum_next += distance(i, parent[v]);
            v = parent[v];
        }
    }
}

void change_color(int u)
{
    int C = inf[u].color ? 1 : -1;
    inf[u].color = 1 - inf[u].color;
    int v = u;
    while (true)
    {
        inf[v].b_size -= C;
        inf[v].b_sum_cur -= distance(u, v) * C;
        inf[v].w_size += C;
        inf[v].w_sum_cur += distance(u, v) * C;
        if (parent[v] == -1)
            break;
        inf[v].b_sum_next -= distance(u, parent[v]) * C;
        inf[v].w_sum_next += distance(u, parent[v]) * C;
        v = parent[v];
    }
}

ll query(int u)
{
    int color = inf[u].color;
    ll ans = color ? inf[u].b_sum_cur : inf[u].w_sum_cur;

    if (parent[u] == -1)
        return ans;

    int prev = u;
    int v = parent[u];
    while (true)
    {
        if (color)
            ans += inf[v].b_sum_cur - inf[prev].b_sum_next +
                    distance(u, v) * (inf[v].b_size - inf[prev].b_size);
        else
            ans += inf[v].w_sum_cur - inf[prev].w_sum_next
                    + distance(u, v) * (inf[v].w_size - inf[prev].w_size);
        if (parent[v] == -1)
            break;
        prev = v;
        v = parent[v];
    }

    return ans;
}

int main()
{
    ios_base::sync_with_stdio(0);
    cin.tie(0), cout.tie(0);

    int m;
    cin >> n >> m;
    g.resize(n);
    parent.resize(n);
    sub_size.resize(n);
    d.resize(n);
    dp.resize(n);
    inf.resize(n);

    for (int i = 0; i < n - 1; i++)
    {
        int u, v;
        cin >> u >> v;
        g[u - 1].insert(v - 1);
        g[v - 1].insert(u - 1);
    }

    calculate();
    decompose(0, -1);
    paint();

    for (int i = 0; i < m; i++)
    {
        int q, u;
        cin >> q >> u;
        if (q == 1)
            change_color(u - 1);
        else
            cout << query(u - 1) << '\n';
    }
}