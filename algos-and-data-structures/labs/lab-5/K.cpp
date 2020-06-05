#pragma comment(linker, "/stack:200000000")
#pragma GCC optimize("Ofast")
#pragma GCC target("sse,sse2,sse3,ssse3,sse4,popcnt,abm,mmx,avx,tune=native")

#include <iostream>
#include <vector>
#include <set>
#include <algorithm>
#include <map>
using namespace std;

int n;

vector<set <int> > g;
vector<int> element_size, d, parent;
int general_size;
vector <vector <int> > mins;
vector <bool> used;
vector <vector <pair <int, int> > > dists; 
vector <map <int, int> > dists_m;
set <int> centroids;


int flexxxxx_resize(int u, int p)
{
    element_size[u] = 1;
    for (int v : g[u])
        if (v != p)
            element_size[u] += flexxxxx_resize(v, u);
    return element_size[u];
}

int flexxx_centroid(int u, int p)
{
    for (int v : g[u])
        if (v != p && element_size[v] > general_size / 2)
            return flexxx_centroid(v, u);
    return u;
}

void flexx_dfs(int u, int par, int dist) {
    if (used[u]) return;
    used[u] = true;
    d[u] = dist;
    for (auto child : g[u]) {
        if (!used[child]) {
            dists[par].push_back(make_pair(dist + 1, child));
            dists_m[par][child] = dist + 1;
            flexx_dfs(child, par, dist + 1);
        }
    }
}

void flex_component(int c) {
    flexx_dfs(c, c, 0);
    int cur_min = c;
    if (dists[c].empty()) {
        mins[c].push_back(c);
        return;
    }
    sort(dists[c].begin(), dists[c].end());
    mins[c].assign(dists[c][dists[c].size() - 1].first + 1, c);
    for (int i = 0; i < dists[c].size(); ++i) {
        mins[c][dists[c][i].first] = min(min(dists[c][i].second, mins[c][dists[c][i].first]), mins[c][dists[c][i].first - 1]);
    }
}

int flex_decompose(int u, int p) {
    flexxxxx_resize(u, -1);
    general_size = element_size[u];
    int centroid = flexxx_centroid(u, -1);
    parent[centroid] = p;
    centroids.insert(centroid);
    used.assign(n, false);

    flex_component(centroid);

    for (int v : g[centroid])
    {
        g[v].erase(centroid);
        flex_decompose(v, centroid);
    }
    return centroid;
}

int solve(int u, int dist) {
    int cur_min = u;
    for (int c = u; c != -1; c = parent[c]) {
        int uc = dists_m[c][u];
        if (uc > dist) continue;
        int vc = dist - uc;
        cur_min = min(cur_min, mins[c][min(vc, (int)mins[c].size() - 1)]);     
    }
    return cur_min;
}


int main(void)
{
    ios_base::sync_with_stdio(0);
    cin.tie(0), cout.tie(0);

    int u, v, m, dist;
    cin >> n >> m;
    g.resize(n), element_size.resize(n), d.resize(n), parent.resize(n), mins.resize(n), dists.resize(n), dists_m.resize(n);

    for (int i = 0; i < n - 1; ++i)
    {
        cin >> u >> v;
        g[u - 1].insert(v - 1), g[v - 1].insert(u - 1);
    }

    int centroid = flex_decompose(0, -1);

    for (int i = 0; i < m; ++i) {
        cin >> u >> dist;
        int ans = solve(u - 1, dist);
        cout <<  (ans + 1) << '\n';
    }

}