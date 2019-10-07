
#include <iostream>
#include <vector>
using namespace std;
 
vector <int> heap;
 
void siftUp(long verticle) {
    if ( heap[verticle] <= heap[(verticle - 1) / 2]) {
        return;
    }
    swap(heap[verticle], heap[(verticle - 1) / 2]);
    return siftUp((verticle - 1) / 2);
}
 
void siftDown( long verticle) {
    if (2 * verticle + 1 >= heap.size()) 
        return;
 
    int left = 2 * verticle + 1;
    int right = 2 * verticle + 2;
    int next = left;
 
    if (right < heap.size() && heap[right] >= heap[left]) {
        next = right;
    }
    if ( heap[next] <= heap[verticle]) {
        return;
    }
    swap(heap[verticle], heap[next]);
    siftDown(next);
}
 
void insert(long value) {
    heap.push_back(value);
    siftUp(heap.size() - 1);
}
 
int extract() {
    int max = heap[0];
    heap[0] = heap[heap.size() - 1];
    heap.erase(heap.begin() + heap.size() - 1);
    siftDown(0);
    return max;
}
 
 
int main(void) {
    int n;
    cin >> n;
    for (int i = 0; i < n; i++) {
        int comm, value;
        cin >> comm;
        if (comm == 0) {
            cin >> value;
            insert(value);
        } else {
            cout << extract() << endl;
        }
    }
}