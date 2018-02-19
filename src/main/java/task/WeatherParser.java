package task;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;

public class WeatherParser {

  private static final String FORECAST_LOCATION =
      "https://www.yr.no/place/Estonia/Tartumaa/Tartu/forecast.xml";

  public int temperatureAt(LocalDateTime time) throws Exception {
    // caching - storing the results of some expensive calculation so
    // that it can be reused later. the WeatherParser stores all already
    // requested temperatures into a file. if the file already contains the
    // temperature, then downloading the latest forecast is skipped.

    Integer cached = findCachedTemperature(time);
    if (cached != null)
      return cached;

    // download the latest forecast from yr.no weather service
    String forecastXml;
    try (InputStream stream = new URL(FORECAST_LOCATION).openStream()) {
      forecastXml = IOUtils.toString(stream, UTF_8);
    }

    // try to find the requested temperature from the downloaded forecast
    Integer result = findTemperatureFromXml(time, forecastXml);

    // write the result to cache so we can reuse it
    writeResultToCache(time, result);

    return result;
  }

  private Integer findTemperatureFromXml(LocalDateTime time, String xml) throws Exception {
    // uses the xml parser built into java so we can skip a bunch of string processing
    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    Document parsedXml = builder.parse(new InputSource(new StringReader(xml)));
    NodeList timeElementList = parsedXml.getElementsByTagName("time");
    for (int i = 0; i < timeElementList.getLength(); i++) {
      Node timeElement = timeElementList.item(i);
      // example: <time from="2018-02-11T22:00:00" to="2018-02-12T00:00:00" period="3">
      LocalDateTime from = LocalDateTime.parse(getAttributeValue(timeElement, "from"));
      LocalDateTime to = LocalDateTime.parse(getAttributeValue(timeElement, "to"));
      if (time.compareTo(from) >= 0 && time.compareTo(to) < 0) {
        NodeList childNodes = timeElement.getChildNodes();
        for (int j = 0; j < childNodes.getLength(); j++) {
          Node child = childNodes.item(j);
          if ("temperature".equals(child.getNodeName())) {
            // example: <temperature unit="celsius" value="-3" />
            return Integer.parseInt(getAttributeValue(child, "value"));
          }
        }
      }
    }
    throw new IllegalStateException("temperature not found");
  }

  private String getAttributeValue(Node element, String attributeName) {
    return element.getAttributes().getNamedItem(attributeName).getTextContent();
  }

  private Integer findCachedTemperature(LocalDateTime time) throws IOException {
    String requestedTime = time.toString();

    Path cacheFile = Paths.get("cache.txt");
    String cache = Files.isRegularFile(cacheFile)
        ? new String(Files.readAllBytes(cacheFile), UTF_8)
        : "";

    // file format: datetime temperature datetime temperature datetime temperature ...
    Scanner scanner = new Scanner(cache);
    while (scanner.hasNext()) {
      String cachedTime = scanner.next();
      int cachedTemperature = scanner.nextInt();
      if (cachedTime.equals(requestedTime))
        return cachedTemperature;
    }
    return null;
  }

  private void writeResultToCache(LocalDateTime time, int temperature) throws IOException {
    Path cacheFile = Paths.get("cache.txt");
    String cache = Files.isRegularFile(cacheFile)
        ? new String(Files.readAllBytes(cacheFile), UTF_8)
        : "";
    cache += " " + time.toString() + " " + temperature;
    Files.write(cacheFile, cache.getBytes(UTF_8));
  }
}
