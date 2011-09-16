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
package net.stickycode.fest;

import java.util.concurrent.TimeUnit;

import net.stickycode.scheduled.Schedule;

import org.fest.assertions.Assertions;
import org.fest.assertions.GenericAssert;

public class ScheduleAssert
    extends GenericAssert<ScheduleAssert, Schedule> {

  static public ScheduleAssert assertThat(Schedule schedule) {
    return new ScheduleAssert(schedule);
  }

  public ScheduleAssert(Schedule actual) {
    super(ScheduleAssert.class, actual);
    isNotNull();
  }

  public ScheduleAssert hasPeriod(long i) {
    Assertions.assertThat(actual.getPeriod()).isEqualTo(i);
    return this;
  }

  public ScheduleAssert days() {
    Assertions.assertThat(actual.getUnits()).isEqualTo(TimeUnit.DAYS);
    return this;
  }

  public ScheduleAssert minutes() {
    Assertions.assertThat(actual.getUnits()).isEqualTo(TimeUnit.MINUTES);
    return this;
  }

  public ScheduleAssert seconds() {
    Assertions.assertThat(actual.getUnits()).isEqualTo(TimeUnit.SECONDS);
    return this;
  }

  public ScheduleAssert hours() {
    Assertions.assertThat(actual.getUnits()).isEqualTo(TimeUnit.HOURS);
    return this;
  }

  public ScheduleAssert millis() {
    Assertions.assertThat(actual.getUnits()).isEqualTo(TimeUnit.MILLISECONDS);
    return this;
  }

  public ScheduleAssert startingAfter(int i) {
    Assertions.assertThat(actual.getInitialDelay()).isEqualTo(i);
    return this;
  }

  public ScheduleAssert delayedByNothing(int i) {
    Assertions.assertThat(actual.getInitialDelay()).isEqualTo(0);
    return this;
  }

}
