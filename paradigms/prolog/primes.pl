init(MAXN) :-
    eratosphenes_sieve_outer(MAXN, 2).

composite(1).
prime(2).

prime(N) :-
    N > 2,
    not(composite(N)).

incr(Next, Num) :-
    Next is 1 + Num.

smaller_or_equal(A, B) :- A =< B.

is_divisor(A, B) :-
    0 is mod(A, B), !.

eratosphenes_sieve_outer(Lim, Num) :-
    smaller_or_equal(Num, Lim),
    not(composite(Num)),
    Next is Num * Num,
    smaller_or_equal(Next, Lim),
    eratosphenes_sieve_inner(Lim, Next, Num).

eratosphenes_sieve_outer(Lim, Num) :-
    incr(Next, Num),
    smaller_or_equal(Num, Lim),
    eratosphenes_sieve_outer(Lim, Next).

eratosphenes_sieve_inner(Lim, Num, Incr) :- 
    smaller_or_equal(Num, Lim),
    assert(composite(Num)),
    Next is Num + Incr,
    eratosphenes_sieve_inner(Lim, Next, Incr).

num_to_divs(1, _, []).

num_to_divs(Num, Prev, [Head | Tail]) :-
    smaller_or_equal(Prev, Head),
    prime(Head),
    num_to_divs(PreNum, Head , Tail),
    Num is PreNum * Head.

divs_to_num(Num, Divisor, [Num]) :-
  Divisor * Divisor > Num, !.

divs_to_num(Num, Divisor, [Head | Tail]) :-
    is_divisor(Num, Divisor),
    Head is Divisor,
    PreDivs is div(Num, Divisor),
    divs_to_num(PreDivs, Divisor, Tail).

divs_to_num(Num, Divisor, [Head | Tail]) :-
    not(is_divisor(Num, Divisor)),
    incr(NextDivisor, Divisor),
    divs_to_num(Num, NextDivisor, [Head | Tail]).

prime_divisors(1, []) :- !.

prime_divisors(Num, Divisors) :-
    not(integer(Num)),
    num_to_divs(Num, 2, Divisors).

prime_divisors(Num, Divisors) :-
    integer(Num),
    divs_to_num(Num, 2, Divisors).

decimal_to_kdigit(0, _, []) :- !.

decimal_to_kdigit(N, K, [Head | Tail]) :-
    Head is mod(N, K),
    CurRes is div(N, K),
    decimal_to_kdigit(CurRes, K, Tail).

prime_palindrome(N, K) :-
    prime(N),
    decimal_to_kdigit(N, K, List),
    reverse(List, List).