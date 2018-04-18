package task;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class IOUtils {

  public static String toString(InputStream stream, Charset charset) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte[] buf = new byte[2048];
    int len;
    while ((len = stream.read(buf)) != -1) {
      baos.write(buf, 0, len);
    }
    byte[] bytes = baos.toByteArray();
    return new String(bytes, charset);
  }
}
