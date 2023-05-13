package com.example.visma;

import com.example.visma.meeting.Meeting;
import com.example.visma.meeting.MeetingRepository;
import com.example.visma.meeting.MeetingService;
import com.example.visma.meeting.enums.Category;
import com.example.visma.meeting.enums.Type;
import com.example.visma.person.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MeetingServiceTest {

    @Mock
    public MeetingRepository meetingRepository;

    @InjectMocks
    public MeetingService meetingService;

    @Test
    public void testCreateMeeting() throws IOException {
        // create a mock Meeting object
        Meeting meeting = mock(Meeting.class);

        // create a mock MeetingRepository object
        MeetingRepository meetingRepository = mock(MeetingRepository.class);
        when(meetingRepository.createMeeting(meeting)).thenReturn(meeting);

        // create a MeetingService object using the mock repository
        MeetingService meetingService = new MeetingService(meetingRepository);

        // call the createMeeting method and verify that the returned Meeting object is not null
        Meeting createdMeeting = meetingService.createMeeting(meeting);
        assertNotNull(createdMeeting);
    }

    @Test
    public void testGetAllMeetings() throws IOException {
        // create a mock list of Meeting objects
        List<Meeting> mockMeetings = Arrays.asList(
                new Meeting(),
                new Meeting()
        );

        // create a mock MeetingRepository object
        MeetingRepository meetingRepository = mock(MeetingRepository.class);
        when(meetingRepository.getAllMeetings()).thenReturn(mockMeetings);

        // create a MeetingService object using the mock repository
        MeetingService meetingService = new MeetingService(meetingRepository);

        // call the getAllMeetings method and verify that the returned list of Meeting objects is not null and has the expected size
        List<Meeting> allMeetings = meetingService.getAllMeetings();
        assertNotNull(allMeetings);
        assertEquals(2, allMeetings.size());
    }

    @Test
    public void testDeleteMeeting() throws IOException {
        // create a mock Meeting object
        Meeting meeting = new Meeting();
        meeting.setResponsiblePerson("John Doe");
        String id = meeting.getId();

        // create a mock MeetingRepository object
        MeetingRepository meetingRepository = mock(MeetingRepository.class);
        when(meetingRepository.getMeetingById(id)).thenReturn(meeting);

        // create a MeetingService object using the mock repository
        MeetingService meetingService = new MeetingService(meetingRepository);

        // call the deleteMeeting method with valid parameters and verify that no exception is thrown
        assertDoesNotThrow(() -> meetingService.deleteMeeting(id, "John Doe"));

        // call the deleteMeeting method with invalid responsible person and verify that a RuntimeException is thrown
        assertThrows(RuntimeException.class, () -> meetingService.deleteMeeting(id, "Jane Smith"));

        // verify that the deleteMeeting method is called with the correct id
        Mockito.verify(meetingRepository).deleteMeeting(id);
    }

    @Test
    public void testAddPersonToMeeting() throws IOException {
        // create a mock Meeting object
        Meeting meeting = new Meeting();
        String id = meeting.getId();

        // create a mock Person object
        Person person = new Person();

        // create a mock MeetingRepository object
        MeetingRepository meetingRepository = mock(MeetingRepository.class);


        // create a MeetingService object using the mock repository
        MeetingService meetingService = new MeetingService(meetingRepository);

        // call the addPersonToMeeting method
        meetingService.addPersonToMeeting(id, person);

        // verify that the addPersonToMeeting method was called with the correct arguments
        verify(meetingRepository).addPersonToMeeting(id, person);
    }

    @Test
    public void testDeletePersonFromMeeting() throws IOException {
        // create a mock Meeting object
        Meeting meeting = new Meeting();
        String id = meeting.getId();
        meeting.setResponsiblePerson("John Doe");

        // create a mock Person object
        Person person = new Person();
        person.setId("1");


        List<Person> attendees= new ArrayList<>();
        attendees.add(person);
        meeting.setAttendees(attendees);

        // create a mock MeetingRepository object
        MeetingRepository meetingRepository = mock(MeetingRepository.class);

        // when getMeetingById method is called with "1" as argument, return the mock Meeting object
        when(meetingRepository.getMeetingById(id)).thenReturn(meeting);

        // create a MeetingService object using the mock repository
        MeetingService meetingService = new MeetingService(meetingRepository);

        // call the deletePersonFromMeeting method
        meetingService.deletePersonFromMeeting(id, "1", "John Doe");

        // verify that the deletePersonFromMeeting method was called with the correct arguments
        verify(meetingRepository).deletePersonFromMeeting(id, "1");
    }

    @Test
    public void testGetMeetingsByDescription() throws IOException {
        // create mock Meeting objects
        Meeting mockMeeting1 = new Meeting();
        mockMeeting1.setDescription("Teaching");
        Meeting mockMeeting2 = new Meeting();
        mockMeeting2.setDescription("Project update");
        Meeting mockMeeting3 = new Meeting();
        mockMeeting3.setDescription("Teaching");

        // create mock list of meetings
        List<Meeting> mockMeetings = new ArrayList<>();
        mockMeetings.add(mockMeeting1);
        mockMeetings.add(mockMeeting2);
        mockMeetings.add(mockMeeting3);

        // create mock MeetingRepository object
        MeetingRepository meetingRepository = mock(MeetingRepository.class);
        when(meetingRepository.getAllMeetings()).thenReturn(mockMeetings);

        // create MeetingService object using the mock repository
        MeetingService meetingService = new MeetingService(meetingRepository);

        // call getMeetingsByDescription method with "Teaching" as argument
        List<Meeting> result = meetingService.getMeetingsByDescription("Teaching");

        // verify that the method returned the correct meetings
        assertEquals(2, result.size());
        assertTrue(result.contains(mockMeeting1));
        assertTrue(result.contains(mockMeeting3));
    }

    @Test
    public void testGetMeetingsByResponsiblePerson() throws IOException {
        // create mock Meeting objects
        Meeting mockMeeting1 = new Meeting();
        mockMeeting1.setResponsiblePerson("Jonas");
        Meeting mockMeeting2 = new Meeting();
        mockMeeting2.setResponsiblePerson("Jonas");
        Meeting mockMeeting3 = new Meeting();
        mockMeeting3.setResponsiblePerson("Ponas");

        // create mock list of meetings
        List<Meeting> mockMeetings = new ArrayList<>();
        mockMeetings.add(mockMeeting1);
        mockMeetings.add(mockMeeting2);
        mockMeetings.add(mockMeeting3);

        // create mock MeetingRepository object
        MeetingRepository meetingRepository = mock(MeetingRepository.class);
        when(meetingRepository.getAllMeetings()).thenReturn(mockMeetings);

        // create MeetingService object using the mock repository
        MeetingService meetingService = new MeetingService(meetingRepository);

        List<Meeting> result = meetingService.getMeetingsByResponsiblePerson("Jonas");

        // verify that the method returned the correct meetings
        assertEquals(2, result.size());
        assertTrue(result.contains(mockMeeting1));
        assertTrue(result.contains(mockMeeting2));
    }

    @Test
    public void testGetMeetingsByCategory() throws IOException {
        // create mock Meeting objects
        Meeting mockMeeting1 = new Meeting();
        mockMeeting1.setCategory(Category.HUB);
        Meeting mockMeeting2 = new Meeting();
        mockMeeting2.setCategory(Category.HUB);
        Meeting mockMeeting3 = new Meeting();
        mockMeeting3.setCategory(Category.CODE_MONKEY);

        // create mock list of meetings
        List<Meeting> mockMeetings = new ArrayList<>();
        mockMeetings.add(mockMeeting1);
        mockMeetings.add(mockMeeting2);
        mockMeetings.add(mockMeeting3);

        // create mock MeetingRepository object
        MeetingRepository meetingRepository = mock(MeetingRepository.class);
        when(meetingRepository.getAllMeetings()).thenReturn(mockMeetings);

        // create MeetingService object using the mock repository
        MeetingService meetingService = new MeetingService(meetingRepository);

        List<Meeting> result = meetingService.getMeetingsByCategory("HUB");

        // verify that the method returned the correct meetings
        assertEquals(2, result.size());
        assertTrue(result.contains(mockMeeting1));
        assertTrue(result.contains(mockMeeting2));
    }

    @Test
    public void testGetMeetingsByType() throws IOException {
        // create mock Meeting objects
        Meeting mockMeeting1 = new Meeting();
        mockMeeting1.setType(Type.INPERSON);
        Meeting mockMeeting2 = new Meeting();
        mockMeeting2.setType(Type.INPERSON);
        Meeting mockMeeting3 = new Meeting();
        mockMeeting3.setType(Type.LIVE);

        // create mock list of meetings
        List<Meeting> mockMeetings = new ArrayList<>();
        mockMeetings.add(mockMeeting1);
        mockMeetings.add(mockMeeting2);
        mockMeetings.add(mockMeeting3);

        // create mock MeetingRepository object
        MeetingRepository meetingRepository = mock(MeetingRepository.class);
        when(meetingRepository.getAllMeetings()).thenReturn(mockMeetings);

        // create MeetingService object using the mock repository
        MeetingService meetingService = new MeetingService(meetingRepository);

        List<Meeting> result = meetingService.getMeetingsByType("INPERSON");

        // verify that the method returned the correct meetings
        assertEquals(2, result.size());
        assertTrue(result.contains(mockMeeting1));
        assertTrue(result.contains(mockMeeting2));
    }

    @Test
    public void testGetMeetingsByDateRange() throws IOException {
        // create mock Meeting objects
        Meeting mockMeeting1 = new Meeting();
        mockMeeting1.setStartDate(LocalDateTime.parse("2023-05-17T09:30"));
        mockMeeting1.setEndDate(LocalDateTime.parse("2023-05-17T16:30"));
        Meeting mockMeeting2 = new Meeting();
        mockMeeting2.setStartDate(LocalDateTime.parse("2023-05-17T09:30"));
        mockMeeting2.setEndDate(LocalDateTime.parse("2023-05-17T16:30"));
        Meeting mockMeeting3 = new Meeting();
        mockMeeting3.setStartDate(LocalDateTime.parse("2023-05-19T09:30"));
        mockMeeting3.setEndDate(LocalDateTime.parse("2023-05-19T16:30"));

        // create mock list of meetings
        List<Meeting> mockMeetings = new ArrayList<>();
        mockMeetings.add(mockMeeting1);
        mockMeetings.add(mockMeeting2);
        mockMeetings.add(mockMeeting3);

        // create mock MeetingRepository object
        MeetingRepository meetingRepository = mock(MeetingRepository.class);
        when(meetingRepository.getAllMeetings()).thenReturn(mockMeetings);

        // create MeetingService object using the mock repository
        MeetingService meetingService = new MeetingService(meetingRepository);

        List<Meeting> result = meetingService.getMeetingsByDateRange(LocalDate.parse("2023-05-17"),LocalDate.parse("2023-05-18"));

        // verify that the method returned the correct meetings
        assertEquals(2, result.size());
        assertTrue(result.contains(mockMeeting1));
        assertTrue(result.contains(mockMeeting2));
    }

    @Test
    public void testGetMeetingsByAttendees() throws IOException {
        // create a mock list of Meeting objects
        Meeting mockMeeting1 = new Meeting();
        List<Person> attendees1 = new ArrayList<>();
        attendees1.add(new Person());
        attendees1.add(new Person());
        mockMeeting1.setAttendees(attendees1);

        Meeting mockMeeting2 = new Meeting();
        List<Person> attendees2 = new ArrayList<>();
        attendees2.add(new Person());
        mockMeeting2.setAttendees(attendees2);

        List<Meeting> mockMeetings = new ArrayList<>();
        mockMeetings.add(mockMeeting1);
        mockMeetings.add(mockMeeting2);

        // create a mock MeetingRepository object
        MeetingRepository meetingRepository = mock(MeetingRepository.class);
        when(meetingRepository.getAllMeetings()).thenReturn(mockMeetings);

        // create a MeetingService object using the mock repository
        MeetingService meetingService = new MeetingService(meetingRepository);

        // call the getMeetingsByAttendees method with 2 attendees
        List<Meeting> meetings = meetingService.getMeetingsByAttendees(2);

        // verify that the returned list contains only the meeting with 2 attendees
        assertEquals(1, meetings.size());
        assertEquals(mockMeeting1, meetings.get(0));
    }



}
