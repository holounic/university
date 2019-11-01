#include <iostream>
#include <algorithm>
#include <vector>
#include <stack>
using namespace std;

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(nullptr);
    cout.tie(nullptr);

    stack <int> s;
    vector <string> res;
    vector <int> storage;

    int n;
    cin >> n;

    int x;
    cin >> x;
    s.push(x);
    res.push_back("push");

    for (int i = 1; i < n; ++i) {
        cin >> x;
        while (!s.empty() && x > s.top()) {
            storage.push_back(s.top());
            res.push_back("pop");
            s.pop();
        }
        s.push(x);
        res.push_back("push");
    }

    while (!s.empty()) {
        storage.push_back(s.top());
        s.pop();
        res.push_back("pop");
    }

    for (int i = 1; i < storage.size(); ++i) {
        if (storage[i] < storage[i - 1]) {
            cout << "impossible" << '\n';
            return 0;
        }
    }

    for (int i = 0; i < res.size(); ++i) {
        cout << res[i] << '\n';
    }
    return 0;
}