#include <iostream>
#include <fstream>
#include <vector>
#include <map>
#include <set>

using namespace std;

ofstream out("problem2.out");

string s;
vector <map <char, vector <int> > > g;
set <int> terminal;
set <pair <int, int> > used;

void this_shit_is_not_dfs(int index, int v) {
    if (used.count(make_pair(v, index)) != 0) return;

    if (index == s.size()) {
        if (terminal.count(v) == 0) return;
        out << "Accepts";
        exit(0);
    }

    auto iter = g[v].find(s[index]);

    if (iter == g[v].end()) return;

    for (int i = 0; i < (*iter).second.size(); ++i) {
        this_shit_is_not_dfs(index + 1, (*iter).second[i]);
    }

    used.insert(make_pair(v, index));
}

int main(void) {
    fstream in("problem2.in");
    in >> s;

    int n, m, k;
    in >> n >> m >> k;

    g.resize(n + 1);

    for (int i = 0; i < k; ++i){
        int a;
        in >> a;
        terminal.insert(a);
    }

    for (int i = 0; i < m; ++i) {
        int a, b;
        char c;
        in >> a >> b >> c;
        g[a][c].push_back(b);
    }
    in.close();

    this_shit_is_not_dfs(0, 1);

    out << "Rejects";
    out.close();
}