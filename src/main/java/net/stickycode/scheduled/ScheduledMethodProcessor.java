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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.stickycode.configured.ConfiguredConfiguration;
import net.stickycode.reflector.MethodProcessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScheduledMethodProcessor
    implements MethodProcessor {

  private Logger log = LoggerFactory.getLogger(ScheduledMethodProcessor.class);

  private ScheduledRunnableRepository schedulingSystem;

  private ConfiguredConfiguration scheduleConfiguration;

  private List<ScheduledMethodInvokerFactory> methodInvokerFactories = new ArrayList<ScheduledMethodInvokerFactory>();

  public ScheduledMethodProcessor() {
  }

  @Deprecated
  public ScheduledMethodProcessor(ScheduledRunnableRepository scheduleRepository,
      ConfiguredConfiguration configurationOfBeanSchedules) {
    this.schedulingSystem = scheduleRepository;
    this.scheduleConfiguration = configurationOfBeanSchedules;
    this.methodInvokerFactories.add(new SimpleScheduledInvokerFactory());
  }

  @Override
  public void processMethod(Object target, Method method) {
    ScheduleConfiguration schedule = new ScheduleConfiguration(method.getName());
    ScheduledRunnable scheduledMethodInvoker = createScheduledMethodInvoker(target, method, schedule);
    scheduleConfiguration.addAttribute(schedule);
    log.debug("Found {} to register for scheduling", scheduledMethodInvoker);
    schedulingSystem.schedule(scheduledMethodInvoker);
  }

  private ScheduledRunnable createScheduledMethodInvoker(Object target, Method method, ScheduleConfiguration schedule) {
    for (ScheduledMethodInvokerFactory factory : methodInvokerFactories) {
      if (factory.canInvoke(method))
        return factory.create(target, method, schedule.getSchedule());
    }

    throw new FactoryNotRegisteredToProcessMethodException(target, method, methodInvokerFactories);
  }

  public ScheduledMethodProcessor withInvokers(Set<ScheduledMethodInvokerFactory> factories) {
    methodInvokerFactories.addAll(factories);
    return this;
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

  public ScheduledMethodProcessor withSchedulingSystem(ScheduledRunnableRepository schedulingSystem) {
    this.schedulingSystem = schedulingSystem;
    return this;
  }

  public ScheduledMethodProcessor withConfiguration(ConfiguredConfiguration configuration) {
    this.scheduleConfiguration = configuration;
    return this;
  }

}
