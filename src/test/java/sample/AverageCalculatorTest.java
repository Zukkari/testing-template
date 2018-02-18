package sample;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AverageCalculatorTest {

  private static final double MAX_ROUNDING_ERROR = 0.000001;
  private AverageCalculator calc = new AverageCalculator();

  @Test(expected = IllegalArgumentException.class)
  public void failsIfNoValuesAreProvided() {
    calc.averageOf();
  }

  @Test
  public void singleValueIsAverageOfSelf() {
    assertEquals(1, calc.averageOf(1), MAX_ROUNDING_ERROR);
  }

  @Test
  public void positiveNumbersAverageIsCorrect() {
    assertEquals(2, calc.averageOf(1, 2, 3), MAX_ROUNDING_ERROR);
  }

  @Test
  public void negativeNumbersAverageIsCorrect() {
    assertEquals(-2, calc.averageOf(-1, -2, -3), MAX_ROUNDING_ERROR);
  }
}
