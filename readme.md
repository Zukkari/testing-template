# Testing applications

Automated testing helps us ensure that the code is working correctly and performs reasonably well.
Every programmer knows that he/she always writes perfect code.
This causes confusion among novice programmers - why then does the perfect code need testing?

Rule #1 of testing - you don't write tests to find bugs in your code.
You write test so that **other people** can't later add bugs in your perfect code.

Regardless, sometimes you really do find bugs/deficiencies when writing tests.
Writing testable code is an art in itself and often improves the structure of the tested code.

There are many types/techniques/methods for testing.
It's important to know at least the following ideas:

* **Unit tests** are used to test a specific piece of code in isolation.
  Unit tests in Java usually test that the methods of a single class produce expected outputs.
  Such tests usually run very fast.
* **Integration tests** are used to test interaction between several pieces of code (classes).
  This could even mean starting up the entire application in a controlled environment.
  Such tests run slower than unit tests, because the test environment must be set up and cleaned up for each test.
  On the upside, integration tests are much better at finding bugs and can test more complicated scenarios.
* **Manual testing** is having a person click around in the application and verify that the functionality matches the specifications.
  This is the slowest and most expensive way of testing and should be reserved for the cases that are very hard to test in an automated manner.

Most projects run all unit and integration tests whenever a new commit is pushed into the code repository.
Also, the developers can run the tests during development to make sure there are no regressions (something is broken that used to work before).

In projects with maven project structure, the tests are usually placed in `src/test/java` and the test resources are placed in `src/test/resources`.
Running `mvn test` will run all tests immediately.
You can also start the tests from your IDE by right clicking on the test and selecting `Run`.

## JUnit 5

One popular testing framework in Java is JUnit.
JUnit provides a way to run setup/teardown code before/after each test.
It also includes countless methods for comparing the program's output to expected values.

Each class that contains JUnit tests should have a name that ends with `Test`.
It's common to see JUnit methods used with [*static imports*](https://docs.oracle.com/javase/8/docs/technotes/guides/language/static-import.html).
A test is a public no-args method annotated with `@Test`.
The method name is the test name - make it descriptive.
Most tests have the following structure:
1. prepare the object for testing (set up known initial state).
2. interact with the object, call the tested methods.
3. verify that the object is in the expected state.

**src/test/java/SampleTest.java**
```java
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SampleTest {

  @BeforeEach
  public void setUp() throws Exception {
    // JUnit runs this method before each test
  }
  
  @BeforeAll
  public static void beforeAll() {
      // JUnit runs this method before all tests
  }

  @AfterEach
  public void tearDown() throws Exception {
    // JUnit runs this method after each test
  }

  @AfterAll
  public static void teardownAll() {
      // JUnit runs this method after all tests
  }

  @Test
  public void stringBuilderCanAppendIntegers() {
    // tests don't have a main method!
    // right click on this test method and select Run

    // set up initial state
    StringBuilder sb = new StringBuilder();

    // call the tested methods
    sb.append(1);
    sb.append(23);

    // verify that object is in the expected state
    // assertEquals is Assert.assertEquals from the static import above
    assertEquals("123", sb.toString());
  }
}
```

Use autocomplete/javadoc to see all the useful methods provided by JUnit.
The `org.junit.Assert` class contains methods such as `assertTrue`, `assertEquals`, `assertNotEquals`, `assertNotNull`, `assertArrayEquals`, etc.

## Writing testable code

Unit testing means testing a single class in isolation.
What if the class we need to test is using other classes - how can we isolate it?
Different techniques are used depending on how the other classes are used.

### 1. Class creates instances of other classes

```java
class Untestable {

  String processDataFromDatabase() {
    String data = new DatabaseConnection().readData();
    /* complicated logic we want to test */
    return result;
  }
}
```

We want to test the logic for processing the data from the database, but avoid accessing an actual database (done in the `readData` method).
Setting up a database, cleaning it up etc is slow, complicated and out of scope for our test.

The solution is two part:
* move the `new DatabaseConnection()` call out from the tested class using a constructor parameter
* use a mock (object that simulates the real behavior in a controlled way) instead of the real DatabaseConnection when running the tests

```java
class Untestable {

  final DatabaseConnection connection;

  Untestable(DatabaseConnection connection) {
    this.connection = connection;
  }

  String processDataFromDatabase() {
    String data = connection.readData();
    /* complicated logic we want to test */
    return result;
  }
}
```

We can now use a mock database in the tests.

```java
class MockDatabaseConnection extends DatabaseConnection {
  @Override
  public String readData() {
    // override the readData method in DatabaseConnection, so
    // that it never connects to the actual database
    return "some fixed data expected by tests";
  }
}

class UntestableTest {
  @Test
  public void testLogic() {
    Untestable nowTestable = new Untestable(new MockDatabaseConnection());
    // interact
    // verify
  }
}
```

This incredibly powerful technique can make almost any class testable.
It's called **dependency injection** - the dependencies that the object requires are provided to it.

### 2. Class uses global state

Java has a lot of features that allow interaction with global state:
* using the current date/time
* using the file system or network connections
* using random number generators
* using mutable static fields

Global state can cause major headache when testing.
Using mutable static fields is especially evil and should be avoided whenever possible.

```java
class Untestable2 {

  List<LocalDate> holidaysThisMonth() {
    LocalDate now = LocalDate.now(); // global state
    /* clever holiday algorithm we want to test */
    return result;
  }
}
```

The strategy for handling global state is similar to the dependency injection solution.
The steps are:
* find the parts that deal with global state
* copy these parts to a new separate class, while keeping as much logic as possible in the original class
* update the original class to use the new class
* use dependency injection as above, mock the created class for tests

```java
class DateProvider {
  public LocalDate now() {
    return LocalDate.now();
  }
}

class Untestable2 {

  final DateProvider provider;

  Untestable2(DateProvider provider) {
    this.provider = provider;
  }

  List<LocalDate> holidaysThisMonth() {
    LocalDate now = provider.now();
    /* clever holiday algorithm we want to test */
    return result;
  }
}

class MockDateProvider extends DateProvider {
  @Override
  public LocalDate now() {
    // always return value expected by tests
    return LocalDate.parse("2018-02-12");
  }
}
```

The trick here is that extracting the global state while keeping most of the logic in place can be quite tricky.
Don't be afraid to make changes to existing classes to enable testing.
Even better would be to write the tests when writing the application code.
This way you are designing the code to be testable from the start.

### When to write unit tests?

When writing tests you should always ask yourself whether to use unit tests or integration tests.

Use unit tests when the code:
* has well defined inputs and outputs
* doesn't depend on many other components of the application
* has dependencies that are easy to mock
* main purpose isn't to interact with global state

Use integration tests when the code:
* mainly deals with global state, e.g. deals with the file system, networks, databases
* depends on many other components of the application, coordinates the program flow
* has dependencies that are not practical to mock

Integration tests are also critical for testing that the components actually work well together.

![only unit tests](https://pbs.twimg.com/media/C2oAur4UcAE-QaF.jpg "all tests passed")

## Mockito

Sometimes we need to mock classes/interfaces that have a large number of methods.
Creating a mock by extending/implementing this class can be frustrating:
* need to implement even the unused methods just for the mock implementation to compile
* mock contains stuff not relevant to the test which makes it difficult to read the test code
* mock needs to be updated when some irrelevant method is changed in the base class

In this cases it is possible to use the *Mockito* testing framework.
Mockito can dynamically generate mocks in a very concise way.
The test can only specify the methods that are actually used.

```java
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ListTest {
  @Test
  public void mockSize() {
    // java.util.List is an interface with more than 10 methods
    List<Object> list = mock(List.class);
    // when size() is called on the mock object, then it should return 0
    when(list.size()).thenReturn(0);
    assertEquals(0, list.size());
  }
}
```

Mockito is black magic in its purest form.
Only the mightiest of Java wizards can hope to fully understand how it works internally.
Thankfully the documentation is pretty good and the test code is usually almost self explanatory.
Using Mockito correctly can result in a very concise and readable test code.

## Task

Read the sample code in `src/main/java/sample`.
Read the matching sample tests in `src/test/java/sample`.
Then try to add tests for the classes in `src/main/java/task` as described below.

The sample tests should contain all relevant JUnit and Mockito methods needed to complete the task.

### WeatherParserTest

Create unit tests for the `WeatherParser` class.
You'll first need to change the `WeatherParser` class to make it testable.

Move the code that downloads the forecast xml into a separate class.
If it seems complicated, break it into smaller steps:
* find the code that downloads the forecast xml
* move that code into a new method in `WeatherParser`
* create a new class for downloading the forecast.
  move the downloading method into the new class.
* add a constructor parameter to the `WeatherParser` for providing the downloading class instance

The `WeatherParser` class should work just like originally after following the steps above.
The benefit is that the forecast downloading logic can now be modified by passing a different object to the constructor.

Create a mock forecast downloader for testing by overriding methods in the new forecast downloading class.
The mock shouldn't download anything from the actual *yr.no* weather service.
The `forecast.xml` in src/test/resources should be used instead (see the `readTestForecast` method in `WeatherParserTest`).

Don't use mockito for this task.

Create at least the following tests:
1. findsCorrectTemperatureFromForecast
2. throwsExceptionIfTemperatureNotFoundForGivenDate

### TodoAppTest

Create unit tests for the `TodoApp` class.
Make the class more easily testable by mocking the file system operations.
The `todo.bin` file should not be touched in the tests.
Instead, create a mock that can store the values in-memory (in a simple field).

Don't use mockito for this task.

Create at least the following tests:
1. itemIsInListAfterBeingAdded
2. itemNotInListAfterBeingRemoved
3. addingDuplicateItemDoesNotChangeStoredData

### TicketOfficeTest

Create unit tests for the `TicketOffice` class.
Use Mockito to mock the `TicketDatabase` interface.

Create at least the following tests:
1. throwsExceptionWhenNoSeatsAvailable
2. returnsAvailableSeatOnPurchase
3. callsReserveSeatWithCorrectArguments
