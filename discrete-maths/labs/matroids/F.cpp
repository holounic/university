#include <iostream>
#include <vector>
#include <queue>
#include <set>
using namespace std;

struct dsu {
    vector<int> parents, rank;

    dsu(int n) {
        rank.resize(n);
        for (int i = 0; i < n; ++i) {
            parents.push_back(i);
        }
    }

    int get_set(int v) {
        return (v == parents[v] ? parents[v] : parents[v] = get_set(parents[v]));
    }

    void union_sets(int v, int u) {
        v = get_set(v), u = get_set(u);
        if (rank[v] < rank[u]) {
            swap(u, v);
        }
        parents[u] = v;
        rank[v] += (rank[v] == rank[u]);
    }
};

struct edge {
    int u, v, c;
};

vector<edge> edges;
vector<bool> used;
set<int> included_c;
vector<int> path, a, z, d;
vector<vector<int> > b;
const int MX = 9999999;

queue<int> assign_get_path(int n, int m, int num_colors) {
    queue<int> q;
    for (int i = 0; i < a.size(); ++i) {
        auto e = a[i];
        d[e] = 1 + (path[e] = -1);
        q.push(e);
    }
    return q;
}

int find_s() {
    int v = -1, w = MX;
    for (auto e : z) {
        if (d[e] >= w) continue;
        v = e, w = d[e];
    }
    return v;
}

void build_d(queue<int> queue_) {
    while (queue_.size()) {
        for (auto e : b[queue_.front()]) {
            if (d[queue_.front()] >= d[e] - 1) continue;
            d[e] = 1 + d[queue_.front()], path[e] = queue_.front(), queue_.push(e);
        }
        queue_.pop();
    }
}

int get_path(int n, int m, int num_colors) {
    d.assign(m, MX);
    auto queue_ = assign_get_path(n, m, num_colors);
    build_d(queue_);
    return find_s();
}

void assign_used(int p) {
    while (p != -1) {
        used[p] = !used[p];
        p = path[p];
    }
}

inline void init_search(int n, int m, int num_colors) {
    a.clear(), z.clear(), path.assign(m, -1), included_c.clear();
}

inline void init_build(int n, int m, int num_colors) {
    b.clear();
    b.resize(m);
}

void search_pro_unused(dsu& cur_dsu, int iter) {
    auto e = edges[iter];
    if (cur_dsu.get_set(e.u) - cur_dsu.get_set(e.v) != 0) a.push_back(iter);
    if (!included_c.count(e.c)) z.push_back(iter);
}

void search_pro_used(dsu& cur_dsu, int iter) {
    auto e = edges[iter];
    included_c.insert(e.c);
    cur_dsu.union_sets(e.u, e.v);
}

bool search(int n, int m, int num_colors) {
    init_search(n, m, num_colors);
    dsu cur_dsu(n);
    for (int i = 0; i < m; ++i) {
        if (!used[i]) continue;
        search_pro_used(cur_dsu, i);
    }

    for (int i = 0; i < m; ++i) {
        if (used[i]) continue;
        search_pro_unused(cur_dsu, i);
    }   
       
    int p = get_path(n, m, num_colors);
    if (p == -1) return true;
    assign_used(p);
    return false;
}

void choose(int n, int m, int num_colors) {
    for (int i = 0; i < m; ++i) {
        if (!used[i]) continue;
        included_c.clear();
        dsu cur_dsu1(n);

        for (int j = 0; j < m; ++j) {
            if (!used[j] || i == j) continue;
            auto e = edges[j];
            included_c.insert(e.c), cur_dsu1.union_sets(e.u, e.v);
        }
        for (int j = 0; j < m; ++j) {
            if (used[j]) continue;
            auto e = edges[j];
            if (!included_c.count(e.c)) b[j].push_back(i);
            if (cur_dsu1.get_set(e.u) - cur_dsu1.get_set(e.v) != 0) b[i].push_back(j);
        }
    }
}

bool build(int n, int m, int num_colors) {
    init_build(n, m, num_colors);
    choose(n, m, num_colors);
    return search(n, m, num_colors);
}

int main(void) {
    freopen("rainbow.in", "r", stdin);
    freopen("rainbow.out", "w", stdout);
    int n, m;
    cin >> n >> m;
    used.resize(m);

    int num_colors = 0;
    for (int i = 0; i < m; ++i) {
        int u, v, c;
        cin >> u >> v >> c;
        num_colors = max(num_colors, c);
        edges.push_back({--u, --v, --c});
    }

    while (true) {
        bool res = build(n, m, num_colors);
        if (res) break;
    }

    set<int> rainbow_forest;
    for (int i = 0; i < m; ++i) {
        if (!used[i]) continue;
        rainbow_forest.insert(i);
    }

    cout << rainbow_forest.size() << endl;
    for (auto e : rainbow_forest) cout << e + 1 << " ";
}   
