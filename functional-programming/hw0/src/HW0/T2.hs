module HW0.T2
  ( doubleNeg
  , reduceTripleNeg
  , Not(..)
  ) where
import           Data.Void

type Not a = a -> Void

doubleNeg :: a -> Not (Not a)
doubleNeg x f = f x

reduceTripleNeg :: Not (Not (Not a)) -> Not a
reduceTripleNeg x y = x $ doubleNeg y
