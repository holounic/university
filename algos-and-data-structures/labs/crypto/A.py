def get_divs(n):
    s = 2
    while not n == 1:
        if n % s == 0:
            print(s, end=' ')
            n /= s
            continue
        if s == 2:
            s += 1
            continue
        else:
            s += 2

n = int(input())
get_divs(n)