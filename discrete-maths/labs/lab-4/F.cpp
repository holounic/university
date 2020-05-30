#include <iostream>
#include <fstream>
#include <algorithm>
#include <vector>
#include <set>
using namespace std;

ofstream out("isomorphism.out");

vector < vector < pair <char, int > > > g1, g2;
set <int> terminal1, terminal2;
vector <bool> used;

void one_more_not_dfs(int v1, int v2) {
    if (used[v1]) return;

    used[v1] = true;
    if ((terminal1.find(v1) == terminal1.end()) != (terminal2.find(v2) == terminal2.end())) {
        out << "NO";
        exit(0);
    }

    if (g1[v1].size() != g2[v2].size()) {
        out << "NO";
        exit(0);
    }

    for (int i = 0; i < g1[v1].size() && i < g2[v2].size(); ++i) {
        if (g1[v1][i].first != g2[v2][i].first) {
            out << "NO";
            exit(0);
        }
        one_more_not_dfs(g1[v1][i].second, g2[v2][i].second);
    }
}

int main(void) {
    ifstream in("isomorphism.in");
    int n, m, k;
    in >> n >> m >> k;

    for (int i = 0; i < k; ++i){
        int a;
        in >> a;
        terminal1.insert(a);
    }

    g1.resize(n + 1);
    for (int i = 0; i < m; ++i) {
        int a, b;
        char c;
        in >> a >> b >> c;
        g1[a].push_back(make_pair(c, b));
    }

    in >> n >> m >> k;

    for (int i = 0; i < k; ++i){
        int a;
        in >> a;
        terminal2.insert(a);
    }

    g2.resize(n + 1);
    for (int i = 0; i < m; ++i) {
        int a, b;
        char c;
        in >> a >> b >> c;
        g2[a].push_back(make_pair(c, b));
    }
    in.close();

    used.resize(n + 1);

    for (auto block : g1) {
        sort(block.begin(), block.end());
    }
    for (auto block : g2) {
        sort(block.begin(), block.end());
    }
    if (g1.size() != g2.size()) {
        out << "NO";
        return 0;
    }

    one_more_not_dfs(1, 1);
    out << "YES";
    return 0;
}