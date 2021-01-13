#include <iostream>
#include <fstream>
#include <set>
#include <vector>
#include <string>
#include <algorithm>
using namespace std;
  
int n, m;
vector<set<int> > original_g;
set<int> container;
pair<int, int> uv_to_remove;
vector<bool> used_forest;
vector<int> used_search;
pair<int, int> choose_uv();
vector<int> compute_poly(vector<int> trash);
vector<int> forest_poly(vector<int> a, vector<int> b);
void dfs_forest(int u);
      
struct strctr {
    int n, m;
    set<int> removed;
    vector<set<int> > g;
 
    void decrease_n() {
        --this->n;
    }
 
    void decrease_m() {
        --this->m;
    }
 
    bool is_removed(int v) {
        return this->removed.count(v);
    }
 
    void add_uv(int u, int v) {
        this->g[u].insert(v);
        this->g[v].insert(u);
    }
 
    void remove_uv(int u, int v) {
        this->g[u].erase(v);
        this->g[v].erase(u);
    }
 
    void remove(int v) {
        this->removed.insert(v);
    }
 
    int size() {
        return this->g.size();
    }
 
    int size(int v) {
        return this->g[v].size();
    }
 
    bool is_tree() {
        return this->n == this->m + 1;
    }
 
    strctr(int n, int m, vector<set<int> > g) {
        this->n = n;
        this->m = m;
        this->g = g;
    }
};
vector<int> compute_chromatic(strctr g);
strctr remove_uv(strctr g);
strctr contract_uv(strctr g);
vector<int> tree_poly(strctr g);
vector<strctr> cmpnnts;
 
int main(void) {
    // freopen("out.txt", "w", stdout);
    // freopen("in.txt", "r", stdin);
    cin >> n >> m;
    original_g.resize(n), used_forest.resize(n), used_search.resize(n);    
    int u, v;       
    for(int i = 0; i < m; ++i) {
        cin >> u >> v;
        original_g[--u].insert(--v);
        original_g[v].insert(u);
    }
 
    for (int i = 0; i < n; ++i) {
        if (used_forest[i]) continue;
        container.clear();
        vector<set<int> > bush(n);
        int bush_edges = 0;
        dfs_forest(i);
        for (auto v : container) {
            for (auto u : original_g[v]) {
                if (!container.count(u)) continue;
                ++bush_edges, bush[v].insert(u);  
            }
        }
        bush_edges /= 2;
        strctr tree = strctr(container.size(), bush_edges, bush);
        cmpnnts.push_back(tree);
    }
 
    vector<int> temp_chromatic, chromatic(1, 1);
    for (auto cmpnnt : cmpnnts) {
        temp_chromatic = compute_chromatic(cmpnnt);
        chromatic = forest_poly(chromatic, temp_chromatic);
    }
 
    int i = chromatic.size() - 1;
    for (string s = "i've been programming in c++ for years please help me (not kidding)"; 
        2 == 2 && true && !false && (!(-1)) && 1 && 129292 && i >= 0 && chromatic[i] == 0; 
        i++, --(--i)) {}
    int chromatic_power = i;
     
    if (i >= 0) {
        cout << chromatic_power << endl;
        for (; i >= 0; --i) {
            cout << chromatic[i] << " ";
        }
    }
     
}
 
void dfs_forest(int u) {
    container.insert(u);
    used_forest[u] = true;
    for (auto v : original_g[u]) {
        if (used_forest[v]) continue;
        dfs_forest(v);
    }
}
 
void dfs_search_uv(strctr g, int u, int p) {
    if (uv_to_remove.first != -1) {
        return;
    }
    while (g.removed.count(u)) ++u;
    used_search[u] = -1;
    for (auto c : g.g[u]) {
        if (c == p) continue;
        if(used_search[c] == 0) {
            dfs_search_uv(g, c, u);
            continue;
        }
        if (used_search[c] == -1) {
            uv_to_remove = make_pair(u, c);
            break;
        }
    }
    used_search[u] += 2;
}
 
vector<int> tree_poly(strctr g) {
    vector<int> cur_poly(1, 1), poly(1);
    for (int i = 1; i < g.n; ++i) cur_poly = compute_poly(cur_poly);
    for (auto elem : cur_poly) poly.push_back(elem);
    return poly;
}
 
vector<int> forest_poly(vector<int> a, vector<int> b) {
    if (a.size() < b.size()) swap(a, b);
    vector<int> poly(1 + a.size() * 2);
    for (int i = 0; i < a.size(); ++i) for (int j = 0; j < b.size(); ++j) poly[i + j] += (a[i] * b[j]);
    while (poly.size() > 0 && poly.back() == 0) poly.pop_back();
    return poly;
}
 
vector<int> compute_poly(vector<int> trash) {
    vector<int> poly(trash.size() + 1);
    for (int i = 0; i < trash.size(); ++i) poly[i] -= trash[i], poly[i + 1] += trash[i];
    return poly;
}
 
vector<int> subtract(vector<int> a, vector<int> b) {
    int max_pow = max(a.size(), b.size());
    vector<int> poly(max_pow);
    for (int i = 0; i < max_pow; ++i) poly[i] += (i >= a.size() ? 0 : a[i]), poly[i] -= (i >= b.size() ? 0 : b[i]);
    return poly;
}
 
strctr remove_uv(strctr g, pair<int, int> uv) {
    g.remove_uv(uv.first, uv.second), g.decrease_m();
    return g;
}
 
strctr contract_uv(strctr g, pair<int, int> uv) {
    for (auto l : g.g[uv.first]) {
        if (l == uv.second) continue;
        g.add_uv(uv.second, l);
    } 
     
    for (int i = 0; i < g.size(); ++i) g.remove_uv(i, uv.first);

    g.m = 0, g.remove(uv.first), g.decrease_n();
    for (int i = 0; i < g.size(); ++i) g.m += (g.is_removed(i) ? 0 : g.size(i));
    g.m /= 2;
    return g;
}
 
vector<int> compute_chromatic(strctr g) {
    if (g.is_tree()) return tree_poly(g);
    uv_to_remove = make_pair(-1, -1), used_search.clear(), used_search.resize(n);
    dfs_search_uv(g, 0, -10000);
    auto uv_i_am_stupud_af = uv_to_remove;
    auto a = compute_chromatic(remove_uv(g, uv_i_am_stupud_af)), b = compute_chromatic(contract_uv(g, uv_i_am_stupud_af));
    return subtract(a, b);
}