def readln():
    return [int(x) for x in input().split()]

def signum(x):
    if x == 0:
        return 0
    return -1 if x <= 0 else 1

def d_smape(x, y_hat, f):
    res = [.0 for _ in x] + [.0]
    y = f(x)
    l = signum(y - y_hat) * (abs(y) + abs(y_hat))
    r = signum(y) * abs(y - y_hat)
    b = (abs(y) + abs(y_hat))**2 
    common = (l - r) / b
    for i in range(len(x)):
        res[i] = x[i] * common
    res[-1] = common
    return res


def lin_func(coeff):
    def f(x):
        return coeff[-1] + sum([a * b for (a, b) in zip(x, coeff)])
    return f

n, _ = readln()

obj = [readln() for _ in range(n)]
obj = [(o[:-1], o[-1]) for o in obj]
coeff = readln()

func = lin_func(coeff)

for (x, y) in obj:
    print(*d_smape(x, y, func))
