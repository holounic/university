#include <iostream>
#include <fstream>
#include <vector>
using namespace std;
 
int main() {
    int n, k;
    long long m;
    ifstream in("num2choose.in");
    in >> n >> k >> m;
    in.close();
 
    long c[n + 1][k + 1];

    for (int i = 0; i <= n; ++i) {
        for (int  j = 0; j <= k; ++j) {
            c[i][j] = 0;
        }
    }
    c[0][1] = 1;
    
 
    for (int i = 1; i <= n; ++i) {
        for (int j = 1; j <= k; ++j) {
            c[i][j] = c[i - 1][j] + c[i - 1][j - 1];
        }
    }
     
 
    vector<int> choose;
    int next = 1;
    while (k > 0) {
        if (m < c[n - 1][k]) {
            choose.push_back(next);
            --k;
        } else {
            m -= c[n - 1][k];
        }
        --n;
        ++next;
    }
 
    ofstream out("num2choose.out", std::ios::app);
    for (int u : choose) out << u << " ";
 
}