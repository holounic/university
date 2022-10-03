import math

def load_data(n, has_y):
    x = []
    y = []
    for i in range(n):
        data = [x for x in input().split()]
        if has_y:
            y.append(int(data[0]) - 1)
        shift = 1 if has_y else 0
        l = int(data[shift])
        cur_set = set()
        for j in range(l):
            cur_set.add(data[j + shift + 1])
        x.append(cur_set)
    return x, y

def train(k, lmbd, x, y):
    freq_by_class = [{} for _ in range(k)]
    objs_of_class = [0 for _ in range(k)]

    words = set()
    for (x_dot, y_dot) in zip(x, y):
        words |= {*x_dot}
        cur_freq = freq_by_class[y_dot]
        for word in x_dot:
            old_freq = cur_freq.get(word, 0)
            cur_freq[word] = old_freq + 1
        objs_of_class[y_dot] += 1
    
    class_prob = []
    for c in range(k):
        class_prob.append(objs_of_class[c] / len(x))

    def get_word_prob(word, clss):
        return (freq_by_class[clss].get(word, 0) + alpha) / (objs_of_class[clss] + 2 * alpha)

    def predict(x_test):
        result_prob = [0 for _ in range(k)]
        for c in range(k):
            if objs_of_class[c] == 0:
                continue
            cur_class_prob = math.log(lmbd[c] * class_prob[c])
            for word in words:
                word_prob = get_word_prob(word, c)
                cur_class_prob += math.log(word_prob if word in x_test else 1 - word_prob)
            result_prob[c] = cur_class_prob
        total = 0
        for c in range(k):
            if objs_of_class[c] != 0:
                result_prob[c] = math.exp(result_prob[c])
                total += result_prob[c]
        for c in range(k):
            result_prob[c] /= total
        return result_prob
    return predict


if __name__ == '__main__':
    k = int(input())
    lmbd = [int(l) for l in input().split()]

    alpha = int(input())
    n = int(input())

    x_train, y_train = load_data(n, True)
    m = int(input())
    x_test, _ = load_data(m, False)
    model = train(k, lmbd, x_train, y_train)
    for x_dot in x_test:
        res = model(x_dot)
        for p in res:
            print('%.10f'%p, end=' ')
        print()