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

import static net.stickycode.fest.ScheduleAssert.assertThat;
import static org.fest.assertions.Assertions.assertThat;
import net.stickycode.scheduled.Schedule;

import org.junit.Test;

public class PeriodicScheduleParserTest {

  @Test
  public void hours() {
    assertThat(parse("every hour")).hasPeriod(1).hours();
    assertThat(parse("every 1 hours")).hasPeriod(1).hours();
    assertThat(parse("every 2 hours")).hasPeriod(2).hours();
    assertThat(parse("every 25 hours")).hasPeriod(25).hours();
  }

  @Test
  public void minutes() {
    assertThat(parse("every minute")).hasPeriod(1).minutes();
    assertThat(parse("every 1 minutes")).hasPeriod(1).minutes();
    assertThat(parse("every 2 minutes")).hasPeriod(2).minutes();
    assertThat(parse("every 45 minutes")).hasPeriod(45).minutes();
    assertThat(parse("every 145 minutes")).hasPeriod(145).minutes();
  }

  @Test
  public void seconds() {
    assertThat(parse("every second")).hasPeriod(1).seconds();
    assertThat(parse("every 1 seconds")).hasPeriod(1).seconds();
    assertThat(parse("every 2 seconds")).hasPeriod(2).seconds();
    assertThat(parse("every 45 seconds")).hasPeriod(45).seconds();
    assertThat(parse("every 145 seconds")).hasPeriod(145).seconds();
  }

  private Schedule parse(String string) {
    PeriodicScheduleParser parser = new PeriodicScheduleParser();
    assertThat(parser.matches(string)).isTrue();
    return parser.parse(string);
  }

}
