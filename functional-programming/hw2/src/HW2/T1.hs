{-# LANGUAGE InstanceSigs #-}
module HW2.T1
  ( Option(..)
  , Pair(..)
  , Quad(..)
  , Annotated(..)
  , Except(..)
  , Tree(..)
  , Prioritised(..)
  , Stream(..)
  , List(..)
  , Fun(..)
  , mapAnnotated
  , mapExcept
  , (.!.)
  , mapTree
  , mapFun
  , mapOption
  , mapPair
  , mapQuad
  , mapStream
  , mapList
  , mapPrioritised
  ) where


data Option a = None | Some a deriving Show

data Pair a = P a a
  deriving Show

data Quad a = Q a a a a
  deriving Show

data Annotated e a = a :# e
  deriving Show
infix 0 :#

data Except e a = Error e | Success a deriving Show

data Prioritised a = Low a | Medium a | High a deriving Show

data Stream a = a :> Stream a
  deriving Show
infixr 5 :>

data List a = Nil | a :. List a deriving Show
infixr 5 :.

data Fun i a = F (i -> a)

data Tree a = Leaf | Branch (Tree a) a (Tree a) deriving Show

instance Semigroup (List a) where
  (<>) :: List a -> List a -> List a
  (<>) Nil       Nil       = Nil
  (<>) x         Nil       = x
  (<>) (x :. xs) y         = x :. (xs <> y)
  (<>) Nil       y         = y

mapOption :: (a -> b) -> (Option a -> Option b)
mapOption _ None     = None
mapOption f (Some x) = Some (f x)

mapPair :: (a -> b) -> (Pair a -> Pair b)
mapPair f (P x y) = P (f x) (f y)

mapQuad :: (a -> b) -> (Quad a -> Quad b)
mapQuad f (Q x y z v) = Q (f x) (f y) (f z) (f v)

mapAnnotated :: (a -> b) -> (Annotated e a -> Annotated e b)
mapAnnotated f (x :# y) = f x :# y

mapExcept :: (a -> b) -> (Except e a -> Except e b)
mapExcept _ (Error   x) = Error x
mapExcept f (Success y) = Success (f y)

mapPrioritised :: (a -> b) -> (Prioritised a -> Prioritised b)
mapPrioritised f (Low    x) = Low (f x)
mapPrioritised f (Medium x) = Medium (f x)
mapPrioritised f (High   x) = High (f x)

mapStream :: (a -> b) -> (Stream a -> Stream b)
mapStream f (x :> y) = f x :> mapStream f y

mapList :: (a -> b) -> (List a -> List b)
mapList _ Nil      = Nil
mapList f (x :. y) = f x :. mapList f y

(.!.) :: (b -> c) -> (a -> b) -> a -> c
(.!.) f g = \x -> f (g x)

mapFun :: (a -> b) -> (Fun i a -> Fun i b)
mapFun f (F x) = F (f .!. x)

mapTree :: (a -> b) -> (Tree a -> Tree b)
mapTree _ Leaf           = Leaf
mapTree f (Branch l y r) = Branch (mapTree f l) (f y) (mapTree f r)
