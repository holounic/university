#include <iostream>
#include <vector>
#include <queue>
using namespace std;

vector<int> children, prufer, parent;
vector<vector<int> > g;
vector<int> used;

void cool_dfs(int u) {
    for (auto v : g[u]) {
        if (v != parent[u]) {
            parent[v] = u;
            cool_dfs(v);
        }
    }
}

void find_prufer() {
    cool_dfs(g.size() - 1);
    priority_queue<int> leafes;

    int current = 0;
    for (int i = 1; i < children.size(); ++i) {
        if (children[i] == 1) {
            leafes.push(-i);
        }
    }

    int iter = 1;
    while (iter < g.size() - 2) {
        ++iter;
        int current = -leafes.top();
        leafes.pop();
        int next_node = parent[current];
        --children[next_node];
        prufer.push_back(next_node);
        if (children[next_node] == 1) {
            leafes.push(-next_node);
        }
    }
    for (auto u : prufer) {
        cout << u << " ";
    }
}

int main(void) {
    int n;
    cin >> n;
    g.resize(n + 1);
    children.resize(n + 1);
    parent.resize(n + 1);
    used.resize(n + 1);

    for (int i = 0; i < n - 1; ++i) {
        int u, v;
        cin >> u >> v;
        g[u].push_back(v);
        g[v].push_back(u);
        children[u]++;
        children[v]++;
    }
    cerr << "EMF\n";

    find_prufer();
}