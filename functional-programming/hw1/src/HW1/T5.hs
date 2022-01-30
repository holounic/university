module HW1.T5 where
import           Data.List.NonEmpty

splitOn :: Eq a => a -> [a] -> NonEmpty [a]
splitOn sep = foldr (fold sep) ([] :| [])
 where
  fold :: Eq a => a -> a -> NonEmpty [a] -> NonEmpty [a]
  fold sep cur result@(x :| xs) | cur == sep = [] <| result
                                | otherwise  = (cur : x) :| xs

joinWith :: a -> NonEmpty [a] -> [a]
joinWith sep = foldr1 (fold sep)
 where
  fold :: a -> [a] -> [a] -> [a]
  fold sep cur result = cur ++ sep : result
