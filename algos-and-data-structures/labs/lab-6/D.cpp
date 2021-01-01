#include <iostream>
#include <vector>
#include <set>
using namespace std;

#define x first
#define y second

vector <vector<size_t> > g;
vector <size_t> d, t_in;
vector<bool> used;
vector<pair<size_t, size_t> > edges;
vector<size_t> components;
set<size_t> bridges;
size_t t = 0;
size_t component = 0;

size_t get_to(size_t v, size_t e) {
    return edges[e].x == v ? edges[e].y : edges[e].x;
}

void punk_dfs(size_t u, size_t e_in = 0) {
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
            if (d[to] > t_in[u]) {
                bridges.insert(e);
            }
        }
    }
}

void say_hi_to_bridges() {
    for (size_t i = 1; i < g.size(); ++i) {
        if (!used[i]) {
            punk_dfs(i);
        }
    }
}

void assign_component(size_t u, size_t cur_component) {
    components[u] = cur_component;
    for (auto e : g[u]) {
        size_t v = get_to(u, e);
        if (components[v]) continue;
        if (d[v] > t_in[u]) {
            ++component;
            assign_component(v, component);
            continue;
        }
        assign_component(v, cur_component);
    }
}

void find_components() {
    say_hi_to_bridges();
    for (size_t i = 1; i < g.size(); ++i) {
        if (components[i] == 0) {
            ++component;
            assign_component(i, component);
        }
    }
    cout << component << endl;
    for (size_t i = 1; i < components.size(); ++i) {
        cout << components[i] << " ";
    }
}

int main(void) {
    size_t n, m;
    cin >> n >> m;
    g.resize(n + 1);
    d.resize(n + 1);
    t_in.resize(n + 1);
    used.resize(n + 1);
    components.resize(n + 1);
    edges.push_back(make_pair(-1, -1));

    for (size_t i = 1; i < m + 1; ++i) {
        size_t u, v;
        cin >> u >> v;
        edges.push_back(make_pair(u, v));
        g[u].push_back(i);
        g[v].push_back(i);
    }
    find_components();
}