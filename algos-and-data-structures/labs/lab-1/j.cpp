#include <iostream>
#include <cmath>
using namespace std;
long p, f;
double a;

double g(double x) {
    double field = sqrt((1 - a) * (1 - a) + x * x) / p;
    double forest =  sqrt(a * a + (1 - x) * (1 - x)) / f;
    return field + forest;
}

int main(void) {
    cin >> p >> f >> a;

    double l, r;
    l = -0.1;
    r = 1.11;
    while (r - l > 0.00001) {

        double m2 = r - (r - l) / 3;
        double m1 = l + (r - l) / 3;
        if (g(m1) < g(m2)) {
            r = m2;
        } else {
            l = m1;
        }
    }
    cout.precision(4);
    cout << l << "\n";
}