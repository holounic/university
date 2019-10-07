#include <iostream>
using namespace std;
long long w, h, n;
 
bool f(long long a) {
    return (a / w) * (a / h) >= n;
}
 
int main(void) {
    cin >> w >> h >> n;
    long long l = min(w, h);
    long long r = n * max(w, h);
 
    while (r > l) {
        long long x = (r + l) / 2;
        if (f(x)) {
            r = x;
        } else {
            l = x + 1;
        }
    }
    
    cout << l << endl;
}