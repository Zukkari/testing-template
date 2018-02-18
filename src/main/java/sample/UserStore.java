package sample;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.nio.charset.StandardCharsets.UTF_8;

public class UserStore {
  public String read() throws IOException {
    // usernames.txt doesn't exist and the tests don't need it
    return new String(Files.readAllBytes(Paths.get("usernames.txt")), UTF_8);
  }
}
