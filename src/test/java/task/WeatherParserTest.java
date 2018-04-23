package task;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class WeatherParserTest {

  // you can hardcode dates to test against:
  // LocalDateTime ldt = LocalDateTime.parse("2018-02-12T00:00:00");

  @Test
  public void testDataMustBeAvailable() throws Exception {
    assertNotNull(readTestForecast());
  }

  private static String readTestForecast() throws IOException {
    // reads src/test/resources/forecast.xml to string
    try (InputStream is = WeatherParserTest.class.getResourceAsStream("/forecast.xml")) {
      return IOUtils.toString(is, UTF_8);
    }
  }

  @Test
  public void findsCorrectTemperatureFromForecast() throws Exception {
    WeatherParser parser = new WeatherParser(new MockForecastDownloader());
    LocalDateTime ldt = LocalDateTime.parse("2018-02-12T00:00:01");
    assertEquals(-4, parser.temperatureAt(ldt));
  }

  @Test(expected = IllegalStateException.class)
  public void throwsExceptionIfTemperatureNotFoundForGivenDate() throws Exception {
    WeatherParser parser = new WeatherParser(new MockForecastDownloader());
    LocalDateTime ldt = LocalDateTime.parse("2016-02-12T00:00:01");
    parser.temperatureAt(ldt);
  }

  static class MockForecastDownloader extends ForecastDownloader {
    @Override
    public String getForecastXml() throws Exception {
      return readTestForecast();
    }
  }
}
