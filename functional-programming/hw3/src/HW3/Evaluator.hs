module HW3.Evaluator
  ( eval
  , evalAction
  ) where

import qualified Codec.Compression.Zlib        as Z
                                                ( CompressParams(compressLevel)
                                                , bestCompression
                                                , compressWith
                                                , decompress
                                                , defaultCompressParams
                                                )
import qualified Codec.Serialise               as SE
import qualified Codec.Serialise               as SD
import           Control.Arrow                  ( (&&&) )
import           Control.Monad.Trans.Except     ( ExceptT(ExceptT)
                                                , runExceptT
                                                , throwE
                                                )
import qualified Data.ByteString               as B
import           Data.ByteString.Lazy           ( fromStrict
                                                , toStrict
                                                )
import qualified Data.ByteString.Lazy          as BL
import qualified Data.Foldable                 as F
import           Data.Foldable                  ( toList )
import           Data.List                      ( group
                                                , sort
                                                )
import qualified Data.Map                      as M
import qualified Data.Map                      as D
import           Data.Semigroup                 ( stimes )
import qualified Data.Sequence                 as S
                                                ( (><)
                                                , drop
                                                , fromList
                                                , index
                                                , length
                                                , reverse
                                                , take
                                                )
import           Data.Sequence                  ( Seq
                                                , takeWhileL
                                                )
import qualified Data.Text                     as T
                                                ( Text
                                                , concat
                                                , drop
                                                , index
                                                , length
                                                , pack
                                                , reverse
                                                , strip
                                                , take
                                                , toLower
                                                , toUpper
                                                , unpack
                                                )
import           Data.Text.Encoding             ( decodeUtf8'
                                                , encodeUtf8
                                                )
import qualified Data.Text.Encoding            as T
import           Data.Text.Encoding.Error       ( UnicodeException )
import           Data.Time                      ( UTCTime
                                                , addUTCTime
                                                , diffUTCTime
                                                )
import           Data.Tuple                     ( swap )
import           Data.Word                      ( Word8 )
import           HW3.Base
import           Text.Read                      ( readMaybe )

eval :: HiMonad m => HiExpr -> m (Either HiError HiValue)
eval e = runExceptT (eval' e)

eval' :: HiMonad m => HiExpr -> ExceptT HiError m HiValue
eval' (HiExprValue e                   ) = pure e
eval' (HiExprApply (HiExprValue f) args) = evalApply f args
eval' (HiExprApply m               args) = do
  evalM <- eval' m
  eval' (HiExprApply (HiExprValue evalM) args)
eval' (HiExprRun m) = do
  evalM <- eval' m
  evalAction evalM
eval' (HiExprDict pairs) = do
  evaluated <- traverse evalPairs pairs
  pure $ HiValueDict $ M.fromList evaluated
 where
  evalPairs
    :: HiMonad m => (HiExpr, HiExpr) -> ExceptT HiError m (HiValue, HiValue)
  evalPairs (x, y) = do
    a <- eval' x
    b <- eval' y
    pure (a, b)

evalApply :: HiMonad m => HiValue -> [HiExpr] -> ExceptT HiError m HiValue
evalApply (HiValueFunction HiFunOr ) args = evalOr args
evalApply (HiValueFunction HiFunAnd) args = evalAnd args
evalApply (HiValueFunction HiFunIf ) args = evalIf args
evalApply (HiValueFunction f       ) args = do
  evaluatedArgs <- traverse eval' args
  evalFunViewArity f evaluatedArgs
evalApply x@(HiValueString _) args = do
  evaluatedArgs <- traverse eval' args
  evalArrLikeViewArity x evaluatedArgs
evalApply x@(HiValueList _) args = do
  evaluatedArgs <- traverse eval' args
  evalArrLikeViewArity x evaluatedArgs
evalApply x@(HiValueBytes _) args = do
  evaluatedArgs <- traverse eval' args
  evalArrLikeViewArity x evaluatedArgs
evalApply x@(HiValueDict _) args = do
  evaluatedArgs <- traverse eval' args
  evalArrLikeViewArity x evaluatedArgs
evalApply _ _ = throwE HiErrorInvalidFunction


evalFunViewArity
  :: HiMonad m => HiFun -> [HiValue] -> ExceptT HiError m HiValue
evalFunViewArity HiFunList args   = evalList args
evalFunViewArity f         [x]    = evalUnary f x
evalFunViewArity f         [x, y] = evalBinary f x y
evalFunViewArity _         _      = throwE HiErrorArityMismatch

-- "Posfix" operations on strings and lists (indexing, slices) --
evalArrLikeViewArity
  :: HiMonad m => HiValue -> [HiValue] -> ExceptT HiError m HiValue
evalArrLikeViewArity s args = case length args of
  1 -> evalIndex s (head args)
  2 -> evalSlice s (head args) (last args)
  _ -> throwE HiErrorArityMismatch

-- Arity-dependend evals --

evalUnary :: HiMonad m => HiFun -> HiValue -> ExceptT HiError m HiValue
evalUnary HiFunNot         = evalNot
evalUnary HiFunLength      = evalLength
evalUnary HiFunToUpper     = evalToUpper
evalUnary HiFunToLower     = evalToLower
evalUnary HiFunReverse     = evalReverse
evalUnary HiFunTrim        = evalTrim
evalUnary HiFunUnzip       = evalUnzip
evalUnary HiFunZip         = evalZip
evalUnary HiFunEncodeUtf8  = evalEncodeUtf8
evalUnary HiFunDecodeUtf8  = evalDecodeUtf8
evalUnary HiFunUnpackBytes = evalUnpackBytes
evalUnary HiFunPackBytes   = evalPackBytes
evalUnary HiFunRead        = evalRead
evalUnary HiFunMkDir       = evalMkDir
evalUnary HiFunChDir       = evalChDir
evalUnary HiFunParseTime   = evalParseTime
evalUnary HiFunEcho        = evalEcho
evalUnary HiFunKeys        = evalKeys
evalUnary HiFunValues      = evalValues
evalUnary HiFunInvert      = evalInvert
evalUnary HiFunCount       = evalCount
evalUnary HiFunSerialise   = evalSerialise
evalUnary HiFunDeserialise = evalDeserialise
evalUnary _                = ignoreUnaryThrow

ignoreUnaryThrow :: HiMonad m => HiValue -> ExceptT HiError m HiValue
ignoreUnaryThrow _ = throwE HiErrorArityMismatch

evalBinary
  :: HiMonad m => HiFun -> HiValue -> HiValue -> ExceptT HiError m HiValue
evalBinary HiFunAdd            = evalAdd
evalBinary HiFunSub            = evalSub
evalBinary HiFunMul            = evalMul
evalBinary HiFunDiv            = evalDiv
evalBinary HiFunEquals         = evalEquals
evalBinary HiFunRange          = evalRange
evalBinary HiFunNotEquals      = evalNotEquals
evalBinary HiFunLessThan       = evalLessThan
evalBinary HiFunGreaterThan    = evalGreaterThan
evalBinary HiFunNotGreaterThan = evalNotGreaterThan
evalBinary HiFunNotLessThan    = evalNotLessThan
evalBinary HiFunFold           = evalFold
evalBinary HiFunWrite          = evalWrite
evalBinary HiFunRand           = evalRand
evalBinary HiFunAnd            = evalLazyWrapExpr evalAnd
evalBinary HiFunOr             = evalLazyWrapExpr evalOr
evalBinary _                   = ignoreBinaryThrow

evalLazyWrapExpr
  :: ([HiExpr] -> ExceptT HiError m HiValue)
  -> HiValue
  -> HiValue
  -> ExceptT HiError m HiValue
evalLazyWrapExpr f x y = f $ map HiExprValue [x, y]

ignoreBinaryThrow
  :: HiMonad m => HiValue -> HiValue -> ExceptT HiError m HiValue
ignoreBinaryThrow _ _ = throwE HiErrorArityMismatch

-- Basic operations (arithmetic + strings) -- 

evalAdd :: HiMonad m => HiValue -> HiValue -> ExceptT HiError m HiValue
evalAdd (HiValueNumber x) (HiValueNumber y) = pure $ HiValueNumber (x + y)
evalAdd (HiValueString x) (HiValueString y) =
  pure $ HiValueString (T.concat [x, y])
evalAdd (HiValueList x) (HiValueList y) = pure $ HiValueList (x S.>< y)
evalAdd (HiValueTime t) (HiValueNumber n) =
  pure $ HiValueTime $ addUTCTime (realToFrac n) t
evalAdd (HiValueBytes x) (HiValueBytes y) =
  pure $ HiValueBytes $ B.concat [x, y]
evalAdd _ _ = throwE HiErrorInvalidArgument

isInt :: Rational -> Bool
isInt x = x == fromInteger (round x)

toInt :: Rational -> Int
toInt = round

mulArrLike
  :: (Semigroup a, Monad m)
  => Rational
  -> a
  -> (a -> HiValue)
  -> ExceptT HiError m HiValue
mulArrLike n x f = if satisfy n
  then pure $ f $ stimes (toInt n) x
  else throwE HiErrorInvalidArgument
 where
  satisfy :: Rational -> Bool
  satisfy v = v > 0 && isInt v

evalMul :: HiMonad m => HiValue -> HiValue -> ExceptT HiError m HiValue
evalMul (HiValueNumber x) (HiValueNumber y) = pure $ HiValueNumber $ x * y
evalMul (HiValueString x) (HiValueNumber y) = mulArrLike y x HiValueString
evalMul (HiValueList   x) (HiValueNumber y) = mulArrLike y x HiValueList
evalMul (HiValueBytes  x) (HiValueNumber y) = mulArrLike y x HiValueBytes
evalMul _                 _                 = throwE HiErrorInvalidArgument

evalDiv :: HiMonad m => HiValue -> HiValue -> ExceptT HiError m HiValue
evalDiv _                 (HiValueNumber 0) = throwE HiErrorDivideByZero
evalDiv (HiValueNumber x) (HiValueNumber y) = pure (HiValueNumber (x / y))
evalDiv (HiValueString x) (HiValueString y) =
  pure (HiValueString (T.concat [x, T.pack "/", y]))
evalDiv _ _ = throwE HiErrorInvalidArgument

evalSub :: HiMonad m => HiValue -> HiValue -> ExceptT HiError m HiValue
evalSub (HiValueNumber x) (HiValueNumber y) = pure (HiValueNumber (x - y))
evalSub (HiValueTime x) (HiValueTime y) =
  pure $ HiValueNumber . realToFrac $ diffUTCTime x y
evalSub _ _ = throwE HiErrorInvalidArgument


-- Boolean algebra --

evalNot :: HiMonad m => HiValue -> ExceptT HiError m HiValue
evalNot (HiValueBool x) = pure (HiValueBool (not x))
evalNot _               = throwE HiErrorInvalidArgument

evalAnd :: HiMonad m => [HiExpr] -> ExceptT HiError m HiValue
evalAnd [a, b] = do
  x <- eval' a
  case x of
    HiValueNull         -> pure x
    (HiValueBool False) -> pure x
    _                   -> eval' b
evalAnd _ = throwE HiErrorArityMismatch

evalOr :: HiMonad m => [HiExpr] -> ExceptT HiError m HiValue
evalOr [a, b] = do
  x <- eval' a
  case x of
    HiValueNull         -> eval' b
    (HiValueBool False) -> eval' b
    _                   -> pure x
evalOr _ = throwE HiErrorArityMismatch


-- Comparisons -- 

evalEquals :: HiMonad m => HiValue -> HiValue -> ExceptT HiError m HiValue
evalEquals x y = pure (HiValueBool (x == y))

evalNotEquals :: HiMonad m => HiValue -> HiValue -> ExceptT HiError m HiValue
evalNotEquals x y = do
  z <- evalEquals x y
  evalNot z

evalLessThan :: HiMonad m => HiValue -> HiValue -> ExceptT HiError m HiValue
evalLessThan (HiValueBool   _) (HiValueNumber _) = pure (HiValueBool True)
evalLessThan (HiValueNumber _) (HiValueBool   _) = pure (HiValueBool False)
evalLessThan x                 y                 = pure (HiValueBool (x < y))

evalNotLessThan :: HiMonad m => HiValue -> HiValue -> ExceptT HiError m HiValue
evalNotLessThan x y = do
  z <- evalLessThan x y
  evalNot z

evalGreaterThan :: HiMonad m => HiValue -> HiValue -> ExceptT HiError m HiValue
evalGreaterThan (HiValueBool   _) (HiValueNumber _) = pure (HiValueBool False)
evalGreaterThan (HiValueNumber _) (HiValueBool   _) = pure (HiValueBool True)
evalGreaterThan x                 y                 = pure (HiValueBool (x > y))

evalNotGreaterThan
  :: HiMonad m => HiValue -> HiValue -> ExceptT HiError m HiValue
evalNotGreaterThan x y = do
  z <- evalGreaterThan x y
  evalNot z

-- If --

evalIf :: HiMonad m => [HiExpr] -> ExceptT HiError m HiValue
evalIf [p, t, f] = do
  condition <- eval' p
  case condition of
    (HiValueBool True ) -> eval' t
    (HiValueBool False) -> eval' f
    _                   -> throwE HiErrorInvalidArgument
evalIf _ = throwE HiErrorArityMismatch


-- Strings and lists --

evalLength :: HiMonad m => HiValue -> ExceptT HiError m HiValue
evalLength (HiValueString s) = pure $ HiValueNumber (toRational (T.length s))
evalLength (HiValueList   l) = pure $ HiValueNumber (toRational (S.length l))
evalLength _                 = throwE HiErrorInvalidArgument

evalToUpper :: HiMonad m => HiValue -> ExceptT HiError m HiValue
evalToUpper (HiValueString s) = pure $ HiValueString (T.toUpper s)
evalToUpper _                 = throwE HiErrorInvalidArgument

evalToLower :: HiMonad m => HiValue -> ExceptT HiError m HiValue
evalToLower (HiValueString s) = pure (HiValueString (T.toLower s))
evalToLower _                 = throwE HiErrorInvalidArgument

evalReverse :: HiMonad m => HiValue -> ExceptT HiError m HiValue
evalReverse (HiValueString s) = pure $ HiValueString (T.reverse s)
evalReverse (HiValueList   l) = pure $ HiValueList (S.reverse l)
evalReverse (HiValueBytes  b) = pure $ HiValueBytes (B.reverse b)
evalReverse _                 = throwE HiErrorInvalidArgument

evalTrim :: HiMonad m => HiValue -> ExceptT HiError m HiValue
evalTrim (HiValueString s) = pure (HiValueString (T.strip s))
evalTrim _                 = throwE HiErrorInvalidArgument

uniIndex :: HiValue -> Int -> HiValue
uniIndex (HiValueString s) idx = HiValueString (T.pack [T.index s idx])
uniIndex (HiValueList   l) idx = S.index l idx
uniIndex (HiValueBytes  b) idx = HiValueNumber $ fromIntegral $ B.index b idx
uniIndex _                 _   = undefined

evalIndex :: HiMonad m => HiValue -> HiValue -> ExceptT HiError m HiValue
evalIndex (HiValueDict x) i =
  pure $ if D.notMember i x then HiValueNull else x D.! i
evalIndex x (HiValueNumber i)
  | not (isInt i)                           = throwE HiErrorInvalidArgument
  | not (0 <= idx && idx < arrLikeLength x) = pure HiValueNull
  | otherwise                               = pure $ uniIndex x idx
  where idx = toInt i
evalIndex _ _ = throwE HiErrorInvalidArgument

fromPythonIndex :: HiValue -> Int -> Int
fromPythonIndex t i =
  min (arrLikeLength t) (max 0 (if i < 0 then arrLikeLength t + i else i))

crop :: HiValue -> Int -> Int -> HiValue
crop (HiValueString t) l r = HiValueString (T.drop l (T.take r t))
crop (HiValueList   t) l r = HiValueList (S.drop l (S.take r t))
crop (HiValueBytes  t) l r = HiValueBytes (B.drop l (B.take r t))
crop _                 _ _ = undefined

arrLikeLength :: HiValue -> Int
arrLikeLength (HiValueString t) = T.length t
arrLikeLength (HiValueList   l) = S.length l
arrLikeLength (HiValueBytes  b) = B.length b
arrLikeLength _                 = undefined

evalSlice
  :: HiMonad m => HiValue -> HiValue -> HiValue -> ExceptT HiError m HiValue
evalSlice x HiValueNull r = evalSlice x (HiValueNumber 0) r
evalSlice x v HiValueNull =
  evalSlice x v (HiValueNumber $ fromIntegral (arrLikeLength x))
evalSlice x (HiValueNumber l) (HiValueNumber r)
  | not (isInt l) || not (isInt r) = throwE HiErrorInvalidArgument
  | otherwise                      = pure $ crop x i1 i2
 where
  i1 = fromPythonIndex x $ toInt l
  i2 = fromPythonIndex x $ toInt r
evalSlice _ _ _ = throwE HiErrorInvalidArgument


-- Lists --

evalList :: HiMonad m => [HiValue] -> ExceptT HiError m HiValue
evalList a = pure $ HiValueList $ S.fromList a

evalRange :: HiMonad m => HiValue -> HiValue -> ExceptT HiError m HiValue
evalRange (HiValueNumber l) (HiValueNumber r) = pure $ HiValueList $ fmap
  HiValueNumber
  (S.fromList $ takeWhile (<= r) $ iterate (+ 1) l)
evalRange _ _ = throwE HiErrorInvalidArgument


evalFold :: HiMonad m => HiValue -> HiValue -> ExceptT HiError m HiValue
evalFold (HiValueFunction f) (HiValueList l) = if null l
  then pure HiValueNull
  else foldl1
    (\v1 v2 -> do
      z1 <- v1
      z2 <- v2
      evalBinary f z1 z2
    )
    (pure <$> F.toList l)
evalFold _ _ = throwE HiErrorInvalidArgument


-- Bytes and serialization -- 

evalDecodeUtf8 :: HiMonad m => HiValue -> ExceptT HiError m HiValue
evalDecodeUtf8 (HiValueBytes s) = pure $ extract $ decodeUtf8' s
 where
  extract :: Either UnicodeException T.Text -> HiValue
  extract (Left  _  ) = HiValueNull
  extract (Right res) = HiValueString res
evalDecodeUtf8 _ = throwE HiErrorInvalidArgument

evalEncodeUtf8 :: HiMonad m => HiValue -> ExceptT HiError m HiValue
evalEncodeUtf8 (HiValueString s) = pure $ HiValueBytes $ encodeUtf8 s
evalEncodeUtf8 _                 = throwE HiErrorInvalidArgument

evalZip :: HiMonad m => HiValue -> ExceptT HiError m HiValue
evalZip (HiValueBytes x) = pure
  (HiValueBytes $ toStrict $ Z.compressWith
    Z.defaultCompressParams { Z.compressLevel = Z.bestCompression }
    (fromStrict x)
  )
evalZip _ = throwE HiErrorInvalidArgument

evalUnzip :: HiMonad m => HiValue -> ExceptT HiError m HiValue
evalUnzip (HiValueBytes x) =
  pure (HiValueBytes $ toStrict $ Z.decompress (fromStrict x))
evalUnzip _ = throwE HiErrorInvalidArgument

evalUnpackBytes :: HiMonad m => HiValue -> ExceptT HiError m HiValue
evalUnpackBytes (HiValueBytes b) =
  evalList $ map (HiValueNumber . fromIntegral) (B.unpack b)
evalUnpackBytes _ = throwE HiErrorInvalidArgument

evalPackBytes :: HiMonad m => HiValue -> ExceptT HiError m HiValue
evalPackBytes (HiValueList l) = do
  let isValid = isValidList l
  if not isValid
    then throwE HiErrorInvalidArgument
    else pure $ HiValueBytes $ B.pack $ toList $ fmap unpackNumber l
 where
  isValidList :: Seq HiValue -> Bool
  isValidList x = length (takeWhileL is8BitNumber x) == length x

  is8BitNumber :: HiValue -> Bool
  is8BitNumber (HiValueNumber n) = n <= 255 && n >= 0
  is8BitNumber _                 = False

  unpackNumber :: HiValue -> Word8
  unpackNumber (HiValueNumber n) = fromIntegral $ toInt n
  unpackNumber _                 = 0
evalPackBytes _ = throwE HiErrorInvalidArgument


evalSerialise :: HiMonad m => HiValue -> ExceptT HiError m HiValue
evalSerialise = pure . HiValueBytes . BL.toStrict . SE.serialise

evalDeserialise :: HiMonad m => HiValue -> ExceptT HiError m HiValue
evalDeserialise (HiValueBytes b) = pure $ SD.deserialise $ BL.fromStrict b
evalDeserialise _                = throwE HiErrorInvalidArgument

-- actions -- 

evalAction :: HiMonad m => HiValue -> ExceptT HiError m HiValue
evalAction (HiValueAction a) = ExceptT (fmap Right (runAction a))
evalAction _                 = throwE HiErrorInvalidArgument


-- directory stuff --

evalWrite :: HiMonad m => HiValue -> HiValue -> ExceptT HiError m HiValue
evalWrite (HiValueString x) (HiValueBytes y) =
  pure $ HiValueAction $ HiActionWrite (T.unpack x) y
evalWrite (HiValueString x) (HiValueString y) =
  pure $ HiValueAction $ HiActionWrite (T.unpack x) (T.encodeUtf8 y)
evalWrite _ _ = throwE HiErrorInvalidArgument

evalRead :: HiMonad m => HiValue -> ExceptT HiError m HiValue
evalRead (HiValueString x) = pure $ HiValueAction $ HiActionRead (T.unpack x)
evalRead _                 = throwE HiErrorInvalidArgument

evalMkDir :: HiMonad m => HiValue -> ExceptT HiError m HiValue
evalMkDir (HiValueString x) = pure $ HiValueAction $ HiActionMkDir (T.unpack x)
evalMkDir _                 = throwE HiErrorInvalidArgument

evalChDir :: HiMonad m => HiValue -> ExceptT HiError m HiValue
evalChDir (HiValueString x) = pure $ HiValueAction $ HiActionChDir (T.unpack x)
evalChDir _                 = throwE HiErrorInvalidArgument


-- time -- 

evalParseTime :: HiMonad m => HiValue -> ExceptT HiError m HiValue
evalParseTime (HiValueString t) =
  case readMaybe (T.unpack t) :: Maybe UTCTime of
    (Just time) -> pure $ HiValueTime time
    Nothing     -> pure HiValueNull
evalParseTime _ = throwE HiErrorInvalidArgument


-- random --

evalRand :: HiMonad m => HiValue -> HiValue -> ExceptT HiError m HiValue
evalRand (HiValueNumber x) (HiValueNumber y) = if isInt x && isInt y
  then pure $ HiValueAction $ HiActionRand (toInt x) (toInt y)
  else throwE HiErrorInvalidArgument
evalRand _ _ = throwE HiErrorInvalidArgument


-- echo -- 

evalEcho :: HiMonad m => HiValue -> ExceptT HiError m HiValue
evalEcho (HiValueString x) = pure $ HiValueAction $ HiActionEcho x
evalEcho _                 = throwE HiErrorInvalidArgument


-- dict -- 

somethingFromDict
  :: HiMonad m
  => (M.Map HiValue HiValue -> [HiValue])
  -> HiValue
  -> ExceptT HiError m HiValue
somethingFromDict f (HiValueDict m) = pure $ HiValueList $ S.fromList $ f m
somethingFromDict _ _               = throwE HiErrorInvalidArgument

evalKeys :: HiMonad m => HiValue -> ExceptT HiError m HiValue
evalKeys = somethingFromDict M.keys

evalValues :: HiMonad m => HiValue -> ExceptT HiError m HiValue
evalValues = somethingFromDict M.elems

evalInvert :: HiMonad m => HiValue -> ExceptT HiError m HiValue
evalInvert (HiValueDict m) = pure $ HiValueDict $ M.fromListWith (\./) pairs
 where
  pairs = map (\(x, y) -> (y, HiValueList $ S.fromList [x])) $ M.toList m
  (\./) :: HiValue -> HiValue -> HiValue
  (\./) (HiValueList x) (HiValueList y) = HiValueList $ x S.>< y
evalInvert _ = throwE HiErrorInvalidArgument

evalCount :: HiMonad m => HiValue -> ExceptT HiError m HiValue
evalCount (HiValueList l) = pure $ HiValueDict $ M.fromList z
 where
  z =
    map (head &&& HiValueNumber . fromIntegral . length)
      . group
      . sort
      $ F.toList l
evalCount (HiValueString s) = pure $ HiValueDict $ M.fromList z
 where
  z =
    map
        (   HiValueString
        .   T.pack
        .   (: [])
        .   head
        &&& HiValueNumber
        .   fromIntegral
        .   length
        )
      . group
      . sort
      $ T.unpack s
evalCount (HiValueBytes b) = do
  l <- evalList $ map (HiValueNumber . fromIntegral) (B.unpack b)
  evalCount l
evalCount _ = throwE HiErrorInvalidArgument
