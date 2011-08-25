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

import net.stickycode.configured.ConfiguredConfiguration;
import net.stickycode.reflector.AnnotatedMethodProcessor;
import net.stickycode.stereotype.Scheduled;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScheduledMethodProcessor
    extends AnnotatedMethodProcessor {

  private Logger log = LoggerFactory.getLogger(ScheduledMethodProcessor.class);

  private final SchedulingSystem schedulingSystem;

  private final ConfiguredConfiguration scheduleConfiguration;

  public ScheduledMethodProcessor(SchedulingSystem schedulingSystem, ConfiguredConfiguration scheduleConfiguration) {
    super(Scheduled.class);
    this.schedulingSystem = schedulingSystem;
    this.scheduleConfiguration = scheduleConfiguration;
  }

  @Override
  public void processMethod(Object target, Method method) {
    ScheduleConfiguration schedule = new ScheduleConfiguration(method.getName());
    scheduleConfiguration.addAttribute(schedule);
    ScheduledMethodInvoker scheduledMethodInvoker = new ScheduledMethodInvoker(method, target, schedule);
    log.debug("Found {} to register for scheduling", scheduledMethodInvoker);
    schedulingSystem.schedule(scheduledMethodInvoker);
  }

}
