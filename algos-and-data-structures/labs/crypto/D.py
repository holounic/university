import sys

def gcd(a, b):
    return gcd(b % a, a) if a > 0 else b

def pow_two(n):
    i = 0
    while n % 2 == 0:
        i += 1
        n //= 2
    return i, n

def bin_pow(a, n, m):
    if n == 0:
        return 1
    if n % 2 == 0:
        b = bin_pow(a, n // 2, m)
        return (b * b) % m
    return (a * bin_pow(a, n - 1, m)) % m

def gcd_ex(a, b):
    if not a:
        return 0, 1, b
    x, y, ans = gcd_ex(b % a, a)
    return y - (b // a) * x, x, ans

def rev_element(a, m):
    x, y, ans = gcd_ex(a, m)
    assert ans == 1
    return (x % m + m) % m

if __name__ == '__main__':
    sys.setrecursionlimit(5000)
    n, e, c  = int(input()), int(input()), int(input())
    i, f = 2, 0
    while i * i <= n:
        if not n % i == 0:
            i += 1
            continue
        f = (i - 1) * (n // i - 1)
        break
    print(bin_pow(c, rev_element(e, f), n))
