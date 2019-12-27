#include <iostream>
#include <fstream>
#include <vector>
using namespace std;

int n;
ofstream out("brackets.out", std::ios::app);

void generate(string sequence, int open, int close) {
    if (open + close > 2 * n) return;

    if (open + close == 2 * n) {
        out << sequence << '\n';
        return;
    }

    if (open < n) {
        generate(sequence + "(", open + 1, close);
    }
    if (open > close) {
        generate(sequence + ")", open, close + 1);
    }
}

int main() {
    ifstream in("brackets.in");
    in >> n;
    in.close();
    generate("", 0, 0);
    return 0;
}