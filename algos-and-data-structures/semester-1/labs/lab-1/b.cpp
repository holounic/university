#include <iostream>
#include <vector>
using namespace std;
 
int main() {
    vector <int> a;
    int temp;
 
    while (cin >> temp) {
        a.push_back(temp);
    }
    int* c = new int[101];
    for (int i = 0; i < 101; i++) *(c + i) = 0;
    for (int i = 0; i < a.size(); i++) *(c + a[i]) += 1;
 
    int k = 0;
    for (int i = 0; i < 101; i++) {
        for (int j = 0; j < c[i]; j++) {
            a[k] = i;
            cout << a[k] << " ";
            k++;
        }
    }
}