module HW1.T3
  ( Tree(..)
  , tsize
  , tdepth
  , tmember
  , tinsert
  , tFromList
  ) where

data Tree a = Leaf | Branch Int (Tree a) a (Tree a)

-- | Size of the tree, O(1).
tsize :: Tree a -> Int
tsize Leaf             = 0
tsize (Branch n _ _ _) = n

-- | Depth of the tree.
tdepth :: Tree a -> Int
tdepth Leaf             = 0
tdepth (Branch _ l _ r) = 1 + max (tdepth l) (tdepth r)

-- | Check if the element is in the tree, O(log n)
tmember :: Ord a => a -> Tree a -> Bool
tmember x Leaf = False
tmember x (Branch _ l v r) | x < v     = tmember x l
                           | x > v     = tmember x r
                           | otherwise = True

mkBranch :: Ord a => Tree a -> a -> Tree a
mkBranch Leaf x = Branch 1 Leaf x Leaf
mkBranch (Branch n l v r) x | x == v    = error "Illegal state"
                            | x < v     = Branch (n + 1) (mkBranch l x) v r
                            | otherwise = Branch (n + 1) l v (mkBranch r x)

-- | Insert an element into the tree, O(log n)
tinsert :: Ord a => a -> Tree a -> Tree a
tinsert x Leaf = mkBranch Leaf x
tinsert x tree | tmember x tree = tree
               | otherwise      = mkBranch tree x

tadd :: Ord a => Tree a -> a -> Tree a
tadd tree x = tinsert x tree

---- | Build a tree from a list, O(n log n)
tFromList :: Ord a => [a] -> Tree a
tFromList = foldl tadd Leaf
