{-# LANGUAGE DerivingStrategies #-}
{-# LANGUAGE GeneralizedNewtypeDeriving #-}
{-# LANGUAGE BlockArguments #-}
{-# LANGUAGE LambdaCase #-}
module HW2.T6
  ( ParseError(..)
  , Parser(..)
  , runP
  , pChar
  , pEof
  , parseExpr
  ) where
import           Control.Applicative
import           Data.Char                      ( digitToInt
                                                , isDigit
                                                , isSpace
                                                )
import           GHC.Natural
import           HW2.T1
import           HW2.T4                         ( Expr(Op, Val)
                                                , Prim(Add, Div, Mul, Sub)
                                                )
import           HW2.T5

data ParseError = ErrorAtPos Natural
  deriving Show

newtype Parser a = P (ExceptState ParseError (Natural, String) a)
  deriving newtype (Functor, Applicative, Monad)

runP :: Parser a -> String -> Except ParseError a
runP (HW2.T6.P parser) s = case runES parser (0, s) of
  Success (value :# _) -> Success value
  Error   e            -> Error e

-- Parser failes if the string is empty,
-- otherwise it consumes a character,
-- increments the position inside the inner Annotated
-- and puts a remaining string there.
pChar :: Parser Char
pChar = HW2.T6.P $ ES \(pos, s) -> case s of
  []       -> Error (ErrorAtPos pos)
  (c : cs) -> Success (c :# (pos + 1, cs))

parseError :: Parser a
parseError = HW2.T6.P $ ES \(pos, _) -> Error (ErrorAtPos pos)

instance Alternative Parser where
  empty = parseError
  (<|>) (HW2.T6.P q) (HW2.T6.P s) = HW2.T6.P $ ES \(pos, c) ->
    let x = runES q (pos, c)
    in  case x of
          Error   _ -> runES s (pos, c)
          Success z -> Success z

pEof :: Parser ()
pEof = HW2.T6.P $ ES \(pos, s) -> case s of
  [] -> Success (() :# (pos, []))
  _  -> Error (ErrorAtPos pos)

satisfy :: (Char -> Bool) -> Parser Char
satisfy predicate = HW2.T6.P $ ES \(pos, s) -> case s of
  [] -> Error (ErrorAtPos pos)
  (c : cs) ->
    if predicate c then Success (c :# (pos + 1, cs)) else Error (ErrorAtPos pos)

element :: Char -> Parser Char
element c = satisfy (== c)

digit :: Parser Int
digit = digitToInt <$> satisfy isDigit

whitespace :: Parser Char
whitespace = satisfy isSpace

whitespaces :: Parser [Char]
whitespaces = many whitespace

nonNeg :: Parser Int
nonNeg = foldl (\s d -> s * 10 + d) 0 <$> some digit

dot :: Parser Char
dot = element '.'

rparen :: Parser Char
rparen = element ')'

lparen :: Parser Char
lparen = element '('

star :: Parser Char
star = element '*'

slash :: Parser Char
slash = element '/'

plus :: Parser Char
plus = element '+'

minus :: Parser Char
minus = element '-'

num :: Parser Double
num = fromIntegral . digitToInt <$> satisfy isDigit

double :: Parser Double
double = nonNegDouble <|> sign <*> nonNegDouble
 where
  sign :: Parser (Double -> Double)
  sign = negate <$ element '-' <|> id <$ element '+'
  nonNegDouble :: Parser Double
  nonNegDouble = withDot <|> beforeDot
   where
    withDot :: Parser Double
    withDot = (+) <$> beforeDot <*> ((id <$ dot) <*> afterDot)
     where
      afterDot :: Parser Double
      afterDot =
        ((/ 10) <$> foldl (\s t -> s / 10 + t) 0.0) . reverse <$> some num

  beforeDot :: Parser Double
  beforeDot = fromIntegral <$> nonNeg

pVal :: Parser Expr
pVal = Val <$> double

pPrimary :: Parser Expr
pPrimary = pVal <|> pParens
 where
  pParens :: Parser Expr
  pParens = (id <$ lparen) *> pLow <* (id <$ rparen)

rule
  :: (Expr -> Expr -> Prim Expr) -> Parser Expr -> Parser Char -> Parser Expr
rule f term char =
  (\x y -> Op (f x y))
    <$> term
    <*> ((id <$ whitespaces) <*> (id <$ char) <*> (id <$ whitespaces) <*> term)

pHigh :: Parser Expr
pHigh = pMul <|> pDiv <|> pPrimary
 where
  pMul :: Parser Expr
  pMul = rule Mul pPrimary star
  pDiv :: Parser Expr
  pDiv = rule Div pPrimary slash

pLow :: Parser Expr
pLow = pSum <|> pSub <|> pHigh
 where
  pSum :: Parser Expr
  pSum = rule Add pHigh plus
  pSub :: Parser Expr
  pSub = rule Sub pHigh minus

pExpr :: Parser Expr
pExpr = pLow <* pEof

parseExpr :: String -> Except ParseError Expr
parseExpr = runP pExpr
