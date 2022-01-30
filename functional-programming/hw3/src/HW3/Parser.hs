{-# LANGUAGE FlexibleInstances #-}
{-# LANGUAGE OverloadedStrings #-}
{-# LANGUAGE ExtendedDefaultRules #-}
{-# OPTIONS_GHC -Wno-unused-top-binds #-}
module HW3.Parser
  ( parse
  ) where
import           Control.Monad                  ( void )
import           Control.Monad.Combinators.Expr ( Operator
                                                  ( InfixL
                                                  , InfixN
                                                  , InfixR
                                                  )
                                                , makeExprParser
                                                )
import qualified Data.ByteString               as B
import           Data.ByteString.Char8         as BS
                                         hiding ( count )
import qualified Data.Text                     as T
import           Data.Text.Internal.Read        ( hexDigitToInt )
import           Data.Void
import           Data.Word                      ( Word8 )
import           HW3.Base
import           Text.Megaparsec                ( (<|>)
                                                , MonadParsec
                                                  ( eof
                                                  , notFollowedBy
                                                  , try
                                                  )
                                                , ParseErrorBundle
                                                , Parsec
                                                , between
                                                , choice
                                                , many
                                                , manyTill
                                                , runParser
                                                , sepBy
                                                , some
                                                )
import           Text.Megaparsec.Char
import qualified Text.Megaparsec.Char.Lexer    as L


type Parser = Parsec Void String

parse :: String -> Either (ParseErrorBundle String Void) HiExpr
parse = runParser (allExprParser <* eof) ""

allExprParser :: Parser HiExpr
allExprParser = sc *> wrapApplyParser (infixExprParser <|> prefixExprParser)

wrapApplyParser :: Parser HiExpr -> Parser HiExpr
wrapApplyParser z = do
  x <- z
  do
    wrapApplyParser (HiExprApply x <$> (parens argsParser <|> dotArg))
      <|> wrapApplyParser (HiExprRun x <$ string "!")
      <|> pure x

prefixExprParser :: Parser HiExpr
prefixExprParser =
  sc
    *> (do
         f <- valueParser
         do
           z <-
             (HiExprApply f <$> (dotArg <|> parens argsParser))
             <|> pure f
             <|> parens prefixExprParser
           (HiExprRun z <$ string "!") <|> pure z
       )
    <* sc

dotArg :: Parser [HiExpr]
dotArg = do
  void (string ".")
  (: [])
    .   HiExprValue
    <$> (try hiValueParser <|> (HiValueString . T.pack <$> innerDot))

innerDot :: Parser String
innerDot = some (alphaNumChar <|> char '-')

numParser :: Parser HiExpr
numParser = signed >>= return . HiExprValue . HiValueNumber

valueParser :: Parser HiExpr
valueParser = do
  v <- (HiExprValue <$> hiValueParser) <|> listExprParser <|> dictExprParser
  pure v

hiValueParser :: Parser HiValue
hiValueParser =
  prefixOperationParser
    <|> (HiValueNumber <$> signed)
    <|> (HiValueBool <$> bool)
    <|> (HiValueNull <$ string "null")
    <|> (HiValueAction HiActionCwd <$ string "cwd")
    <|> (HiValueAction HiActionNow <$ string "now")
    <|> (HiValueString . T.pack <$> str)
    <|> (HiValueBytes <$> bytearr)

argsParser :: Parser [HiExpr]
argsParser = allExprParser `sepBy` symbol ","

prefixOperationParser :: Parser HiValue
prefixOperationParser = HiValueFunction <$> lexeme
  (choice
    [ singleOpParser HiFunAdd "add"
    , singleOpParser HiFunDiv "div"
    , singleOpParser HiFunMul "mul"
    , singleOpParser HiFunSub "sub"
    , singleOpParser HiFunAnd "and"
    , singleOpParser HiFunOr  "or"
    , singleOpParserNotFollowed HiFunNot "not" "-"
    , singleOpParser HiFunEquals         "equals"
    , singleOpParser HiFunNotEquals      "not-equals"
    , singleOpParser HiFunGreaterThan    "greater-than"
    , singleOpParser HiFunLessThan       "less-than"
    , singleOpParser HiFunNotGreaterThan "not-greater-than"
    , singleOpParser HiFunNotLessThan    "not-less-than"
    , singleOpParser HiFunIf             "if"
    , singleOpParser HiFunLength         "length"
    , singleOpParser HiFunToUpper        "to-upper"
    , singleOpParser HiFunToLower        "to-lower"
    , singleOpParser HiFunReverse        "reverse"
    , singleOpParser HiFunTrim           "trim"
    , singleOpParser HiFunList           "list"
    , singleOpParser HiFunRange          "range"
    , singleOpParser HiFunFold           "fold"
    , singleOpParser HiFunPackBytes      "pack-bytes"
    , singleOpParser HiFunUnpackBytes    "unpack-bytes"
    , singleOpParser HiFunEncodeUtf8     "encode-utf8"
    , singleOpParser HiFunDecodeUtf8     "decode-utf8"
    , singleOpParser HiFunZip            "zip"
    , singleOpParser HiFunUnzip          "unzip"
    , singleOpParser HiFunSerialise      "serialise"
    , singleOpParser HiFunDeserialise    "deserialise"
    , singleOpParser HiFunWrite          "write"
    , singleOpParser HiFunRead           "read"
    , singleOpParser HiFunMkDir          "mkdir"
    , singleOpParser HiFunChDir          "cd"
    , singleOpParser HiFunParseTime      "parse-time"
    , singleOpParser HiFunRand           "rand"
    , singleOpParser HiFunEcho           "echo"
    , singleOpParser HiFunCount          "count"
    , singleOpParser HiFunKeys           "keys"
    , singleOpParser HiFunValues         "values"
    , singleOpParser HiFunInvert         "invert"
    ]
  )

singleOpParser :: HiFun -> String -> Parser HiFun
singleOpParser f s = f <$ string s

singleOpParserNotFollowed :: HiFun -> String -> String -> Parser HiFun
singleOpParserNotFollowed f s follow =
  f <$ (lexeme . try) (string s <* notFollowedBy (symbol follow))

parens :: Parser a -> Parser a
parens = between (symbol "(") (symbol ")")

symbol :: String -> Parser String
symbol = L.symbol sc

sc :: Parser ()
sc = L.space space1 (L.skipLineComment "//") (L.skipBlockComment "/*" "*/")

lexeme :: Parser a -> Parser a
lexeme = L.lexeme sc

number :: Parser Rational
number = lexeme L.scientific >>= return . toRational

signed :: Parser Rational
signed = L.signed sc number

bool :: Parser Bool
bool = sc *> choice [True <$ string "true", False <$ string "false"] <* sc

-- infix ops --

infixExprParser :: Parser HiExpr
infixExprParser = makeExprParser termParser operatorTable

termParser :: Parser HiExpr
termParser = choice [parens infixExprParser, prefixExprParser]

operatorTable :: [[Operator Parser HiExpr]]
operatorTable =
  [ [ binaryLeft "*" HiFunMul
    , binaryPrioritizedNotFollowed InfixL "/" "=" HiFunDiv
    ]
  , [binaryLeft "+" HiFunAdd, binaryLeft "-" HiFunSub]
  , [ binaryNon "==" HiFunEquals
    , binaryNon "/=" HiFunNotEquals
    , binaryPrioritizedNotFollowed InfixN ">" "=" HiFunGreaterThan
    , binaryPrioritizedNotFollowed InfixN "<" "=" HiFunLessThan
    , binaryNon "<=" HiFunNotGreaterThan
    , binaryNon ">=" HiFunNotLessThan
    ]
  , [binaryRight "&&" HiFunAnd]
  , [binaryRight "||" HiFunOr]
  ]


type Priority = (Parser (HiExpr -> HiExpr -> HiExpr) -> Operator Parser HiExpr)

binaryPrioritizedNotFollowed
  :: Priority -> String -> String -> HiFun -> Operator Parser HiExpr
binaryPrioritizedNotFollowed p name follow f = p
  (  (\x y -> HiExprApply (HiExprValue $ HiValueFunction f) [x, y])
  <$ (lexeme . try) (symbol name <* notFollowedBy (symbol follow))
  )

binaryPrioritized :: Priority -> String -> HiFun -> Operator Parser HiExpr
binaryPrioritized p name f = p
  (  (\x y -> HiExprApply (HiExprValue $ HiValueFunction f) [x, y])
  <$ lexeme (symbol name)
  )

binaryLeft :: String -> HiFun -> Operator Parser HiExpr
binaryLeft = binaryPrioritized InfixL

binaryRight :: String -> HiFun -> Operator Parser HiExpr
binaryRight = binaryPrioritized InfixR

binaryNon :: String -> HiFun -> Operator Parser HiExpr
binaryNon = binaryPrioritized InfixN

-- string-related --

str :: Parser String
str = char '\"' *> manyTill L.charLiteral (char '\"')

-- list related -- 

list :: Parser [HiExpr]
list = between (symbol "[") (symbol "]") argsParser

wrapListFun :: HiExpr
wrapListFun = HiExprValue $ HiValueFunction HiFunList

listExprParser :: Parser HiExpr
listExprParser = HiExprApply wrapListFun <$> list


-- Byte related -- 

byte :: Parser Word8
byte = do
  x <- hexDigitChar
  y <- hexDigitChar
  _ <- sc
  pure $ fromIntegral (hexDigitToInt x * 16 + hexDigitToInt y)

bytearr :: Parser ByteString
bytearr = B.pack <$> between (symbol "[#") (symbol "#]") (many byte)


-- Dict related -- 

dictPairParser :: Parser (HiExpr, HiExpr)
dictPairParser = do
  key <- allExprParser
  _   <- symbol ":"
  val <- allExprParser
  pure (key, val)

dictExprParser :: Parser HiExpr
dictExprParser = between (symbol "{")
                         (symbol "}")
                         (HiExprDict <$> (dictPairParser `sepBy` symbol ","))
