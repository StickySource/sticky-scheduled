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
package net.stickycode.scheduled.configuration;

import static org.fest.assertions.Assertions.assertThat;
import net.stickycode.coercion.CoercionType;
import net.stickycode.scheduled.PeriodicSchedule;
import net.stickycode.scheduled.Schedule;
import net.stickycode.scheduled.ScheduleAssert;
import net.stickycode.scheduled.ScheduleConfiguration;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ScheduleConfigurationCoercionTest {

  ScheduleConfigurationCoercion coercion = new ScheduleConfigurationCoercion();

  @Test
  public void applicability() {
    assertThat(coercion.isApplicableTo(new CoercionType(Integer.class))).isFalse();
    assertThat(coercion.isApplicableTo(new CoercionType(ScheduleConfiguration.class))).isTrue();
    assertThat(coercion.isApplicableTo(new CoercionType(PeriodicSchedule.class))).isTrue();
  }

  @Test(expected = ScheduleMustBeDefinedButTheValueWasBlankException.class)
  public void blank() {
    coerce("");
  }

  ScheduleAssert assertThatSchedule(String value) {
    return new ScheduleAssert(coerce(value));
  }

  private Schedule coerce(String value) {
    return coercion.coerce(target(), value);
  }

  @Test
  public void daily() {
    assertThatSchedule("every day at 3 hours past").hasPeriod(1).days();
  }

  @Ignore("Not done yet")
  @Test
  public void specificDay() {
    assertThatSchedule("every tuesday at 00:15").hasPeriod(7).days();
    assertThatSchedule("every wednesday at 3 hours past midnight").hasPeriod(7).days();
    assertThatSchedule("every sunday at 3 hours past").hasPeriod(7).days();
  }

  @Test
  public void hourly() {
    assertThatSchedule("every hour at 15 minutes past").hasPeriod(1).hours();
    assertThatSchedule("every 3 hours starting at 1 hours past").hasPeriod(3).hours();
    assertThatSchedule("every 3 hours starting at 2 hours past").hasPeriod(3).hours();
    assertThatSchedule("every 3 hours starting at 30 minutes past").hasPeriod(3).hours();
  }

  @Test
  public void minutely() {
    assertThatSchedule("every 15 minutes starting at 5 minutes past").hasPeriod(15).minutes();
    assertThatSchedule("every 15 minutes starting at 5 seconds past").hasPeriod(15).minutes();
  }

  private ScheduleConfiguration target() {
    return new ScheduleConfiguration("bob");
  }
}
