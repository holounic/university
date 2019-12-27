#include <iostream>
#include <vector>
#include <fstream>
using namespace std;

int n;
ofstream out("subsets.out", std::ios::app);

void generate(int a, string prefix) {
    prefix += to_string(a) + " ";
    out << prefix << endl;
    for (int j = a + 1; j <= n; ++j) {
        generate(j, prefix);
    }
}

int main() {
    ifstream in("subsets.in");
    in >> n;
    in.close();
    out << "" << endl;
    for (int i = 1; i <=n; ++i) {
        generate(i, "");
    }
}