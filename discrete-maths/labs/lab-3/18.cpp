#include <iostream>
#include <fstream>
using namespace std;

int main() {
    long long dp[41][41];
    for (int i = 0; i < 41; ++i) {
        for (int j = 0; j < 41; ++j) {
            dp[i][j] = 0;
        }
    }
    dp[0][0] = 1;
    for (int i = 1; i < 41; ++i) {
        for (int j = 0; j < 41; ++j) {
            long long first = 0;
            if (j != 0) first = dp[i - 1][j - 1];
            long long second = 0;
            if (j != 40) second = dp[i - 1][j + 1];
            dp[i][j] = first + second;
        }
    }

    string s;
    ifstream in("brackets2num.in");
    in >> s;
    in.close();

    long long num = 0;
    int depth = 0;
    int l = s.size();

    for (int i = 0; i < l; ++i) {
        if (s[i] == '(') {
            ++depth;
            continue;
        }

        num += dp[l - i - 1][depth + 1];
        --depth;
    }

    ofstream out("brackets2num.out", std::ios::app);
    out << num;
}