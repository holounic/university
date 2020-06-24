#include <iostream>
#include <vector>
#include <set>
#include <fstream>
using namespace std;
  
struct Edge {
    vector <int> terminals;
    vector <int> non_terminals;
    Edge() = default;
};
  
vector <vector <Edge> > g(100);
set <int> states;
set <int> useless;
  
void non_generating() {
    set <int> set_; 
    for (int i = 0; i < g.size(); ++i) {
        if (states.count(i) == 0) continue;
        for (auto next : g[i]) {
            if (next.non_terminals.empty()) {
                set_.insert(i);
            }
        }
    }
 
    bool updated = true;
    while (updated) {
        updated = false;
        for (int i = 0; i < g.size(); ++i) {
            bool flag = false;
            if (set_.count(i) != 0 || states.count(i) == 0) continue;
            for (auto next : g[i]) {
                int j;
                for (j = 0; j < next.non_terminals.size(); ++j) {
                    if (set_.count(next.non_terminals[j]) == 0) {
                        break;
                    }
                }
                if (j == next.non_terminals.size()) {
                    flag = true;
                }
            }
            if (flag)  {
                if (set_.count(i) == 0) {
                    set_.insert(i);
                    updated = true;
                }
            }
        }
    }
  
    for (int e : states) {
        if (set_.count(e) == 0) {
            useless.insert(e);
        }
    }
 
    for (auto& v : g) {
        for (auto& rule : v) {
            for (auto& to : rule.non_terminals) {
                if (useless.count(to) != 0) {
                    rule.non_terminals.clear();
                    break;
                }
            }
        }
    }
}
  
void non_achivable(int start) {
    set<int> set_;
    set_.insert(start);
  
    bool updated = true;
    while (updated) {
        updated = false;
        for (auto i : set_) {
            if (useless.count(i) == 0) {
                for (auto next : g[i]) {
                    for (int to : next.non_terminals) {
                        if (set_.count(to) == 0) {
                            set_.insert(to);
                            updated = true;
                        }
                    }
                } 
            }
        }
    }
  
    for (auto v : states) {
        if (set_.count(v) == 0) {
            useless.insert(v);
        }
    }
}
  
int main(void) {
    ifstream in("useless.in");
    int n;
    char start;
    in >> n >> start;
    states.insert(start - 'A');
    in.get();
  
    for (int i = 0; i < n; ++i) {
        string rule;
        getline(in, rule);
        int from = rule[0] - 'A';
        states.insert(from);
        int ptr = 5;
        Edge e = Edge();
        while (ptr < rule.size()) {
            if (!('a' - 1 < rule[ptr] && rule[ptr]  < 'z' + 1 ||  'A' - 1 < rule[ptr] && rule[ptr]  < 'Z' + 1)) {
                ++ptr;
                e.terminals.push_back(-1);
                continue;
            }
            if (rule[ptr] > 'a' - 1 && rule[ptr] < 'z' + 1) {
                e.terminals.push_back(rule[ptr] - 'a');
            } else {
                states.insert(rule[ptr] - 'A');
                e.non_terminals.push_back(rule[ptr] - 'A');
            }
            ++ptr;
        } 
        g[from].push_back(e);
    }
    in.close();
    ofstream out("useless.out");
  
    {
        non_generating();
        non_achivable(start - 'A');
    }
  
    for (int e : useless) {
        out << (char)(e + 'A') << " ";
    }
}