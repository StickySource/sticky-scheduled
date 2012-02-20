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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.stickycode.exception.TransientException;
import net.stickycode.stereotype.Configured;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScheduledMethodInvoker
    implements ScheduledRunnable {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final Method method;

  private final Object target;
  
  @Configured
  private Schedule schedule;

  public ScheduledMethodInvoker(Method method, Object target) {
    this.method = method;
    this.target = target;
  }

  @Override
  public void run() {
    try {
      log.debug("invoking {} using {}", this, schedule);
      // TODO need a means of cancelling the call if it take too long
      // cancel method? or invoke it async and return an handler?
      method.invoke(target, new Object[0]);
    }
    catch (IllegalArgumentException e) {
      // means we are invoking the method incorrectly which should not be possible 
      throw new ThisShouldNeverHappenException(e, 
          "{}. Failed to invoke the scheduled method {} on class {}", 
          e.getMessage(), toString(), target.getClass().getName());
    }
    catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
    catch (InvocationTargetException e) {
      // TODO this needs to more cleanly defined
      if (e.getCause() instanceof TransientException)
        log.error("Failed to execute {}", this, e);
      else
        throw new ScheduledMethodExecutionFailureException(e, method, target);
    }
  }

  @Override
  public String toString() {
    return method.getDeclaringClass().getSimpleName() + "." + method.getName();
  }

  @Override
  public Schedule getSchedule() {
    return schedule;
  }

}
