import copy
import queue

class dsu:
    def __init__(self, n):
        self.rank = [0] * n
        self.parents = list(range(n))
    
    def get_set(self, v):
        if not self.parents[v] == v:
            self.parents[v] = self.get_set(self.parents[v])
        return self.parents[v]
    
    def union_sets(self, u, v):
        u_p = self.get_set(u)
        v_p = self.get_set(v)
        if self.rank[u_p] < self.rank[v_p]:
            self.parents[v_p] = u_p
        else:
            self.parents[u_p] = v_p
        if self.rank[u_p] == self.rank[v_p]:
            self.rank[v_p] += 1


def get_path(n, m, g, s, t, f):
    d = [1000000] * m
    q = queue.Queue()

    for e in s:
        q.put(e)
        d[e] = 0
        f[e] = -1
    
    while not q.empty():
        cur = q.get()
        for e in g[cur]:
            if d[cur] < d[e] - 1:
                d[e] = 1 + d[cur]
                f[e] = cur
                q.put(e)

    v = -1
    w = 1000000
    for e in t:
        if d[e] < w:
            v = e
            w = d[e]
    
    return v

def build(n, m, num_colors, edges, used):
    g = [[]] * m

    for i in range(m):
        if not used[i]:
            continue
        
        included_colors = [False] * num_colors
        cur_dsu = dsu(n)
        for j in range(m):
            if not used[j] or i == j:
                continue
            e = edges[j]
            included_colors[e[2]] = True
            cur_dsu.union_sets(e[0], e[1])

        for j in range(m):
            if used[j]:
                continue
            e = edges[j]
            if not included_colors[e[2]]:
                g[j].append(i)
            if not cur_dsu.get_set(e[0]) == cur_dsu.get_set(e[1]):
                g[i].append(j)

    s = []
    t = []
    included_colors = [False] * num_colors
    cur_dsu = dsu(n)

    for i in range(m):
        if not used[i]:
            continue
        e = edges[i]
        included_colors[e[2]] = True
        cur_dsu.union_sets(e[0], e[1])
    
    for i in range(m):
        if used[i]:
            continue
        e = edges[i]
        if not cur_dsu.get_set(e[0]) == cur_dsu.get_set(e[1]):
            s.append(i)
        if not included_colors[e[2]]:
            t.append(i)
    
    f = [-1] * m
    p = get_path(n, m, g, s, t, f)
    if p == -1:
        return False
    while p != -1:
        used[p] = not used[p]
        p = f[p]
    return True 


input_file = open("rainbow.in")
n, m = [int(x) for x in input_file.readline().split()]

num_colors = 0
edges = []
used = [False] * m

for _ in range(m):
    u, v, c = [int(x) for x in input_file.readline().split()]
    edges.append([u - 1, v - 1, c - 1])
    num_colors = max(num_colors, c)

while True:
    res = build(n, m, num_colors, edges, used)
    if not res:
        break

rainbow_forest = []
for i in range(m):
    if used[i]:
        rainbow_forest.append(i)

output_file = open("rainbow.out", "w")
# print(len(rainbow_forest))
output_file.write(str(len(rainbow_forest)) + "\n")
for e in rainbow_forest:
    # print(e + 1)
    output_file.write(str(e + 1) + " ")

    