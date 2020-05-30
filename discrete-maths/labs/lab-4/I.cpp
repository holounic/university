#include <iostream>
#include <fstream>
#include <vector>
#include <set>
#include <map>
#include <queue>
using namespace std;

//using drdr(A)

struct edge {
    int to;
    char c;
    edge(int to, char c) : to(to), c(c) {};
};

struct automaton {
    vector <vector <edge> > g;
    set <int> start, terminal;
    int num_t;
    int num_states;
    int compute_m() {
        int res = 0;
        if (&g == nullptr) {
            return 0;
        }
        for (vector <edge>  block : g) {
            res += block.size();
        }
        return res;
    }
    automaton(int n) {
        g = vector <vector <edge> > (n);
        num_t = 0;
        num_states = 0;
    }
};

automaton reverse(automaton from) {
    automaton reversed = automaton(from.g.size());
    for (int i = 1; i < from.g.size(); ++i) {
        for (int j = 0; j < from.g[i].size(); ++j) {
            edge current = from.g[i][j];
            reversed.g[current.to].push_back(edge(i, current.c));
        }
    }
    for (int v : from.terminal) {
        reversed.start.insert(v);
    }
    reversed.num_states = from.num_states;
    for (int v : from.start) {
        reversed.terminal.insert(v);
        reversed.num_t++;
    }
    return reversed;
}


automaton determineize(automaton from) {
    automaton determ = automaton(from.g.size());
    queue <set <int> > Q;
    map <set <int>, int> group_number;
    set <set <int> > used;
    set <int> start;

    int idx = 1;

    Q.push(from.start);
    used.insert(from.start);

    for (int v : from.start) {
        if (from.terminal.count(v) != 0) {
            determ.num_t = 1;
            determ.terminal.insert(1);
            break;
        }
    }
    determ.start.insert(1);
    group_number[from.start] = idx++;

    while (!Q.empty()) {
        set <int> current_group = Q.front();
        Q.pop();

        for (char s = 'a'; s <= 'z'; ++s) {
            set <int> next_group;
            for (int node : current_group) {
                for (edge next : from.g[node]) {
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
                        if (from.terminal.count(v) != 0) {
                            determ.terminal.insert(idx);
                            determ.num_t++;
                            break;
                        }
                    }
                    for (int v : next_group) {
                        if (from.start.count(v) != 0) {
                            determ.start.insert(idx);
                            break;
                        }
                    }
                    ++idx;
                }
                determ.g[group_number[current_group]].push_back(edge(group_number[next_group], s));
            }
        }
    }
    determ.num_states = idx - 1;
    return determ;
}

int main(void) {
    ifstream in("minimization.in");

    int n, m, k;
    in >> n >> m >> k;
    automaton aut = automaton(n + 1);
    aut.start.insert(1);

    for (int i = 0; i < k; ++i) {
        int a;
        in >> a;
        aut.terminal.insert(a);
    }

    for (int i = 0; i < m; ++i) {
        int a, b;
        char c;
        in >> a >> b >> c;
        aut.g[a].push_back(edge(b, c));
    }

    in.close();

    ofstream out("minimization.out");

    auto nfa1 = reverse(aut);

    auto  dfa1 = determineize(nfa1);

    auto nfa2 = reverse(dfa1);

    auto dfa2 = determineize(nfa2);

    out << dfa2.num_states << " " << dfa2.compute_m()  << " " << dfa2.num_t << '\n'; 
    for (int t : dfa2.terminal) {
        out << t << '\n';
    } 
    for (int i = 0; i < dfa2.g.size(); ++i) {
        for (edge e : dfa2.g[i]) {
            out << i << " " << e.to << " " << e.c << '\n';
        }
    }
    out.close();
}