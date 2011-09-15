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
package net.stickycode.scheduled.aligned;

import java.util.concurrent.TimeUnit;

import net.stickycode.exception.PermanentException;

@SuppressWarnings("serial")
public class AlignmentCannotBeLessThanPeriodUnitsException
    extends PermanentException {

  public AlignmentCannotBeLessThanPeriodUnitsException(TimeUnit alignmentUnit, TimeUnit periodUnit) {
    super("The alignment '{}' of the schedule cannot be greater than the period '{}' otherwise the alignment would be meaningless."
        + " e.g. 'every 30 seconds at 1 hour past' makes no sense to me", alignmentUnit, periodUnit);
  }

}
