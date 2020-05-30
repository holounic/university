#include <iostream>
#include <fstream>
#include <vector>
#include <algorithm>
#include <set>
using namespace std;

const int MOD = static_cast<int>(1e9 + 7);

ofstream out("problem3.out");

vector <vector <int> > g, reversed;
vector <int> sorted, color, paths;
vector <bool> used;
set <int> terminal;

void no_dfs(int v) {
    if (used[v]) return;
    used[v] = true;

    for (int next : reversed[v]) {
        no_dfs(next);
    }
}

void top_sort(int v) {
    if (color[v] == 2) return;
    color[v] = 1;

    for (int next : g[v]) {
        if (used[next]) {
            if (color[next] == 0) {
                top_sort(next);
            }
            if (color[next] == 1) {
                out << "-1";
                exit(0);
            }
        }
    }

    sorted.push_back(v);
    color[v] = 2;
}

int KOMPUCT() {
    reverse(sorted.begin(), sorted.end());

    paths[1] = 1;
    int res = 0;

    for (int v : sorted) {
        for (int next : g[v]) {
            if (used[next]) paths[next] = (paths[v] + paths[next]) % MOD;
        }
    }

    for (int t : terminal) {
        res = (res + paths[t]) % MOD;
    }
    return res;
}


int main(void) {
    int n, m, k;

    ifstream in("problem3.in");

    in >> n >> m >> k;

    g.resize(n + 1);
    reversed.resize(n + 1);
    color.resize(n + 1);
    used.resize(n + 1);
    paths.resize(n + 1);

    for (int i = 0; i < k; i++) {
        int a;
        in >> a;
        terminal.insert(a);
    }

    for (int i = 0; i < m; i++) {
        int a, b;
        char c;
        in >> a >> b >> c;

        g[a].push_back(b);
        reversed[b].push_back(a);
    }

    for (int t : terminal) {
        no_dfs(t);
    }

    top_sort(1);
    out << KOMPUCT();
}