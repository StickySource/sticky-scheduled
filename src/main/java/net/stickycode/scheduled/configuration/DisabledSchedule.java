package net.stickycode.scheduled.configuration;

import java.util.concurrent.TimeUnit;

import net.stickycode.scheduled.Schedule;


public class DisabledSchedule
    implements Schedule {

  @Override
  public long getInitialDelay() {
    throw new UnsupportedOperationException("Disabled schedule cannot be run");
  }

  @Override
  public long getPeriod() {
    throw new UnsupportedOperationException("Disabled schedule cannot be run");
  }

  @Override
  public TimeUnit getUnits() {
    throw new UnsupportedOperationException("Disabled schedule cannot be run");
  }

  @Override
  public boolean isEnabled() {
    return false;
  }

}
