package net.stickycode.scheduled.configuration;

import net.stickycode.scheduled.Schedule;
import net.stickycode.scheduled.ScheduleParser;
import net.stickycode.stereotype.StickyPlugin;

@StickyPlugin
public class DisabledScheduleParser
    implements ScheduleParser {

  @Override
  public boolean matches(String specification) {
    if ("off".equalsIgnoreCase(specification))
      return true;

    if ("disabled".equalsIgnoreCase(specification))
      return true;

    if ("never".equalsIgnoreCase(specification))
      return true;

    return false;
  }

  @Override
  public Schedule parse(String specification) {
    return new DisabledSchedule();
  }

}
