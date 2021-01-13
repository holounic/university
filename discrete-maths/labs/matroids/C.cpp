#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

vector<vector<int> > g;
vector<int> m1, m2, used;

bool compuct(int v);

int main(void) {
    freopen("matching.in", "r", stdin);
    int n, k, v, c;
    cin >> n;

    g.resize(n), m1.assign(n, -1);
    vector<pair<int, int> > w;
    for (int i = 0; i < n; ++i) {
        cin >> c;
        w.push_back(make_pair(c, i));
    }

    sort(w.begin(), w.end());
    reverse(w.begin(), w.end());

    for (int u = 0; u < n; ++u) {
        cin >> k;
        for (int j = 0; j < k; ++j) {
            cin >> v;
            g[u].push_back(--v);
        }
    }
    
    for (auto e : w) {
        used.assign(n, 0);
        compuct(e.second);
    }
    m2.assign(n, -1);
    for (int i = 0; i < n; ++i) m2[m1[i]] = i;

    freopen("matching.out", "w", stdout);
    for (auto e : m2) cout << 1 + e << " ";
}

bool compuct(int v) {
    if (used[v]) return false;
    used[v] = 1;
    for (auto u : g[v]) {
        if (m1[u] == -1 || compuct(m1[u])) {
            m1[u] = v;
            return true;
        }
    }
    return false;
}