module Main where

import           Control.Monad.IO.Class
import           Data.Either                    ( fromRight )
import           Data.Set                       ( fromList )
import           HW3.Action
import           HW3.Base                       ( HiError
                                                , HiExpr(HiExprValue)
                                                , HiValue(HiValueNull)
                                                )
import           HW3.Evaluator
import           HW3.Parser
import           HW3.Pretty
import           System.Console.Haskeline

main :: IO ()
main = runInputT defaultSettings loop
 where
  loop :: InputT IO ()
  loop = do
    minput <- getInputLine "\nhi> "
    case minput of
      Nothing    -> return ()
      Just "bye" -> return ()
      Just input -> do
        exPrint <- getExternalPrint
        let parsed           = fromRight (HiExprValue HiValueNull) (parse input)
        let evaluatedWrapped = eval parsed :: HIO (Either HiError HiValue)
        value <- liftIO $ runHIO evaluatedWrapped
                                 (fromList [AllowRead, AllowWrite, AllowTime])
        _ <- liftIO $ case value of
          Left  e -> exPrint $ show e
          Right m -> print (prettyValue m)
        loop
