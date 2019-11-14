#include <fstream>
#include <vector>
#include <algorithm>
using namespace std;

void slide(string& str) {
    char buffered = str[0];
    for (int i = 0; i < str.length() - 1; ++i) {
        str[i] = str[i + 1];
    }
    str[str.length() - 1] = buffered;
}

int main() {
    ifstream inputstream("bwt.in");

    string s;
    inputstream >> s;
    inputstream.close();

    vector <string> bwt;

    for (int i = 0; i < s.length(); ++i) {
        slide(s);
        bwt.push_back(s);
    }

    sort(bwt.begin(), bwt.end());
    ofstream outstream("bwt.out", std::ios::app);
    for (int i = 0; i < bwt.size(); ++i) {
        outstream << bwt[i][bwt[i].length() - 1];
    }
    return 0;
}
