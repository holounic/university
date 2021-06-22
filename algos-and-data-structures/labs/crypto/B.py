import random

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

def miller_rabin(n):
    if n == 1:
        return False
    if n == 2 or n == 3:
        return True
    
    s, t = pow_two(n - 1)
    for i in range(10):
        a = random.randint(2, n - 1)
        x = bin_pow(a, t, n)
        if x == 1 or x == n - 1:
            continue
        
        c = False
        for j in range(s - 1):
            x = (x * x) % n
            if x == 1:
                break
            if x == n - 1:
                c = True
                break
        if c:
            continue
        return False
    return True


if __name__ == '__main__':
    n = int(input())
    for _ in range(n):
        x = int(input())
        result = miller_rabin(x)
        print('NO' if not result else 'YES')