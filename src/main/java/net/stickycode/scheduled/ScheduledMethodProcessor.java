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
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import net.stickycode.configured.ConfigurationRepository;
import net.stickycode.reflector.MethodProcessor;
import net.stickycode.stereotype.StickyComponent;
import net.stickycode.stereotype.StickyFramework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@StickyComponent
@StickyFramework
public class ScheduledMethodProcessor
    implements MethodProcessor {

  private Logger log = LoggerFactory.getLogger(ScheduledMethodProcessor.class);

  @Inject
  private ScheduledRunnableRepository schedulingSystem;

  @Inject
  private ConfigurationRepository repository;

  @Inject
  private Set<ScheduledMethodInvokerFactory> methodInvokerFactories;

  @Override
  public void processMethod(Object target, Method method) {
    ScheduledRunnable scheduledMethodInvoker = createScheduledMethodInvoker(target, method);
    repository.register(scheduledMethodInvoker);
    log.debug("Found {} to register for scheduling", scheduledMethodInvoker);
    schedulingSystem.schedule(scheduledMethodInvoker);
  }

  private ScheduledRunnable createScheduledMethodInvoker(Object target, Method method) {
    for (ScheduledMethodInvokerFactory factory : methodInvokerFactories) {
      if (factory.canInvoke(method))
        return factory.create(target, method);
    }

    throw new FactoryNotRegisteredToProcessMethodException(target, method, methodInvokerFactories);
  }

  @Override
  public boolean canProcess(Method method) {
    for (ScheduledMethodInvokerFactory factory : methodInvokerFactories) {
      if (factory.canInvoke(method))
        return true;
    }

    return false;
  }

  @Override
  public void sort(List<Method> methods) {
  }

}
