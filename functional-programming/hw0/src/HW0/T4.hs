module HW0.T4
  ( repeat'
  , map'
  , fac
  , fib
  ) where
import           Data.Function                  ( fix )
import           GHC.Natural


repeat' :: a -> [a]
repeat' x = fix (x :)

map' :: (a -> b) -> [a] -> [b]
map' f = fix
  (\rec z -> case z of
    (x : xs) -> f x : rec xs
    []       -> []
  )

fib :: Natural -> Natural
fib n = z
 where
  comp :: NatF -> Natural -> (Natural, Natural)
  comp _  0 = (0, 0)
  comp _  1 = (0, 1)
  comp nf b = (y, y + x) where (x, y) = nf $ b - 1

  (_, z) = fix comp n

type NatF = (Natural -> (Natural, Natural))

fac :: Natural -> Natural
fac = fix (\rec x -> if x <= 1 then 1 else x * rec (x - 1))
