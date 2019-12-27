#include <iostream>
#include <fstream>
#include <vector>
#include <cmath>
using namespace std;

ofstream outstream("antigray.out", std::ios::app);

void generate_base(vector<int>& base) {
    int transp = 1;

    for (int i = base.size() - 1; i >=0 && transp != 0; --i) {
        base[i] = (base[i] + transp) % 3;
        transp = (transp == 1 && base[i] == 0 ? 1 : 0);
    }
}

void generate_code(vector<int>& code) {
    for (int i = 0; i < code.size(); ++i) {
        code[i] = (code[i] + 1) % 3;
    }
}

void out(vector<int> v) {
    for (int u : v) {
        outstream << u;
    }
    outstream << '\n';
}

int main() {
    ifstream inputstream("antigray.in");
    int n;
    inputstream >> n;
    inputstream.close();

    vector<int> base(n);

    for (int i = 0; i < pow(3, n - 1); ++i) {
        out(base);
        vector<int> code = base;
        for (int j = 0; j < 2; ++j) {
            generate_code(code);
            out(code);
        }
        generate_base(base);
    }
    return 0;
}