package task;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TodoAppTest {

  @Test
  public void itemIsInListAfterBeingAdded() throws IOException {
    TodoApp app = new TodoApp(new MockTodoStore());

    assertEquals(0, app.getTodoList().size());
    app.addTodoItem("add tests");
    assertEquals(1, app.getTodoList().size());
    assertTrue(app.getTodoList().contains("add tests"));
  }

  @Test
  public void itemNotInListAfterBeingRemoved() throws IOException {
    TodoApp app = new TodoApp(new MockTodoStore());

    app.addTodoItem("run tests");
    assertEquals(1, app.getTodoList().size());
    assertTrue(app.getTodoList().contains("run tests"));
    app.removeTodoItem("run tests");
    assertEquals(0, app.getTodoList().size());
  }

  @Test
  public void addingDuplicateItemDoesNotChangeStoredData() throws IOException {
    MockTodoStore store = new MockTodoStore();
    TodoApp app = new TodoApp(store);

    assertEquals(0, store.writes);
    app.addTodoItem("fix tests");
    assertEquals(1, store.writes);
    app.addTodoItem("fix tests");
    assertEquals(1, store.writes);
  }

  static class MockTodoStore extends TodoStore {

    byte[] storedValue;
    int writes = 0;

    @Override
    public byte[] readStoredValues() {
      return storedValue;
    }

    @Override
    public void writeValuesToStore(byte[] values) {
      this.storedValue = values;
      writes++;
    }
  }
}
