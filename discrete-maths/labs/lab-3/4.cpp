#include <iostream>
#include <vector>
#include <set>
#include <fstream>
#include <string>
using namespace std;

void slide(string& s) {
    for (int i = 0; i < s.length() - 1; ++i) {
        s[i] = s[i + 1];
    }
}

int main() {
    ifstream inputstream("chaincode.in");
    int n;
    inputstream >> n;
    inputstream.close();

    string chain = string(n, '0');
    set<string> used;

    ofstream outstream("chaincode.out", std::ios::app);
    while (true) {
        outstream << chain << endl;
        used.insert(chain);
        slide(chain);
        chain[n - 1] = '1';
        if (used.find(chain) == used.end()) {
            continue;
        }
        chain[n - 1] = '0';
        if (used.find(chain) == used.end()) {
            continue;
        }
        return 0;
    }
}