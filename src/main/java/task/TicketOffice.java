package task;

import java.util.List;

public class TicketOffice {

  private final TicketDatabase db;

  public TicketOffice(TicketDatabase db) {
    this.db = db;
  }

  public int buyTicket(String show) {
    List<Integer> freeSeats = db.getFreeSeats(show);
    if (freeSeats.isEmpty())
      throw new IllegalStateException("no seats available");
    int seat = freeSeats.get(0);
    db.reserveSeat(show, seat);
    return seat;
  }
}
