#include <iostream>
#include <fstream>
#include <vector>
#include <memory.h>
using namespace std;
 
#define ll long long
  
int MOD = 1000000007;
ll dp[26][101][101];
  
struct Edge {
    char l, r;
    bool terminal;
    Edge() = default;
};
  
vector <vector <Edge> > g(26);
string word;
  
ll compute(int state, int l, int r) {
    if (dp[state][l][r] == -1) {
        dp[state][l][r] = 0;
        for (Edge u : g[state]) {
            if (!u.terminal) {
                for (int i = l + 1; i < r; ++i) {
                    dp[state][l][r] = (int)((compute(u.l - 'A', l, i) * compute(u.r - 'A', i, r) + dp[state][l][r]) % MOD);
                }
            } else {
                if (l == r - 1 && word[l] == u.l) {
                    dp[state][l][r] = (1 + dp[state][l][r]) % MOD;
                }
            }
        }
    }
    return dp[state][l][r];
}
  
  
int main(void) {
    memset(dp, false, sizeof(dp));
    ifstream in("nfc.in");
  
    string s;
    int n;
    in >> n >> s;
     
    for (int i = 0, j = -1; i < n; ++i) {
        string a, b;
        in >> a >> b;
        getline(in, b);
  
        j = a[0] - 'A';
        Edge e = Edge();
        e.l = b[1];
 
        if (b.length() > 2) {
            e.r = b[2];
        }
  
        e.terminal = (b.length() <= 2);
        g[j].push_back(e);
    }
  
    in >> word;
    in.close();
 
    ofstream out("nfc.out");
    out << compute(s[0] - 'A', 0, (int)(word.length()));
    out.close();
}