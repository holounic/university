#include <iostream>
#include <fstream> 
#include <set>
#include <vector>
#include <algorithm>
using namespace std;

struct Edge {
    bool e = false;
    vector <int> terminals;
    vector <int> non_terminals;
    Edge() = default;
};

set<int> eps;
vector<vector <Edge> > g(26);


bool is_eps(Edge e) {
    if (!e.terminals.empty()) {
        return false;
    }
    for (int to : e.non_terminals) {
        if (eps.count(to) == 0) {
            return false;
        }
    }
    return true;
}



int main(void) {
    ifstream in("epsilon.in");
    size_t n;
    char start;
    in >> n >> start;
    in.get();

    for (int i = 0; i < n; ++i) {
        string rule;
        getline(in, rule);
        int from = rule[0] - 'A';
        int ptr = 5;
        Edge e = Edge();
        while (ptr < rule.size()) {
            if (!('a' - 1 < rule[ptr] && rule[ptr]  < 'z' + 1 ||  'A' - 1 < rule[ptr] && rule[ptr]  < 'Z' + 1)) {
                eps.insert(from);
                ++ptr;
                continue;
            }
            if (rule[ptr] > 'a' - 1 && rule[ptr] < 'z' + 1) {
                e.terminals.push_back(rule[ptr] - 'a');
            } else {
                e.non_terminals.push_back(rule[ptr] - 'A');
            }
            ++ptr;
        } 
        g[from].push_back(e);
    }
    in.close();

    bool updated;
    do {
        updated = false;
        for (int i = 0; i < g.size(); ++i) {
            for (auto edge : g[i]) {
                if (is_eps(edge) && eps.count(i) == 0) {
                    eps.insert(i);
                    updated = true;
                }
            }
        }
    } while (updated);
    
    ofstream out("epsilon.out");
    for (auto e : eps) {
        out << (char)(e + 'A') << " ";
    }
    out.close();
}