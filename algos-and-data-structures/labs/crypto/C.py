def swap(a, b):
    a += b
    b = a - b
    a -= b
    return a, b

if __name__ == '__main__':
    a, b, n, m = [int(x) for x in input().split()]
    if n < m:
        a, b = swap(a, b)
        m, n = swap(m, n)
    
    for k in range(n):
        s = a + k * n
        if (s % m == b):
            print(s)
            exit(0)
        