## Алгоритм Блюма - Флойда - Пратта - Ривеста - Тарьяна

Алгоритм поиска k - ой порядковой статистики за O(n). 

#### Идея алгоритма

Алгоритм гарантирует хорошее разбиение массива: выбирается такой элемент, что количество элементов меньших или больших рассекающего элемента x не меньше 0.3 n. Благодаря этому алгоритм всегда работает за линейное время.

#### Алгоритм 

- Все n элементов исходного массива разбиваются на группы по 5 элементов. В последней группе n % 5 элементов, она может быть пустой, если n кратно пяти.

- Каждая группа сортируется и из неё выбирается медиана.

- Из всех медиан путём рекурсивного вызова шага выбирается медиана x. Найденный элемент x с индексом i используется в качестве рассекающего.

- Если i = k, то возвращается значение x. Иначе рекурсивно запускается поиск элемента: если i > k, то в левой части массива, если i < k, то в правой части.

![Так интерпретируется полученный массив](https://neerc.ifmo.ru/wiki/images/6/6c/%D0%9F%D0%BE%D0%B8%D1%81%D0%BA5.png)

```
int median(int [] a, int l, int r) {
    for (int i = l; i < r - 1; i++) {
        for (int j = i; j < r; j++) {
            if (a[j] <  a[i]) 
                swap(a[i], a[j])
        }
    }
    return a[l+3]
}

int find(int [] a, k) {
    int [] b = new int [a.length / 5]
    int i = 0;
    int j = 0;
    while (i < a.length) {
        int l = i
        int r = (l + 5 < a.length ? l + 5 : a.length - 1)
        a[j] = median(a, l, r)
        i += 5
        j++
    }

    int x = find(b, b.length / 2)
    if (x == k) return x
    l, r = split(a, x)
    if (x > k) return find(l, k)
    else return find(r, k - r.length)
}
```

#### Разбор времени работы

_T(n)_ = n + T(0.2n) + T(0.7n)

- Сортируем полученные подмассивы из пяти элементов за O(n)

- Ищем медиану медиан за T(0.2)

- Ищем k-ый элемент в одной из частей рассеченного массива. Длина такого массива не превышает 0.7n

**Докажем по индукции, что T(n) = O(n)**

T(n) <= Cn

T(n) <= n + 0.2Cn + 0.7Cn <= Cn

n + 0.9Cn <= Cn

Cправедливо при C >= 10: n + 9n <= 10n

Значит, время работы алгоритма Блюма - Флойда - Пратта - Ривеста - Тарьяна действительно О(n).

