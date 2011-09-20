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

import net.stickycode.coercion.CoercionTarget;
import net.stickycode.configured.ConfigurationAttribute;


public class ScheduleConfiguration
    implements ConfigurationAttribute {
  
  private Schedule schedule;
  private final String name;

  public ScheduleConfiguration(String name) {
    this.name = name;
  }

  @Override
  public Class<?> getType() {
    return PeriodicSchedule.class;
  }

  @Override
  public boolean isArray() {
    return false;
  }

  @Override
  public Class<?> getComponentType() {
    // XXX document parent that this only needs to be set if getType returns an array or parameterized type
    return null;
  }

  @Override
  public CoercionTarget getComponentCoercionType() {
    return null;
  }

  @Override
  public boolean hasDefaultValue() {
    return false;
  }

  @Override
  public Object getDefaultValue() {
    return null;
  }

  @Override
  public void setValue(Object value) {
    this.schedule = (Schedule)value;
  }

  @Override
  public String getName() {
    return name;
  }

  public Schedule getSchedule() {
    return schedule;
  }
  
  @Override
  public String toString() {
    if (schedule == null)
      return name + " with undefined schedule";
    
    return name + " with " + schedule.toString();
  }

}
