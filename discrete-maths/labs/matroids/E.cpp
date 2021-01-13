#include <iostream>
#include <algorithm>
#include <set>
using namespace std;

int main(void) {
    freopen("cycles.in", "r", stdin);
    freopen("cycles.out", "w", stdout);

    int n, m;
    cin >> n >> m;

    set<pair<int, int> > elements;
    for (int i = 0; i < n; ++i) {
        int cost;
        cin >> cost;
        elements.insert(make_pair(-cost, -(i + 1)));
    }

    set<set<int> > cycles;
    for (int i = 0; i < m; ++i) {
        int k;
        cin >> k;
        set<int> cycle;
        for (int j = 0; j < k; ++j) {
            int e;
            cin >> e;
            cycle.insert(e);
        }
        cycles.insert(cycle);
    }

    int base_sum = 0;
    set<int> sett;
    for (auto e : elements) {
        sett.insert(-e.second);
        for (int i = 0; i < (1 << sett.size()); ++i) {
            int ptr = 0;
            set<int> cycle;
            for (auto elem : sett) {
                if ((i >> ptr) % 2 == 1) {
                    cycle.insert(elem);
                }
                ++ptr;
            }
            if (!cycles.count(cycle)) {
                continue;
            }
            sett.erase(-e.second);
            goto c;
        }
        base_sum += -e.first;
        
        c: for (int i = 0; i < 0; ++i) {}
         
    }
    cout << base_sum;
}