#include <iostream>
#include <vector>

using namespace std;
int log_n;
vector <int> d;
vector <vector <int> > dp;

int lca(int u, int v) {
    if (d[v] > d[u]) {
        swap(u, v);
    }
        
    for (int i = log_n; i >= 0; --i) {
        if (d[u] - (1 << i) >= d[v]) {
            u = dp[u][i];
        }
    }

    if (u == v) {
        return u;
    }

    for (int i = log_n; i >= 0; i--) {
        if (dp[v][i] != dp[u][i]) {
            v = dp[v][i];
            u = dp[u][i];
        }
    }
    return dp[v][0];
}

int main(void) {
    int n;
    cin >> n;
    d.resize(n), dp.resize(n);
    dp[0].push_back(0);

    for (int i = 1; i < n; ++i) {
        int a;
        cin >> a;
        dp[i].push_back(a - 1);
        d[i] = d[a - 1] + 1;
    }

    log_n = 0;
    for (int j = 1; 1 << (j - 1) < n; ++j) {
        for (int i = 0; i < n; ++i) {
            dp[i].push_back(dp[dp[i][j - 1]][j - 1]);
        }
        log_n++;
    }

    int m;
    cin >> m;
    for (int i = 0; i < m; ++i) {
        int u, v;
        cin >> u >> v;
        cout << lca(u - 1, v - 1) + 1 << '\n';
    }
}