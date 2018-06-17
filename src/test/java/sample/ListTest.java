package sample;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class ListTest {
  
  @Test
  public void mockReturnValuesOfMethods() {
    // create a mock object that implements List
    List<Object> list = mock(List.class);
    // program the mock object:
    // when size() is called on the mock, then the mock should return 0
    when(list.size()).thenReturn(0);

    // actually call size() on the mock
    int size = list.size();

    // check that the mock returns the value it should
    assertEquals(0, size);
  }

  @Test
  public void verifyThatSomeMethodIsCalledOnTheMock() {
    List<String> list = mock(List.class);

    list.add("some string");

    // check that list.add() was called at least once
    verify(list, atLeastOnce()).add(any(String.class));

    // or check that the concrete value was added
    verify(list, atLeastOnce()).add("some string");

    // or check that add was called exactly once
    verify(list, times(1)).add(anyString());
  }
}
