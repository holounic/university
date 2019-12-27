#include <iostream>
#include <fstream>
#include <vector>
using namespace std;

void generate(vector<int>& codes) {
    int i = codes.size() - 1;
    int transp = 1;
    do {
        codes[i] = (transp + codes[i]) % 2;
        transp = (transp == 1 && codes[i] == 0 ? 1 : 0);
        --i;
    } while (i >= 0 && transp != 0);
}

int main() {
    ifstream inputstream("allvectors.in");
    int n;
    inputstream >> n;
    inputstream.close();

    vector<int> code(n);
    int count = (1<<n);
    ofstream outstream("allvectors.out", std::ios::app);

    while (count-- > 0) {
        for (int i = 0; i < n; ++i) {
            outstream << code[i];
        }
        outstream << '\n';
        generate(code);
    }


} 