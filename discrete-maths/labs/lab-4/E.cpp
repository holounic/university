#include <iostream>
#include <vector>
#include <fstream>
#include <queue>
#include <set>
#include <map>
using namespace std;

int MOD = static_cast<const long>(1e9 + 7);

struct edge {
    int to;
    char c;
    edge(int to, char c) : to(to), c(c) {};
};

vector <vector <edge> > g_nfa, g_dfa;
set <int> dfa_terminal, nfa_terminal;

int idx = 1;
void determineize() {
    queue <set <int> > Q;
    map <set <int>, int> group_number;
    set <set <int> > used;
    set <int> apple;

    apple.insert(1);
    used.insert(apple);
    Q.push(apple);
    group_number[apple] = 1;
    idx = 2;
    if (nfa_terminal.count(1) != 0) {
        dfa_terminal.insert(1);
    }

    while (!Q.empty()) {
        set <int> current_group = Q.front();
        Q.pop();

        for (char s = 'a'; s <= 'z'; ++s) {
            set <int> next_group;
            for (int node : current_group) {
                for (edge next : g_nfa[node]) {
                    if (next.c == s) {
                        next_group.insert(next.to);
                    }
                }
            }

            if (!next_group.empty()) {
                if (used.count(next_group) == 0) {
                    group_number[next_group] = idx;
                    used.insert(next_group);
                    Q.push(next_group);
                    for (int v : next_group) {
                        if (nfa_terminal.count(v) != 0) {
                            dfa_terminal.insert(idx);
                            break;
                        }
                    }
                    ++idx;
                }
                g_dfa[group_number[current_group]].push_back(edge(group_number[next_group], s));
            }
        }
    }
}

int paths[101][1001];

int count_paths(int v, int l) {
    if (paths[v][l] != -1) {
        return paths[v][l];
    }
    if (l == 0) {
        paths[v][0] = (dfa_terminal.count(v) != 0) ? 1 : 0;
        return paths[v][0];
    }

    paths[v][l] = 0;
    for (int i = 0; i < g_dfa[v].size(); i++) {
        paths[v][l] = (paths[v][l] + count_paths(g_dfa[v][i].to, l - 1)) % MOD;
    }
    return paths[v][l];
}

int main(void) {

    ifstream in("problem5.in");
    int n, m, k, l;
    in >> n >> m >> k >> l;

    g_nfa.resize(n + 1);
    g_dfa.resize(101);

    for (int i = 0; i < k; ++i) {
        int a;
        in >> a;
        nfa_terminal.insert(a);
    }
    for (int i = 0; i < m; ++i) {
        int a, b;
        char c;
        in >> a >> b >> c;
        g_nfa[a].push_back(edge(b, c));
    }
    in.close();

    for (int i = 0; i < 101; ++i) {
        for (int j = 0; j < 1001; ++j) {
            paths[i][j] = -1;
        }
    }


    determineize();


    queue <int> q;
    q.push(1);

    ofstream out("problem5.out");
    out << count_paths(1, l);
    out.close();
}