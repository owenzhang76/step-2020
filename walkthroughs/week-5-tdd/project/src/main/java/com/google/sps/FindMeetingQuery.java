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
    long requestDuration = request.getDuration();
    ArrayList<TimeRange> thirtyMinuteTimeSlots = new ArrayList<>(); 
    TimeRange eightToNineWorkDay = TimeRange.fromStartEnd(8, 21, false);

    HashMap<String, ArrayList<ArrayList<TimeRange>>> peopleAndTimeRangesMap = new HashMap<String,  ArrayList<ArrayList<TimeRange>>>();
    // Put all invitee names in a hashmap as the key, and their free and busy time in an ArrayList<TimeRange> as the value 
    for (String invitee : request.getAttendees()) {
        System.out.println("The person invited to this meeting request is " + invitee);
        ArrayList<TimeRange> freeTimeRanges = new ArrayList<>(); 
        ArrayList<TimeRange> busyTimeRanges = new ArrayList<>(); 
        ArrayList<ArrayList<TimeRange>> freeAndBusyTimeRanges = new ArrayList<>();
        freeAndBusyTimeRanges.add(freeTimeRanges);
        freeAndBusyTimeRanges.add(busyTimeRanges);
        peopleAndTimeRangesMap.put(invitee, freeAndBusyTimeRanges);
    }

    // For each invitee we care about in this particular request
    for (String invitee : peopleAndTimeRangesMap.keySet()) {
        // Iterate through all the events
        for (Event event : events) {
            // Iterate through all the attendees of the event
            for(String busyPerson : event.getAttendees()) {
                // check if invited person are in any of these events
                if (invitee.equals(busyPerson)) {
                    // if they are in events, add the event TimeRange into their busyTimeRanges array
                    peopleAndTimeRangesMap.get(invitee).get(1).add(event.getWhen());
                }
            }
        }
        // Sort the busy TimeRanges after everything is added.
        Collections.sort(peopleAndTimeRangesMap.get(invitee).get(1), TimeRange.ORDER_BY_START);
    }






    // view peopleAndFreeTime hashmap
    Set set = peopleAndTimeRangesMap.entrySet();
    Iterator iterator = set.iterator();
    while(iterator.hasNext()) {
        Map.Entry mentry = (Map.Entry)iterator.next();
        System.out.print("key is: "+ mentry.getKey() + " & Value is: ");
        System.out.println(mentry.getValue());
        System.out.println();
    }




    return null;
  }

  public void findFreeTime(ArrayList<TimeRange> busyTimeRanges) {
      
  }
}
