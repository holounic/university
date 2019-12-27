#include <iostream>
#include <vector>
#include <fstream>
#include <algorithm>
using namespace std;

ofstream out("partition.out", std::ios::app);
int n;

void generate(int t, int sum, int l, vector<int> buff) {
    if (sum == n) {
        for (int i = 0; i < l; ++i) {
            out << buff[i] << (i + 1 == l ? "" : "+");
        }
        out << '\n';
    }
    int b = 1;

    if (l > 0) {
        b = buff[l - 1];
    }

    for (int i = 1; i <= t; ++i) {
        if (b <= i && sum + i <= n) {
            buff[l] = i;
            generate(t - 1, sum + i, l+ 1, buff);
        }
    }
}


int main() {
    ifstream in("partition.in");
    in >> n;
    in.close();
    vector<int> buff(n);
    generate(n, 0, 0, buff);
}