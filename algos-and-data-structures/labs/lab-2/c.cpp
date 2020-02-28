#include <iostream>
#include <deque>
using namespace std;

int main(void) {
    ios_base::sync_with_stdio(false);
    cin.tie(nullptr);
    cout.tie(nullptr);

    int n;
    cin >> n;
    deque<int> queue;
    
    for (int i = 0; i < n; i++) {
        int req;
        cin >> req;
        switch(req) {
            case(1):
                int id;
                cin >> id;
                queue.push_back(id);
                break;
            case(2):
                queue.pop_front();
                break;
            case(3):
                queue.pop_back();
                break;
            case(5):
                cout << queue.front() << '\n';
                break;
            case(4):
                int q;
                cin >> q;
                int j;
                for (j = 0; queue[j] != q; j++) {
                }
                cout << j << '\n';
                break;
        }
    }
}