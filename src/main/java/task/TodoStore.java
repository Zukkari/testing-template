package task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TodoStore {

  public byte[] readStoredValues() throws IOException {
    Path path = Paths.get("todo.bin");
    if (!Files.isRegularFile(path))
      return null;
    return Files.readAllBytes(path);
  }

  public void writeValuesToStore(byte[] values) throws IOException {
    Files.write(Paths.get("todo.bin"), values);
  }
}
