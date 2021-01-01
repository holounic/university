#include <iostream>
#include <algorithm>
using namespace std;
 
int main(void) {
    int n;
    cin >> n;
    int m[n][n];
 
    for (int i = 0; i < n; ++i) {
        for (int j = 0; j < n; ++j) {
            cin >> m[i][j];
        }
    }
 
    for (int i = 0; i < n; ++i) {
        for (int j = 0; j < n; ++j) {
            for (int k = 0; k < n; ++k) {
                m[j][k] = min(m[j][k], m[j][i] + m[i][k]);
            }
        }
    }
 
    for (int i = 0; i < n; ++i) {
        for (int j = 0; j < n; ++j) {
            cout << m[i][j] << " ";
        }
        cout << '\n';
    }
}