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

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

import net.stickycode.coercion.AbstractFailedToCoerceValueException;
import net.stickycode.coercion.Coercion;
import net.stickycode.coercion.CoercionTarget;
import net.stickycode.scheduled.PeriodicSchedule;
import net.stickycode.scheduled.Schedule;
import net.stickycode.scheduled.ScheduleConfiguration;
import net.stickycode.stereotype.StickyFramework;
import net.stickycode.stereotype.StickyPlugin;

@StickyPlugin
@StickyFramework
public class ScheduleConfigurationCoercion
    implements Coercion<Schedule> {
  
  private List<ScheduleParser> parsers = Arrays.asList(new PeriodicScheduleParser(), new AlignedPeriodicScheduleParser());

  @Override
  public Schedule coerce(CoercionTarget type, String value) throws AbstractFailedToCoerceValueException {
    if (value.length() == 0)
      throw new ScheduleMustBeDefinedButTheValueWasBlankException(type);
    
    for (ScheduleParser parser : parsers) {
      Matcher matcher = parser.matches(value);
      if (matcher.matches())
        return parser.parse(matcher);
    }
    
    throw new ScheduleDefintionIsNotValidException(value);
  }

  @Override
  public boolean isApplicableTo(CoercionTarget type) {
    if (type.getType().isAssignableFrom(ScheduleConfiguration.class))
      return true;

    if (type.getType().isAssignableFrom(PeriodicSchedule.class))
      return true;

    return false;
  }

}
