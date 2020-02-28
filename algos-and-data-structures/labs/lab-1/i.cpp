#include <iostream>
#include <cmath>
using namespace std;
 
float f(float x) {
    return sqrt(x) + pow(x, 2);
}
 
 
float binarySearchByCPlusPlusDaddy(float l, float r, float c) {
    if (r - l <= 0.000001) {
        return r;
    }
 
    float x = (l + r) / 2;
    float y = f(x);
    if (fabs(y - c) <= 0.000001) {
        return x;
    }
    if (y < c) {
        l = x;
    } else {
        r = x;
    }
    return binarySearchByCPlusPlusDaddy(l, r, c);
}
 
int main(void) {
    float c;
    cin >> c;
    cout << fixed << binarySearchByCPlusPlusDaddy(0.8, c, c) << endl;
}