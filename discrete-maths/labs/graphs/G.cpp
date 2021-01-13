#include <iostream>
#include <vector>
#include <algorithm>
#include <queue>
#include <set>
using namespace std;

vector<vector<int> > g;
vector<int> colours;
int num_colours = 0;

void dfs(int u = 1) {
    set<int> occupied;
    for (auto v : g[u]) {
        if (colours[v]) {
            occupied.insert(colours[v]);
        }
    }

    int min_available = 1;
    for (auto c : occupied) {
        if (min_available != c) {
            break;
        }
        ++min_available;
    }
    colours[u] = min_available;

    for (auto v : g[u]) {
        if (!colours[v]) {
            dfs(v);
        }
    }
}


int main(void) {
    int n, m;
    cin >> n >> m;

    g.resize(n + 1);
    colours.resize(n + 1);

    int max_deg = 0;

    for (int i = 0; i < m; ++i) {
        int u, v;
        cin >> u >> v;
        g[u].push_back(v);
        g[v].push_back(u);
        max_deg = max(max((int) g[u].size(), (int) g[v].size()), max_deg);
    }

    num_colours = (max_deg % 2 ? max_deg : max_deg + 1);
    dfs();

    cout << num_colours << endl;
    for (int i = 1; i < n + 1; ++i) {
        cout << colours[i] << endl;
    }
}