#include <iostream>
#include <algorithm>
using namespace std;

#define ARR_LENGTH 1000000 + 9
#define MAX 1000000000 + 9 

class MinStack {
    int* values;
    int* mins;
    int size;

    public:
        MinStack() {
            values = new int[ARR_LENGTH];
            mins = new int[ARR_LENGTH];
            size = 0;
        }

        pair<int, int> top() {
            if (size == 0) throw "Empty stack";
            return make_pair(values[size - 1], mins[size - 1]);
        }

        void push(int x) {
            values[size] = x;
            mins[size] = min(x, (size > 0 ? mins[size - 1] : MAX));
            size++;
        }

        void pop() {
            if (size == 0) return;
            size--;
        }

        ~MinStack() {
            delete [] mins;
            delete [] values;
        }
};

int main(void) {
    ios_base::sync_with_stdio(false);
    cin.tie(nullptr);
    cout.tie(nullptr);

    int n;
    cin >> n;
    MinStack stack = MinStack();
    for (int i = 0; i < n; i++) {
        int req;
        cin >> req;
        switch(req) {
            case(1):
                int x;
                cin >> x;
                stack.push(x);
                break;
            case(2):
                stack.pop();
                break;
            case(3):
                cout << stack.top().second << '\n';
                break;
        }
    }
}