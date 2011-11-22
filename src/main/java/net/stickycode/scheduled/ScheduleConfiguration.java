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

import java.lang.reflect.ParameterizedType;

import net.stickycode.coercion.AbstractCoercionType;
import net.stickycode.configured.ConfigurationAttribute;

public class ScheduleConfiguration
    extends AbstractCoercionType
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
    throw new UnsupportedOperationException("Scheduling has no components");
  }

  @Override
  public AbstractCoercionType getComponentCoercionType() {
    throw new UnsupportedOperationException("Scheduling has no component type to coerce");
  }

  @Override
  public AbstractCoercionType[] getComponentCoercionTypes() {
    throw new UnsupportedOperationException("Scheduling has no component targets");
  }

  @Override
  public boolean hasDefaultValue() {
    return false;
  }

  @Override
  public Object getDefaultValue() {
    throw new UnsupportedOperationException("Scheduling cannot have a default value");
  }

  @Override
  public void setValue(Object value) {
    this.schedule = (Schedule) value;
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

  @Override
  public boolean isGenericType() {
    return false;
  }

  @Override
  public ParameterizedType getGenericType() {
    throw new UnsupportedOperationException("Schedule configurations are not generic");
  }

}
