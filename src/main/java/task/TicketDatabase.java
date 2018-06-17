package task;

import java.util.List;

public interface TicketDatabase {

  List<Integer> getFreeSeats(String show);

  void reserveSeat(String show, int seat);
}
