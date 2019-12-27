#include <iostream>
#include <fstream>
using namespace std;

string next(string sequence) {
    int open = 0;
    int close = 0;

    for (int i = sequence.size() - 1; i >= 0; --i) {
        if (sequence[i] == '(') {
            ++open;
            if (close > open) break;
        } else ++close;
        
    }

    string next = sequence.substr(0, sequence.size() - open - close);
    if (next.size() == 0) return "-";
    next += ")";
    for (int i = 0; i < open; ++i) next += "(";
    for (int i = 0; i < close - 1; ++i) next += ")";
    
    return next;
}

int main() {

    string sequence;
    ifstream in("nextbrackets.in");
    in >> sequence;
    in.close();

    ofstream out("nextbrackets.out", std::ios::app);
    out << next(sequence) << '\n';
}