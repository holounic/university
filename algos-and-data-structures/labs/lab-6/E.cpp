#include <iostream>
#include <vector>
#include <set>
#include <algorithm>
using namespace std;
 
#define y second
#define x first
 
vector<vector<size_t> > g;
vector<size_t> components, d, t_in;
vector<pair<size_t, size_t> > edges;
vector<bool> used;
size_t t = 0;
size_t component = 0;
 
size_t get_to(size_t v, size_t e) {
    return edges[e].x == v ? edges[e].y : edges[e].x;
}
 
void punk_dfs(size_t u, size_t e_in = -1) {
    used[u] = true;
    d[u] = t++;
    t_in[u] = d[u];
    for (auto e : g[u]) {
        if (e == e_in) continue;
        size_t to = get_to(u, e);
        if (used[to]) {
            d[u] = min(d[u], t_in[to]);
        } else {
            punk_dfs(to, e);
            d[u] = min(d[u], d[to]);
        }
    }
}
 
void assign_components(size_t u, size_t cur_component, size_t p = -1) {
    used[u] = true;
    set<pair<size_t, size_t> > used_edges;
    for (auto e : g[u]) {
        if (used_edges.count(edges[e]) && !components[e]) {
            components[e] = cur_component;
            continue;
        }
        used_edges.insert(edges[e]);
        size_t to = get_to(u, e);
        if (to == p) continue;
        if (!used[to]) {
            if (d[to] >= t_in[u]) {
                components[e] = ++component;
                assign_components(to, component, u);
            } else {
                components[e] = cur_component;
                assign_components(to, cur_component, u);
            }
            continue;
        }
        if (t_in[u] > t_in[to]) 
            components[e] = cur_component;
    }
}
 
void find_components() {
    for (size_t i = 1; i < g.size(); ++i) {
        if (!used[i]) punk_dfs(i);
    }
    used.assign(g.size(), false);
    for (size_t i = 1; i < g.size(); ++i) {
        if (!used[i]) {
            assign_components(i, component);
        }
    }
    cout << component << '\n';
    for (auto c : components) {
        cout << c << " ";
    }
}
 
int main(void) {
    size_t n, m;
    cin >> n >> m;
    g.resize(n + 1);
    used.resize(n + 1);
    components.resize(m);
    d.resize(n + 1);
    t_in.resize(n + 1);
 
    for (size_t i = 0; i < m; ++i) {
        size_t u, v;
        cin >> u >> v;
        if (u > v) {
            swap(u, v);
        }
        edges.push_back(make_pair(u, v));
        g[u].push_back(i);
        g[v].push_back(i);
    }
    find_components();
}