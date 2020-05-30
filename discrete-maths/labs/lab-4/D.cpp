#include <iostream>
#include <vector>
#include <queue>
#include <set>
#include <fstream>
using namespace std;

int MOD = static_cast<const long>(1e9 + 7);

struct edge {
    int to;
    char c;
    edge(int to, char c) : to(to), c(c) {};
};

int main(void) {
    int n, m, k, l;

    ifstream in("problem4.in");
    in >> n >> m >> k >> l;

    vector < vector <edge> > g(n + 1);
    vector <int> terminal;

    for (int i = 0; i < k; ++i) {
        int a;
        in >> a;
        terminal.push_back(a);
    }
    for (int i = 0; i < m; ++i) {
        int a, b;
        char c;
        in >> a >> b >> c;
        g[a].push_back(edge(b, c));
    }
    in.close();

    queue < int > q;
    q.push(1);

    int paths[n + 1][2];
    for (int i = 0; i < n + 1; ++i) {
        paths[i][0] = 0;
        paths[i][1] = 0;
    }
    paths[1][0] = 1;

    for (int i = 0; i < l; ++i) {
        int orange = q.size();
        int j = 0;
        while (orange != 0) {
            auto current = q.front();
            q.pop();
            orange--;
            for (edge to : g[current]) {
                if (paths[to.to][(i + 1) % 2] == 0) {
                    q.push(to.to);
                }
                paths[to.to][(i + 1) % 2] = (paths[to.to][(i + 1) % 2] + paths[current][i % 2]) % MOD;
            }
            paths[current][i % 2] = 0;
        }
    }
    int res = 0;
    for (int t : terminal) {
        res = (res + paths[t][l % 2]) % MOD;
    }
    ofstream out("problem4.out");
    out << res << '\n';
    out.close();
}