#include <iostream>
using namespace std;
int points = 0;

struct Node {
    int size, points, parent;

    Node() {
        size = 0;
        parent = (int)NULL;
        points = 0;
    }

    Node(int x) {
        size = 1;
        parent = x;
        points = 0;
    }
};

struct Set {
    Node* array;

    Set(int capacity) {
        array = new Node[capacity + 1];
        for (int i = 0; i <= capacity; ++i) {
            array[i] = Node(i);
        }
    }

    int find(int x) {
        if (x == array[x].parent) {
            return x;
        }
        return find(array[x].parent);
    }

    void unite(int x, int y) {
        x = find(x);
        y = find(y);
        if (y != x) {
            array[x].parent = y;
            array[y].size += array[x].size;
            array[x].points -= array[y].points;
        }
    }

    void get(int x) {
        points += array[x].points;
        if (x == array[x].parent) return;
        get(array[x].parent);
    }

    void add(int x, int points) {
        array[find(x)].points += points;
    }
};


int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    cout.tie(nullptr);

    int n, m;
    cin >> n >> m;

    Set set = Set(n);

    string req;
    for (int i = 0; i < m; ++i){
        int x, y;
        cin >> req;
        if (req == "join") {
            cin >> x >> y;
            set.unite(x, y);
            continue;
        }
        if (req == "add") {
            cin >> x >> y;
            set.add(x, y);
            continue;
        }

        if (req == "get") {
            cin >> x;
            set.get(x);
            cout << points << '\n';
            points = 0;
            continue;
        }
    }
    return 0;
}