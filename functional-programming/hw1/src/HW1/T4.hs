module HW1.T4 where

import           HW1.T3                         ( Tree(..) )

tfoldr :: (a -> b -> b) -> b -> Tree a -> b
tfoldr op res Leaf             = res
tfoldr op res (Branch _ l x r) = tfoldr op (op x (tfoldr op res r)) l


treeToList :: Tree a -> [a]
treeToList = tfoldr (:) []
