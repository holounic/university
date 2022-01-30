{-# LANGUAGE InstanceSigs #-}
module HW1.T7 where

data ListPlus a = a :+ ListPlus a | Last a
infixr 5 :+

instance Semigroup (ListPlus a) where
  (<>) :: ListPlus a -> ListPlus a -> ListPlus a
  (<>) (Last x ) (Last y ) = x :+ Last y
  (<>) (x :+ xs) (Last y ) = x :+ (xs <> Last y)
  (<>) (x :+ xs) (y :+ ys) = x :+ (xs <> (y :+ ys))
  (<>) (Last x ) (y :+ ys) = x :+ (y :+ ys)

data Inclusive a b = This a | That b | Both a b

instance (Semigroup a, Semigroup b) => Semigroup (Inclusive a b) where
  (<>) :: Inclusive a b -> Inclusive a b -> Inclusive a b
  (<>) (This x  ) (This y  ) = This (x <> y)
  (<>) (That x  ) (That y  ) = That (x <> y)
  (<>) (This x  ) (That y  ) = Both x y
  (<>) (That y  ) (This x  ) = Both x y
  (<>) (Both x y) (This z  ) = Both (x <> z) y
  (<>) (Both x y) (That z  ) = Both x (y <> z)
  (<>) (This x  ) (Both z y) = Both (x <> z) y
  (<>) (That z  ) (Both x y) = Both x (y <> z)
  (<>) (Both x y) (Both z m) = Both (x <> z) (y <> m)

newtype DotString = DS String deriving (Show)

instance Semigroup DotString where
  (<>) :: DotString -> DotString -> DotString
  (<>) x       (DS "") = x
  (<>) (DS "") x       = x
  (<>) (DS x ) (DS y)  = DS (x ++ "." ++ y)

instance Monoid DotString where
  mempty :: DotString
  mempty = DS ""

newtype Fun a = F (a -> a)

instance Semigroup (Fun a) where
  (<>) :: Fun a -> Fun a -> Fun a
  (<>) (F x) (F y) = F (x . y)

instance Monoid (Fun a) where
  mempty :: Fun a
  mempty = F id
