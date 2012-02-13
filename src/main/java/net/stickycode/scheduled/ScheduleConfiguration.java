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

import java.lang.reflect.AnnotatedElement;

import net.stickycode.coercion.CoercionTarget;
import net.stickycode.configured.ConfigurationAttribute;

public class ScheduleConfiguration
    implements ConfigurationAttribute, CoercionTarget {

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

  public Object getValue() {
    return this.schedule;
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
  public boolean isArray() {
    return false;
  }

  @Override
  public boolean hasComponents() {
    return false;
  }

  @Override
  public CoercionTarget[] getComponentCoercionTypes() {
    throw new UnsupportedOperationException("Schedule configurations have no components");
  }

  @Override
  public boolean isPrimitive() {
    return false;
  }

  @Override
  public Class<?> boxedType() {
    throw new UnsupportedOperationException("Schedule configurations are not primitive and as such cannot be boxed");
  }

  @Override
  public CoercionTarget getCoercionTarget() {
    return this;
  }

  @Override
  public boolean canBeAnnotated() {
    return false;
  }

  @Override
  public AnnotatedElement getAnnotatedElement() {
    return null;
  }

  @Override
  public Class<?> getOwner() {
    return null;
  }

  @Override
  public CoercionTarget getParent() {
    return null;
  }

  @Override
  public boolean hasParent() {
    return false;
  }

  @Override
  public boolean canBeUpdated() {
    return true;
  }

  @Override
  public boolean hasValue() {
    return schedule != null;
  }

}
