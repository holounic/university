#include <iostream>
#include <vector>
#include <set>
#include <queue>
using namespace std;

#define u second
#define w first
#define INF 100000

vector<vector<pair<size_t, size_t> > > g;
vector<size_t> min_e;
size_t weight = 0;

void lol_kek_prim_cheburek() {
    min_e[1] = 0;
    priority_queue<pair<size_t, size_t> > q;
    q.insert(make_pair(0, 1));
    for (size_t i = 1; i < g.size(); ++i) {
        size_t v = q.first()->u;
        q.pop();
        weight += min_e[v];
        for (auto e : g[v]) {
            size_t to = e.u;
            size_t cost = e.w;
            if (cost < min_e[to]) {
                q.erase(make_pair(min_e[to], to));
                min_e[to] = cost;
                q.insert(make_pair(min_e[to], to));
            }
        }
    }
    cout << weight << endl;
}



int main(void) {
    size_t n, m;
    cin >> n >> m;
    min_e.assign(n + 1, INF);
    g.resize(n + 1);

    for (size_t i = 0; i < m; ++i) {
        size_t u, v, w;
        cin >> u >> v >> w;
        g[u].push_back(make_pair(w, v));
        g[v].push_back(make_pair(w, u));
    }

    lol_kek_prim_cheburek();
}