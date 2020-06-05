#include <iostream>
#include <vector>
using namespace std;

int cnt = 1, log_m = 0, m;
vector<int> d, p;
vector <vector <int> > dp;

void init(int v)
{
    p[cnt] = cnt;
    dp[cnt][0] = v;
    d[cnt] = 1 + d[v];

    for (int j = 1; 1 << (j - 1) < m; j++) dp[cnt][j] = dp[dp[cnt][j - 1]][j - 1];
    ++cnt;
}

int info(int x)
{
    if (p[x] != x) p[x] = info(p[x]);
    return p[x];
}

void join(int x, int y)
{
    int u = info(x), v = info(y);

    if (u != v) p[u] = v;
}

int lca(int u, int v)
{
    if (d[v] > d[u]) swap(u, v);

    for (int i = log_m; i >= 0; --i) {
        if (d[u] - (1 << i) >= d[v]) u = dp[u][i];      
    }
        
    if (u == v) return u;

    for (int i = log_m; i >= 0; i--) {
        if (dp[v][i] != dp[u][i]) {
            v = dp[v][i];
            u = dp[u][i];
        }
    }

    return dp[u][0];
}

int main()
{
    ios_base::sync_with_stdio(0);
    cin.tie(0);
    cout.tie(0);

    cin >> m;
    d.resize(m);
    dp.resize(m);
    p.resize(m);

    for (int j = 1; 1 << (j - 1) < m; j++)
        log_m++;

    for (int i = 0; i < m; i++)
        dp[i].resize(log_m + 1);

    for (int i = 0; i < m; i++)
    {
        char c;
        cin >> c;
        switch(c) {
            case '+':
                int v;
                cin >> v;
                init(v - 1);
                break;
            case '-':
                cin >> v;
                join(v - 1, dp[v - 1][0]);
                break;
            case '?':
                int u;
                cin >> u >> v;
                cout << info(lca(u - 1, v - 1)) + 1 << '\n';
        }
    }
    return 0;
}