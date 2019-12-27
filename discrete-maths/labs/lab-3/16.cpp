#include <iostream>
#include <fstream>
#include <vector>
using namespace std;
int n, k;

long long f(int a) {
    if (a == 0) return 1;
    return a * f(a - 1);
}

long long C(int n, int k) {
    long long temp = f(n);
    temp /= f(k);
    temp /= f(n - k);
    return temp;
}

int main() {
    ifstream in("choose2num.in");
    in >> n >> k;
    vector<int> choose(k + 1);
    for (int i = 1; i <= k; ++i) in >> choose[i];
    in.close();

    ofstream out("choose2num.out", std::ios::app);
    long long dp[n + 1][k + 1];

    //обожаю с++ за то, что нужно заполнять двумерные массивы ручками. ммм лучше, чем даниссимо!!!
    for (int i = 0; i <= n; ++i) {
        for (int j = 0; j <= k; ++j) dp[i][j] = 0;
    }

    dp[0][1] = 1;
    for (int i = 1; i <= n; ++i) {
        for (int j = 1; j <= k; ++j) {
            dp[i][j] = dp[i - 1][j] + dp[i - 1][j - 1];
        }
    }
    long long num  = 0;
    for (long long i = 1; i <= k; ++i) {
        for (long long j = choose[i - 1] + 1; j <= choose[i] - 1; ++j) {
            num += dp[n - j][k - i + 1];
        }
    }
    out << num;
}