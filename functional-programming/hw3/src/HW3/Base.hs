{-# LANGUAGE BlockArguments #-}
module HW3.Base
  ( HiFun(..)
  , HiExpr(..)
  , HiError(..)
  , HiValue(..)
  , HiAction(..)
  , HiMonad(..)
  ) where
import qualified Codec.Serialise               as CS
import qualified Codec.Serialise.Decoding      as CSD
import qualified Codec.Serialise.Encoding      as CSE
                                                ( Encoding
                                                , encodeInt
                                                , encodeListLen
                                                , encodeWord
                                                )
import           Data.ByteString                ( ByteString )
import           Data.Map
import           Data.Sequence                  ( Seq )
import           Data.Text                      ( Text )
import           Data.Time                      ( UTCTime )

data HiFun =
  HiFunDiv
  | HiFunMul
  | HiFunAdd
  | HiFunSub
  | HiFunNot
  | HiFunAnd
  | HiFunOr
  | HiFunLessThan
  | HiFunGreaterThan
  | HiFunEquals
  | HiFunNotLessThan
  | HiFunNotGreaterThan
  | HiFunNotEquals
  | HiFunIf
  | HiFunLength
  | HiFunToUpper
  | HiFunToLower
  | HiFunReverse
  | HiFunTrim
  | HiFunList
  | HiFunRange
  | HiFunFold
  | HiFunPackBytes
  | HiFunUnpackBytes
  | HiFunEncodeUtf8
  | HiFunDecodeUtf8
  | HiFunZip
  | HiFunUnzip
  | HiFunSerialise
  | HiFunDeserialise
  | HiFunRead
  | HiFunWrite
  | HiFunMkDir
  | HiFunChDir
  | HiFunParseTime
  | HiFunRand
  | HiFunEcho
  | HiFunCount
  | HiFunKeys
  | HiFunValues
  | HiFunInvert
   deriving (Show, Eq, Ord, Enum)

data HiValue =
  HiValueNumber Rational
  | HiValueFunction HiFun
  | HiValueBool Bool
  | HiValueNull
  | HiValueString Text
  | HiValueList (Seq HiValue)
  | HiValueBytes ByteString
  | HiValueAction HiAction
  | HiValueTime UTCTime
  | HiValueDict (Map HiValue HiValue)
  deriving (Show, Ord, Eq)

data HiExpr =
  HiExprValue HiValue
  | HiExprApply HiExpr [HiExpr]
  | HiExprRun HiExpr
  | HiExprDict [(HiExpr, HiExpr)] deriving (Eq, Show)

data HiError =
  HiErrorInvalidArgument
  | HiErrorInvalidFunction
  | HiErrorArityMismatch
  | HiErrorDivideByZero deriving (Eq, Ord, Show)

data HiAction =
    HiActionRead  FilePath
  | HiActionWrite FilePath ByteString
  | HiActionMkDir FilePath
  | HiActionChDir FilePath
  | HiActionCwd
  | HiActionNow
  | HiActionRand Int Int
  | HiActionEcho Text
   deriving(Show, Ord, Eq)

class Monad m => HiMonad m where
  runAction :: HiAction -> m HiValue


encodeHiValue :: HiValue -> CSE.Encoding
encodeHiValue (HiValueNumber n) =
  CSE.encodeListLen 2 <> CSE.encodeWord 0 <> CS.encode n
encodeHiValue (HiValueFunction f) =
  CSE.encodeListLen 2 <> CSE.encodeWord 1 <> CS.encode f
encodeHiValue (HiValueBool b) =
  CSE.encodeListLen 2 <> CSE.encodeWord 2 <> CS.encode b
encodeHiValue HiValueNull = CSE.encodeListLen 1 <> CSE.encodeWord 3
encodeHiValue (HiValueString t) =
  CSE.encodeListLen 2 <> CSE.encodeWord 4 <> CS.encode t
encodeHiValue (HiValueBytes bs) =
  CSE.encodeListLen 2 <> CSE.encodeWord 5 <> CS.encode bs
encodeHiValue (HiValueAction a) =
  CSE.encodeListLen 2 <> CSE.encodeWord 6 <> CS.encode a
encodeHiValue (HiValueTime t) =
  CSE.encodeListLen 2 <> CSE.encodeWord 7 <> CS.encode t
encodeHiValue (HiValueList l) =
  CSE.encodeListLen 2 <> CSE.encodeWord 8 <> CS.encode l
encodeHiValue (HiValueDict d) =
  CSE.encodeListLen 2 <> CSE.encodeWord 9 <> CS.encode d

decodeHiValue :: CSD.Decoder s HiValue
decodeHiValue = do
  len <- CSD.decodeListLen
  tag <- CSD.decodeWord
  case (len, tag) of
    (2, 0) -> HiValueNumber <$> CS.decode
    (2, 1) -> HiValueFunction <$> CS.decode
    (2, 2) -> HiValueBool <$> CS.decode
    (1, 3) -> pure HiValueNull
    (2, 4) -> HiValueString <$> CS.decode
    (2, 5) -> HiValueBytes <$> CS.decode
    (2, 6) -> HiValueAction <$> CS.decode
    (2, 7) -> HiValueTime <$> CS.decode
    (2, 8) -> HiValueList <$> CS.decode
    (2, 9) -> HiValueDict <$> CS.decode
    _      -> undefined


encodeAction :: HiAction -> CSE.Encoding
encodeAction (HiActionRead p) =
  CSE.encodeListLen 2 <> CSE.encodeWord 0 <> CS.encode p
encodeAction (HiActionWrite p s) =
  CSE.encodeListLen 3 <> CSE.encodeWord 1 <> CS.encode p <> CS.encode s
encodeAction (HiActionMkDir p) =
  CSE.encodeListLen 2 <> CSE.encodeWord 2 <> CS.encode p
encodeAction (HiActionChDir p) =
  CSE.encodeListLen 2 <> CSE.encodeWord 3 <> CS.encode p
encodeAction HiActionCwd = CSE.encodeListLen 1 <> CSE.encodeWord 4
encodeAction HiActionNow = CSE.encodeListLen 1 <> CSE.encodeWord 5
encodeAction (HiActionRand x y) =
  CSE.encodeListLen 3 <> CSE.encodeWord 6 <> CS.encode x <> CS.encode y
encodeAction (HiActionEcho t) =
  CSE.encodeListLen 2 <> CSE.encodeWord 7 <> CS.encode t

decodeAction :: CSD.Decoder s HiAction
decodeAction = do
  len <- CSD.decodeListLen
  tag <- CSD.decodeWord
  case (len, tag) of
    (2, 0) -> HiActionRead <$> CS.decode
    (3, 1) -> HiActionWrite <$> CS.decode <*> CS.decode
    (2, 2) -> HiActionMkDir <$> CS.decode
    (2, 3) -> HiActionChDir <$> CS.decode
    (1, 4) -> pure HiActionCwd
    (1, 5) -> pure HiActionNow
    (3, 6) -> HiActionRand <$> CS.decode <*> CS.decode
    (4, 6) -> HiActionEcho <$> CS.decode
    _      -> fail "No such action"

instance CS.Serialise HiFun where
  encode x = CSE.encodeInt (fromEnum x)
  decode = toEnum <$> CS.decode

instance CS.Serialise HiAction where
  encode = encodeAction
  decode = decodeAction

instance CS.Serialise HiValue where
  encode = encodeHiValue
  decode = decodeHiValue
