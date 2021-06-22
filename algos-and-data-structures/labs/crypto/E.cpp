#include <iostream>
#include <vector>
#include <complex>
#include <cmath>
#include <algorithm>
using namespace std;

const long double PI =  3.14159265358979323846;

void fft(vector<complex<long double> > &a) {
    if (a.size() == 1) {
        return;
    }
    int size = a.size();
    vector<complex<long double> > a0(size / 2), a1(size / 2);
    for (int i = 0, j = 0; i < a.size(); i += 2, ++j) {
        a0[j] = a[i], a1[j] = a[i + 1];
    }
    fft(a0);
    fft(a1);
    long double arg = 2 * PI / ((long double) a.size());
    complex<long double> w(1), degree(cos(arg), sin(arg));
    for (int i = 0; i < a.size() / 2; i++) {
        a[i] = a0[i] + w * a1[i];
        a[i + a.size() / 2] = a0[i] - w * a1[i];
        w *= degree;
    }
}

void inverse_fft(vector<complex<long double> > &a) {
    int size = a.size();
    if (size == 1) {
        return;
    }
    vector<complex<long double> > a0(size / 2), a1(size / 2);
    for (int i = 0, j = 0; i < size; i += 2, j++) {
        a0[j] = a[i];
        a1[j] = a[i + 1];
    }
    inverse_fft(a0);
    inverse_fft(a1);
    long double arg =  -2 * PI / ((long double) size);
    complex<long double> w(1), deg(cos(arg), sin(arg));
    for (int i = 0; i < size / 2; ++i) {
        a[i] = (a0[i] + w * a1[i]);
        a[i + size / 2] = a0[i] - w * a1[i];
        a[i] /= 2;
        a[i + size / 2] /= 2;
        w *= deg;
    }
}

vector<int> multiply(vector<int> &a, vector<int> &b) {
    vector<complex<long double> > fa(a.begin(), a.end()), fb(b.begin(), b.end());
    int n = 1;
    while (n < max(a.size(), b.size())) {
        n <<= 1;
    }
    n <<= 1;
    fa.resize(n), fb.resize(n);
    fft(fa);
    fft(fb);
    for (int i = 0; i < n; i++) {
        fa[i] *= fb[i];
    }
    inverse_fft(fa);
    vector<int> res(n);
    for (int i = 0; i < n; i++) {
        res[i] = int(0.5 + fa[i].real());
    }
    return res;
}

int main(void) {
    int n;
    cin >> n;
    vector<int> a(n + 1), b(n + 1);
    for (int i = 0; i < n + 1; ++i) {
        cin >> a[i];
    }
    for (int i = 0; i < n + 1; ++i) {
        cin >> b[i];
    }
    auto c = multiply(a, b);
    for (int i = 0; i < 2 * n + 1; ++i) {
        cout << c[i] << " ";
    }
}