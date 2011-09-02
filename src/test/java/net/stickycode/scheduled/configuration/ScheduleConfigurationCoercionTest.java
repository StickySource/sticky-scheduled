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
import net.stickycode.scheduled.Schedule;
import net.stickycode.scheduled.ScheduleConfiguration;

import org.junit.Test;


public class ScheduleConfigurationCoercionTest {

  ScheduleConfigurationCoercion coercion = new ScheduleConfigurationCoercion();
  
  @Test
  public void applicability() {
    assertThat(coercion.isApplicableTo(new CoercionType(Integer.class))).isFalse();
    assertThat(coercion.isApplicableTo(new CoercionType(ScheduleConfiguration.class))).isTrue();
    assertThat(coercion.isApplicableTo(new CoercionType(Schedule.class))).isTrue();
  }
  
  @Test(expected=ScheduleMustBeDefinedButTheValueWasBlankException.class)
  public void blank() {
    assertThat(coercion.coerce(target(), ""));
  }
  
  @Test
  public void daily() {
    assertThat(coercion.coerce(target(), "every day at 00:15").getPeriod()).isEqualTo(60*60*24);
    assertThat(coercion.coerce(target(), "every day at 3 hours past midnight").getPeriod()).isEqualTo(60*60*24);
    assertThat(coercion.coerce(target(), "every day at 3 hours past").getPeriod()).isEqualTo(60*60*24);
  }
  
  public void specificDay() {
    assertThat(coercion.coerce(target(), "every tuesday at 00:15").getPeriod()).isEqualTo(60*60*24*7);
    assertThat(coercion.coerce(target(), "every wednesday at 3 hours past midnight").getPeriod()).isEqualTo(60*60*24*7);
    assertThat(coercion.coerce(target(), "every sunday at 3 hours past").getPeriod()).isEqualTo(60*60*24*7);
  }
  
  public void hourly() {
    assertThat(coercion.coerce(target(), "every hour at 15 minutes past").getPeriod()).isEqualTo(60*60*24);
    assertThat(coercion.coerce(target(), "every 3 hours starting at 3 hours past midnight").getPeriod()).isEqualTo(60*60*24);
    assertThat(coercion.coerce(target(), "every 3 hours starting at 1 hours past midnight").getPeriod()).isEqualTo(60*60*24);
    assertThat(coercion.coerce(target(), "every 3 hours starting at 30 minutes past midnight").getPeriod()).isEqualTo(60*60*24);
  }
  
  public void minutely() {
    assertThat(coercion.coerce(target(), "every 15 minutes starting at 5 minutes past").getPeriod()).isEqualTo(60*60*24);
    assertThat(coercion.coerce(target(), "every 15 minutes starting at 5 seconds past").getPeriod()).isEqualTo(60*60*24);
    assertThat(coercion.coerce(target(), "every 10 minutes at 3 hours past midnight").getPeriod()).isEqualTo(60*60*24);
  }

  private ScheduleConfiguration target() {
    return new ScheduleConfiguration("bob");
  }
}
