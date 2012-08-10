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

import net.stickycode.bootstrap.ComponentContainer;
import net.stickycode.coercion.CoercionFinder;
import net.stickycode.coercion.CoercionTarget;
import net.stickycode.configuration.ConfigurationTarget;
import net.stickycode.configuration.ResolvedConfiguration;
import net.stickycode.exception.TransientException;
import net.stickycode.stereotype.configured.Configured;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScheduledMethodInvoker
    implements ScheduledRunnable {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final Method method;

  private final Object target;
  
  private ConfigurationTarget name;

  @Configured
  private Schedule schedule;

  private ResolvedConfiguration resolution;

  public ScheduledMethodInvoker(Method method, Object target) {
    this.method = method;
    this.target = target;
    this.name = new MethodConfigurationTarget(method);
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
    return join(".");
  }

  @Override
  public Schedule getSchedule() {
    return schedule;
  }

  @Override
  public void applyCoercion(CoercionFinder coercions) {
  }

  @Override
  public void update() {
  }

  @Override
  public void invertControl(ComponentContainer container) {
  }

  @Override
  public void resolvedWith(ResolvedConfiguration resolved) {
    this.resolution = resolved;
  }

  @Override
  public ResolvedConfiguration getResolution() {
    return resolution;
  }

  @Override
  public String join(String delimeter) {
    return name.join(delimeter);
  }

  @Override
  public Object getTarget() {
    return target;
  }

  @Override
  public CoercionTarget getCoercionTarget() {
    return null;
  }

  @Override
  public boolean requiresResolution() {
    return resolution == null;
  }

}
