module HW1.T2 where
import           Numeric.Natural                ( Natural )

data N = Z | S N deriving (Show)

nplus :: N -> N -> N
nplus n (S m) = nplus (S n) m
nplus Z Z     = Z
nplus n Z     = n

nmult :: N -> N -> N
nmult _ Z     = Z
nmult Z _     = Z
nmult n (S m) = nplus (nmult n m) n

nsub :: N -> N -> Maybe N
nsub Z     (S n) = Nothing
nsub n     Z     = Just n
nsub (S n) (S m) = nsub n m

sub :: N -> N -> N
sub Z     (S n) = error ":("
sub n     Z     = n
sub (S n) (S m) = sub n m

ncmp :: N -> N -> Ordering
ncmp Z     Z     = EQ
ncmp (S n) Z     = GT
ncmp Z     (S n) = LT
ncmp (S n) (S m) = ncmp n m

nFromNatural :: Natural -> N
nFromNatural 0 = Z
nFromNatural n = S (nFromNatural (n - 1))

nToNum :: Num a => N -> a
nToNum (S n) = 1 + nToNum n
nToNum Z     = 0

nEven :: N -> Bool
nEven Z     = True
nEven (S n) = not (nEven n)

nOdd :: N -> Bool
nOdd Z     = False
nOdd (S n) = not (nOdd n)

ndiv :: N -> N -> N
ndiv Z _ = Z
ndiv _ Z = error "Division by zero is not a good idea, man!"
ndiv n m | ncmp n m == LT = Z
         | otherwise      = S (ndiv (sub n m) m)


extract :: Maybe N -> N
extract Nothing  = Z
extract (Just n) = n


nmod :: N -> N -> N
nmod n m = extract (nsub n (nmult m (ndiv n m)))
