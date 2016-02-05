import Data.Char
import Control.Monad
import System.Environment

calculate :: String -> Either String Double
calculate expr = do
    stack <- foldM applyToken [] (words expr)
    result <- checkComplete stack
    return result

checkComplete :: [Double] -> Either String Double
checkComplete [result] = Right result
checkComplete stack    = Left $ "missing an operator on current stack of " ++ (show stack)

applyToken :: [Double] -> String -> Either String [Double]
applyToken (x:y:ys) "*" = return ((y * x):ys)
applyToken (x:y:ys) "/" = return ((y / x):ys)
applyToken (x:y:ys) "+" = return ((y + x):ys)
applyToken (x:y:ys) "-" = return ((y - x):ys)
applyToken xs numberString = liftM (:xs) (tryParse numberString)

tryParse :: (Read a) => String -> Either String a
tryParse st = case reads st of [(x,"")] -> Right x
                               _ -> Left $ "unknown token or too few arguments at " ++ st

main = do
  exprs <- getArgs
  let results = map calculate exprs
      outputs = zip exprs results
  mapM_ (\(expr, res) -> putStrLn (expr ++ " = " ++ showResult res)) outputs
  where showResult (Right n) = show n
        showResult (Left  s) = "error: " ++ (show s)
