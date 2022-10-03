from math import log

if __name__ == '__main__':

    _ = input()
    n = int(input())

    p_x, p_x_y = {}, {}

    for _ in range(n):
        x_dot, y_dot = (int(j) for j in input().split())
        prev_p_x_y = p_x_y.get(x_dot, {})
        prev_p_x = p_x.get(x_dot, 0.)
        prev_p_x_y_y_dot = prev_p_x_y.get(y_dot, 0.)
        prev_p_x_y[y_dot] = prev_p_x_y_y_dot + 1 / n
        p_x_y[x_dot] = prev_p_x_y
        p_x[x_dot] = prev_p_x + 1 / n

    h = 0
    for x_dot in p_x.keys():
        for y_dot in p_x_y[x_dot].keys():
            h += p_x_y[x_dot][y_dot] * (log(p_x_y[x_dot][y_dot]) - log(p_x[x_dot]))
    
    print( - h)

