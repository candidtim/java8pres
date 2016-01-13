package com.example.rpn;

import fj.data.Either;


public interface Calculator
{
  Either<String,Double> calculate(String expression);
}
