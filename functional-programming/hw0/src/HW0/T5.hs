module HW0.T5
  ( nz
  , ns
  , nplus
  , nmult
  , nToNum
  , nFromNatural
  , Nat(..)
  ) where
import           Data.Function                  ( fix )
import           GHC.Natural                    ( Natural )

type Nat a = (a -> a) -> a -> a

nz :: Nat a
nz _ x = x

ns :: Nat a -> Nat a
ns nat f x = f $ nat f x

nplus :: Nat a -> Nat a -> Nat a
nplus nat1 nat2 f x = nat2 f (nat1 f x)

nmult :: Nat a -> Nat a -> Nat a
nmult nat1 nat2 f = nat1 (nat2 f)

nToNum :: Num a => Nat a -> a
nToNum nat = nat (+ 1) 0

nFromNatural :: Natural -> Nat a
nFromNatural = fix (\rec n -> if n == 0 then nz else ns (rec (n - 1)))
