#include <iostream>
#include <vector>
#include <set>
using namespace std;

#define u first
#define w second

long long INF = 9999999999999999;

vector<vector<pair<int, long> > > e;
vector<long long> d;

void dijkstra__in_da_house(int s) {
    d[s] = 0;
    set<pair<int, int> > q;
    q.insert(make_pair(d[s], s));

    while (!q.empty()) {
        int next = q.begin()->w;
        q.erase(q.begin());

        for (auto edge : e[next]) {
            int v = edge.u;
            if (d[next] + edge.w < d[v]) {
                q.erase(make_pair(d[v], v));
                d[v] = d[next] + edge.w;
                q.insert(make_pair(d[v], v));
            }
        }
    }
}

int main(void) {
    ios::sync_with_stdio(false);
    cin.tie();
    int n, m;
    cin >> n >> m;

    e.resize(n + 1);
    d.resize(n + 1, INF);

    for (int i = 0; i < m; i++) {
        int a, b;
        long long w;
        cin >> a >> b >> w;
        e[a].push_back(make_pair(b, w));
        e[b].push_back(make_pair(a, w));
    }
    
    int a, b, c;
    cin >> a >> b >> c;

    dijkstra__in_da_house(a);

    long long a_b = d[b];
    long long a_c = d[c];

    d.assign(n + 1, INF);

    dijkstra__in_da_house(b);
    long long b_c = d[c];

    long long ans = min(min(a_b + a_c, a_b + b_c), a_c + b_c);
    if (ans >= INF) {
        cout << -1;
    } else {
        cout << ans;
    }
}