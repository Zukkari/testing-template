package sample;

import org.junit.Test;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;

public class AverageCalculatorTest {

  private static final double MAX_ROUNDING_ERROR = 0.000001;
  private AverageCalculator calc = new AverageCalculator();

  @Test(expected = IllegalArgumentException.class)
  public void failsIfNoValuesAreProvided() {
    calc.averageOf(emptyList());
  }

  @Test
  public void singleValueIsAverageOfSelf() {
    assertEquals(1, calc.averageOf(asList(1.0)), MAX_ROUNDING_ERROR);
  }

  @Test
  public void positiveNumbersAverageIsCorrect() {
    assertEquals(2, calc.averageOf(asList(1.0, 2.0, 3.0)), MAX_ROUNDING_ERROR);
  }

  @Test
  public void negativeNumbersAverageIsCorrect() {
    assertEquals(-2, calc.averageOf(asList(-1.0, -2.0, -3.0)), MAX_ROUNDING_ERROR);
  }
}
