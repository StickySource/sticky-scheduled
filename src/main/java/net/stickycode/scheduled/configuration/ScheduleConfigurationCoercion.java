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
package net.stickycode.scheduled.configuration;

import javax.inject.Inject;

import net.stickycode.coercion.AbstractFailedToCoerceValueException;
import net.stickycode.coercion.Coercion;
import net.stickycode.coercion.CoercionTarget;
import net.stickycode.scheduled.Schedule;
import net.stickycode.scheduled.ScheduleConfiguration;
import net.stickycode.stereotype.component.StickyMapper;

@StickyMapper
public class ScheduleConfigurationCoercion
    implements Coercion<Schedule> {
  
  @Inject
  private ScheduleParser parser;

  @Override
  public Schedule coerce(CoercionTarget type, String value) throws AbstractFailedToCoerceValueException {
    if (value.length() == 0)
      throw new ScheduleMustBeDefinedButTheValueWasBlankException(type);
    
    return parser.parse(value);
  }

  @Override
  public boolean isApplicableTo(CoercionTarget type) {
    if (type.getType().isAssignableFrom(ScheduleConfiguration.class))
      return true;

    if (type.getType().isAssignableFrom(Schedule.class))
      return true;

    return false;
  }

}
