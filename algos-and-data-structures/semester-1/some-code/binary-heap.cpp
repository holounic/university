#include <iostream>
#include <cmath>

class BinaryHeap {
    private:
    int* heap;
    int heapSize;
    int height;

    void resize(int size) {
        int* newHeap = new int[size + 1];
        for (int i = 0; i < this->heapSize; i++) {
            *(newHeap + i) = *(this->heap + i);
        }
        *(newHeap + size) = 0;

        delete[] this->heap;
        this->heap = newHeap;
        this->heapSize = size;
        this->height = ceil(log2(this->heapSize + 1));
    }

     void siftDown(int verticle) {
        if (2 * verticle + 1 >= this->heapSize) {
            return;
        }
        int left = 2 * verticle + 1;
        int right = 2 * verticle + 2;
        int next = left;
        if (right < this->heapSize && *(this->heap + left) > *(this->heap + right)) {
            next = right;
        }
        if ( *(this->heap + next) >= *(this->heap + verticle)) {
            return;
        }
        std::swap( *(this->heap + next), *(this->heap + verticle));
        return siftDown(next);
    }

    void siftUp(int verticle) {
        if ( *(this->heap + verticle) >= *(this->heap + (verticle - 1) / 2)){
            return;
        }
        std::swap( *(this->heap + verticle), *(this->heap + (verticle - 1) / 2));
        return siftUp((verticle - 1) / 2);
    }

    public:
    BinaryHeap(int n, int* heap) {
        this->heap = new int [n+1];
        this->heapSize = n;
        *(this->heap + n) = 0;
        this->height = ceil(log2(this->heapSize + 1));

        for (int i = 0; i < n; i++) {
            *(this->heap + i) = *(heap + i);
        }

        for (int i = this->heapSize - 1 ; i >= 0; i--) {
            siftDown(i);
        }
    }

    int extractMin() { 
        int min = *(this->heap + 0);
        *(this->heap + 0) = *(this->heap + this->heapSize - 1);
        resize(this->heapSize - 1);
        siftDown(0);
        return min;
    }

    void insert(int value) {
        resize(this->heapSize + 1);
        *(this->heap + this->heapSize - 1) = value;
        siftUp(this->heapSize - 1);
    }

    void show(int level) {
        int idxStart = -1;
        int idxFinish = -1;
        for (int i = 0; i < level; i++) {
            idxStart += pow(2, i);
        }
        idxFinish = idxStart + pow(2, level);

        int idx = idxStart + 1;
        while (idx <= idxFinish && idx < this->heapSize) {
            std::cout << *(this->heap + idx) << " ";
            idx++;
        }
        std::cout << std::endl;
    }

    void show() {
        int idx = 0;
        for (int level = 0; level <= this->height; level++) {
            int idxFinish = idx + pow(2, level) - 1;

            if (idxFinish > this->heapSize - 1) {
                idxFinish = this->heapSize - 1;
            }
            for (int i = 0; i < (pow(2, this->height - 1) - 3 * pow(2, level) + 1) / 2; i++) {
                std::cout << " ";
            }
            while (idx <= idxFinish) {
                std::cout << *(this->heap + idx) << " ";
                idx++;
            }
            std::cout << std::endl;
        }
    }

    ~BinaryHeap(){
        delete[] this->heap;
    }

};
