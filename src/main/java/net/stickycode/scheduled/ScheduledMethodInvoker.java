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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScheduledMethodInvoker
    implements ScheduledRunnable {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final Method method;

  private final Object target;
  
  private final ScheduleConfiguration schedule;

  public ScheduledMethodInvoker(Method method, Object target, ScheduleConfiguration schedule) {
    this.method = method;
    this.target = target;
    this.schedule = schedule;
  }

  @Override
  public void run() {
    try {
      method.invoke(target, new Object[0]);
    }
    catch (TransientException e) {
      log.error("Failed to execute {}", this, e);
    }
    catch (IllegalArgumentException e) {
      throw new ThisShouldNeverHappenException(e, 
          "No arguments are passed to the scheduled method {} so this should never happen",
          toString());
    }
    catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
    catch (InvocationTargetException e) {
      throw new ScheduledMethodExecutionFailureException(e, method, target);
    }
  }

  @Override
  public String toString() {
    return target.getClass().getSimpleName() + "." + method.getName();
  }

  @Override
  public Schedule getSchedule() {
    return schedule.getSchedule();
  }

}
