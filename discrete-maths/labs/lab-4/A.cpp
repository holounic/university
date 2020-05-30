#include <iostream>
#include <fstream>
#include <vector>
#include <set>
using namespace std;

struct edge {
    int to;
    char letter;
    edge(int to, char c) : to(to) , letter(c) {};
};

set <int> terminal;
vector <vector <edge> > g;
string s;

bool definitely_not_dfs(int index, int v){
    if (index == s.length() - 1) {
        return terminal.find(v) != terminal.end();
    }
    for (edge next : g[v]) {
        if (next.letter == s[index + 1]) {
            return definitely_not_dfs(index + 1, next.to);
        }
    }
    return false;
}

int main(void) {
    ifstream in("problem1.in");
    in >> s;

    int n, m, k;
    in >> n >> m >> k;
    g.resize(n + 1);

    
    for (int i = 0; i < k; ++i) {
        int a;
        in >> a;
        terminal.insert(a);
    }

    for (int i = 0; i < m; ++i) {
        int a, b;
        char c;
        in >> a >> b >> c;
        g[a].push_back(edge(b, c));
    }
    in.close();
    ofstream out("problem1.out");
    out << (definitely_not_dfs(-1, 1) ? "Accepts" : "Rejects");
    out.close();
}