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

import org.junit.Test;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import net.stickycode.exception.TransientException;

public class ScheduledMethodInvokerTest {

  private static interface RunIt {

    void invoke();

    void invoke(Object arg);
  }

  @Mocked
  RunIt runIt;

  @Test
  public void run() throws SecurityException, NoSuchMethodException {
    invoke("invoke", runIt.getClass());
    new Verifications() {
      {
        runIt.invoke();
      }
    };
  }

  @Test(expected = ScheduledMethodExecutionFailureException.class)
  public void except() throws SecurityException, NoSuchMethodException {
    new Expectations() {
      {
        runIt.invoke();
        result = new RuntimeException();
      }
    };

    invoke("invoke", runIt.getClass());

    new Verifications() {
      {
        runIt.invoke();
      }
    };
  }

  @Test
  public void transientExcept() throws SecurityException, NoSuchMethodException {
    new Expectations() {
      {
        runIt.invoke();
        result = new TransientException("Oops");
      }
    };

    invoke("invoke", runIt.getClass());

    new Verifications() {
      {
        runIt.invoke();
      }
    };
  }

  @Test(expected = ThisShouldNeverHappenException.class)
  public void wrongClass() throws SecurityException, NoSuchMethodException {
    invoke("wrongClass", getClass());
  }

  @Test(expected = ThisShouldNeverHappenException.class)
  public void wrongParameters() throws SecurityException, NoSuchMethodException {
    invoke("invoke", runIt.getClass(), Object.class);
  }

  private void invoke(String name, Class<?> type, Class<?>... parameterTypes) throws NoSuchMethodException {
    Method m = type.getDeclaredMethod(name, parameterTypes);
    run(m);
  }

  private void run(Method m) {
    ScheduledMethodInvoker scheduledMethodInvoker = new ScheduledMethodInvoker(m, runIt);
    scheduledMethodInvoker.run();
  }
}
