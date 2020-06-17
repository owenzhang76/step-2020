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
    private final HashMap<String, ArrayList<ArrayList<TimeRange>>> peopleAndTimeRangesMap = new HashMap<String,  ArrayList<ArrayList<TimeRange>>>();

  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    long requestDuration = request.getDuration();
    ArrayList<TimeRange> thirtyMinuteTimeSlots = new ArrayList<>(); 
    TimeRange eightToNineWorkDay = TimeRange.fromStartEnd(8, 21, false);

  
    // Put all invitee names in a hashmap as the key, and their free and busy time in an ArrayList<TimeRange> as the value 
    for (String invitee : request.getAttendees()) {

        // System.out.println("The person invited to this meeting request is " + invitee);
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
        // ArrayList<TimeRange> sortedBusyTimeRanges = Collections.sort(peopleAndTimeRangesMap.get(invitee).get(1), TimeRange.ORDER_BY_START);
        Collections.sort(peopleAndTimeRangesMap.get(invitee).get(1), TimeRange.ORDER_BY_START);
        removeContainedBusyEvents(peopleAndTimeRangesMap.get(invitee).get(1), invitee);
        findFreeTime(peopleAndTimeRangesMap.get(invitee).get(1), 0, 0, invitee);
    }

    // view peopleAndFreeTime hashmap
    System.out.println();
    System.out.println();
    System.out.println();
    System.out.println();
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

  public void removeContainedBusyEvents(ArrayList<TimeRange> busyTimeRanges, String name) {
    System.out.println("This is the busyTimeRanges: ");
    for(int i = 0; i < busyTimeRanges.size(); i++) {   
        System.out.print(busyTimeRanges.get(i));
    }   
    System.out.println();
    for (int i = 0; i < busyTimeRanges.size()-1; i++) {
       for (int j = 1; j < busyTimeRanges.size(); j++) {
           TimeRange timeOne = busyTimeRanges.get(i);
           TimeRange timeTwo = busyTimeRanges.get(j);
            System.out.println("timeOne is " + timeOne);
            System.out.println("timeTwo is " + timeTwo);
           if (timeOne.overlaps(timeTwo)) {
               if (timeOne.equals(timeTwo)) {
                peopleAndTimeRangesMap.get(name).get(1).set(i, null);
                System.out.println(" case 1 setting " + peopleAndTimeRangesMap.get(name).get(1).get(i) + " to null");
               } else if (timeOne.contains(timeTwo)) {
                  System.out.println("case 2 setting " + peopleAndTimeRangesMap.get(name).get(1).get(j) + " to null");
                  peopleAndTimeRangesMap.get(name).get(1).set(j, null);
               } else if (timeTwo.contains(timeOne)) {
                    System.out.println("case 3 setting " + peopleAndTimeRangesMap.get(name).get(1).get(j) + " to null");
                    peopleAndTimeRangesMap.get(name).get(1).set(j, null);
               } else {
                  System.out.println("doing nothing");
               }
           } else {
                 System.out.println("these two times don't overlap.");
           }
       }
    }
  }


  public void findFreeTime(ArrayList<TimeRange> busyTimeRanges, int index, int startTime, String name) {
    findFreeTimeHelper(busyTimeRanges, index, startTime, name);
  }

  public void findFreeTimeHelper(ArrayList<TimeRange> busyTimeRanges, int index, int startTime, String name) {
    if (startTime == 0) {
        if (busyTimeRanges.size() == 0) {
            TimeRange freeTime = TimeRange.fromStartEnd(0, 1440, false);
            peopleAndTimeRangesMap.get(name).get(0).add(freeTime);
            return;
        } else {
            int start = startTime;
            int end = busyTimeRanges.get(index).start();
            int duration = busyTimeRanges.get(index).duration();
            int newStartTime = end + duration;
            TimeRange freeTime = TimeRange.fromStartEnd(start, end-1, false);

            peopleAndTimeRangesMap.get(name).get(0).add(freeTime);
            
            findFreeTimeHelper(busyTimeRanges, index + 1, newStartTime, name);
        }
    } else if (startTime == 1440) {
        return;
    } else {
        if (index < busyTimeRanges.size()) {
            int start = startTime;
            int end = busyTimeRanges.get(index).start();
            int duration = busyTimeRanges.get(index).duration();
            int newStartTime = end + duration;
            TimeRange freeTime = TimeRange.fromStartEnd(start+1, end-1, false);

            peopleAndTimeRangesMap.get(name).get(0).add(freeTime);
            
            findFreeTimeHelper(busyTimeRanges, index + 1, newStartTime, name);
        } else {
            int start = startTime;
            int end = 1440;
            TimeRange freeTime = TimeRange.fromStartEnd(start+1, end, false);

            peopleAndTimeRangesMap.get(name).get(0).add(freeTime);

            findFreeTimeHelper(busyTimeRanges, index + 1, 1440, name);
        }
    }
  }

}
