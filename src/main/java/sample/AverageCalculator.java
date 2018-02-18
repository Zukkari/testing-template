package sample;

public class AverageCalculator {

  double averageOf(double... values) {
    if (values == null || values.length == 0)
      throw new IllegalArgumentException("no values provided");

    double sum = 0;
    for (double value : values)
      sum += value;
    return sum / values.length;
  }
}
