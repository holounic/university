#include <iostream>
#include <algorithm>
#include <vector>
#include <set>
using namespace std;

struct strctr {
    int u, v, num;
    long long w;
    strctr(int u, int v, int num, long long w) {
        this->u = u;
        this->v = v;
        this->num = num;
        this->w = w;
    }
    strctr() {
        this->u = this->v = this->num = this->w = 0;
    }
};

int component(vector<int>& segments, int v) { return (v == segments[v] ? v : segments[v] = component(segments, segments[v])); }

void sum(vector<int>& segments, int u, int v) {
    u = component(segments, u), v = component(segments, v);
    if (u != v) segments[u] = v;
}

bool edge_comp(strctr s1, strctr s2) { return s1.w == s2.w && s1.num < s2.num || s1.w < s2.w; }

int main(void) {
    freopen("destroy.in", "r", stdin);
    freopen("destroy.out", "w", stdout);
    int n, m;
    long long s, deleted_s;
    deleted_s = 0;
    cin >> n >> m >> s;
    vector<strctr> g;
    vector<int> segments;
    vector<bool> mst_contains(m);
    for (int i = 0; i < m; ++i) {
        int u, v;
        long long w;
        cin >> u >> v >> w;
        g.push_back(strctr(--u, --v, i, -w));
    }
    sort(g.begin(), g.end(), edge_comp);
    for (int i = 0; i < n; ++i) {
        segments.push_back(i);
    }
    for (auto e : g) {
        if (component(segments, e.u) == component(segments, e.v)) continue;
        mst_contains[e.num] = true;
        sum(segments, e.u, e.v);
    }
    for (int i = 0; i < m; ++i) {
        g[i].w *= -1;
    }
    sort(g.begin(), g.end(), edge_comp);
    set<int> deleted;
    for (auto e : g) {
        if (mst_contains[e.num]) {
            continue;
        }
        if (deleted_s + e.w > s) break;
        deleted_s += e.w;
        deleted.insert(e.num + 1);
    }
    cout << deleted.size() << '\n';
    for (auto id : deleted) {
        cout << id << " ";
    }
}