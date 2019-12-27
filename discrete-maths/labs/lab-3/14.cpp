#include <iostream>
#include <vector>
#include <fstream>
using namespace std;

long long f(int n) {
    if (n == 0) return 1;
    return n * f(n - 1);
}

long long compute(vector<int> p) {
    long long num = 0;
    int l = p.size();
    for (int i = 0; i < l; ++i) {
        long long temp = 0;
        for (int j = i + 1; j < l; ++j) {
            if (p[j] < p[i]) ++temp;
        }
        num += temp * f(l - i - 1);
    }
    return num;
}

int main(void) {
    ifstream in("perm2num.in");
    int n;
    in >> n;
    vector<int> p(n);

    for (int i = 0; i < n; ++i) {
        in >> p[i];
    }
    in.close();
    ofstream out("perm2num.out", std::ios::app);
    out << compute(p);
}