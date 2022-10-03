def get_rank(a):
    d_a = {}
    cur_rank = 1
    for a_dot in sorted(a):
        if a_dot in d_a:
            continue
        d_a[a_dot] = cur_rank
        cur_rank += 1
    return d_a
        

if __name__ == '__main__':
    n = int(input())
    x, y = [], []
    for _ in range(n):
        x_dot, y_dot = tuple(int(i) for i in input().split())
        x.append(x_dot)
        y.append(y_dot)
    
    d_x, d_y = get_rank(x), get_rank(y)
    d_squared = sum([(d_x[x_dot] - d_y[y_dot])**2 for (x_dot, y_dot) in zip(x, y)])
    print(1 - 6 * d_squared / (n**3 - n))
