#include <iostream>
#include <fstream>
#include <vector>
using namespace std;
ofstream out("nextvector.out", std::ios::app);

vector<int> next(vector<int> s) {
    int t = 1;
    int i = s.size() - 1;

    while (t == 1 && i >= 0) {
        s[i] = (s[i] + t) & 1;
        t = (s[i] == 0 && t == 1 ? 1 : 0);
        --i;
    }

    if (t == 1) {
        s[0] = -1;
    }
    return s;
}

vector<int> prev(vector<int> s) {
    int t = 1;
    int i = s.size() - 1;

    while(t == 1 && i >= 0) {
        if (s[i] == 1) {
            s[i] -= t;
            t = 0;
            --i;
            continue;
        }

        if (s[i] == 0) {
            s[i] = 1;
            t = 1;
            --i;
            continue;
        }
    }
    return s;
    // int start = 0;
    // while (s[start] == 0) ++start;
    // vector<int> res;

    // for (int i = start; i < s.size(); ++i) {
    //     res.push_back(s[i]);
    // }
    // return res;
}

int main() {
    ifstream in("nextvector.in");
    string s;
    in >> s;
    in.close();
    vector<int> v;
    bool zero = true;
    for (int i = 0; i < s.length(); ++i) {
        if (s[i] == '1') zero = false;
        v.push_back(s[i] - '0');
    }

    vector<int> prev_v = prev(v);
    vector<int> next_v = next(v);

    if (zero) out << '-';
    else {
        for (int u : prev_v) out << u;
    }
    out << '\n';
    if (next_v[0] == -1) {
        out << '-';
    } else {
        for (int u  : next_v) out << u;
    }
}