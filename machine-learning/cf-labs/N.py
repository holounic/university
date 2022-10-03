if __name__ == '__main__':
    k = int(input())
    n = int(input())
    x, y = [], []
    p_x, p_e = {}, {} 
    for _ in range(n):
        x_dot, y_dot = tuple(int(i) for i in input().split())
        x_dot -= 1
        x.append(x_dot)
        y.append(y_dot)
        prev_p_x = p_x.get(x_dot, 0.)
        prev_p_e = p_e.get(x_dot, 0.)
        p_x[x_dot] = prev_p_x + 1. / n
        p_e[x_dot] = prev_p_e + y_dot / n

    e, r = 0, 0
    for i in range(n):
        e += y[i] * y[i] / n
    for x_dot in p_x.keys():
        r += p_e[x_dot] * p_e[x_dot] / p_x[x_dot]
    print(e - r)
