package task;

import org.junit.Test;

import java.util.concurrent.Callable;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class AlarmClockTest {

  @Test
  public void mockitoIsAvailable() throws Exception {
    Callable<Boolean> c = mock(Callable.class);
    when(c.call()).thenReturn(true);
    assertTrue(c.call());
  }

  // Many ScheduledExecutorService methods return a ScheduledFuture<T>.
  // Your mock can return a mock ScheduledFuture or null:
  // A) when(executor.something()).thenReturn(mock(ScheduledFuture.class));
  // B) when(executor.something()).thenReturn(null);

  // TODO add your tests here
}
