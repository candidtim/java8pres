package com.example.rpn;

import java.util.Arrays;


public class Main
{
  public static void main(String[] args)
  {
    Calculator calculator = new ReduceCalculator(AlgebraicOperators.OPERATORS);
    Arrays.stream(args).forEach(expr -> calculator.calculate(expr).bimap(
        error  -> System.out.printf("Error in expression: `%s`: %s\n", expr, error),
        result -> System.out.printf("%s = %.2f\n", expr, result)
    ));
  }
}
