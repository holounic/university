#include <iostream>
#include <vector>
#include <algorithm>
#include <set>
using namespace std;

int main(void) {
    freopen("schedule.in", "r", stdin);
    freopen("schedule.out", "w", stdout);
    long long n;
    cin >> n;
    vector<pair<long long, long long> > tasks(n);
    long long fine = 0, time = 1;
    for (long long i = 0; i < n; ++i) {
        long long d, w;
        cin >> d >> w;
        tasks[i] = make_pair(d, w), fine += w;
    }
    sort(tasks.begin(), tasks.end());
    multiset<long long> intime_tasks;
    for (auto task : tasks) {
        intime_tasks.insert(task.second), fine -= task.second;
        if (task.first < time) {
            fine += *(intime_tasks.begin());
            intime_tasks.erase(intime_tasks.begin());
            continue;
        }
        ++time;
    }
    cout << fine;
}