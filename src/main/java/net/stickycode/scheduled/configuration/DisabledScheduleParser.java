package net.stickycode.scheduled.configuration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.stickycode.scheduled.Schedule;
import net.stickycode.stereotype.StickyPlugin;

@StickyPlugin
public class DisabledScheduleParser
    extends ScheduleParser {

  private Pattern disabled = Pattern.compile("^off|disabled|never$", Pattern.CASE_INSENSITIVE);

  @Override
  protected Pattern getPattern() {
    return disabled;
  }

  @Override
  Schedule parse(Matcher matched) {
    return new DisabledSchedule();
  }

}
