package task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Scanner;

public class CachedWeatherParser {

  private final WeatherParser parser;

  public CachedWeatherParser(WeatherParser parser) {
    this.parser = parser;
  }

  public int temperatureAt(LocalDateTime time) throws Exception {
    Integer storedValue = findStoredValueForTime(time);
    if (storedValue != null)
      return storedValue;

    // only tries to load the temperature from the parser when
    // no stored value from an earlier request was found
    int value = parser.temperatureAt(time);

    storeValueForTime(time, value);
    return value;
  }

  private Integer findStoredValueForTime(LocalDateTime time) throws IOException {
    String values = readStoredValues();
    Scanner scanner = new Scanner(values);
    while (scanner.hasNext()) {
      String storedTime = scanner.next();
      int storedTemperature = scanner.nextInt();
      if (time.toString().equals(storedTime)) {
        return storedTemperature;
      }
    }
    return null;
  }

  private void storeValueForTime(LocalDateTime time, int result) throws IOException {
    String values = readStoredValues();
    values += time.toString() + " " + result + "\n";
    writeValuesToStore(values);
  }

  private String readStoredValues() throws IOException {
    Path path = Paths.get("weather-store.txt");
    if (!Files.isRegularFile(path))
      return "";
    return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
  }

  private void writeValuesToStore(String values) throws IOException {
    Files.write(Paths.get("weather-store.txt"), values.getBytes(StandardCharsets.UTF_8));
  }
}
