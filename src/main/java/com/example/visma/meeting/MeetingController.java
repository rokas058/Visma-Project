package com.example.visma.meeting;

import com.example.visma.meeting.dto.DateRange;
import com.example.visma.person.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/meetings")
public class MeetingController {

    private final MeetingService meetingService;

    @Autowired
    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @GetMapping
    public List<Meeting> getAllMeetings() throws IOException {
        return meetingService.getAllMeetings();
    }
    @PostMapping
    public ResponseEntity<Meeting> createMeeting(@RequestBody Meeting meeting) throws IOException {
        return ResponseEntity.ok(meetingService.createMeeting(meeting));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMeeting(
            @PathVariable String id,
            @RequestBody Meeting meeting) throws IOException {
        meetingService.deleteMeeting(id, meeting.getResponsiblePerson());
        return ResponseEntity.ok("Deleted");
    }

    @PutMapping("attendees/{id}")
    public ResponseEntity<?> addPersonToMeeting(@PathVariable String id, @RequestBody Person person) throws IOException {
        meetingService.addPersonToMeeting(id, person);
        return ResponseEntity.ok("Added");
    }

    @DeleteMapping("attendees/{meetingId}/{personId}")
    public ResponseEntity<?> deletePersonFromMeeting(
            @PathVariable String meetingId,
            @PathVariable String personId,
            @RequestBody Meeting meeting) throws IOException {
        meetingService.deletePersonFromMeeting(meetingId, personId, meeting.getResponsiblePerson());
        return ResponseEntity.ok("Person deleted");
    }

    @GetMapping("description")
    public List<Meeting> getMeetingsByDescription(@RequestBody Map<String, Object> requestBody) throws IOException {
        String description = (String) requestBody.get("description");
        return meetingService.getMeetingsByDescription(description);
    }

    @GetMapping("responsiblePerson")
    public List<Meeting> getMeetingsByResponsiblePerson(@RequestBody Map<String, String> requestBody) throws IOException {
        String responsiblePerson = requestBody.get("responsiblePerson");
        return meetingService.getMeetingsByResponsiblePerson(responsiblePerson);
    }

    @GetMapping("category")
    public List<Meeting> getMeetingsByCategory(@RequestBody Map<String, Object> requestBody) throws IOException {
        String category = (String) requestBody.get("category");
        return meetingService.getMeetingsByCategory(category);
    }

    @GetMapping("type")
    public List<Meeting> getMeetingsByType(@RequestBody Map<String, Object> requestBody) throws IOException {
        String type = (String) requestBody.get("type");
        return meetingService.getMeetingsByType(type);
    }

    @GetMapping("date")
    public List<Meeting> getMeetingsByDateRange(@RequestBody DateRange dateRange) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(dateRange.getStartDate(), formatter);
        LocalDate endDate = LocalDate.parse(dateRange.getEndDate(), formatter);
        return meetingService.getMeetingsByDateRange(startDate, endDate);
    }

    @GetMapping("attendees")
    public List<Meeting> getMeetingsByAttendees(@RequestBody Map<String, Object> requestBody) throws IOException {
        int attendees = (int) requestBody.get("attendees");
        return meetingService.getMeetingsByAttendees(attendees);
    }

}
