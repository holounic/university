#include <iostream>
#include <vector>
#include <set>
using namespace std;

vector<vector<size_t> > g;
vector<bool> used;
vector<size_t> t_in, d;
set<size_t> cutpoints;
size_t t = 0;

void punk_dfs(size_t u, size_t p = 0) {
    used[u] = true;
    d[u] = (t_in[u] = t++);
    size_t children = 0;
    for (auto v : g[u]) {
        if (v == p) continue;
        if (used[v]) {
            d[u] = min(d[u], t_in[v]);
            continue;
        }
        punk_dfs(v, u);
        ++children;
        d[u] = min(d[u], d[v]);
        if (p != 0 && d[v] >= t_in[u]) {
            cutpoints.insert(u);
        }
    }
    if (children > 1 && p == 0) {
        cutpoints.insert(u);
    }
}

void say_hi_to_cutpoints() {
    for (size_t i = 1; i < g.size(); ++i) {
        if (!used[i]) punk_dfs(i);
    }
    cout << cutpoints.size() << endl;
    for (auto u : cutpoints) {
        cout << u << " ";
    }
}

int main(void) {
    size_t n, m;
    cin >> n >> m;
    g.resize(n + 1);
    used.resize(n + 1);
    t_in.resize(n + 1);
    d.resize(n + 1);
    for (size_t i = 0; i < m; ++i) {
        size_t u, v;
        cin >> u >> v;
        g[u].push_back(v);
        g[v].push_back(u);
    }
    say_hi_to_cutpoints();
}