package com.example.rpn;

import java.util.*;
import fj.data.*;


public class ReduceCalculator implements Calculator
{
  private OperatorsRegistry operators;

  public ReduceCalculator(OperatorsRegistry operators)
  {
    this.operators = operators;
  }

  public Either<String,Double> calculate(String expr)
  {
    return Array.array(expr.split(" ")).foldLeft(
        (errorOrStack, token) -> errorOrStack.right().bind(stack -> {
          if (NumberUtils.isNumeric(token)) {
            return Either.right(stack.cons(Double.parseDouble(token)));
          } else if (operators.find(token).isPresent() && stack.length() >= 2) {
            Double nextResult = operators.find(token).get().apply(stack.tail().head(), stack.head());
            return Either.right(stack.drop(2).cons(nextResult));
          } else if (operators.find(token).isPresent()) {
            return Either.left(String.format("not enough arguments for %s", token));
          } else {
            return Either.left(String.format("cannot parse element %s", token));
          }
        }),
        Either.<String,Seq<Double>>right(Seq.empty())
    ).right().bind(stack -> stack.length() == 1 ?
        Either.right(stack.head()) :
        Either.left(String.format("no operator found but result is not final: %s", stack.toString()))
    );
  }
}
