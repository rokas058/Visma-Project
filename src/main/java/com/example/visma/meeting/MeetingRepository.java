package com.example.visma.meeting;

import com.example.visma.person.Person;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MeetingRepository {

    public final ObjectMapper objectMapper = new ObjectMapper();
    public final File meetingsFile = new File("meetings.json");
    public final List<Meeting> meetings;

    public MeetingRepository() throws IOException {
        if (meetingsFile.exists()) {
            meetings = objectMapper.readValue(meetingsFile, objectMapper.getTypeFactory().constructCollectionType(List.class, Meeting.class));
        } else {
            meetings = new ArrayList<>();
        }
    }

    public Meeting createMeeting(Meeting meeting) throws IOException {
        meetings.add(meeting);
        saveMeetings();
        return meeting;
    }

    public void saveMeetings() throws IOException {
        objectMapper.writeValue(meetingsFile, meetings);
    }

    public List<Meeting> getAllMeetings() throws IOException {
        return objectMapper.readValue(meetingsFile, objectMapper.getTypeFactory().constructCollectionType(List.class, Meeting.class));
    }

    public Meeting getMeetingById(String id) throws IOException {
        List<Meeting> meetings = getAllMeetings();
        for(Meeting meeting: meetings){
            if (meeting.getId().equals(id)) {
                return meeting;
            }else throw new RuntimeException("Meeting not found");
        }
        return null;
    }
    public void deleteMeeting(String id) throws IOException {
        List<Meeting> meetingsToKeep = new ArrayList<>();
        List<Meeting> allMeetings = getAllMeetings();
        for (Meeting meeting : allMeetings) {
            if (meeting.getId().equals(id)) {
                continue;
            }
            meetingsToKeep.add(meeting);
        }
        meetings.clear();
        meetings.addAll(meetingsToKeep);
        saveMeetings();

    }

    public void addPersonToMeeting(String id, Person person) throws IOException {
        Meeting meeting = getMeetingById(id);
        List<Person> attendees = meeting.getAttendees();

        if (attendees.contains(person)) {
            throw new RuntimeException("Person is already in the meeting");
        }

        for (Person attendee : attendees) {
            if (attendee.getFullName().equals(person.getFullName())) {
                LocalDateTime startTime1 = meeting.getStartDate();
                LocalDateTime endTime1 = meeting.getEndDate();
                LocalDateTime startTime2 = attendee.getStartMeeting();
                LocalDateTime endTime2 = attendee.getEndMeeting();

                if (startTime1.isBefore(endTime2) && startTime2.isBefore(endTime1)) {
                    throw new RuntimeException("Person is already in a meeting which intersects with the one being added");
                }
            }
        }
        attendees.add(person);
        for ( int i=0; i<meetings.size(); i++){
            if(meetings.get(i).getId().equals(meeting.getId()))
                meetings.set(i, meeting);
        }
        saveMeetings();
    }

    public void deletePersonFromMeeting(String meetingId, String personId) throws IOException {
        Meeting meeting = getMeetingById(meetingId);

        List<Person> attendees = meeting.getAttendees();
        boolean personRemoved = attendees.removeIf(person -> person.getId().equals(personId));

        if (!personRemoved) {
            throw new RuntimeException("Person not found in the meeting");
        }

        meeting.setAttendees(attendees);

        for ( int i=0; i<meetings.size(); i++){
            if(meetings.get(i).getId().equals(meeting.getId()))
                meetings.set(i, meeting);
        }
        saveMeetings();

    }
}
