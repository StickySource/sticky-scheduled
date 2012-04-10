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

import java.util.concurrent.TimeUnit;

import net.stickycode.scheduled.Schedule;

import org.junit.Test;

public class ScheduleParserTest {

  private final class NoopScheduleParser
      extends AbstractScheduleParser {

    @Override
    public boolean matches(String specification) {
      return false;
    }

    @Override
    public Schedule parse(String specification) {
      return null;
    }

  }

  @Test(expected = UnsupportedUnitForSchedulingException.class)
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

  private TimeUnit parseUnit(String string) {
    return new NoopScheduleParser().parseTimeUnit(string);
  }
}
