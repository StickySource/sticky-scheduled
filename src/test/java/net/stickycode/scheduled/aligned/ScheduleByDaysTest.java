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

import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.HOURS;
import static org.fest.assertions.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import net.stickycode.scheduled.aligned.AlignedPeriodicSchedule;
import net.stickycode.scheduled.aligned.AlignmentMustBeLessThanPeriodException;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ScheduleByDaysTest {

  @Before
  public void freezeTime() {
    DateTime now = new DateTime();
    DateTime midnight = now.minusMillis(now.getMillisOfDay());
    DateTimeUtils.setCurrentMillisFixed(midnight.plusHours(5).getMillis());
  }

  @After
  public void unfreezeTime() {
    DateTimeUtils.setCurrentMillisSystem();
  }

  @Test(expected=AlignmentMustBeLessThanPeriodException.class)
  public void alignedGreaterThanMinute() {
    delay(49, HOURS, 1, DAYS);
  }
  
  @Test(expected=AlignmentMustBeLessThanPeriodException.class)
  public void makeSureThatUnitConversionDoesLoseAccuracy() {
    // 'upcasting' time truncates, so 70 seconds as a minute == 1 minute
    // this test would not except if 
    delay(25, HOURS, 1, DAYS);
  }
  
  @Test
  public void offsetIsGreaterThanCurrentTime() {
    // 5 hours past
    // delay is 6 hours
    // next run is at 6 HOURS past in 1 hour
    assertThat(delay(6, HOURS, 1, DAYS)).isEqualTo(1);
  }
  
  @Test
  public void offsetIsSameUnitAsPeriod() {
    // 5 hours past
    // delay is 48 hours
    // next run is 48 - 5
    assertThat(delay(2 * 24, HOURS, 3, DAYS)).isEqualTo(43);
  }
  
  @Test
  public void offsetIsLessThanCurrentTime() {
    // 5 hours past
    // delay is 2 hours
    // next run is 19 + 2 == 21
    assertThat(delay(2, HOURS, 1,DAYS)).isEqualTo(21);
  }
/*  
 for fixed time scheduling
  @Test
  public void fixedTimeStartAfterNow() {
    // 5 hours past
    // start at 6:30 
    // next run is 60 * 5 + 30 minutes 
    // which is 90 minutes from now
    assertThat(delay(6 * 60 + 30, MINUTES, 1,DAYS)).isEqualTo(90);
  }
  
  @Test
  public void fixedTimeStartBeforeNow() {
    // 5 hours past
    // start at 01:30 
    // next run is 60 + 30 minutes 
    // which is 90 minutes from midnight (+ 60 * 17)
    assertThat(delay(60 + 30, MINUTES, 1, DAYS)).isEqualTo(19 * 60 + 90);
  }
  */
  private long delay(int alignment, TimeUnit minutes, int period, TimeUnit hours) {
    return new AlignedPeriodicSchedule(alignment, minutes, period, hours).getInitialDelay();
  }
}
