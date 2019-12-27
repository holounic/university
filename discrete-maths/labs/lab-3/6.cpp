#include <iostream>
#include <cmath>
#include <vector>
#include <fstream>
using namespace std;

ofstream outstream("vectors.out", std::ios::app);

void out(vector<int> v) {
    for (int u : v) {
        outstream << u;
    }
    outstream << '\n';
}

vector<int> generate(vector<int> v) {
    int transp = (1 + v[v.size() - 1]) / 2;
    v[v.size() - 1] = (1 + v[v.size() - 1]) % 2;

    for (int i = v.size() - 2; i >= 0; --i) {
        v[i] = (transp + v[i]) % 2;

        if (v[i] + v[i + 1]  == 2) {
            v[i] = 0;
            v[i + 1] = 0;
            transp = 1;
            continue;
        }
        transp = (transp == 1 && v[i] == 0 ? 1 : 0);
    }
    if (transp == 1) v[0] = -1;
    return v;
}


int main() {
    int n;
    ifstream inputstream("vectors.in");
    inputstream >> n;
    inputstream.close();

    vector<vector<int> > vectors;
    vectors.push_back(vector<int>(n));

    while (vectors[vectors.size() - 1][0] != -1) {
        vectors.push_back(generate(vectors[vectors.size() - 1]));
    }

    outstream << vectors.size() - 1 << '\n';
    for (int i = 0; i < vectors.size() - 1; ++i) {
        out(vectors[i]);
    }
    return 0;
}