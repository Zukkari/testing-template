package task;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertNotNull;

public class WeatherParserTest {

  // you can hardcode dates to test against:
  // LocalDateTime ldt = LocalDateTime.parse("2018-02-12T00:00:00");

  @Test
  public void testDataMustBeAvailable() throws Exception {
    assertNotNull(readTestForecast());
  }

  // TODO add your tests here

  private static String readTestForecast() throws IOException {
    // reads src/test/resources/forecast.xml to string
    try (InputStream is = WeatherParserTest.class.getResourceAsStream("/forecast.xml")) {
      return IOUtils.toString(is, UTF_8);
    }
  }
}
