if __name__ == '__main__':
    n = int(input())
    x, y = [], []
    x_hat, y_hat = 0, 0
 
    for _ in range(n):
        x_dot, y_dot = map(int, input().split())
        x_hat += x_dot / n
        y_hat += y_dot / n
        x.append(x_dot)
        y.append(y_dot)
 
    dividend, divider = 0, {'x': 0, 'y': 0}
    
    for (x_dot, y_dot) in zip(x, y):
        dividend += (x_dot - x_hat) * (y_dot - y_hat)
        divider['x'] += (x_dot - x_hat) ** 2
        divider['y'] += (y_dot - y_hat) ** 2
 
    divider = (divider['x'] * divider['y']) ** 0.5
    print(dividend / divider)