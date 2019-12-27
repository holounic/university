#include <iostream>
#include <vector>
#include <fstream>
using namespace std;

int main() {
    ifstream inputstream("gray.in");
    int n;
    inputstream >> n;
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

    ofstream outstream("gray.out", std::ios::app);

    for (int i = 0; i < code.size(); ++i) {
        for (int j = 0; j < code[i].size(); ++j) {
            outstream << code[i][j];
        }
        outstream << '\n';
    }
}