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
import static java.util.concurrent.TimeUnit.SECONDS;
import static net.stickycode.fest.ScheduleAssert.assertThat;
import static org.fest.assertions.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import net.stickycode.scheduled.PeriodicSchedule;
import net.stickycode.scheduled.Schedule;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ScheduleByMinutesTest {

  @Before
  public void freezeTime() {
    DateTime now = new DateTime();
    DateTime midnight = now.minusMillis(now.getMillisOfDay());
    DateTimeUtils.setCurrentMillisFixed(midnight.plusSeconds(5).getMillis());
  }

  @After
  public void unfreezeTime() {
    DateTimeUtils.setCurrentMillisSystem();
  }

  @Test(expected = AlignmentMustBeLessThanPeriodException.class)
  public void alignedGreaterThanMinute() {
    delay(120, SECONDS, 1, MINUTES);
  }

  @Test(expected = AlignmentMustBeLessThanPeriodException.class)
  public void makeSureThatUnitConversionDoesLoseAccuracy() {
    // 'upcasting' time truncates, so 70 seconds as a minute == 1 minute
    delay(70, SECONDS, 1, MINUTES);
  }

  @Test
  public void offsetIsGreaterThanCurrentTime() {
    // 5 seconds past
    // delay is 30 seconds
    // next run is 25 seconds
    assertThat(delay(30, SECONDS, 1, MINUTES)).hasPeriod(60).seconds().startingAfter(25);
  }

  @Test
  public void offsetIsSameUnitAsPeriod() {
    assertThat(delay(3 * 60, SECONDS, 10, MINUTES)).hasPeriod(10 * 60).seconds().startingAfter(175);
  }

  @Test
  public void offsetIsLessThanCurrentTime() {
    // 5 seconds past
    // delay is 2 seconds
    // next run is 55 + 2 == 57
    assertThat(delay(2, SECONDS, 1, MINUTES)).hasPeriod(60).seconds().startingAfter(57);
  }
  
  @Test 
  public void stringify() {
    assertThat(new AlignedPeriodicSchedule(10, MINUTES, 1, TimeUnit.HOURS).toString()).isEqualTo("period 60 minutes starting in 10 minutes");
    assertThat(new PeriodicSchedule(1, TimeUnit.HOURS).toString()).isEqualTo("period 1 hours");
  }

  private Schedule delay(long alignment, TimeUnit minutes, long period, TimeUnit hours) {
    return new AlignedPeriodicSchedule(alignment, minutes, period, hours);
  }
}
