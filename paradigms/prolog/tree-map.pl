map_build([], nil) :- !.

map_build([(Key, Value) | Left], Tree) :- 
    map_build(Left, NewTree), 
    map_put(NewTree, Key, Value, Tree).

map_put(Tree, Key, Value, Right) :- 
    find(Tree, Key, _, Found), 
    rand_int(2147483647, Y), 
    update(Tree, Key, Value, Right, Y, Found).

map_remove(nil, _, nil) :- !.

map_remove(tree_imp((Key, Value, Y), Left, Right), NewK, tree_imp((Key, Value, Y), NewN, Right)) :- 
    NewK < Key, 
    map_remove(Left, NewK, NewN).

map_remove(tree_imp((Key, Value, Y), Left, Right), NewK, tree_imp((Key, Value, Y), Left, NewN)) :- 
    NewK > Key, 
    map_remove(Right, NewK, NewN).

map_floorKey(tree_imp((Key, Value, Y), Left, Right), X, S) :- 
    split(tree_imp((Key, Value, Y), Left, Right), X, RightL, RightR), 
    find_max(RightL, S).

update(Tree, Key, Value, Right, _, Found) :- 
    Found is 1, 
    replace(Tree, Key, Value, Right), !.

update(Tree, Key, Value, Right, Y, _) :- 
    insert(Tree, (Key, Value, Y), Right).

split(nil, _, nil, nil) :- !.

split(tree_imp((Key, Value, Y), Left, Right), X, RightL, RightR) :- 
    Key > X, 
    split(Left, X, RightL, NewLeft), 
    RightR = tree_imp((Key, Value, Y), NewLeft, Right), !.

split(tree_imp((Key, Value, Y), Left, Right), X, RightL, RightR) :- 
    split(Right, X, NewRight, RightR), 
    RightL = tree_imp((Key, Value, Y), Left, NewRight).

merge(nil, nil, nil) :- !.

merge(Left, nil, Left).

merge(nil, Right, Right).

merge(Tree, tree_imp((LeftK, LeftV, LeftY), LeftL, LeftR), tree_imp((RightK, RightV, RightY), RightL, RightR)) :- 
    LeftY > RightY, 
    merge(LeftR, tree_imp((RightK, RightV, RightY), RightL, RightR), NewRight), 
    Tree = tree_imp((LeftK, LeftV, LeftY), LeftL, NewRight), !.

merge(Tree, tree_imp((LeftK, LeftV, LeftY), LeftL, LeftR), tree_imp((RightK, RightV, RightY), RightL, RightR)) :- 
    Tree = tree_imp((RightK, RightV, RightY), NewLeft, RightR),
    merge(tree_imp((LeftK, LeftV, LeftY), LeftL, LeftR), RightL, NewLeft).

insert(nil, (Key, Value, Y), tree_imp((Key, Value, Y), nil, nil)) :- !.

insert(tree_imp((Key, Value, Y), Left, Right), (NewK, NewVal, NewY), tree_imp((NewK, NewVal, NewY), NewLeft, NewRight)) :- 
    NewY > Y, 
    split(tree_imp((Key, Value, Y), Left, Right), NewK, NewLeft, NewRight), !.

insert(tree_imp((Key, Value, Y), Left, Right), (NewK, NewVal, NewY), tree_imp((Key, Value, Y), Left, NewN)) :- 
    Key =< NewK,
    Y >= NewY, 
    insert(Right, (NewK, NewVal, NewY), NewN).

insert(tree_imp((Key, Value, Y), Left, Right), (NewK, NewVal, NewY), tree_imp((Key, Value, Y), NewN, Right)) :- 
    NewY =< Y, 
    NewK < Key, 
    insert(Left, (NewK, NewVal, NewY), NewN).

find(nil, _, _, 0) :- !.

find(tree_imp((Key, Value, _), _, _), Key, Value, 1) :- !.

find(tree_imp((Key, _, _), Left, _), NewK, NewVal, Found) :- 
    Key > NewK, 
    find(Left, NewK, NewVal, Found).

find(tree_imp((Key, _, _), _, Right), NewK, NewVal, Found) :- 
    NewK > Key, 
    find(Right, NewK, NewVal, Found).

map_get(Tree, Key, Value) :- 
    find(Tree, Key, Value, Found), 
    Found is 1.

replace(nil, _, _, nil).

replace(tree_imp((Key, _, Y), Left, Right), Key, NewVal, tree_imp((Key, NewVal, Y), Left, Right)).

replace(tree_imp((Key, Value, Y), Left, Right), NewK, NewVal, tree_imp((Key, Value, Y), NewLeft, Right)) :- 
    Key > NewK, 
    replace(Left, NewK, NewVal, NewLeft).

replace(tree_imp((Key, Value, Y), Left, Right), NewK, NewVal, tree_imp((Key, Value, Y), Left, NewRight)) :- 
    Key < NewK, 
    replace(Right, NewK, NewVal, NewRight).

find_max(tree_imp((Key, Value, Y), Left, nil), Key) :- !.
find_max(tree_imp((Key, Value, Y), Left, Right), S) :- find_max(Right, S).