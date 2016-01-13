package com.example.rpn;

import org.junit.Test;
import static org.junit.Assert.*;


public class RpnTest
{
  private Calculator calculator = new ReduceCalculator(AlgebraicOperators.OPERATORS);
  // private Calculator calculator = new RecursiveCalculator(AlgebraicOperators.OPERATORS);

  @Test
  public void testOperators()
  {
    assertEquals( 5.0, calculator.calculate("2 3 +").right().value(), 0.01);
    assertEquals(-1.0, calculator.calculate("2 3 -").right().value(), 0.01);
    assertEquals( 6.0, calculator.calculate("2 3 *").right().value(), 0.01);
    assertEquals(0.66, calculator.calculate("2 3 /").right().value(), 0.01);
  }

  @Test
  public void testExpressions()
  {
    assertEquals(-4.0, calculator.calculate("10 4 3 + 2 * -").right().value(), 0.01);
  }

  @Test
  public void testUnknownOperator()
  {
    assertTrue(calculator.calculate("1 2 ?").isLeft());
  }

  @Test
  public void testNotEnoughOperators()
  {
    assertTrue(calculator.calculate("1 2").isLeft());
  }

  @Test
  public void testNotEnoughArguments()
  {
    assertTrue(calculator.calculate("1 +").isLeft());
  }
}
