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

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;

import org.junit.Test;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import net.stickycode.configured.ConfigurationRepository;
import net.stickycode.stereotype.scheduled.Scheduled;

public class ScheduledMethodProcessorTest {

  static class WithASchedule {

    @Scheduled
    public void one() {

    }
  }

  @Injectable
  private ScheduledRunnableRepository schedulingSystem;

  @Injectable
  ConfigurationRepository configurationRepository;

  @Injectable
  private Set<ScheduledMethodInvokerFactory> methodInvokerFactories;

  @Tested
  ScheduledMethodProcessor scheduledMethodProcessor;

  @Test
  public void oneSchedule() throws NoSuchMethodException {
    new Expectations() {
      {
        methodInvokerFactories.iterator();
        result = Collections.singleton(new SimpleScheduledInvokerFactory()).iterator();
      }
    };

    WithASchedule one = new WithASchedule();
    Method m = getMethod("one", one);
    scheduledMethodProcessor
        .processMethod(one, m);

    new Verifications() {
      {
        configurationRepository.register(withNotNull());
        schedulingSystem.schedule(withNotNull());
      }
    };
  }

  private Method getMethod(String name, Object target, Class<?>... parameters) throws NoSuchMethodException {
    return target.getClass().getDeclaredMethod(name, parameters);
  }
}
