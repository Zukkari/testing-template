package task;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TicketOfficeTest {

  // TODO add your tests here

  /* test template
  @Test
  public void testSomething() {
    TicketDatabase db = mock(TicketDatabase.class);
    TicketOffice office = new TicketOffice(db);

    // prepare the mock (when+thenReturn)
    // call office.buyTicket
    // assert/verify conditions
  }
  */

  @Test(expected = IllegalStateException.class)
  public void throwsExceptionWhenNoSeatsAvailable() {
    TicketDatabase db = mock(TicketDatabase.class);
    TicketOffice office = new TicketOffice(db);

    when(db.getFreeSeats(anyString())).thenReturn(Collections.emptyList());
    office.buyTicket("any");
  }

  @Test
  public void returnsAvailableSeatOnPurchase() {
    TicketDatabase db = mock(TicketDatabase.class);
    TicketOffice office = new TicketOffice(db);

    List<Integer> available = Arrays.asList(3, 6, 9);
    when(db.getFreeSeats(anyString())).thenReturn(available);
    int seat = office.buyTicket("show");
    assertTrue(available.contains(seat));
  }

  @Test
  public void callsReserveSeatWithCorrectArguments() {
    TicketDatabase db = mock(TicketDatabase.class);
    TicketOffice office = new TicketOffice(db);

    when(db.getFreeSeats(anyString())).thenReturn(singletonList(42));
    office.buyTicket("show");

    verify(db, times(1)).reserveSeat("show", 42);
  }
}
