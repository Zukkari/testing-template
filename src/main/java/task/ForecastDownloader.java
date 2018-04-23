package task;

import java.io.InputStream;
import java.net.URL;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ForecastDownloader {

  private static final String FORECAST_LOCATION =
      "https://www.yr.no/place/Estonia/Tartumaa/Tartu/forecast.xml";

  public String getForecastXml() throws Exception {
    // download the latest forecast from yr.no weather service
    try (InputStream stream = new URL(FORECAST_LOCATION).openStream()) {
      return IOUtils.toString(stream, UTF_8);
    }
  }
}
