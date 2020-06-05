#include <iostream>
#include <vector>
#include <cmath>
using namespace std;

int n;
vector <int> p;
vector <vector <int> > dp;

void compute_power_parents() {
    for (int i = 1; i < n + 1; ++i) {
        dp[i][0] = (p[i] == 0 ? -1 : p[i]);
    }

    for (int i = 1; i < (ceil)(log2(n)); ++i) {
        for (int j = 1; j < n + 1; ++j) {
            if (dp[j][i - 1] == -1) {
                dp[j][i] = -1;
                continue;
            }
            dp[j][i] = dp[dp[j][i - 1]][i - 1];
        }
    }
}


int main(void) {
    cin >> n;

    p.resize(n + 1);
    dp.assign(n + 1, vector <int> ((ceil)(log2(n))));

    int root = -1;
    for (int i = 1; i < n + 1; ++i) {
        cin >> p[i];
        if (p[i] == 0) root = i;
    }

    compute_power_parents();

    for (int i = 1; i < n + 1; ++i) {
        cout << i << ": ";
        if (i != root) {
            for (int j = 0; j < dp[i].size(); ++j) {
                if (dp[i][j] == -1) continue;
                cout << dp[i][j] << " ";
            }
        }
        cout << '\n';
    }
}
