#include <iostream>
#include <vector>
#include <fstream>
using namespace std;


int main() {
    ifstream in("num2brackets.in");
    int n;
    long long k;
    in >> n >> k;
    in.close();
    ++k;
    long long dp[2 * n + 1][2 * n + 1];
    for (int i = 0; i <= 2 * n; ++i) {
        for (int j = 0; j <= 2*n; ++j) {
            dp[i][j] = 0;
        }
    }
    dp[0][0] = 1;
    for (int i = 1; i <= 2 * n; ++i) {
        for (int j = 0; j <= 2 * n; ++j) {
            long long first = 0;
            if (j != 0) first = dp[i - 1][j - 1];
            long long second = 0;
            if (j != 2 * n) second = dp[i - 1][j + 1];

            dp[i][j] = first + second;
        }
    }


    long long depth = 0;
    string ans = "";
    for (int i = 0; i < 2 * n; ++i) {
        if (dp[2 * n - i - 1][depth + 1] >= k) {
            ans += '(';
            ++depth;
            continue;
        }
        k -= dp[2 * n - 1 - i][depth + 1];
        ans += ')';
        --depth;
    }
    ofstream out("num2brackets.out", std::ios::app);
    out << ans;
}