#include <iostream>
#include <deque>
using namespace std;

int main(void) {
    ios_base::sync_with_stdio(false);
    cin.tie(nullptr);
    cout.tie(nullptr);

    int n;
    cin >> n;

    deque<int> first_part;
    deque<int> second_part;

    bool prev = false;
    for (int i = 0; i < n; i++) {
        char c;
        cin >> c;
        switch(c) {
            case('+'):
                if (second_part.size() > first_part.size()) {
                    int mid = second_part.front();
                    second_part.pop_front();
                    first_part.push_back(mid);
                }
                int k;
                cin >> k;
                second_part.push_back(k);
                if (second_part.size() > first_part.size()) {
                    int mid = second_part.front();
                    second_part.pop_front();
                    first_part.push_back(mid);
                }
                break;
            case('-'):
                if (second_part.size() > first_part.size()) {
                    int mid = second_part.front();
                    second_part.pop_front();
                    first_part.push_back(mid);
                }
                if (first_part.empty()) {
                    cout << second_part.front() << '\n';
                    second_part.pop_front();
                } else {
                    cout << first_part.front() << '\n';
                    first_part.pop_front();
                }
                if (second_part.size() > first_part.size()) {
                    int mid = second_part.front();
                    second_part.pop_front();
                    first_part.push_back(mid);
                }
                break;
            case('*'):
                if (second_part.size() > first_part.size()) {
                    int mid = second_part.front();
                    second_part.pop_front();
                    first_part.push_back(mid);
                }
                int m;
                cin >> m;
                first_part.push_back(m);
                if (first_part.size() - second_part.size() > 1) {
                    int mid = first_part.back();
                    first_part.pop_back();
                    second_part.push_front(mid);
                } 
                break;
        }
    } 
}