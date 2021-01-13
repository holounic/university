#include <iostream>
#include <vector>
#include <deque>
#include <set>
#include <algorithm>
using namespace std;
  
vector<set<int> > g;
vector<bool> used;
vector<int> path;
  
void dfs(int u) {
    used[u] = true;
    for (auto v : g[u]) {
        if (!used[v]) dfs(v);
    }
    path.push_back(u);
}
  
int main(void) {
    int n;
    cin >> n;
    g.resize(n + 1);
    used.resize(n + 1);
  
    for (int i = 2; i < n + 1; ++i) {
        string s;
        cin >> s;
        for (int j = 0; j < s.size(); ++j) {
            if (s[j] == '1') {
                g[i].insert(j + 1);
            } else {
                g[j + 1].insert(i);
            }
        }
    }
  
    for (int i = 1; i < n + 1; ++i) {
        dfs(i);
        reverse(path.begin(), path.end());
        if (g[path[path.size() - 1]].count(path[0])) {
            for (auto v : path) {
                cout << v << " ";
            }
            return 0;
        }
        used.assign(n + 1, false);
        path.clear();
    }
      
}