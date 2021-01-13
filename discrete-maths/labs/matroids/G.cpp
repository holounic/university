#include <iostream>
#include <vector>
#include <stack>
#include <set>
#include <queue>
using namespace std;

#define p_nxt p[next_k]
#define el_num elements[num]
#define u_cur cur_e.u
#define v_cur cur_e.v
#define id_cur cur_e.id
#define pb push_back
#define eck emplace_back
#define g_nxt g[next_k]
#define cookie {cur_v, -1}

struct edge {
    int u, v, id;
};

struct u_id {
    int u, id;
};
void dfs(int v, int k, vector<u_id> &p);

bool operator<(u_id const &one, u_id const &another) {
    if (one.u < another.u) return true;
    return  (one.u == another.u && one.id < another.id);
}

struct dsu {
    vector<int> parents, rank;

    dsu(int n) {
        rank.resize(n);
        for (int i = 0; i < n; ++i) {
            parents.pb(i);
        }
    }

    int get_set(int v) {
        return (v == parents[v] ? parents[v] : parents[v] = get_set(parents[v]));
    }

    void union_sets(int v, int u) {
        v = get_set(v), u = get_set(u);
        if (rank[v] < rank[u]) {
            swap(u, v);
        }
        parents[u] = v;
        rank[v] += (rank[v] == rank[u] ? 1 : 0);
    }
};

int n, m, p_max = 1;
vector<dsu> dsus;
vector<set<int> > spannings;
vector<int> elements;
vector<vector<set<u_id> > > g;
vector<edge> edges;

const u_id PLACEHOLDER = {-1, -1};

void update(int e) {
    vector<int> number(m, -1);
    vector<vector<u_id> > p(1, vector<u_id>(n, PLACEHOLDER));
    int cur_v = edges[e].u;
    p[0][cur_v] = cookie;
    dfs(cur_v, 0, p[0]);

    int cur_p = 1;
    while (p_max > cur_p && spannings[cur_p - 1].size() == n - 1) {
        ++cur_p;
        p.eck(n, PLACEHOLDER);
        p[cur_p - 1][cur_v] = cookie;
        dfs(cur_v, cur_p - 1, p[cur_p - 1]);
    }

    queue<int> q;
    q.push(e);
    while (true) {
        vector<int> cur_marked;
        while (q.size()) {
            int id = q.front();
            q.pop();
            edge cur_e = edges[id];
            int next_k = (1 + elements[id]) % cur_p;
            if (dsus[next_k].get_set(v_cur) == dsus[next_k].get_set(u_cur)) {
                int u = (p_nxt[u_cur].u == u_cur || number[p_nxt[u_cur].id] != -1 ? v_cur : u_cur);
                stack<int> stack;

                while (p_nxt[u].u != u && number[p_nxt[u].id] == -1) {
                    stack.push(p_nxt[u].id);
                    u = p_nxt[u].u;
                }

                int cur_id;
                while (!stack.empty()) {
                    cur_id = stack.top();
                    stack.pop();
                    cur_marked.pb(cur_id), q.push(cur_id), number[cur_id] = id;
                }

            } else {
                dsus[next_k].union_sets(v_cur, u_cur);
                while (number[id_cur] != -1) {
                    int num = id_cur;
                    u_id id_v = {v_cur, id_cur};
                    u_id id_u = {u_cur, id_cur};
                    g[el_num][u_cur].erase(id_v), g_nxt[u_cur].insert(id_v);
                    g[el_num][v_cur].erase(id_u), g_nxt[v_cur].insert(id_u);
                    
                    spannings[el_num].erase(num), spannings[next_k].insert(num);
                    swap(next_k, el_num);
                    cur_e = edges[number[num]];
                }
                int num = id_cur;
                u_id id_v = {v_cur, id_cur};
                u_id id_u = {u_cur, id_cur};
                spannings[next_k].insert(id_cur), g_nxt[u_cur].insert(id_v), g_nxt[v_cur].insert(id_u);
                el_num = next_k;
                return;
            }
        }

        if (++cur_p > p_max) {
            ++p_max, g.eck(n), spannings.eck(), dsus.eck(n);
        }

        p.eck(n, PLACEHOLDER);
        p[cur_p - 1][cur_v] = cookie;
        dfs(cur_v, cur_p - 1, p[cur_p - 1]);

        for (int i = 0; i < cur_marked.size(); ++i) {
            q.push(cur_marked[i]);
        }
    }
}

int main(void) {
    freopen("multispan.in", "r", stdin);
    freopen("multispan.out", "w", stdout);

    cin >> n >> m;
    spannings.eck();
    dsus.eck(n);
    elements.assign(m, -1);
    g.eck(n);

    for (int i = 0; i < m; ++i) {
        int u, v;
        cin >> u >> v;
        edges.pb({--u, --v, i});
    }

    for (int i = 0; i < m; ++i) {
        update(i);
    }

    int num_good_spannings;
    for (num_good_spannings = 1; num_good_spannings < spannings.size() + 1 && num_good_spannings <= p_max && spannings[num_good_spannings - 1].size() == n - 1; ++num_good_spannings) {}

    cout << --num_good_spannings << endl;
    for (int i = 0; i < num_good_spannings; ++i) {
        for (int e_id : spannings[i]) {
            cout << ++e_id << ' ';
        }
        cout << endl;
    }
}

void dfs(int v, int k, vector<u_id> &p) {
    for (auto e : g[k][v]) {
        if (p[e.u].u != -1) continue;
        p[e.u] = {v, e.id};
        dfs(e.u, k, p);
    }
}