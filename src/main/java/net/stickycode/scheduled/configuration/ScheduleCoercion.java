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

import java.util.Set;

import javax.inject.Inject;

import net.stickycode.coercion.AbstractNoDefaultCoercion;
import net.stickycode.coercion.CoercionTarget;
import net.stickycode.scheduled.Schedule;
import net.stickycode.scheduled.ScheduleParser;
import net.stickycode.stereotype.StickyPlugin;

@StickyPlugin
public class ScheduleCoercion
    extends AbstractNoDefaultCoercion<Schedule> {

  @Inject
  private Set<ScheduleParser> parsers;

  @Override
  public Schedule coerce(CoercionTarget type, String value) {
    if (value.length() == 0)
      throw new ScheduleMustBeDefinedButTheValueWasBlankException(type);

    for (ScheduleParser parser : parsers) {
      if (parser.matches(value))
        return parser.parse(value);
    }

    throw new ScheduleDefintionIsNotValidException(value);
  }

  @Override
  public boolean isApplicableTo(CoercionTarget type) {
    if (Schedule.class.isAssignableFrom(type.getType()))
      return true;

    return false;
  }

}
