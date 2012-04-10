/**
 * Copyright (c) 2011 RedEngine Ltd, http://www.RedEngine.co.nz. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */
package net.stickycode.scheduled.aligned;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.stickycode.scheduled.Schedule;
import net.stickycode.scheduled.configuration.AbstractScheduleParser;
import net.stickycode.scheduled.configuration.UnsupportedUnitForSchedulingException;
import net.stickycode.stereotype.StickyPlugin;

@StickyPlugin
public class AlignedPeriodicScheduleParser
    extends AbstractScheduleParser {

  private Pattern alignedPeriodic = Pattern.compile("every ([0-9]+)? ?([a-zA-Z]+)(?: starting)? at ([0-9]+) ([a-zA-Z]+) past");

  @Override
  public boolean matches(String specification) {
    return alignedPeriodic.matcher(specification).matches();
  }

  @Override
  public Schedule parse(String specification) {
    Matcher match = alignedPeriodic.matcher(specification);
    if (!match.matches())
      throw new IllegalStateException("The schedule specification must match if matches was called"); 
      
    long period = parseNumber(match.group(1));
    TimeUnit periodUnit = parseTimeUnit(match.group(2));
    if (TimeUnit.NANOSECONDS.equals(periodUnit))
      throw new UnsupportedUnitForSchedulingException(periodUnit);

    TimeUnit derivedAlignmentUnit = deriveAlignment(periodUnit);
    TimeUnit alignmentUnit = parseTimeUnit(match.group(4));
    if (alignmentUnit.compareTo(periodUnit) > 0)
      throw new AlignmentCannotBeLessThanPeriodUnitsException(alignmentUnit, periodUnit);

    long alignment = derivedAlignmentUnit.convert(Long.valueOf(match.group(3)), alignmentUnit);

    return new AlignedPeriodicSchedule(alignment, derivedAlignmentUnit, period, periodUnit);
  }

  TimeUnit deriveAlignment(TimeUnit periodUnit) {
    return TimeUnit.values()[periodUnit.ordinal() - 1];
  }

}
