#include <iostream>
#include <vector>
#include <fstream>
#include <memory.h>
using namespace std;

#define x first
#define y second
#define kek rule.x - 'A'
#define unicorn pair <char, string>
#define forx(x, y) for (size_t x = 0; x < y; ++x)
#define hprev h[idx][i][v][q - 1]
#define hcur h[idx][i][j][q]

int num(char c) {
    return c - 'A';
}

size_t n;
string trash, a, to;
char start, from;

bool dp[26][101][101], h[51][101][101][10];
size_t states[101][101];

vector < vector <string> > g(26);
vector <unicorn> rules;
string word;

void init() {
    forx(i, word.size()) {
        forx(j, rules.size()) {
            unicorn rule = rules[j];
            dp[kek][i][i] = rule.y.empty(), 
            h[j][i][i][0] = true,
            dp[kek][i][1 + i] = rule.y.size() == 1 && rule.y[0] == word[i];
        }
    }
}
bool small(char c) {
    return c <= 'z' && c >= 'a';
}

bool CYK() {
    forx(cnt, 26) {
        forx(k, 1 + word.size()) {
            forx(i, word.size()) {
                int j = i + k;
                if (j > word.size()) break;
                for (int q = 1; q < 6; ++q) {
                    forx(idx, rules.size()) {
                        if (rules[idx].y.size() < q) continue;
                        for (int v = i; v <= j; ++v) {
                            hcur = (small(rules[idx].y[q - 1]) ?
                            (hcur || hprev && (v + 1 == j) && (rules[idx].y[q - 1] == word[v])) : 
                            (hcur || hprev && dp[num(rules[idx].y[q - 1])][v][j]));
                        }
                    }
                }
                forx (z, 26) {
                    forx(t,g[z].size()) 
                        dp[z][i][j] = (dp[z][i][j] || h[states[z][t]][i][j][g[z][t].size()]);
                }
            }
        }
    }
    return dp[num(start)][0][word.size()];
}

int main(void) {
    memset(dp, false, sizeof(dp)), memset(h, false, sizeof(h)), memset(states, -1, sizeof(states));
    ifstream in("cf.in");
    ofstream out("cf.out");

    in >> n >> start;
    getline(in, trash);

    forx(i, n) {
        getline(in, a);
        if (a.empty()) {
            getline(in, a);
        }
        from = a[0];
        to = "";
        if (a[a.size() - 1] != '>') {
            for (int j = a.find('>') + 2; j < a.size(); ++j) to = to + a[j];
        }
        g[num(from)].push_back(to), rules.push_back(make_pair(from, to)), states[num(from)][g[num(from)].size() - 1] = rules.size() - 1;
    }
    in >> word;
    init();
    out << (CYK() ? "yes" : "no");
}