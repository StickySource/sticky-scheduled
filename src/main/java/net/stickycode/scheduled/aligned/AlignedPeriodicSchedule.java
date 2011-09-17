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

import net.stickycode.scheduled.PeriodicSchedule;

import org.joda.time.DateTime;

public class AlignedPeriodicSchedule
    extends PeriodicSchedule {

  private final long alignment;

  private final TimeUnit alignmentUnit;

  public AlignedPeriodicSchedule(long alignment, TimeUnit alignmentUnit, long period, TimeUnit periodUnit) {
    super(period, periodUnit);
    if (alignment < 1)
      throw new AlignmentOfLessThanOneIsMeaninglessException(alignment);

    if (alignmentUnit.compareTo(periodUnit) > 0)
      throw new AlignmentCannotBeLessThanPeriodUnitsException(alignmentUnit, periodUnit);

    if (alignment > alignmentUnit.convert(period, periodUnit))
      throw new AlignmentMustBeLessThanPeriodException(alignment, alignmentUnit, period, periodUnit);

    this.alignment = alignment;
    this.alignmentUnit = alignmentUnit;
  }

  @Override
  public long getPeriod() {
    return alignmentUnit.convert(super.getPeriod(), super.getUnits());
  }
  
  @Override
  public TimeUnit getUnits() {
    return alignmentUnit;
  }
  
  /**
   * The delay in seconds to wait before the initial execution to align the schedule as specified.
   * <b>An alignment of 0 means there is no delay</b>
   * <p>
   * e.g.
   * <ul>
   * <li>if the user configured a schedule as 'every hour at 15 minutes past'</li>
   * <li>and the service started at 10 minutes past</li>
   * <li>then the period would be 60 * 60 seconds</li>
   * <li>and the delay would be 5 * 60 seconds such that the first execution is 15 minutes past</li>
   * </ul>
   * </p>
   */
  @Override
  public long getInitialDelay() {
    if (alignment == 0)
      return 0;

    DateTime time = new DateTime();
    switch (alignmentUnit) {
    case HOURS:
      return calculateDelay(time.getHourOfDay(), alignment, 24);

    case MINUTES:
      return calculateDelay(time.getMinuteOfHour(), alignment, 60);

    case SECONDS:
      return calculateDelay(time.getSecondOfMinute(), alignment, 60);

    case MILLISECONDS:
      return calculateDelay(time.getMillisOfSecond(), alignment, 1000);

    default:
      throw new AlignmentNotSupportedException(alignmentUnit);
    }
  }

  private long calculateDelay(int current, long offset, long max) {
    if (current < offset)
      return offset - current;

    return max - current + offset;
  }

  @Override
  public String toString() {
    String units = getUnits().toString().toLowerCase();
    return String.format("period %d %s starting in %d %s",
        getPeriod(), units,
        getInitialDelay(), alignmentUnit.toString().toLowerCase());
  }
}
