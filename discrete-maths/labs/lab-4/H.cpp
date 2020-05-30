#include <iostream>
#include <vector>
#include <set>
#include <queue>
#include <map>
#include <fstream>
using namespace std;

ofstream out("fastminimization.out");

struct vertex {
    bool is_term, to_term, from_st;
    int value;
    vertex *character[26];

    vertex(int value) : from_st(false), is_term(false), to_term(false), value(value) {
        for (int i = 0; i < 26; ++i) {
            character[i] = nullptr;
        }
    }
};

int n, m, k, m_final = 0, k_final = 0;
vector <vector <int> > g;
vector <vector <vector <int> > > P;
vector <int> class_number;
vector <vertex *> states, upd_states;
vector <set <vertex *> *> p;
queue <pair <set<vertex *> *, int> > q;

void split_by_equivalent_classes() {
    vertex *state, *to;
    for (int i = 1; i < 2 * n + 1; ++i) {
        state = states[i];
        if (!(state->to_term && state->from_st)) continue;
        for (int j = 0; j < 26; ++j) {
            to = state->character[j];
            if (to != nullptr) {
                int numerl = to->value;
                int numers = state->value;
                P[numerl][j].push_back(numers);
            }
        }
    }
    set<vertex *> A, B;
    for (int i = 1; i < 2 * n + 1; ++i) {
        if (!(states[i]->to_term && states[i]->from_st)) continue;
        class_number[states[i]->value] = (!states[i]->is_term) ? 1 : 0;
        if (!states[i]->is_term) {
            B.insert(states[i]);
        } else {
            A.insert(states[i]);
        }
    }
    if (!A.empty()) {
        p.push_back(new set<vertex *>(A));
    } 
    if (!B.empty()){
        p.push_back(new set<vertex *>(B));
    } 
    for (int j = 0; j < 26; ++j) {
        q.push(pair <set<vertex *> *, int>(new set<vertex *>(A), j));
        q.push(pair <set<vertex *> *, int>(new set<vertex *>(B), j));
    }
    while (!q.empty()) {
        auto &cur = q.front();
        q.pop();
        map<int, set<vertex *> > used;
        for (auto st = cur.first->begin(); st != cur.first->end(); ++st) {
            for (int x : P[(*st)->value][cur.second]) {
                int i = class_number[x];
                used[i].insert(states[x]);
            }
        }
        for (auto &pr : used) {
            int i = pr.first;
            if (p[i]->size() > pr.second.size()) {
                int needed_idx = p.size();
                p.push_back(new set<vertex *>());
                for (vertex *r : pr.second) {
                    p[needed_idx]->insert(r);
                    p[i]->erase(r);
                    class_number[r->value] = needed_idx;
                }
                for (int j = 0; j < 26; ++j) {
                    q.push(pair <set <vertex *> *, int>(p[needed_idx], j));
                }
            }
        }
    }
    for (int i = 1; i < 2 * n + 1; ++i) {
        if (!(states[i]->to_term && states[i]->from_st)) class_number[states[i]->value] = -1;
    } 
}

void state_init_dfs(vertex *v) {
    if (!(v->from_st == false)) return;
    vertex *time;
    v->from_st = true;
    for (int j = 0; j < 26; ++j) {
        time = v->character[j];
        if (time != nullptr) state_init_dfs(time);
    }
}

void term_init_dfs(int v) {
    if (!(states[v]->to_term == false)) return;
    states[v]->to_term = true;
    for (auto i : g[v]) term_init_dfs(i);
}

void not_dfs_i_promise(vertex *state) {
    for (int i = 1; i < 2 * n + 1; ++i)
        if (states[i]->is_term) term_init_dfs(states[i]->value);
    state_init_dfs(state);
}

int from(int x) {
    return upd_states[x]->value + 1;
}

int to(int x, int y) {
    return upd_states[x]->character[y]->value + 1;
}

void build_dfa() {
    vertex *state_a, *state_b;
    int from, first = class_number[1], state_num;
    upd_states.resize(p.size());
    if (first != -1) {
        for (int i = 1; i < 2 * n + 1; ++i) {
            state_num = states[i]->value;
            if (class_number[state_num] == first) {
                class_number[state_num] = 0;
                continue;
            }
            if (class_number[state_num] == 0) {
                class_number[state_num] = first;
            }
        }
    }

    for (int i = 0; i < upd_states.size(); ++i) upd_states[i] = new vertex(i);

    for (int i = 1; i < 2 * n + 1; ++i) {
        state_a = states[i], state_num = state_a->value;
        if (class_number[state_num] == -1) continue;
        from = int(class_number[state_num]);
        if (state_a->is_term && !upd_states[from]->is_term) {
            upd_states[from]->is_term = true;
            ++k_final;
        }
        for (int j = 0; j < 26; ++j) {
            if (!state_a->character[j] || class_number[state_a->character[j]->value] == -1) continue;
            int to = (int)(class_number[state_a->character[j]->value]);
            if (!upd_states[from]->character[j]) {
                ++m_final;
                state_b = upd_states[to];
                upd_states[from]->character[j] = state_b;
            }
        }
    }
}

int main(void) {
    ifstream in("fastminimization.in");
    in >> n >> m >> k;

    states.resize(2 * n + 1);
    g.resize(2 * n + 1);
    class_number.resize(2 * n + 1);
    P.resize(2 * n + 1);

    for (int i = 1; i < 2 * n + 1; ++i) {
        states[i] = new vertex(i);
    }
    for (int i = 0; i < P.size(); ++i) {
        P[i].resize(26);
    }

    for (int i = 0; i < k; ++i) {
        int a;
        in >> a;
        states[a]->is_term = true;
    }

    for (int i = 0; i < m; ++i) {
        int a, b;
        char c;
        in >> a >> b >> c;
        c = c - 'a';
        g[b].push_back(a);
        states[a]->character[c] = states[b];
    }
    in.close();

    auto start = states[1];
    not_dfs_i_promise(start);
    split_by_equivalent_classes();
    build_dfa();

    int new_n = p.size();
    out << new_n << ' ' << m_final << ' ' << k_final << '\n';

    for (int i = 0; i < upd_states.size(); ++i) {
        if (upd_states[i]->is_term == true) {
            out << upd_states[i]->value + 1 << ' ';
        }
    }

    out << '\n';

    for (int i = 0; i < upd_states.size(); ++i) {
        for (int j = 0; j < 26; ++j) {
            if (upd_states[i]->character[j] != nullptr) {
                out << from(i) << ' ' << to(i, j) << ' ' << (char)('a' + j) << '\n';
            }
        }
    }

}