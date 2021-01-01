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
vector<long> p;
vector<long> d;
 
void neg_cycle_otstoi(int num_v) {
    d[1] = 0;
    for (int i = 1; i < num_v; ++i) {
        for (auto e : g) {
            if (d[e.v] > d[e.u] + e.w) {
                d[e.v] = d[e.u] + e.w; 
                p[e.v] = e.u;
            }
        }
    }
    vector<int> cycle;
    for (auto e : g) {
        if (d[e.v] > d[e.u] + e.w) {
            int v = e.v;
            for (int i = 1; i < num_v; ++i) {
                v = p[v];
            }
            int u = v;
            while (u != p[v]) {
                v = p[v];
                cycle.push_back(v);
            }
            cycle.push_back(u);
            reverse(cycle.begin(), cycle.end());
            cout << "YES\n" << cycle.size() << '\n';
            for (auto m : cycle) {
                cout << m << " ";
            }
            return;
        }
    }
    cout << "NO";
}
 
int main(void) {
    int n;
    cin >> n;
 
    for (int u = 1; u < n + 1; ++u) {
        for (int v = 1; v < n + 1; ++v) {
            int w;
            cin >> w;
            if (w == 100000) continue;
            g.push_back(edge(u, v, w));
        }
    }
    d.assign(n + 1, INF);
    p.assign(n + 1, -1);
    neg_cycle_otstoi(n + 1);
 
}