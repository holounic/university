#include <iostream>
#include <vector>
#include <fstream>
#include <algorithm>
using namespace std;

bool check(vector<int> v, int k) {
    int cnt = 0;
    for (int bit : v) {
        cnt += bit;
    }
    return cnt == k;
}

int main() {
ifstream inputstream("choose.in");
    int n, k;
    inputstream >> n >> k;
    inputstream.close();

    vector <vector<int> > code(2);
    code[0].push_back(0);
    code[1].push_back(1);

    while (code[0].size() != n) {
        for (int i = code.size() - 1; i >= 0;  --i) {
            code.push_back(code[i]);
        }
        int m = code.size() / 2;
        for (int i = 0; i < code.size() / 2; ++i) {
            code[i].push_back(0);
            code[i + m].push_back(1);
        }
    }
    sort(code.begin(), code.end());
    ofstream outstream("choose.out", std::ios::app);

    for (int i = code.size() - 1; i >=0; --i) {
        if (!check(code[i], k)) continue;
        
        for (int j = 0; j < code[i].size(); ++j) {
            if (code[i][j] == 1) outstream << j + 1 << " ";
        }
        outstream << '\n';
    }
}
