#include <iostream>
#include <vector>
#include <fstream>
#include <algorithm>
using namespace std;

vector<int> next_perm(vector<int> p, int n) {
  for (int i = n - 2; i >= 0; --i) {
      if (p[i] < p[i + 1]) {
          int min = i + 1;
          for (int j = i + 1; j <= n; ++j) {
              if (p[j] < p[min] && p[j] > p[i]) min = j;
          }
          swap(p[min], p[i]);
          reverse(p.begin() + i + 1, p.end());
          return p;
      }
  }
  return vector<int> (n, 0);
}

vector<int> prev_perm(vector<int> p, int n) {
    for (int i = n - 2; i >= 0; --i) {
        if (p[i] > p[i + 1]) {
            int max = i + 1;
            for (int j = i + 1; j < n; ++j) {
                if (p[j] > p[max] && p[j] < p[i]) max = j;
            }
            swap(p[max], p[i]);
            reverse(p.begin() + i + 1, p.end());
            return p;
        }
    }
    return vector<int> (n, 0);
}

int main() {
    int n;
    ifstream in("nextperm.in");
    in >> n;
    vector<int> p(n);
    for (int i = 0; i < n; ++i) {
        in >> p[i];
    }
    vector<int> next = next_perm(p, n);
    vector<int> prev = prev_perm(p, n);

    ofstream out("nextperm.out", std::ios::app);
    for (int u : prev) out << u << " ";
    out << '\n';
    for (int u : next) out << u << " ";

}