package sample;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserFinderTest {

  private static final String SAMPLE_NORMAL = "" +
      "mbakhoff Märt Bakhoff\n" +
      "ssaan Simmo Saan\n" +
      "mlepp Marina Lepp\n";

  private static final String SAMPLE_STRANGE = "" +
      "potato\n" +
      "\n" +
      "           \n" +
      "mbakhoff Märt Bakhoff";

  @Test
  public void findsSimmo() throws Exception {
    UserFinder finder = new UserFinder(new TestUserStore(SAMPLE_NORMAL));
    String name = finder.findFullNameByUsername("ssaan");
    assertEquals("Simmo Saan", name);
  }

  @Test
  public void failsOnUnknownUser() {
    assertThrows(NoSuchElementException.class, () -> {
      UserFinder finder = new UserFinder(new TestUserStore(SAMPLE_NORMAL));
      finder.findFullNameByUsername("ivaat");
    });
  }

  @Test
  public void weirdLinesIgnored() throws Exception {
    UserFinder finder = new UserFinder(new TestUserStore(SAMPLE_STRANGE));
    String name = finder.findFullNameByUsername("mbakhoff");
    assertEquals("Märt Bakhoff", name);
  }

  // overrides UserStore not to use the file system.
  // much easier to test with, because there's no need
  // to create files and clean them up after the tests.
  static class TestUserStore extends UserStore {

    private final String data;

    TestUserStore(String data) {
      this.data = data;
    }

    @Override
    public String read() {
      return data;
    }
  }
}
