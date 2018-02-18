package sample;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class ListTest {
  
  @Test
  public void mockSize() {
    List<Object> list = mock(List.class);
    when(list.size()).thenReturn(0);

    int size = list.size();

    assertEquals(0, size);
  }

  @Test
  public void mockAdd() {
    // capture the argument value using the ArgumentCaptor
    List<Object> list = mock(List.class);
    ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
    when(list.add(captor.capture())).thenReturn(true);

    boolean added = list.add("the item");

    assertTrue(added);
    assertEquals("the item", captor.getValue()); // check the captured value
  }

  @Test
  public void useArgument() {
    List<Object> list = mock(List.class);

    doAnswer(invocation -> {
      int arg = invocation.getArgument(0); // list.contains(arg)
      return arg >= 0;
    }).when(list).contains(any(Integer.class));

    assertTrue(list.contains(1));
    assertFalse(list.contains(-1));
  }

  @Test
  public void countCalls() {
    List<Object> list = mock(List.class);

    list.add("some string");

    verify(list, atLeastOnce()).add(any(String.class));
    // atLeastOnce()
    // atMost(5)
    // verifyNoMoreInteractions(list)
  }
}
