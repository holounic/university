#include <iostream>
#include <deque>
#include <vector>
#include <queue>
#include <algorithm>
#include <random>
using namespace std;

deque<int> q;
vector<vector <bool> > g;

int max_colour;

void cool_guy_hamilton(int n) {
    for (int k = 0; k < n * (n - 1); ++k) {
        auto ptr_second = ++(q.begin());
        if (!g[*(ptr_second)][*(q.begin())]) {
            auto ptr_next = ++(++(++(q.begin())));
            auto ptr_next_plus_one = ++(++(++(++(q.begin()))));
            while (ptr_next != q.end() && !g[*(ptr_next)][*(q.begin())] || ptr_next_plus_one != q.end() && !g[*(ptr_next_plus_one)][*(ptr_second)]) {
                ++ptr_next;
                if (ptr_next_plus_one != q.end()) ++ptr_next_plus_one;
            }
            reverse(ptr_second, ++ptr_next);
        }

        q.push_back(q.front());
        q.pop_front();

    }
    if (!g[q.front()][q.back()]) {
        return;
    }
    auto ptr = q.begin();
    auto next_ptr = ++(q.begin());
    while (next_ptr != q.end()) {
        if (!g[*ptr][*next_ptr]) return;
        ++ptr, ++next_ptr;
    }
    while (!q.empty()) {
        int u = q.front();
        q.pop_front();
        cout << u << " ";
    }
    exit(0);
}

int main(void) {
    int n;
    cin >> n;
    g.assign(n + 1, vector<bool>(n + 1));

    std::random_device random1;
    std::mt19937 mt(random1());

    for (int i = 2; i < n + 1; ++i) {
        string s;
        cin >> s;
        for (int j = 0; j < s.size(); ++j) {
            g[i][j + 1] = (s[j] == '1');
            g[j + 1][i] = g[i][j + 1];
        }
    }
    for (int i = 1; i < n + 1; ++i) {
        q.push_back(i);
    }
    while (true) {
        cool_guy_hamilton(n);
        random_shuffle(q.begin(), q.end());
    }
    
}