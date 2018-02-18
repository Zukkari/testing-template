package task;

import java.time.LocalTime;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AlarmClock {

  private final ScheduledExecutorService scheduler;

  public AlarmClock(ScheduledExecutorService scheduler) {
    this.scheduler = scheduler;
  }

  /**
   * beep a little after the given duration
   */
  public void setTimerAlarm(int minutes, int seconds) {
    long secondsToAlarm = minutes * 60 + seconds;
    scheduler.schedule(
        () -> System.out.println("beeeep! beeeeeeep!"),
        secondsToAlarm, TimeUnit.SECONDS
    );
  }

  /**
   * schedules alarm today at given time.
   * does nothing if the time has already passed.
   */
  public void scheduleAlarm(LocalTime time) {
    LocalTime now = LocalTime.now();
    if (now.isAfter(time))
      return;

    int delaySeconds = time.toSecondOfDay() - now.toSecondOfDay();
    scheduler.schedule(
        () -> System.out.println("beep-beep! beep-beep! beep-beep!"),
        delaySeconds, TimeUnit.SECONDS
    );
  }

  /**
   * start ticking every 1s
   * @throws IllegalStateException when executor is shut down
   */
  public void startTicking() {
    if (scheduler.isShutdown())
      throw new IllegalStateException();

    scheduler.scheduleAtFixedRate(
        () -> System.out.println("tick"),
        0L, 1L, TimeUnit.SECONDS
    );
  }
}
