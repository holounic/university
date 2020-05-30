#include <iostream>
#include <fstream>
#include <vector>
#include <set>
#include <queue>
using namespace std;
  
ofstream out("equivalence.out");
  
vector < vector <int> > g1, g2;
vector <bool> terminal1, terminal2;
  
int main(void) {
    ifstream in("equivalence.in");
    in.tie();
    out.tie();
  
    int n1, m, k;
    in >> n1 >> m >> k;
 
    terminal1.resize(n1 + 1);
  
    for(int i = 0; i < k; i++) {
        int a;
        in >> a;
        terminal1[a] = true;
    }
  
    g1.assign(n1 + 1, vector <int> (26, 0));
  
    for(int i = 0; i < m; i++) {
        int a, b;
        char c;
        in >> a >> b >> c;
        g1[a][(c - 'a')] = b;
    }
  
    int n2;
    in >> n2 >> m >> k;
 
    terminal2.resize(n2 + 1);
  
    for(int i = 0; i < k; i++) {
        int a;
        in >> a;
        terminal2[a] = true;
    }
  
    g2.assign(n2 + 1, vector <int> (26, 0));
    for(int i = 0; i < m; i++) {
        int a, b;
        char c;
        in >> a >> b >> c;
        g2[a][(c - 'a')] = b;
    }
    in.close();
    bool used[n1 + 1][n2 + 1];
    for (int i = 0; i < n1 + 1; ++i) {
        for (int j = 0; j < n2 + 1; ++j) {
            used[i][j] = false;
        }
    }

    used[1][1] = true;
    queue <pair <int, int> > q;
    q.push(make_pair(1, 1));
    while (!q.empty()) {
        int next1 = q.front().first, next2 = q.front().second;
        q.pop();
  
        if (terminal1[next1] != terminal2[next2]) {
            out << "NO";
            return 0;
        }
  
        for (int c = 0; c < 26; ++c) {
            int to1 = g1[next1][c], to2 = g2[next2][c];
            if (!used[to1][to2]) {
                q.push(make_pair(to1, to2));
                used[to1][to2] = true;
            }
        }
    }
    out << "YES";
    out.close();
}