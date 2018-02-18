package task;

import org.apache.commons.io.IOUtils;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class WeatherParser {

  private static final String FORECAST_LOCATION =
      "https://www.yr.no/place/Estonia/Tartumaa/Tartu/forecast.xml";

  public int temperatureAt(LocalDateTime time) throws Exception {
    Integer cached = findCachedTemperature(time);
    if (cached != null)
      return cached;

    TemperatureFindingHandler handler = new TemperatureFindingHandler(time);

    // SAXParser walks through the xml and calls TemperatureFindingHandler#startElement for
    // every opening tag in the xml, e.g. for <time>, but not for </time>
    SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
    String xml = IOUtils.toString(new URL(FORECAST_LOCATION), UTF_8);
    parser.parse(new InputSource(new StringReader(xml)), handler);

    if (handler.temperature == null)
      throw new IllegalStateException("not found");

    writeResultToCache(time, handler.temperature);

    return handler.temperature;
  }

  private Integer findCachedTemperature(LocalDateTime time) throws IOException {
    String timeAsString = time.toString();

    List<String> lines = Files.readAllLines(Paths.get("cache.txt"));
    for (String line : lines) {
      String[] parts = line.split(" ");
      if (parts.length == 2 && parts[0].equals(timeAsString))
        return Integer.parseInt(parts[1]);
    }
    return null;
  }

  private void writeResultToCache(LocalDateTime time, int temperature) throws IOException {
    Files.write(
        Paths.get("cache.txt"),
        Collections.singleton(
            time.toString() + " " + temperature
        ),
        StandardOpenOption.APPEND
    );
  }

  private static class TemperatureFindingHandler extends DefaultHandler {

    final LocalDateTime target;
    boolean inMatchingForecast = false;
    Integer temperature;

    TemperatureFindingHandler(LocalDateTime target) {
      this.target = target;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
      if ("time".equals(qName)) {
        // <time from="2018-02-11T22:00:00" to="2018-02-12T00:00:00" period="3">
        LocalDateTime from = LocalDateTime.parse(attributes.getValue("from"));
        LocalDateTime to = LocalDateTime.parse(attributes.getValue("to"));
        inMatchingForecast = target.compareTo(from) >= 0 && target.compareTo(to) < 0;
      }
      if ("temperature".equals(qName) && inMatchingForecast) {
        // <temperature unit="celsius" value="-3" />
        temperature = Integer.parseInt(attributes.getValue("value"));
      }
    }
  }
}
