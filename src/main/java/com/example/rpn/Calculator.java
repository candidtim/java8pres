package com.example.rpn;

import java.util.*;
import java.util.function.BinaryOperator;


public class Calculator
{
  private OperatorsRegistry operators;

  public Calculator(OperatorsRegistry operators)
  {
    this.operators = operators;
  }

  public Either<String,Double> calculate(String expr)
  {
    String[] elements = expr.split(" ");
    Queue<Double> calcStack = Collections.asLifoQueue(new ArrayDeque<Double>());
    for (String element : elements) {
      BinaryOperator<Double> op = operators.find(element);
      if (op != null && calcStack.size() >= 2) {
        Double op2 = calcStack.poll();
        Double op1 = calcStack.poll();
        Double nextResult = op.apply(op1, op2);
        calcStack.add(nextResult);
      } else if (op != null) {
        return new Either.Left<>(String.format("not enough arguments for %s", element));
      } else if (!NumberUtils.isNumeric(element)) {
        return new Either.Left<>(String.format("cannot parse element %s", element));
      } else {
        calcStack.add(Double.parseDouble(element));
      }
    }

    if (calcStack.size() == 1) {
      return new Either.Right<>(calcStack.poll());
    } else {
      return new Either.Left<>(String.format("no operator found but result is not final: %s", calcStack.toString()));
    }
  }
}
