import cmath
import math
import copy

PI = 3.14159265358979323846

def FFT(a):
    size = len(a)
    if size == 1:
        return

    a0 = [0] * (size // 2)
    a1 = [0] * (size // 2)

    i, j = 0, 0
    while i < len(a):
        a0[j] = a[i]
        a1[j] = a[i + 1]
        j += 1
        i += 2
    
    FFT(a0)
    FFT(a1)
    arg = (2 * PI) / size
    w = complex(1, 0)
    deg = complex(math.cos(arg), math.sin(arg))
    for i in range(size // 2):
        a[i] = a0[i] + w * a1[i]
        a[i + size // 2] = a0[i] - w * a1[i]
        w *= deg

def inverse_FFT(a):
    size = len(a)
    if size == 1:
        return

    a0 = [0] * (size // 2)
    a1 = [0] * (size // 2)

    i, j = 0, 0
    while i < size:
        a0[j] = a[i]
        a1[j] = a[i + 1]
        j += 1
        i += 2
    
    inverse_FFT(a0)
    inverse_FFT(a1)

    arg = (-2 * PI) / size
    w = complex(1, 0)
    deg = complex(math.cos(arg), math.sin(arg))
    for i in range(size // 2):
        a[i] = a0[i] + w * a1[i]
        a[i + size // 2] = a0[i] - w * a1[i]
        a[i] /= 2
        a[i + size // 2] /= 2
        w *= deg

def multiply(a, b):
    fa = copy.copy(a)
    fb = copy.copy(b)
    n = 1
    while n < max(len(a), len(b)):
        n <<= 1
    n <<= 1
    while len(fa) != n:
        fa.append(0)
    while len(fb) != n:
        fb.append(0)

    FFT(fa)
    FFT(fb)
    for i in range(n):
        fa[i] *= fb[i]
    inverse_FFT(fa)
    res = [0] * n
    for i in range(n):
        res[i] = int(fa[i].real + 0.5)
    return res


if __name__ == '__main__':
    s = str(input())
    inp = []
    for c in s:
        inp.append(int(c))
    inp.reverse()
    ans = 0
    sq = multiply(inp, inp)
    for i in range(len(inp)):
        if inp[i] != 1:
            continue
        ans += sq[2 * i] // 2
    print(ans)

