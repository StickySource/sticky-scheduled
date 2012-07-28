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

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.stickycode.configured.ConfigurationAttribute;
import net.stickycode.configured.ConfigurationRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ScheduledBeanProcessorTest {

  @Mock
  ConfigurationRepository configurations;

  @Mock
  ScheduledRunnableRepository schedulingSystem;

  @Spy
  Set<ScheduledMethodInvokerFactory> factories = new HashSet<ScheduledMethodInvokerFactory>(
      Arrays.asList(new SimpleScheduledInvokerFactory()));

  @InjectMocks
  ScheduledBeanProcessor injector = new ScheduledBeanProcessor();

  @InjectMocks
  ScheduledMethodProcessor processor = new ScheduledMethodProcessor();

  @Test
  public void scheduled() throws InterruptedException {
    ScheduleTestObject schedule = new ScheduleTestObject();
    assertThat(injector.isSchedulable(schedule.getClass())).isTrue();

    assertThat(injector.isSchedulable(ScheduleTestObject.class));
    // verify(beanProcessor).process(any(ScheduleTestObject.class), eq("scheduleTestObject.runIt"), eq((CoercionTarget) null));
  }
}
