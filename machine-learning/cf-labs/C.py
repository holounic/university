import math

def minkowski(x1, x2, p):
    return sum(abs(a - b)**p for (a, b) in zip(x1, x2))**(1 / p)

def manhattan(x1, x2):
    return minkowski(x1, x2, 1)

def euclidean(x1, x2):
    return minkowski(x1, x2, 2)

def chebyshev(x1, x2):
    return max([abs(a - b) for (a, b) in zip(x1, x2)])

def uniform(u):
    return 0 if abs(u) >= 1 else 0.5

def triangular(u):
    return 0 if abs(u) > 1 else 1 - abs(u)

def epanechnikov(u):
    return 0 if abs(u) > 1 else 3 / 4 * (1 - u * u)

def quartic(u):
    return 0 if abs(u) > 1 else 15 / 16 * (1 - u * u)**2

def triweight(u):
    return 0 if abs(u) > 1 else 35 / 32 * (1 - u * u)**3

def tricube(u):
    return 0 if abs(u) > 1 else 70 / 81 * (1 - abs(u)**3)**3

def gaussian(u):
    return 1 / math.sqrt(2 * math.pi) * math.exp(-0.5 * u * u)

def cosine(u):
    return 0 if abs(u) > 1 else math.pi / 4 * math.cos(math.pi / 2 * u)

def logistic(u):
    return 1 / (2 + math.exp(u) + math.exp(-u))

def sigmoid(u):
    return 2 / math.pi * 1 / (math.exp(u) + math.exp(-u))

def weight(dist_q_i, dist_q_sorted, window, kernel, h):
    den = h
    if  window == 'fixed':
        den = h
    if window == 'variable':
        den = dist_q_sorted[h]
    return kernel(dist_q_i / den)


def non_param_regr(q, obj, dist, window, kernel, h):
    d_q = [dist(o[0], q) for o in obj]
    d_q_sorted = sorted(d_q)
    if (window == 'fixed' and h == 0) or (window == 'variable' and d_q_sorted[h] == 0):
        return sum([o[1] for o in obj]) / len(obj)
    nomin, denomin = 0, 0
    for i in range(len(obj)):
        w = weight(d_q[i], d_q_sorted, window, kernel, h)
        nomin += obj[i][1] * w
        denomin += w
    if denomin == 0:
        return sum([o[1] for o in obj]) / len(obj)
    return nomin / denomin


n, m = [int(x) for x in input().split()]
obj = [[int(x) for x in input().split()] for _ in range(n)]
obj = [(o[:-1], o[-1]) for o in obj]
q = [int(x) for x in input().split()]

dist = globals()[input()]
kernel = globals()[input()]
window = input()
h = int(input())

print('%.10f'%non_param_regr(q, obj, dist, window, kernel, h))