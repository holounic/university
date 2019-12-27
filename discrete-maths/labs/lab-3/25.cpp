#include <iostream>
#include <fstream>
#include <vector>
using namespace std;

ofstream out("nextchoose.out", std::ios::app);

vector<int> next_choose(int n, int k, vector<int> p) {
    vector<int> temp(p);
    temp[k] = n + 1;

    int i = k - 1;
    while ((2 > temp[i + 1] - temp[i]) && (i >= 0)) {
        --i;
    }
    if (i < 0) {
        out << -1;
        exit(0);
    }
    ++temp[i];
    for (int j = i + 1; j < k; ++j) {
        temp[j] = temp[j - 1] + 1;
    }
    return temp;

}

int main() {
    ifstream in("nextchoose.in");

    int n, k;
    in >> n >> k;
    vector<int> p(k + 1);

    for (int i = 0; i < k; ++i) {
        in >> p[i];
    }
    in.close();
    
    vector<int> res = next_choose(n, k, p);
    for (int i = 0; i < k; ++i) out << res[i] << " ";
}