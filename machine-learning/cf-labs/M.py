def clever_inner(x, objs):
    res = 0
    for k in objs.keys():
        pref_sum, suf_sum = 0, sum(objs[k])
        x_size = len(objs[k])
        arr = sorted(list(objs[k]))
        for i in range(x_size):
            x = arr[i]
            pref_sum += x
            suf_sum -= x
            res += (x * (1 + i) - pref_sum) - (x * (x_size - i - 1) - suf_sum)
    return res

def clever_outer(k, pairs):
    pref_sum = [0 for _ in range(k + 1)]
    pref_cnt = [0 for _ in range(k + 1)]
    pref_total = 0
    suf_sum = [0 for _ in range(k + 1)]
    suf_cnt = [0 for _ in range(k + 1)]
    suf_total = 0

    for pair in pairs:
        suf_sum[pair[1]] += pair[0]
        suf_cnt[pair[1]] += 1
        suf_total += pair[0]

    res = 0
    for (i, pair) in enumerate(pairs):
        pref_sum[pair[1]] += pair[0]
        pref_cnt[pair[1]] += 1
        pref_total += pair[0]

        suf_sum[pair[1]] -= pair[0]
        suf_cnt[pair[1]] -= 1
        suf_total -= pair[0]

        res += pair[0] * (i + 1 - pref_cnt[pair[1]]) - pref_total + pref_sum[pair[1]] - (pair[0] * (len(pairs) - i - 1 - suf_cnt[pair[1]]) - suf_total + suf_sum[pair[1]])
    return res

if __name__ == '__main__':
    k = int(input())
    n = int(input())

    objs = {}
    x, y = [], []
    pairs = []
    for _ in range(n):
        x_dot, y_dot = map(int, input().split())
        x_by_y = objs.get(y_dot, [])
        x_by_y.append(x_dot)
        objs[y_dot] = x_by_y
        x.append(x_dot)
        y.append(y_dot)
        pairs.append((x_dot, y_dot))
    
    pairs.sort()
    print(clever_inner(x, objs))
    print(clever_outer(k, pairs))