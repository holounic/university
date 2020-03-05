## Вектор (ArrayList)

Создаем небольшой массив, заполняем его, пока он не кончится, создаем массив побольше, копируем в него элементы, продолжаем жить и радоваться.

```
void push(T element) {
    if (n == array.length) {
        T [] a' = new T[n * 1.5]
        copy(a, a') 
        a = a'
    }   
    a[n++] = x
}
```
Очевидно, что такая штука будет работать за О(n) только при переполнении, все остальное время операция будет выполняться за О(1).

Для оценки времени работы подобных алгоритмов используют понятие __амортизированного времени работы__.

## Амортизированное время работы для самых маленьких

T(О<sub>i</sub>) - время на выполнение i-ой операции

Ť(О<sub>i</sub>) - _амортизированное время работы_

#### Правильность оценки амортизированного времени работы

Оценка считается верной, если ∑T(О<sub>i</sub>) <= ∑Ť(О<sub>i</sub>)

Вычислим амортизированное время работы K операций push. 

Эта штука работает каждый раз, поэтому отработает K раз.
```
 a[n++] = x 
```

Эта штука отработает 1 + 2<sup>1</sup> + 2<sup>2</sup> + ... + 2<sup>x-1</sup> <=  2<sup>x</sup> <= 2K т. к. срабатывает только тогда, когда необходимо расширить. С каждым расширением длина массива увеличивается вдвое.

```
if (n == array.length) {
    T [] a' = new T[n * 1.5]
    copy(a, a') 
    a = a'
}   
```

Итого ∑T(push) <= K + 2K т.е. ∑T(push) <= 3K, значит

∑Ť(push) = 3


#### Другие способы вычисления амортизированного времени работы

__Потенциал__ 

_Физический смысл_: сколько времени "запасено" у структуры данных на последующие операции.

Во время работы СД меняет свой потенциал: накапливает его во время быстрых операций или высвобождает его во время долгих. Потенциал до первой операции Ф<sub>0</sub> = 0, тогда амортизированное время работы i-ой операции будет равно **Ť(O<sub>i</sub>) = T(O<sub>i</sub>) + (Ф<sub>i</sub> - Ф<sub>i-1</sub>)**

∑ Ť(O<sub>i</sub>) = ∑ T(O<sub>i</sub> + Ф<sub>i</sub> + Ф<sub>i-1</sub> = ∑ T(O<sub>i</sub>) + Ф<sub>K</sub> + Ф<sub>0</sub>, Ф<sub>0</sub> = 0, Ф<sub>К</sub> >= 0, тогда ∑ T(O<sub>i</sub>) + Ф<sub>K</sub> + Ф<sub>0</sub> >= ∑ T(O<sub>i</sub>)

Потенциал должен быть такой, чтобы долгая операция уменьшала потенциал.

Вычислим время работы операции push по формуле **Ť = T + <sub>Δ</sub>Ф**. Пусть потенциал - это количество элементов в правой половине массива, умноженное на два. В полность заполненом массиве Ф = n, в только что расширенном массиве Ф = 0.

Изменение потенциала в такой штуке <sub>Δ</sub>Ф = -n, истинное время работы T = n.

```
if (n == array.length) {
    T [] a' = new T[n * 1.5]
    copy(a, a') 
    a = a'
}   
```

Изменение потенциала такой штуки <sub>Δ</sub>Ф = 2, истинное время работы T = 1.
```
 a[n++] = x 
```
итого: Ť = n - n + 2 + 1 = 3

__Метод бухгалтерского учета (метод с монетками)__

Будем считать, что помимо обычных операций, есть операция save(запасти время на будущее) и все операции выполняются за одинаковое время. Если какая-то операция выполняется быстрее положенного, то она запасает время для будущей операции.

#### Очередь на двух стеках

Сделаем два стека L и R, из L будем вынимать элементы, в R будем класть элементы.

Найдем амортизированное время работы очереди на двух стеках.

```
void add(T x) {
    R.push(x) //1 ед. времени + 1 "монетка" = О(1)
}

void remove() {
    if (L.isEmpty()) {
        while (!R.isEmpty()) {
            L.push(R.pop())             
            //1 ед. времени - 1 "монетка"
        }
    }
    return L.pop() //1 едю времени
}
```
