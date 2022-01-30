module HW2.T3
  ( joinOption
  , joinExcept
  , joinAnnotated
  , joinFlattened
  , joinList
  , joinFun
  ) where

import           HW2.T1                         ( Annotated(..)
                                                , Except(..)
                                                , Fun(..)
                                                , List(..)
                                                , Option(..)
                                                )


joinOption :: Option (Option a) -> Option a
joinOption None            = None
joinOption (Some None    ) = None
joinOption (Some (Some x)) = Some x

joinExcept :: Except e (Except e a) -> Except e a
joinExcept (Error   i          ) = Error i
joinExcept (Success (Error   i)) = Error i
joinExcept (Success (Success x)) = Success x

joinAnnotated :: Semigroup e => Annotated e (Annotated e a) -> Annotated e a
joinAnnotated ((x :# i) :# j) = x :# (j <> i)

joinFlattened :: List a -> List a -> List a
joinFlattened Nil       y   = y
joinFlattened x         Nil = x
joinFlattened (x :. xs) y   = x :. (joinFlattened xs y)

joinList :: List (List a) -> List a
joinList Nil              = Nil
joinList (wrapped :. y  ) = joinFlattened wrapped (joinList y)

unwrapFun :: Fun i a -> (i -> a)
unwrapFun (F z) = z

joinFun :: Fun i (Fun i a) -> Fun i a
joinFun (F f) = F (\i -> unwrapFun (f i) i)
