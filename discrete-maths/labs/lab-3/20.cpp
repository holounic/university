#include <iostream>
#include <stack>
#include <fstream>
using namespace std;

long long power2(long long power) {
    return (1 << power);
}

int main() {
    string s;
    ifstream in("brackets2num2.in");
    in >> s;
    in.close();

    int len = s.size();

    long long dp[len][len];

    for (int i = 0; i < len; ++i) {
        for (int j = 0; j < len; ++j) {
            dp[i][j] = 0;
        }
    }
    dp[0][0] = 1;
    for (int i = 1; i < len; i++) {
        for (int j = 0; j < len; j++) {
            if (j > 0) {
                dp[i][j] = dp[i - 1][j - 1];
            }
            if (j < len - 1) {
                dp[i][j] += dp[i - 1][j + 1];
            }
        }
    }

    int littleConst = len - 1;
    long long num = 0;
    stack<bool> prevIndicators;
    int d = 0;

    for (int i = 0; i < len; ++i) {
            switch(s[i]) {
                case ('('):
                    ++d;
                    prevIndicators.push(false);
                    continue;
                case(')'):
                    num += power2((littleConst - i - d - 1) / 2) * dp[littleConst - i][d + 1] ;
                    --d;
                    prevIndicators.pop();
                    continue;
                case('['):
                    num +=  power2((littleConst - i - d - 1) / 2) * dp[littleConst - i][d + 1];
                    if (!prevIndicators.empty()  && !prevIndicators.top()) {
                        num += power2((littleConst - i - d + 1) / 2) * dp[littleConst - i][d - 1] ;
                    }
                    ++d;
                    prevIndicators.push(true);
                    continue;
                default:
                    num += 2 * power2((littleConst - i - d - 1) / 2) * dp[littleConst - i][d + 1];
                    if (!prevIndicators.empty() && !prevIndicators.top()) {
                        num += power2((littleConst - i - d + 1) / 2) * dp[littleConst - i][d - 1];
                    }
                    --d;
                    prevIndicators.pop();
            }
        }
        ofstream out("brackets2num2.out", std::ios::app);
        out << num;
        out.close();
}