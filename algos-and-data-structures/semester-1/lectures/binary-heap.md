## Двоичная куча

_Двоичная куча_ - двоичное подвешенное дерево, для которого выполнены следующие условия:

    1. Значение в любой вершине не больше (если куча для минимума), чем значение в её потомках

    2. На i-ом слое 2^i, кроме последнего. Слои нумеруются с нуля

    3. Слои заполняются слева направо
    
    4. На элементах кучи должен быть линейный порядок (все элементы сравнимы)

#### Хранение двоичной кучи

Удобнее всего хранить двоичную кучу в массиве a[0...n-1], где a[0] - корень, а потомками элемента a[i] являются a[2i + 1] и a[2i + 2]. Высота кучи О(log n) или log(n+1) с округление в большую сторону.

## Базовые процедуры кучи

_Sift Down_ Если i-й элемент меньше, чем его сыновья, всё поддерево уже является кучей, и ничего менять не надо. Иначе меняем i-й элемент с наименьшим из его сыновей и выполняем SiftDown для этого сына. Время O(log n).


```
void siftDown(int i) {
    while (2 * i + 1 < n) {
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        int j = left;
        if (right < n && a[right] < a[left]) {
            j = right;
        }
        if (a[i] <= a[j]) {
            break;
        }
        swap(a[i], a[j]);
        i = j;
    }
}
```

_Sift Up_ Если i-й элемент больше своего отца, то всё и так классно и ничего делать не нужно. Иначе меняем местами i-й элемент его родителем и выполняем процедуру для обновлённого родителя. Время O(log n).


```
void siftUp(int i) {
    while (a[i] < a[(i - 1) / 2]) {
        swap(a[i], a[(i - 1) / 2];
        i = (i - 1) / 2;
    }
}
```

_GetMinValue_ Извлечение минимального элемента выполняется в 4 этапа за O(log n).

    1. Значение минимального элемента сохраняется
    
    2. В корень копируется последний элемент, а затем удаляется из кучи

    3. Вызывается SiftDown для корня 

    4. Возвращается значение минимального элемента


``` 
int getMinValue() {
    int min = a[0];
    a[0] = a[n - 1];
    remove(a[n - 1]);
    siftDown(0);
    return min;
}
```

_insert_ Добавление нового элемента в кучу за O(log n) выполняется в два этапа.

    1. Новый элемент добавляется в конец кучи

    2. Выполняется SiftUp для последнего элемента кучи


```
void insert(int value) {
    a[n] = value;
    SiftUp(n);
}
```

#### Построение кучи

_За O(n log n)_ Сделать нулевой элемент массива корнем кучи, а дальше добавлять в кучу элементы, запуская для каждого SiftUp.

_За О(n)_ Представим, что в массиве хранится куча, где а[0] - корень. Запустим siftDown для всех вершин, имеющих хотя бы одного потомка.