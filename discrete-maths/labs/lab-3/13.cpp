#include <iostream>
#include <vector>
#include <fstream>
using namespace std;
 
ofstream out("num2perm.out", std::ios::app);
long long n;
 
long long factorial(long long a) {
    if (a == 0) return 1;
    return a * factorial(a - 1);
}
vector <bool> used;
 
void num2perm(long long k) {
    vector<int> p(n + 1);
    for (long long i = 1; i <= n; ++i) {
        long long f = factorial(n - i);
        long long already = k / f;
        k %= f;
        long long av = 0;
        for (long long j = 1; j <= n; ++j) {
            if (!used[j]) {
                ++av;
                if (av == already + 1) {
                    used[j] = true;
                    p[i] = j;
                }
            }
        }
    }
    for (long long i = 1; i < p.size(); ++i) {
        out << p[i] << " ";
    }
}
 
int main() {
    long long k;
    ifstream in("num2perm.in");
    in >> n >> k;
    used.resize(n + 1);
    num2perm(k);
}