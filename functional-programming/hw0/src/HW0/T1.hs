{-# LANGUAGE TypeOperators #-}
{-# LANGUAGE LambdaCase #-}
module HW0.T1(distrib, flipIso, runIso, distrib, assocPair, assocEither, type (<->)(Iso)) where

data a <-> b = Iso (a -> b) (b -> a)

flipIso :: (a <-> b) -> (b <-> a)
flipIso (Iso f g) = Iso g f

runIso :: (a <-> b) -> (a -> b)
runIso (Iso f _) = f

distrib :: Either a (b, c) -> (Either a b, Either a c)
distrib (Left a) = (Left a, Left a)
distrib (Right (b, c)) = (Right b, Right c)

assocPair :: (a, (b, c)) <-> ((a, b), c)
assocPair = Iso (\x -> ((fst x, (fst $ snd x)), snd $ snd x)) (\y -> (fst $ fst y, (snd $ fst y, snd y)))

assocEither :: Either a (Either b c) <-> Either (Either a b) c
assocEither = Iso (\case 
                      (Left x) -> Left (Left x)
                      (Right (Left x)) -> Left (Right x)
                      (Right (Right x)) -> Right x
                      ) 
                    (\case 
                      (Left (Left x)) -> Left x
                      (Left (Right x)) -> Right (Left x)
                      (Right x) -> Right (Right x)
                      ) 