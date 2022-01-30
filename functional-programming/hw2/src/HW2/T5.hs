module HW2.T5
  ( ExceptState(..)
  , EvaluationError(..)
  , mapExceptState
  , wrapExceptState
  , joinExceptState
  , modifyExceptState
  , throwExceptState
  , eval
  ) where
import qualified Control.Monad
import           HW2.T1                         ( (.!.)
                                                , Annotated(..)
                                                , Except(Error, Success)
                                                , mapAnnotated
                                                , mapExcept
                                                )
import           HW2.T2                         ( wrapExcept )
import           HW2.T4                         ( Expr(..)
                                                , Prim(..)
                                                )

data ExceptState e s a = ES
  { runES :: s -> Except e (Annotated s a)
  }

mapExceptState :: (a -> b) -> ExceptState e s a -> ExceptState e s b
mapExceptState f state = ES (mapExcept (mapAnnotated f) .!. runES state)

wrapExceptState :: a -> ExceptState e s a
wrapExceptState x = ES (\e -> wrapExcept (x :# e))

joinExceptState :: ExceptState e s (ExceptState e s a) -> ExceptState e s a
joinExceptState x = ES
  (\s -> case runES x s of
    Error   e        -> Error e
    Success (a :# y) -> runES a y
  )

modifyExceptState :: (s -> s) -> ExceptState e s ()
modifyExceptState f = ES (\s -> wrapExcept (() :# f s))

throwExceptState :: e -> ExceptState e s a
throwExceptState x = ES (\_ -> Error x)

instance Functor (ExceptState e s) where
  fmap = mapExceptState

instance Applicative (ExceptState e s) where
  pure = wrapExceptState
  p <*> q = Control.Monad.ap p q

instance Monad (ExceptState e s) where
  m >>= f = joinExceptState (fmap f m)

data EvaluationError = DivideByZero

evalUnary
  :: Expr
  -> (Double -> Prim Double)
  -> (Double -> Double)
  -> ExceptState EvaluationError [Prim Double] Double
evalUnary x op fun = do
  x1 <- eval x
  modifyExceptState (op x1 :)
  return (fun x1)

evalBinary
  :: Expr
  -> Expr
  -> (Double -> Double -> Prim Double)
  -> (Double -> Double -> Double)
  -> ExceptState EvaluationError [Prim Double] Double
evalBinary l r op fun = do
  l1 <- eval l
  r1 <- eval r
  modifyExceptState (op l1 r1 :)
  return (l1 `fun` r1)


eval :: Expr -> ExceptState EvaluationError [Prim Double] Double
eval (Val x        ) = return x
eval (Op  (Abs x  )) = evalUnary x Abs abs
eval (Op  (Sgn x  )) = evalUnary x Sgn signum
eval (Op  (Add l r)) = evalBinary l r Add (+)
eval (Op  (Div l r)) = do
  l1 <- eval l
  r1 <- eval r
  if r1 == 0
    then throwExceptState DivideByZero
    else modifyExceptState (Div l1 r1 :)
  return (l1 / r1)
eval (Op (Mul l r)) = evalBinary l r Mul (*)
eval (Op (Sub l r)) = evalBinary l r Sub (-)
