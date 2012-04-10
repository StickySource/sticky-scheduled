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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.stickycode.coercion.CoercionTarget;
import net.stickycode.coercion.target.CoercionTargets;
import net.stickycode.fest.ScheduleAssert;
import net.stickycode.scheduled.PeriodicSchedule;
import net.stickycode.scheduled.Schedule;
import net.stickycode.scheduled.ScheduleParser;
import net.stickycode.scheduled.aligned.AlignedPeriodicScheduleParser;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ScheduleConfigurationCoercionTest {

  @Before
  public void freezeTime() {
    DateTime now = new DateTime();
    DateTime midnight = now.minusMillis(now.getMillisOfDay());
    DateTimeUtils.setCurrentMillisFixed(midnight.getMillis());
  }

  @After
  public void unfreezeTime() {
    DateTimeUtils.setCurrentMillisSystem();
  }

  @Spy
  Set<ScheduleParser> parsers = new HashSet<ScheduleParser>(Arrays.asList(new PeriodicScheduleParser(), new AlignedPeriodicScheduleParser()));
  
  @InjectMocks
  ScheduleCoercion coercion = new ScheduleCoercion();

  @Test
  public void applicability() {
    assertThat(coercion.isApplicableTo(CoercionTargets.find(Integer.class))).isFalse();
    assertThat(coercion.isApplicableTo(CoercionTargets.find(Schedule.class))).isTrue();
    assertThat(coercion.isApplicableTo(CoercionTargets.find(PeriodicSchedule.class))).isTrue();
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
    assertThatSchedule("every day at 3 hours past").hasPeriod(24).hours().startingAfter(3);
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
    assertThatSchedule("every hour at 15 minutes past").hasPeriod(60).minutes().startingAfter(15);
    assertThatSchedule("every 3 hours starting at 1 hours past").hasPeriod(180).minutes().startingAfter(60);
    assertThatSchedule("every 3 hours starting at 2 hours past").hasPeriod(180).minutes().startingAfter(120);
    assertThatSchedule("every 3 hours starting at 30 minutes past").hasPeriod(180).minutes().startingAfter(30);
  }

  @Test
  public void minutely() {
    assertThatSchedule("every 15 minutes starting at 5 minutes past").hasPeriod(15 * 60).seconds().startingAfter(5 * 60);
    assertThatSchedule("every 15 minutes starting at 5 seconds past").hasPeriod(15 * 60).seconds().startingAfter(5);
  }

  private CoercionTarget target() {
    return CoercionTargets.find(Schedule.class);
  }
}
