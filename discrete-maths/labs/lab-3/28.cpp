#include <iostream>
#include <vector>
#include <fstream>
#include <algorithm>
using namespace std;

ofstream out("nextmultiperm.out", std::ios::app);

vector<int> next_multyperm(vector<int> p) {
    int i = p.size() - 2;
    while ((i >= 0) && (p[i] - p[i + 1] >= 0)) {
        --i;
    }
    if (i < 0) {
        return vector<int>(p.size());
    }
    int j = i + 1;
    while (j < p.size() - 1 && p[j + 1] - p[i] > 0) {
        ++j;
    }
    swap(p[j], p[i]);
    reverse(p.begin() + i + 1, p.end());
    return p;
}

int main() {
    ifstream in("nextmultiperm.in");
    int n;
    in >> n;
    vector<int> p(n);

    for (int i = 0; i < n; ++i) {
        in >> p[i];
    }
    in.close();
    vector<int> res = next_multyperm(p);
    for (int u : res) out << u << " ";
}