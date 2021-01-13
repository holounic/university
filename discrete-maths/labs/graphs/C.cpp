#include <iostream>
#include <vector>
#include <set>
#include <queue>
using namespace std;

vector<int> order;

bool ask(int u, int v) {
    cout << 1 << " " << u << " " << v << '\n';
    cout.flush();
    string ans;
    cin >> ans;
    return ans == "YES";
}

bool binary_search(int u) {
    int l = -1;
    int r = order.size();

    while (l < r - 1) {
        int m = (l + r) / 2;
        if (ask(order[m], u)) {
            l = m;
        } else {
            r = m;
        }
    }
    order.insert(order.begin() + r, u);
}

int solve(int n) {
    order.push_back(1);

    for (int i = 2; i < n + 1; ++i) {
        binary_search(i);
    }
    cout << 0 << " ";
    for (auto u : order) {
        cout << u << " ";
    }
    cout.flush();
}

int main(void) {
    int n;
    cin >> n;
    solve(n);
}