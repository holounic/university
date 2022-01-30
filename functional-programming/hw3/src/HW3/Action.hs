{-# LANGUAGE BlockArguments #-}
module HW3.Action
  ( PermissionException(..)
  , HIO(..)
  , HiPermission(..)
  ) where

import           Control.Exception
import           Control.Monad.IO.Class         ( liftIO )
import           Data.ByteString                ( writeFile )
import           Data.Functor                   ( (<$>) )
import           Data.Sequence                  ( fromList )
import           Data.Set                       ( Set
                                                , member
                                                )
import qualified Data.Text                     as T
                                         hiding ( map )
import           Data.Time                      ( getCurrentTime )
import           GHC.Base
import           GHC.Enum                       ( Bounded )
import           HW3.Base
import           Prelude                        ( Show
                                                , fromIntegral
                                                , print
                                                )
import           System.Directory
import           System.Directory.Internal.Prelude
                                                ( Enum )
import qualified System.Random                 as R

data HiPermission = AllowRead | AllowWrite | AllowTime deriving (Show, Ord, Eq, Enum, Bounded)
data PermissionException = PermissionRequired HiPermission
  deriving Show
instance Exception PermissionException

newtype HIO a = HIO { runHIO :: Set HiPermission -> IO a }

instance Monad HIO where
  (>>=) x f = HIO
    (\s -> do
      z <- runHIO x s
      runHIO (f z) s
    )
  return x = HIO (\_ -> pure x)

instance Functor HIO where
  fmap = liftM

instance Applicative HIO where
  pure  = return
  (<*>) = ap


instance HiMonad HIO where
  runAction HiActionCwd = HIO
    (\s -> if member AllowRead s
      then HiValueString . T.pack <$> getCurrentDirectory
      else throwIO $ PermissionRequired AllowRead
    )
  runAction (HiActionMkDir path) = HIO
    (\s -> if member AllowWrite s
      then HiValueNull <$ createDirectory path
      else throwIO $ PermissionRequired AllowWrite
    )
  runAction (HiActionWrite path bs) = HIO
    (\s -> if member AllowWrite s
      then HiValueNull <$ writeFile path bs
      else throwIO $ PermissionRequired AllowWrite
    )
  runAction (HiActionRead path) = HIO
    (\s -> if member AllowRead s
      then do
        m <- liftIO $ listDirectory path
        pure (HiValueList $ fromList $ map (HiValueString . T.pack) m)
      else throwIO $ PermissionRequired AllowRead
    )
  runAction (HiActionChDir path) = HIO
    (\s -> if member AllowRead s
      then HiValueNull <$ setCurrentDirectory path
      else throwIO $ PermissionRequired AllowRead
    )
  runAction HiActionNow = HIO
    (\s -> if member AllowTime s
      then HiValueTime <$> getCurrentTime
      else throwIO $ PermissionRequired AllowTime
    )
  runAction (HiActionRand x y) = HIO
    (\_ -> do
      g <- R.newStdGen
      let (n, _) = R.uniformR (x, y) g in pure $ HiValueNumber $ fromIntegral n
    )
  runAction (HiActionEcho t) = HIO
    (\s -> if member AllowWrite s
      then do
        _ <- liftIO $ print t
        pure HiValueNull
      else throwIO $ PermissionRequired AllowWrite
    )
