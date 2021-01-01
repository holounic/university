#include <iostream>
#include <algorithm>
#include <vector>
using namespace std;
 
#define INF 10000001
 
struct edge {
    int u, v, w; 
 
    edge(int u, int v, int w) {
        this->u = u;
        this->v = v;
        this->w = w;
    }
};
 
vector<edge> g;
vector<long> d;
 
void ford_bellman_really_cool(int num_v) {
    d[1] = 0;
    while (true) {
        bool changed = false;
        for (auto e : g) {
            if (d[e.u] < INF) {
                if (d[e.v] > d[e.u] + e.w) {
                    d[e.v] = d[e.u] + e.w;
                    changed = true;
                }
            }
        }
        if (!changed) break;
    }
    for (int i = 1; i < num_v; ++i) cout << d[i] << " ";
}
 
int main(void) {
    int n, m;
    cin >> n >> m;
 
    for (int i = 0; i < m; ++i) {
        int u, v, w;
        cin >> u >> v >> w;
        g.push_back(edge(u, v, w));
        g.push_back(edge(v, u, w));
    }
 
    d.assign(n + 1, INF);
    ford_bellman_really_cool(n + 1);
 
}
