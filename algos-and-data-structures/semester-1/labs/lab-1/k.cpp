#include <iostream>
#include <algorithm>
#include <vector>

using namespace std;

long long *v, *w;
long long n, k;
long double* arr;

bool checker(long double x) {
    for (int i = 0; i < n; i++) {
        arr[i] = v[i] - x * w[i];
    }
    sort(arr, arr + n);
    long double sum = 0;
    for (int i = n - k; i < n; i++) {
        sum += arr[i];
    }
    return (sum >= 0);
}

int main() {

    cin >> n >> k;
    v = new long long[n];
    w = new long long[n];
    arr = new long double[n];
    for (int i = 0; i < n; i++) {
        cin >> v[i] >> w[i];
    }

    long double l = 0, r = 1e18;
    for (int i = 0; i < 100; i++) {
        long double m = (l + r) / 2;
        if (checker(m)) {
            l = m;
        } else {
            r = m;
        }
    }
    vector <pair<long double, long long>> ans;
    for (int i = 0; i < n; i++) {
        ans.push_back(make_pair(v[i] - l * w[i], i));
    }

    sort(ans.begin(), ans.end());

    for (long long i = n - k; i < n; i++) {
        cout << ans[i].second + 1 << endl;
    }
}