#include <iostream>
using namespace std;
#define ARR_LENGTH 1024

class stack {
    int* arr;
    int size;
    int pointer;

    void resize() {
        int* new_arr = new int[(size *= 2)];
        for (int i = 0; i < size / 2; ++i) {
            new_arr[i] = arr[i];
        }
        delete[] arr;
        arr = new_arr;
    }

    public:
    stack() {
        arr = new int[ARR_LENGTH];
        pointer = 0;
        int size = ARR_LENGTH;
    }

    void push(int x) {
        if (pointer == size - 1) {
            resize();
        }
        arr[pointer++] = x;
    }

    int top() {
        return arr[pointer - 1];
    }

    void pop() {
        --pointer;
    }

    ~stack() {
        delete[] arr;
    }
};

int main(void) {
    ios_base::sync_with_stdio(false);
    cin.tie(nullptr);
    cout.tie(nullptr);

    stack balls = stack();
    int last_deleted = -1;
    int deleted = 0;
    balls.push(-1);
    int x;

    while (cin >> x) {
        int prev = balls.top();
        deleted++;

        if (last_deleted == x) {
            continue;
        }
        if (prev != x) {
            deleted--;
            last_deleted = -1;
            balls.push(x);
            continue;
        }
        
        bool popped = false;
        balls.pop();
        deleted++;
        last_deleted = x;

        while (balls.top() == x) {
            balls.pop();
            deleted++;
            popped = true;
        }

        if (!popped) {
            balls.push(x);
            balls.push(x);
            deleted -= 2;
            last_deleted = -1;
        }
    }
    cout << deleted << '\n';
}