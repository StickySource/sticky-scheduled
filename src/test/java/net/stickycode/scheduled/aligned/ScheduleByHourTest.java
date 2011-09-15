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

import static java.util.concurrent.TimeUnit.MINUTES;
import static org.fest.assertions.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import net.stickycode.scheduled.aligned.AlignedPeriodicSchedule;
import net.stickycode.scheduled.aligned.AlignmentMustBeLessThanPeriodException;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ScheduleByHourTest {

  @Before
  public void freezeTime() {
    DateTime now = new DateTime();
    DateTime midnight = now.minusMillis(now.getMillisOfDay());
    DateTimeUtils.setCurrentMillisFixed(midnight.plusMinutes(5).getMillis());
  }

  @After
  public void unfreezeTime() {
    DateTimeUtils.setCurrentMillisSystem();
  }

  @Test(expected=AlignmentMustBeLessThanPeriodException.class)
  public void alignedGreaterThanHour() {
    delay(120, MINUTES, 1, TimeUnit.HOURS);
  }
  
  @Test(expected=AlignmentMustBeLessThanPeriodException.class)
  public void makeSureThatUnitConversionDoesLoseAccuracy() {
    // 'upcasting' time truncates, so 70 minutes as a hour == 1 hour
    delay(70, MINUTES, 1, TimeUnit.HOURS);
  }
  
  @Test
  public void offsetIsGreaterThanCurrentTime() {
    assertThat(delay(30, MINUTES, 1, TimeUnit.HOURS)).isEqualTo(25);
  }
  
  @Test
  public void offsetIsLessThanPeriod() {
    assertThat(delay(3 * 60, MINUTES, 10, TimeUnit.HOURS)).isEqualTo(175);
  }
  
  @Test
  public void alignedToHour() {
    assertThat(delay(10 * 60, MINUTES, 15, TimeUnit.HOURS)).isEqualTo(10 * 60 - 5);
  }
  
  private long delay(int alignment, TimeUnit minutes, int period, TimeUnit hours) {
    return new AlignedPeriodicSchedule(alignment, minutes, period, hours).getInitialDelay();
  }
}
