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
package net.stickycode.scheduled;

import static org.assertj.core.api.StrictAssertions.assertThat;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Injectable;
import mockit.Tested;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public class ScheduledBeanProcessorTest {

  @Injectable
  ScheduledMethodProcessor processor;

  @Injectable
  Set<ScheduledMethodInvokerFactory> factories = new HashSet<ScheduledMethodInvokerFactory>(
      Arrays.asList(new SimpleScheduledInvokerFactory()));

  @Tested
  ScheduledBeanProcessor injector;

  @Test
  public void scheduled() throws InterruptedException {
    ScheduleTestObject schedule = new ScheduleTestObject();
    assertThat(injector.isSchedulable(schedule.getClass())).isTrue();

    assertThat(injector.isSchedulable(ScheduleTestObject.class));
    // verify(beanProcessor).process(any(ScheduleTestObject.class), eq("scheduleTestObject.runIt"), eq((CoercionTarget) null));
  }
}
