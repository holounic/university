#include <iostream>
#include <vector>

using namespace std;

size_t log_n;
vector <int> d;
vector <vector<int> > dp, min_path;

int get_min(int u, int v)
{
    if (d[v] > d[u])
        swap(u, v);

    int res = INT_MAX;

    for (int i = log_n; i >= 0; i--)
    {
        if (d[u] - (1 << i) >= d[v])
        {
            res = min(res, min_path[u][i]);
            u = dp[u][i];
        }
    }

    if (u == v) return res;

    for (int i = log_n; i >= 0; i--)
    {
        if (dp[v][i] != dp[u][i])
        {
            res = min(min(min_path[v][i], min_path[u][i]), res);
            v = dp[v][i];
            u = dp[u][i];
        }
    }

    return min(min(min_path[v][0], min_path[u][0]), res);
}

int main(void)
{
    ios_base::sync_with_stdio(0);
    cin.tie(0), cout.tie(0);

    freopen("minonpath.in", "r", stdin);
    freopen("minonpath.out", "w", stdout);

    int n;
    cin >> n;
    d.resize(n), dp.resize(n), min_path.resize(n);

    log_n = 0;
    for (int j = 1; 1 << (j - 1) <= n; ++j)
        log_n++;
    for (int i = 0; i < n; i++)
    {
        min_path[i].resize(log_n + 1);
        dp[i].resize(log_n + 1);
    }

    dp[0][0] = 0;
    min_path[0][0] = INT_MAX;

    for (int i = 1; i < n; i++)
    {
        int x, c;
        cin >> x >> c;
        dp[i][0] = x - 1;
        d[i] = d[x - 1] + 1;
        min_path[i][0] = c;
    }

    for (int j = 1; 1 << (j - 1) < n; j++)
    {
        for (int i = 0; i < n; i++)
        {
            dp[i][j] = dp[dp[i][j - 1]][j - 1];
            min_path[i][j] = min(min_path[i][j - 1], min_path[dp[i][j - 1]][j - 1]);
        }
    }

    int m, u, v;
    cin >> m;
    for (int i = 0; i < m; i++)
    {
        cin >> u >> v;
        cout << get_min(u - 1, v - 1) << '\n';
    }

}