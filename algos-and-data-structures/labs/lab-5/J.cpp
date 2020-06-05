#include <iostream>
#include <vector>
#include <set>
using namespace std;

vector<set <int> > g;
vector<int> element_size, parent;
int general_size;

int resize(int u, int p)
{
    element_size[u] = 1;
    for (int v : g[u])
        if (v != p)
            element_size[u] += resize(v, u);
    return element_size[u];
}

int flexxx_centroid(int u, int p)
{
    for (int v : g[u])
        if (v != p && element_size[v] > general_size / 2)
            return flexxx_centroid(v, u);
    return u;
}

void decompose(int u, int p)
{
    resize(u, -1);
    general_size = element_size[u];
    int centroid = flexxx_centroid(u, -1);
    parent[centroid] = p;
    for (int v : g[centroid])
    {
        g[v].erase(centroid);
        decompose(v, centroid);
    }
    g[centroid].clear();
}

int main(void)
{
    ios_base::sync_with_stdio(0);
    cin.tie(0), cout.tie(0);
    int n, u, v;
    cin >> n;
    g.resize(n), parent.resize(n), element_size.resize(n);

    for (int i = 0; i < n - 1; i++)
    {
        cin >> u >> v;
        g[u - 1].insert(v - 1), g[v - 1].insert(u - 1);
    }
    decompose(0, -1);

    for (int i = 0; i < n; i++) cout << parent[i] + 1 << ' ';
}