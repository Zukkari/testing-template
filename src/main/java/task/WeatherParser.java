package task;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;

import java.time.LocalDateTime;
import java.util.List;

public class WeatherParser {

  private final ForecastDownloader downloader;

  public WeatherParser(ForecastDownloader downloader) {
    this.downloader = downloader;
  }

  public int temperatureAt(LocalDateTime time) throws Exception {
    String forecastXml = downloader.getForecastXml();
    // try to find the requested temperature from the downloaded forecast
    // look at the sample xml at src/test/resources/forecast.xml to understand selectNodes()
    Document document = DocumentHelper.parseText(forecastXml);
    List<Node> timeElements = document.selectNodes("//weatherdata/forecast/tabular/time");
    for (Node timeElement : timeElements) {
      LocalDateTime from = LocalDateTime.parse(timeElement.selectSingleNode("@from").getText());
      LocalDateTime to = LocalDateTime.parse(timeElement.selectSingleNode("@to").getText());
      if (time.compareTo(from) >= 0 && time.compareTo(to) < 0) {
        return Integer.parseInt(timeElement.selectSingleNode("temperature/@value").getText());
      }
    }
    throw new IllegalStateException("temperature not found");
  }
}
