package com.example.visma.meeting;

import com.example.visma.meeting.enums.Category;
import com.example.visma.meeting.enums.Type;
import com.example.visma.person.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MeetingService {

    private final MeetingRepository meetingRepository;

    @Autowired
    public MeetingService(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    public Meeting createMeeting(Meeting meeting) throws IOException {
        return meetingRepository.createMeeting(meeting);
    }

    public List<Meeting> getAllMeetings() throws IOException {
        return meetingRepository.getAllMeetings();
    }

    public void deleteMeeting(String id, String responsiblePerson) throws IOException {
        Meeting meeting = meetingRepository.getMeetingById(id);
        if (!meeting.getResponsiblePerson().equals(responsiblePerson)) {
            throw new RuntimeException("Only the responsible person can delete the meeting");
        }
        meetingRepository.deleteMeeting(id);

    }

    public void addPersonToMeeting(String id, Person person) throws IOException {
        meetingRepository.addPersonToMeeting(id, person);
    }


    public void deletePersonFromMeeting(String meetingId, String personId, String responsiblePerson) throws IOException {
        Meeting meeting = meetingRepository.getMeetingById(meetingId);
        if (!meeting.getResponsiblePerson().equals(responsiblePerson)) {
            throw new RuntimeException("Only the responsible person can delete the meeting");
        }
        meetingRepository.deletePersonFromMeeting(meetingId, personId);
    }

    public List<Meeting> getMeetingsByDescription(String description) throws IOException {
        List<Meeting> descriptionMeetings = new ArrayList<>();
        List<Meeting> meetings = meetingRepository.getAllMeetings();
        for (Meeting meeting : meetings){
            if ( meeting.getDescription().equals(description) )
                descriptionMeetings.add(meeting);
        }
        return descriptionMeetings;
    }

    public List<Meeting> getMeetingsByResponsiblePerson(String responsiblePerson) throws IOException {
        List<Meeting> responsiblePersonMeetings = new ArrayList<>();
        List<Meeting> meetings = meetingRepository.getAllMeetings();
        for (Meeting meeting : meetings){
            if ( meeting.getResponsiblePerson().equals(responsiblePerson) )
                responsiblePersonMeetings.add(meeting);
        }
        return responsiblePersonMeetings;
    }

    public List<Meeting> getMeetingsByCategory(String category) throws IOException {
        List<Meeting> categoryMeetings = new ArrayList<>();
        List<Meeting> meetings = meetingRepository.getAllMeetings();
        for (Meeting meeting : meetings){
            if ( meeting.getCategory().equals(Category.valueOf(category)))
                categoryMeetings.add(meeting);
        }
        return categoryMeetings;
    }

    public List<Meeting> getMeetingsByType(String type) throws IOException {
        List<Meeting> typeMeetings = new ArrayList<>();
        List<Meeting> meetings = meetingRepository.getAllMeetings();
        for (Meeting meeting : meetings){
            if ( meeting.getType().equals(Type.valueOf(type)) )
                typeMeetings.add(meeting);
        }
        return typeMeetings;
    }

    public List<Meeting> getMeetingsByDateRange(LocalDate startDate, LocalDate endDate) throws IOException {
        List<Meeting> meetings = meetingRepository.getAllMeetings();
        List<Meeting> filteredMeetings = new ArrayList<>();
        for (Meeting meeting : meetings) {
            LocalDateTime meetingStartDateTime = meeting.getStartDate();
            LocalDateTime meetingEndDateTime = meeting.getEndDate();
            LocalDate meetingStartDate = meetingStartDateTime.toLocalDate();
            LocalDate meetingEndDate = meetingEndDateTime.toLocalDate();
            if ((meetingStartDate.isAfter(startDate) || meetingStartDate.isEqual(startDate))
                    && (meetingEndDate.isBefore(endDate) || meetingEndDate.isEqual(endDate))) {
                filteredMeetings.add(meeting);
            }
        }
        return filteredMeetings;
    }

    public List<Meeting> getMeetingsByAttendees(Integer attendees) throws IOException {
        List<Meeting> attendeesMeetings = new ArrayList<>();
        List<Meeting> meetings = meetingRepository.getAllMeetings();
        for (Meeting meeting : meetings){
            if ( meeting.getAttendees().size() == attendees )
                attendeesMeetings.add(meeting);
        }
        return attendeesMeetings;

    }
}
