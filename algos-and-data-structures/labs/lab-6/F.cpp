#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>
#include <set>
using namespace std;

#define x first
#define y second

vector<vector<size_t> > g_dir, g_inv;
vector<bool> used;
vector<size_t> order, components;
set<pair<size_t, size_t> > comp_edges;
size_t component = 1;

void punk_dfs_inverted(size_t u) {
    used[u] = true;
    for (auto v : g_inv[u]) {
        if (!used[v]) {
            punk_dfs_inverted(v);
        }
    }
    order.push_back(u);
}

void punk_dfs_direct(size_t u) {
    components[u] = component;
    for (auto v : g_dir[u]) {
        if (!components[v]) {
            punk_dfs_direct(v);
        } else if (components[v] != components[u]) {
            comp_edges.insert(make_pair(min(components[v], components[u]), max(components[v], components[u])));
        }
    }
}

void count_components() {
    for (size_t i = 1; i < g_dir.size(); ++i) {
        if (!used[i]) punk_dfs_inverted(i);
    }
    reverse(order.begin(), order.end());
    for (auto v : order) {
        if (!components[v]) {
            punk_dfs_direct(v);
            ++component;
        }
    }
    cout << comp_edges.size() << '\n';
}

int main(void) {
    size_t n, m;
    cin >> n >> m;

    g_dir.resize(n + 1);
    g_inv.resize(n + 1);
    used.resize(n + 1);
    components.resize(n + 1);

    for (size_t i = 0; i < m; ++i) {
        size_t u, v;
        cin >> u >> v;
        g_dir[u].push_back(v);
        g_inv[v].push_back(u);
    }

    count_components();
}