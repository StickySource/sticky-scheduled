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

import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class ScheduleParserTest {

  @Test(expected = UnknownUnitForSchedulingException.class)
  public void undefinedUnit() {
    parseUnit("rubbish");
  }

  @Test
  public void upperCaseUnit() {
    parseUnit("MINUTE");
  }

  @Test
  public void lowerCaseUnit() {
    parseUnit("day");
  }

  @Test
  public void mixedCaseUnit() {
    parseUnit("Day");
  }

  @Test
  public void allUnits() {
    for (TimeUnit unit : TimeUnit.values()) {
      parseUnit(unit.name().substring(0, unit.name().length() - 1));
    }
  }

  @Test
  public void withMultiplier() {
    assertThat(new ScheduleParser().parse("every 30 seconds").getPeriod()).isEqualTo(30);
    assertThat(new ScheduleParser().parse("every second").getPeriod()).isEqualTo(1);
    assertThat(new ScheduleParser().parse("every 1 hour").getPeriod()).isEqualTo(60 * 60);
    assertThat(new ScheduleParser().parse("every 3 hours").getPeriod()).isEqualTo(60 * 60 * 3);
    assertThat(new ScheduleParser().parse("every 3 days").getPeriod()).isEqualTo(60 * 60 * 24 * 3);
  }
  
  @Test
  public void withDelay() {
    assertThat(new ScheduleParser().parse("every 30 seconds").getPeriod()).isEqualTo(30);
    assertThat(new ScheduleParser().parse("every second").getPeriod()).isEqualTo(1);
    assertThat(new ScheduleParser().parse("every 1 hour").getPeriod()).isEqualTo(60 * 60);
    assertThat(new ScheduleParser().parse("every 3 hours").getPeriod()).isEqualTo(60 * 60 * 3);
    assertThat(new ScheduleParser().parse("every 3 days").getPeriod()).isEqualTo(60 * 60 * 24 * 3);
  }

  private Object parseUnit(String string) {
    return new ScheduleParser().parseUnit(string);
  }
}
