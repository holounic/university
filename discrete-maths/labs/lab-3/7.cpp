#include <fstream>
#include <vector>
using namespace std;

vector<bool> used;
int n;

ofstream outstream("permutations.out", std::ios::app);

char to_char(int a) {
    return '0' + a;
}

void generate(int a, string prefix) {
    used[a] = true;
    bool leaf = true;

    for (int i = 1; i <= n; ++i) {
        if (!used[i]) {
            leaf = false;
            generate(i, prefix  + to_char(a) + " ");
        }
    }
    used[a] = false;
    if (leaf) {
        outstream << prefix + to_char(a) << '\n';
    }
}

int main() {
    ifstream inputstream("permutations.in");
    inputstream >> n;
    inputstream.close();

    used.resize(n + 1, false);
    for (int i = 1; i <= n; ++i) {
        generate(i, "");
    }
}
