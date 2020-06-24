#include <iostream>
#include <vector>
#include <fstream>
using namespace std;

struct Edge {
    bool to_terminal = true;
    char c = '\0';
    char next = '\0';
    Edge(char c, char next) : to_terminal(false), c(c), next(next) {}
    Edge(char c) : c(c) {}
    Edge() = default;
};

vector <vector <Edge> > g(26);

bool is_valid_word(int state, string word, int index, bool term) {
    if (index == word.size() && term) {
        return true;
    }
    for (Edge edge : g[state]) {
        if (edge.c == word[index]) {
            if (is_valid_word((edge.next - 'A'), word, index + 1, edge.to_terminal)) {
                return true;
            }
        }
    }
    return false;
}

int main(void) {
    ifstream in("automaton.in");
    ofstream out("automaton.out");
    size_t n, m;
    char start;

    in >> n >> start;

    for (int i = 0; i < n; ++i) {
        char state, arr;
        string right;
        in >> state >> arr >> arr >> right;
        Edge e;
        if (right.size() == 0) {
            continue;
        }
        if (right.size() == 1) {
            e = Edge(right[0]);
        } else {
            e = Edge(right[0], right[1]);
        }
        g[state - 'A'].push_back(e);
    }

    in >> m;
    for (int i = 0; i < m; ++i) {
        string word;
        in >> word;
        out << (is_valid_word(start - 'A', word, 0, false) ? "yes" : "no") << '\n';
    }

}