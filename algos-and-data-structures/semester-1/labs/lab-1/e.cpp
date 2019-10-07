#include <iostream>
using namespace std;
 
void slowQuickSort(long* a, int l, int r) {
    if (l >= r) {
        return;
    }
    long x = a[(l + r) / 2];
    int i = l;
    int j = r;
 
    while (i <= j) {
        while(a[i] < x) i++;
 
        while (a[j] > x) j--;
 
        if (i <= j) {
            swap(a[i++], a[j--]);
        }
    }
 
    if (l < j) slowQuickSort(a, l , j);
    if (i < r ) slowQuickSort(a, i, r);
}
 
long slowBinarySearchByCPlusPlusDaddy(long* a, int l, int r, long num, bool mode) {
    if (r - l <= 1) {
        return r;
    }
    int m = (l + r) / 2;
    //mode true: l
    if (mode) {
            if (a[m] < num) {
                l = m;
            } else {
                r = m;
            }
        } else {
            if (a[m] <= num) {
                l = m;
            } else {
                r = m;
            }
        }
    return slowBinarySearchByCPlusPlusDaddy(a, l, r, num, mode);
}
 
int main(void) {
    int n;
    cin >> n;
    long* a = new long[n];
    for (int i = 0; i < n; i++) cin >> a[i];
    slowQuickSort(a, 0, n - 1);
    int k;
    cin >> k;
    for (int i = 0; i < k; i++) {
        long l, r;
        cin >> l >> r;
        long lResp = slowBinarySearchByCPlusPlusDaddy(a, -1, n, l, true);
        long rResp = slowBinarySearchByCPlusPlusDaddy(a, -1, n, r, false);
        cout << rResp - lResp << endl;
    }
}