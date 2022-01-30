module HW1.T1 where
import           Numeric.Natural

data Day = Monday | Tuesday | Wednesday | Thursday | Friday | Saturday | Sunday
    deriving (Show)

nextDay :: Day -> Day
nextDay Monday    = Tuesday
nextDay Tuesday   = Wednesday
nextDay Wednesday = Thursday
nextDay Thursday  = Friday
nextDay Friday    = Saturday
nextDay Saturday  = Sunday
nextDay Sunday    = Monday

isWeekend :: Day -> Bool
isWeekend Saturday = True
isWeekend Sunday   = True
isWeekend x        = False

afterDays :: Natural -> Day -> Day
afterDays 0 day = day
afterDays x day = afterDays (x - 1) (nextDay day)

daysToParty :: Day -> Natural
daysToParty Friday = 0
daysToParty day    = daysToParty (nextDay day) + 1
