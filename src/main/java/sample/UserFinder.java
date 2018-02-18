package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.NoSuchElementException;

public class UserFinder {

  private final UserStore store;

  public UserFinder(UserStore store) {
    this.store = store;
  }

  public String findFullNameByUsername(String userToFind) throws IOException {
    String data = store.read();
    BufferedReader reader = new BufferedReader(new StringReader(data));
    String line;
    while ((line = reader.readLine()) != null) {
      int separator = line.indexOf(' ');
      if (separator == -1)
        continue; // ignore strange lines
      String user = line.substring(0, separator);
      String fullName = line.substring(separator + 1);
      if (user.equals(userToFind))
        return fullName;
    }
    throw new NoSuchElementException(userToFind);
  }
}
