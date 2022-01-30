module HW2.T4
  ( Expr(..)
  , Prim(..)
  , State(..)
  , wrapState
  , mapState
  , modifyState
  , joinState
  , eval
  ) where
import qualified Control.Monad
import           HW2.T1


data State s a = S
  { runS :: s -> Annotated s a
  }


wrapState :: a -> State s a
wrapState x = S (x :#)

mapState :: (a -> b) -> State s a -> State s b
mapState f x = S (mapAnnotated f .!. runS x)

joinState :: State s (State s a) -> State s a
joinState x = S (\s -> let (z :# v) = runS x s in runS z v)

modifyState :: (s -> s) -> State s ()
modifyState f = S (\s -> () :# f s)

instance Functor (State s) where
  fmap = mapState

instance Applicative (State s) where
  pure = wrapState
  p <*> q = Control.Monad.ap p q

instance Monad (State s) where
  m >>= f = joinState (fmap f m)


data Prim a =
    Add a a      -- (+)
  | Sub a a      -- (-)
  | Mul a a      -- (*)
  | Div a a      -- (/)
  | Abs a        -- abs
  | Sgn a        -- signum

data Expr = Val Double | Op (Prim Expr)

instance Num Expr where
  x + y = Op (Add x y)
  x - y = Op (Sub x y)
  x * y = Op (Mul x y)
  abs x = Op (Abs x)
  signum x = Op (Sgn x)
  fromInteger x = Val (fromInteger x)

evalUnary
  :: Expr
  -> (Double -> Prim Double)
  -> (Double -> Double)
  -> State [Prim Double] Double
evalUnary x op fun = do
  x1 <- eval x
  modifyState (op x1 :)
  return (fun x1)

evalBinary
  :: Expr
  -> Expr
  -> (Double -> Double -> Prim Double)
  -> (Double -> Double -> Double)
  -> State [Prim Double] Double
evalBinary l r op fun = do
  l1 <- eval l
  r1 <- eval r
  modifyState (op l1 r1 :)
  return (l1 `fun` r1)

eval :: Expr -> State [Prim Double] Double
eval (Val x        ) = return x
eval (Op  (Abs x  )) = evalUnary x Abs abs
eval (Op  (Sgn x  )) = evalUnary x Sgn signum
eval (Op  (Add l r)) = evalBinary l r Add (+)
eval (Op  (Sub l r)) = evalBinary l r Sub (-)
eval (Op  (Mul l r)) = evalBinary l r Mul (*)
eval (Op  (Div l r)) = evalBinary l r Div (/)

showUnary :: String -> Expr -> String
showUnary name x = "Op (" <> name <> " " <> show x <> ")"

showBinary :: String -> Expr -> Expr -> String
showBinary name x y = "Op (" <> name <> " " <> show x <> ", " <> show y <> ")"

instance Show Expr where
  show (Val x        ) = "(Val " <> show x <> ")"
  show (Op  (Add x y)) = showBinary "Add" x y
  show (Op  (Sub x y)) = showBinary "Sub" x y
  show (Op  (Mul x y)) = showBinary "Mul" x y
  show (Op  (Div x y)) = showBinary "Div" x y
  show (Op  (Abs x  )) = showUnary "Abs" x
  show (Op  (Sgn x  )) = showUnary "Sgn" x
