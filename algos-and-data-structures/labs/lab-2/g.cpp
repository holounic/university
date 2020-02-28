#include <iostream>

using namespace std;

struct mem {
    int max, min, size, p;
};

mem info[300001];

void init(int x) {
    info[x].size = 1;
    info[x].max = info[x].min = info[x].p = x;
}

int find(int x) {
    if (x == info[x].p) {
        return x;
    }
    return info[x].p = find(info[x].p);
}

void unite(int x, int y) {
    x = find(x);
    y = find(y);

    if (info[x].size > info[y].size) {
        swap(x, y);
    }
    if (x != y) {
        info[y].p = x;
        info[x].size += info[y].size;
        info[x].max = max(info[y].max, info[x].max);
        info[x].min = min(info[y].min, info[x].min);
    }

}

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(nullptr);
    cout.tie(nullptr);

    int n;
    cin >> n;

    for (int i = 1; i <= n; i++) {
        init(i);
    }

    string req;
    while (cin >> req) {
        if (req == "union") {
            int x, y;
            cin >> x >> y;
            unite(x, y);
        } else {
            int x;
            cin >> x;
            x = find(x);
            cout << info[x].min << " " << info[x].max << " " << info[x].size << endl;
        }
    }
    return 0;
}