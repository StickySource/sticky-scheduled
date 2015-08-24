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
import static org.assertj.core.api.StrictAssertions.assertThat;

import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import mockit.Expectations;

public class ScheduleByHourTest {

  public void freezeTimeAtMidnightPlus5minutes() {
    LocalTime now = LocalTime.of(0, 5, 0);
    new Expectations(LocalTime.class) {
      {
        LocalTime.now(); result = now;
      }
    };
  }

  @Test(expected=AlignmentMustBeLessThanPeriodException.class)
  public void alignedGreaterThanHour() {
    delay(120, MINUTES, 1, TimeUnit.HOURS);
  }

  @Test(expected=AlignmentMustBeLessThanPeriodException.class)
  public void makeSureThatUnitConversionDoesLoseAccuracy() {
    // 'upcasting' time truncates, so 70 minutes as aory).register(any(ConfigurationAttribute.class));
//  verify(schedulingSystem).schory).register(any(ConfigurationAttribute.class));
//  verify(schedulingSystem).sch hour == 1 hour
    delay(70, MINUTES, 1, TimeUnit.HOURS);
  }

  @Test
  public void offsetIsGreaterThanCurrentTime() {
    freezeTimeAtMidnightPlus5minutes();
    assertThat(delay(30, MINUTES, 1, TimeUnit.HOURS)).isEqualTo(25);
  }

  @Test
  public void offsetIsLessThanPeriod() {
    freezeTimeAtMidnightPlus5minutes();
    assertThat(delay(3 * 60, MINUTES, 10, TimeUnit.HOURS)).isEqualTo(175);
  }

  @Test
  public void alignedToHour() {
    freezeTimeAtMidnightPlus5minutes();
    assertThat(delay(10 * 60, MINUTES, 15, TimeUnit.HOURS)).isEqualTo(10 * 60 - 5);
  }

  private long delay(int alignment, TimeUnit minutes, int period, TimeUnit hours) {
    return new AlignedPeriodicSchedule(alignment, minutes, period, hours).getInitialDelay();
  }
}
