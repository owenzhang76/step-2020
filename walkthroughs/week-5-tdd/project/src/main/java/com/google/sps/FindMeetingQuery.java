// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.io.*; 
import java.util.*; 
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.Collection;
// import java.util.Collections;
// import java.util.HashSet;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    Collection<TimeRange> thirtyMinuteTimeSlots = new HashSet<>();
    TimeRange eightToNineWorkDay = TimeRange.fromStartEnd(8, 21, false);
    for(int i = eightToNineWorkDay.start(); i < eightToNineWorkDay.end(); i++) {
        TimeRange newTimeRange = TimeRange.fromStartEnd(TimeRange.getTimeInMinutes(i, 0), TimeRange.getTimeInMinutes(i, 30), false);
        TimeRange newTimeRangeTwo = TimeRange.fromStartEnd(TimeRange.getTimeInMinutes(i, 30), TimeRange.getTimeInMinutes(i+1, 0), false);
        thirtyMinuteTimeSlots.add(newTimeRange);
        thirtyMinuteTimeSlots.add(newTimeRangeTwo);
    }
    return null;
  }
}
