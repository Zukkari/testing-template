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
  public void captureArgumentsPassedToMockMethods() {
    List<String> list = mock(List.class);
    // program the mock object:
    // when add() is called on the mock, then store the argument of the method
    // call in the captor object. the add method should return true
    ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
    when(list.add(captor.capture())).thenReturn(true);

    // actually call add() on the mock. the string "the item" is saved in the captor
    boolean added = list.add("the item");

    // check if the captor managed to save the correct argument value
    assertTrue(added);
    assertEquals("the item", captor.getValue());
  }

  @Test
  public void calculateReturnValueOfMockMethod() {
    List<Integer> list = mock(List.class);

    // program the mock:
    // when list.contains() is called and the argument is of type Integer,
    // then calculate the value returned by the contains method as follows:
    // 1) inspect the Integer argument that was passed to contains()
    // 2) have the mock return true if the argument value is non-negative
    doAnswer(invocation -> {
      // invocation = the method call
      Integer arg = invocation.getArgument(0);
      return arg >= 0;
    }).when(list).contains(any(Integer.class));

    // check that the return value is calculated correctly
    assertTrue(list.contains(1));
    assertFalse(list.contains(-1));
  }

  @Test
  public void verifyThatSomeMethodIsCalledOnTheMock() {
    List<String> list = mock(List.class);

    list.add("some string");

    // check that list.add() was called at least once
    verify(list, atLeastOnce()).add(any(String.class));

    // other options:
    // verify(list, atLeast(n)).. - method called at least n times
    // verify(list, atMost(n)).. - method called at most n times
    // verifyNoMoreInteractions(list) - method has not been called
  }
}
