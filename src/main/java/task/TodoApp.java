package task;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public class TodoApp {

  private final TodoStore store;

  public TodoApp(TodoStore store) {
    this.store = store;
  }

  public Set<String> getTodoList() throws IOException {
    return Collections.unmodifiableSet(fromBytes(store.readStoredValues()));
  }

  public void addTodoItem(String item) throws IOException {
    Set<String> todos = fromBytes(store.readStoredValues());
    if (todos.add(item)) {
      store.writeValuesToStore(toBytes(todos));
    }
  }

  public void removeTodoItem(String item) throws IOException {
    Set<String> todos = fromBytes(store.readStoredValues());
    if (todos.remove(item)) {
      store.writeValuesToStore(toBytes(todos));
    }
  }

  private byte[] toBytes(Set<String> todos) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    dos.writeInt(todos.size());
    for (String todo : todos) {
      dos.writeUTF(todo);
    }
    return baos.toByteArray();
  }

  private Set<String> fromBytes(byte[] bytes) throws IOException {
    Set<String> todos = new TreeSet<>();
    if (bytes != null) {
      DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
      int items = dis.readInt();
      for (int i = 0; i < items; i++) {
        todos.add(dis.readUTF());
      }
    }
    return todos;
  }
}
