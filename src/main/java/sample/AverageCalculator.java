package sample;

import java.util.List;

public class AverageCalculator {

  double averageOf(List<Double> values) {
    if (values == null || values.isEmpty())
      throw new IllegalArgumentException("no values provided");

    double sum = 0;
    for (double value : values)
      sum += value;
    return sum / values.size();
  }
}
