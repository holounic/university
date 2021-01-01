#include <iostream>
#include <vector>
using namespace std;

vector<vector<size_t> > g;
vector<size_t> order;
vector<size_t> colour;

void dfs(size_t u) {
    if (colour[u] == 2) return;
    if (colour[u] == 1) {
        cout << -1;
        exit(0);
    }
    colour[u] = 1;
    for (auto v : g[u]) {
        dfs(v);
    }
    colour[u] = 2;
    order.push_back(u);
}

void topsort() {
    for (size_t i = 1; i < g.size(); ++i) {
        dfs(i);
    }
    auto ptr = --order.end();
    auto prt_end = --order.begin();

    while (ptr != prt_end) {
        cout << *ptr << " ";
        --ptr;
    }
}

int main(void) {
    size_t n, m;
    cin >> n >> m;
    g.resize(n + 1);
    colour.resize(n + 1);

    for (size_t i = 0; i < m; ++i) {
        size_t u, v;
        cin >> u >> v;
        g[u].push_back(v); 
    }
    topsort();
}