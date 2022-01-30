module HW1.T6 where


mcat :: Monoid a => [Maybe a] -> a
mcat x = extract (foldr fold Nothing x)
 where
  fold :: Monoid a => Maybe a -> Maybe a -> Maybe a
  fold Nothing  Nothing    = Nothing
  fold Nothing  (Just acc) = Just acc
  fold (Just x) Nothing    = Just x
  fold (Just x) (Just acc) = Just (x <> acc)
  extract :: Monoid a => Maybe a -> a
  extract (Just a) = a
  extract Nothing  = mempty


epart :: (Monoid a, Monoid b) => [Either a b] -> (a, b)
epart = foldr fold (mempty, mempty)
 where
  fold :: (Monoid a, Monoid b) => Either a b -> (a, b) -> (a, b)
  fold (Right r) (x, y) = (x, r <> y)
  fold (Left  l) (x, y) = (l <> x, y)

