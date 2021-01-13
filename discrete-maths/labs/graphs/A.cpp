#include <iostream>
#include <deque>
#include <vector>
#include <queue>
#include <algorithm>
using namespace std;

vector<int> colours;
deque<int> path, q;
vector<vector <bool> > g;

int max_colour;

void out() {
    for (auto u : path) {
        cout << u << " ";
    }
}

bool cool_guy_hamilton(int n) {
    for (int i = 1; i < n + 1; ++i) {
        q.push_back(i);
    }
    for (int k = 0; k < n * (n - 1); ++k) {
        auto ptr_second = ++(q.begin());
        if (!g[*(ptr_second)][*(q.begin())]) {
            auto ptr_next = ++(++(++(q.begin())));
            auto ptr_next_plus_one = ++(++(++(++(q.begin()))));
            while (!g[*(ptr_next)][*(q.begin())] || !g[*(ptr_next_plus_one)][*(ptr_second)]) {
                ++ptr_next;
                if (ptr_next_plus_one != q.end()) ++ptr_next_plus_one;
            }
            reverse(ptr_second, ++ptr_next);
        }

        q.push_back(q.front());
        q.pop_front();

    }

    while (!q.empty()) {
        int u = q.front();
        q.pop_front();
        cout << u << " ";
    }
}

int main(void) {
    int n;
    cin >> n;

    g.assign(n + 1, vector<bool>(n + 1));
    colours.resize(n + 1);

    for (int i = 2; i < n + 1; ++i) {
        string s;
        cin >> s;
        for (int j = 0; j < s.size(); ++j) {
            g[i][j + 1] = (s[j] == '1');
            g[j + 1][i] = g[i][j + 1];
        }
    }
    cool_guy_hamilton(n);
    
}

//4
//1 11 101