#include <iostream>
#include <set>
#include <queue>
using namespace std;

queue<int> punk_prufer;
set<int> punk_verticles;
set<int> used;
vector<int> in;
vector<pair<int, int> > g;

void decode_punk_prufer() {
    while (!punk_prufer.empty()) {
        int u = punk_prufer.front();
        punk_prufer.pop();
        --in[u];
        
        auto ptr = punk_verticles.begin();
        int v = *(ptr);
        if (v == u) {
            v = *(++ptr);
        }
        punk_verticles.erase(v);
        if (!in[u]) {
            punk_verticles.insert(u);
        }
        g.push_back(make_pair(u, v));
    }

    auto ptr = punk_verticles.begin();
    int u = *(ptr);
    int v = *(++ptr);

    g.push_back(make_pair(u, v));

    for (auto node : g) {
        cout << node.first << " " << node.second << endl;
    }
}

int main(void) {
    int n;
    cin >> n;

    in.resize(n + 1);

    for (int i = 1; i < n + 1; ++i) {
        punk_verticles.insert(i);
    }

    for (int i = 0; i < n - 2; ++i) {
        int u;
        cin >> u;
        punk_prufer.push(u);
        punk_verticles.erase(u);
        ++in[u];
    }
    decode_punk_prufer();
}