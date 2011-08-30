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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.lang.reflect.Method;

import net.stickycode.configured.ConfiguredConfiguration;
import net.stickycode.stereotype.Scheduled;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ScheduledMethodProcessorTest {
  
  static class WithASchedule {
    @Scheduled
    public void one() {
      
    }
  }

  @Mock
  private ScheduledRunnableRepository repository;
  
  @Mock
  private ConfiguredConfiguration configuration;

  @Test
  public void nothing() {
    new ScheduledMethodProcessor(repository, configuration);
    verifyZeroInteractions(repository, configuration);
  }
  
  @Test
  public void oneSchedule() throws NoSuchMethodException {
    WithASchedule one = new WithASchedule();
    Method m = getMethod("one", one);
    new ScheduledMethodProcessor(repository, configuration).processMethod(one, m);
    verify(configuration).addAttribute(any(ScheduleConfiguration.class));
    verify(repository).schedule(any(ScheduledMethodInvoker.class));
  }
  
  private Method getMethod(String name, Object target, Class<?>... parameters) throws NoSuchMethodException {
    return target.getClass().getDeclaredMethod(name, parameters);
  }
}
