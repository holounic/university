if __name__ == '__main__':
    k1, k2 = [int(x) for x in input().split()]
    n = int(input())
    X = {}
    X1, X2 = {}, {}
    for _ in range(n):
        pair = tuple(int(x) for x in input().split())
        f = X.get(pair, 0)
        X[pair] = f + 1
        f1 = X1.get(pair[0], 0)
        f2 = X2.get(pair[1], 0)
        X1[pair[0]] = f1 + 1
        X2[pair[1]] = f2 + 1
    
    chi = n
    for pair in X.keys():
        e = X1[pair[0]] * X2[pair[1]] / n
        n_dot = X[pair] - e
        chi += (n_dot - e) * (n_dot + e) / e
    print(chi)



