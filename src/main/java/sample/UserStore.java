package sample;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class UserStore {
  public String read() throws IOException {
    // usernames.txt doesn't exist and the tests don't need it
    return Files.readString(Path.of("usernames.txt"));
  }
}
