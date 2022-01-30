{-# OPTIONS_GHC -Wno-incomplete-patterns #-}
module HW3.Pretty
  ( prettyValue
  ) where

import           Control.Monad.Fix              ( fix )
import qualified Data.ByteString               as B
import           Data.Char                      ( intToDigit )
import           Data.Foldable                  ( toList )
import qualified Data.Map                      as M
import           Data.Ratio                     ( denominator )
import           Data.Scientific                ( Scientific
                                                , fromRationalRepetendUnlimited
                                                , toRealFloat
                                                )
import           Data.Word
import           GHC.Real                       ( numerator )
import           HW3.Base
import           Numeric                        ( showFFloat )
import           Prettyprinter                  ( Doc
                                                , Pretty(pretty)
                                                )
import           Prettyprinter.Render.Terminal

actionToStr :: HiAction -> String
actionToStr HiActionNow      = "now"
actionToStr (HiActionRead p) = "read(" ++ show p ++ ")"
actionToStr (HiActionWrite p bs) =
  "write(" ++ show p ++ ", " ++ prettyString (HiValueBytes bs) ++ ")"
actionToStr (HiActionMkDir p)  = "mkdir(" ++ show p ++ ")"
actionToStr (HiActionChDir p)  = "cd(" ++ show p ++ ")"
actionToStr HiActionCwd        = "cwd"
actionToStr (HiActionRand x y) = "rand(" ++ show x ++ ", " ++ show y ++ ")"
actionToStr (HiActionEcho t  ) = "echo(" ++ show t ++ ")"

funToStr :: HiFun -> String
funToStr HiFunDiv            = "div"
funToStr HiFunMul            = "mul"
funToStr HiFunAdd            = "add"
funToStr HiFunSub            = "sub"
funToStr HiFunNot            = "not"
funToStr HiFunAnd            = "and"
funToStr HiFunOr             = "or"
funToStr HiFunLessThan       = "less-than"
funToStr HiFunGreaterThan    = "greater-than"
funToStr HiFunEquals         = "equals"
funToStr HiFunNotLessThan    = "not-less-than"
funToStr HiFunNotGreaterThan = "not-greater-than"
funToStr HiFunNotEquals      = "not-equals"
funToStr HiFunIf             = "if"
funToStr HiFunLength         = "length"
funToStr HiFunToUpper        = "to-upper"
funToStr HiFunToLower        = "to-lower"
funToStr HiFunReverse        = "reverse"
funToStr HiFunTrim           = "trim"
funToStr HiFunList           = "list"
funToStr HiFunRange          = "range"
funToStr HiFunFold           = "fold"
funToStr HiFunPackBytes      = "pack-bytes"
funToStr HiFunUnpackBytes    = "unpack-bytes"
funToStr HiFunEncodeUtf8     = "encode-utf8"
funToStr HiFunDecodeUtf8     = "decode-utf8"
funToStr HiFunZip            = "zip"
funToStr HiFunUnzip          = "unzip"
funToStr HiFunSerialise      = "serialise"
funToStr HiFunDeserialise    = "deserialise"
funToStr HiFunRead           = "read"
funToStr HiFunWrite          = "write"
funToStr HiFunMkDir          = "mkdir"
funToStr HiFunChDir          = "cd"
funToStr HiFunParseTime      = "parse-time"
funToStr HiFunRand           = "rand"
funToStr HiFunEcho           = "echo"
funToStr HiFunCount          = "count"
funToStr HiFunKeys           = "keys"
funToStr HiFunValues         = "values"
funToStr HiFunInvert         = "invert"

numToStr :: Rational -> String
numToStr n = compose integ res den resulting repetend
 where
  num  = numerator n
  den  = denominator n

  den2 = fix (\rec x -> if x `mod` 2 == 0 then rec $ x `div` 2 else x) den
  den5 = fix (\rec x -> if x `mod` 5 == 0 then rec $ x `div` 5 else x) den2

  (resulting, repetend) =
    if den5 == 1 then fromRationalRepetendUnlimited n else (-1, Just $ -1)

  (integ, res) = quotRem num den

  compose :: Integer -> Integer -> Integer -> Scientific -> Maybe Int -> String
  compose 0 0 _ _ _ = "0"
  compose intPart 0 _ _ _ = show intPart
  compose _ _ _ x Nothing = showFFloat Nothing (toRealFloat x :: Double) ""
  compose 0 resPart denomPart _ _ = show resPart ++ "/" ++ show denomPart
  compose intPart resPart denomPart _ _ =
    show intPart
      ++ (if resPart > 0 then " + " else " - ")
      ++ show (abs resPart)
      ++ "/"
      ++ show denomPart

prettyString :: HiValue -> String
prettyString (HiValueString t)   = show t
prettyString (HiValueBool   l)   = if l then "true" else "false"
prettyString (HiValueTime   t)   = "parse-time(\"" ++ show t ++ "\")"
prettyString HiValueNull         = "null"
prettyString (HiValueNumber   n) = numToStr n
prettyString (HiValueAction   a) = actionToStr a
prettyString (HiValueFunction f) = funToStr f
prettyString (HiValueList l) =
  "[ "
    ++ fix
         (\rec z -> case z of
           [x     ] -> prettyString x
           (x : xs) -> prettyString x ++ ", " ++ rec xs
           []       -> ""
         )
         (toList l)
    ++ " ]"
prettyString (HiValueBytes b) =
  "[# "
    ++ fix
         (\rec z -> case z of
           [x     ] -> prettyWord8 x
           (x : xs) -> prettyWord8 x ++ " " ++ rec xs
           []       -> ""
         )
         (B.unpack b)
    ++ " #]"
 where
  prettyWord8 :: Word8 -> String
  prettyWord8 w =
    let x = fromIntegral w
    in  [intToDigit $ x `div` 16, intToDigit $ x `mod` 16]
prettyString (HiValueDict d) =
  "{ "
    ++ fix
         (\rec z -> case z of
           [p     ] -> prettyPair p
           (p : ps) -> prettyPair p ++ ", " ++ rec ps
           []       -> ""
         )
         (M.toList d)
    ++ " }"
 where
  prettyPair :: (HiValue, HiValue) -> String
  prettyPair (x, y) = prettyString x ++ ": " ++ prettyString y


prettyValue :: HiValue -> Doc AnsiStyle
prettyValue v = pretty $ prettyString v
