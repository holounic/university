{-# LANGUAGE InstanceSigs #-}
module HW2.T2
  ( distOption
  , wrapOption
  , distPair
  , wrapPair
  , distQuad
  , wrapQuad
  , distAnnotated
  , wrapAnnotated
  , distExcept
  , wrapExcept
  , distPrioritised
  , wrapPrioritised
  , distStream
  , wrapStream
  , distList
  , wrapList
  , distFun
  , wrapFun
  ) where
import           HW2.T1                         ( Annotated(..)
                                                , Except(..)
                                                , Fun(..)
                                                , List(..)
                                                , Option(..)
                                                , Pair(..)
                                                , Prioritised(..)
                                                , Quad(..)
                                                , Stream(..)
                                                )

-- Option
distOption :: (Option a, Option b) -> Option (a, b)
distOption (None  , _     ) = None
distOption (_     , None  ) = None
distOption (Some x, Some y) = Some (x, y)

wrapOption :: a -> Option a
wrapOption = Some

-- Pair
distPair :: (Pair a, Pair b) -> Pair (a, b)
distPair (P x y, P z v) = P (x, z) (y, v)

wrapPair :: a -> Pair a
wrapPair x = P x x

-- Quad
distQuad :: (Quad a, Quad b) -> Quad (a, b)
distQuad (Q x y z v, Q m n l k) = Q (x, m) (y, n) (z, l) (v, k)

wrapQuad :: a -> Quad a
wrapQuad x = Q x x x x

-- Annotated
distAnnotated
  :: Semigroup e => (Annotated e a, Annotated e b) -> Annotated e (a, b)
distAnnotated (x :# i, y :# j) = (x, y) :# i <> j

wrapAnnotated :: Monoid e => a -> Annotated e a
wrapAnnotated x = x :# mempty

-- Except
distExcept :: (Except e a, Except e b) -> Except e (a, b)
distExcept (Error x  , _        ) = Error x
distExcept (_        , Error x  ) = Error x
distExcept (Success x, Success y) = Success (x, y)

wrapExcept :: a -> Except e a
wrapExcept = Success

-- Prioritised
upackPrioritised :: Prioritised a -> a
upackPrioritised (Low    x) = x
upackPrioritised (Medium x) = x
upackPrioritised (High   x) = x

distPrioritised :: (Prioritised a, Prioritised b) -> Prioritised (a, b)
distPrioritised (High x  , y       ) = High (x, upackPrioritised y)
distPrioritised (x       , High y  ) = High (upackPrioritised x, y)
distPrioritised (Medium x, y       ) = Medium (x, upackPrioritised y)
distPrioritised (x       , Medium y) = Medium (upackPrioritised x, y)
distPrioritised (Low x   , Low y   ) = Low (x, y)

wrapPrioritised :: a -> Prioritised a
wrapPrioritised = Low

-- Stream
distStream :: (Stream a, Stream b) -> Stream (a, b)
distStream (x :> y, z :> v) = (x, z) :> distStream (y, v)

wrapStream :: a -> Stream a
wrapStream x = x :> wrapStream x

-- List
distList :: (List a, List b) -> List (a, b)
distList (Nil, _  ) = Nil
distList (_  , Nil) = Nil
distList (x :. y, z :. v) =
  (x, z) :. distList (x :. Nil, v) <> distList (y, z :. v)

wrapList :: a -> List a
wrapList x = x :. Nil

-- Fun
distFun :: (Fun i a, Fun i b) -> Fun i (a, b)
distFun (F x, F y) = F (\i -> (x i, y i))

wrapFun :: a -> Fun i a
wrapFun x = F (const x)
