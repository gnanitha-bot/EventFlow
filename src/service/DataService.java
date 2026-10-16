package service;

import model.Activity;
import model.Event;
import model.Participant;
import model.Registration;
import model.Schedule;
import model.Venue;
import model.Volunteer;

import java.util.ArrayList;
import java.util.List;

/**
 * Service providing in-memory sample data for the EventFlow application.
 */
public class DataService {
    private static DataService instance;

    private final List<Event> events;
    private final List<Participant> participants;
    private final List<Venue> venues;
    private final List<Activity> activities;
    private final List<Volunteer> volunteers;
    private final List<Registration> registrations;
    private final List<Schedule> schedules;

    private DataService() {
        events = new ArrayList<>();
        participants = new ArrayList<>();
        venues = new ArrayList<>();
        activities = new ArrayList<>();
        volunteers = new ArrayList<>();
        registrations = new ArrayList<>();
        schedules = new ArrayList<>();

        initSampleData();
    }

    public static synchronized DataService getInstance() {
        if (instance == null) {
            instance = new DataService();
        }
        return instance;
    }

    private void initSampleData() {
        // 1. Sample Venues (V-101 to V-115) covering all event hosting locations
        venues.add(new Venue("V-101", "Main Auditorium", "Central Block, Ground Floor", 1500, "Projector, Sound System, Stage, Air Conditioned", true));
        venues.add(new Venue("V-102", "Open Air Theatre", "Campus South Lawn", 2000, "Lighting, Sound System, Open Seating", true));
        venues.add(new Venue("V-103", "Indoor Sports Complex", "Sports Wing, Block C", 800, "Basketball Court, Badminton Courts, Bleachers", false));
        venues.add(new Venue("V-104", "Seminar Hall A", "Academic Block 1, Floor 2", 200, "Podium, Projector, Mic System, WiFi", true));
        venues.add(new Venue("V-105", "Lab 302", "IT Building, Floor 3", 100, "80 Workstations, LAN, Smart Board", true));
        venues.add(new Venue("V-106", "Innovation Hub", "R&D Complex, Floor 1", 250, "Modular Desks, Display Screens, 3D Printers", true));
        venues.add(new Venue("V-107", "Design Studio", "Arts & Architecture Wing", 60, "Drafting Tables, High-End Workstations", false));
        venues.add(new Venue("V-108", "University Ground", "Sports Complex Outdoor", 800, "Track, Floodlights, Spectator Stands", true));
        venues.add(new Venue("V-109", "Seminar Hall B", "Academic Block 2, Floor 1", 150, "Smart Screen, Audio Setup, WiFi", true));
        venues.add(new Venue("V-110", "Media Lab", "Arts Wing, Floor 1", 120, "Projection Screen, Audio Mixing Desk", true));
        venues.add(new Venue("V-111", "Lab 105", "IT Building, Floor 1", 70, "50 PCs, High-Speed Internet, Projector", true));
        venues.add(new Venue("V-112", "E-Sports Arena", "Student Activity Center, Floor 2", 250, "Gaming Rigs, High Refresh Displays, Stage", true));
        venues.add(new Venue("V-113", "Conference Hall A", "Admin Block, Floor 3", 180, "Conference Table, Delegation Audio, Screens", true));
        venues.add(new Venue("V-114", "Student Center", "Campus Core, Ground Floor", 60, "Tables, Boards, Lounge Seating", true));
        venues.add(new Venue("V-115", "Banquet Hall", "Guest House Complex", 200, "Dining Setup, Stage, PA System", true));

        // 2. 25 Sample Events (8 Active, 12 Upcoming, 5 Completed)
        events.add(new Event("EVT-101", "Hackathon 2026 - CodeFest", "Technology", "2026-10-01", "09:00 AM", "Main Auditorium", 300, 280, "Active", "Annual 24-hour coding challenge and TechFest hackathon for developers."));
        events.add(new Event("EVT-102", "AI & Deep Learning Workshop", "Academic", "2026-10-02", "10:00 AM", "Lab 302", 80, 78, "Active", "Hands-on workshop on neural networks, computer vision, and TechFest AI projects."));
        events.add(new Event("EVT-103", "Annual Cultural Fest - Rhythm 26", "Cultural", "2026-10-03", "05:00 PM", "Open Air Theatre", 1500, 1420, "Active", "Mega annual festival showcasing music, dance, and creative performing arts."));
        events.add(new Event("EVT-104", "Inter-College Robotics League", "Technology", "2026-10-04", "11:00 AM", "Indoor Sports Complex", 400, 310, "Active", "Robotics design competition and autonomous bot battles at TechFest arena."));
        events.add(new Event("EVT-105", "Cyber Security Symposium", "Technology", "2026-10-05", "09:30 AM", "Seminar Hall A", 200, 190, "Active", "Ethical hacking, threat analysis, and network defense technical symposium."));
        events.add(new Event("EVT-106", "Design Thinking & UI/UX Summit", "Workshop", "2026-10-06", "02:00 PM", "Design Studio", 60, 58, "Active", "Interactive workshop on user experience, prototyping, and design systems."));
        events.add(new Event("EVT-107", "Annual Athletics Championship", "Sports", "2026-10-07", "08:00 AM", "University Ground", 800, 750, "Active", "Track and field events including sprint, relay, long jump, and shot put."));
        events.add(new Event("EVT-108", "Startup & Innovation Expo", "Academic", "2026-10-08", "10:30 AM", "Innovation Hub", 250, 240, "Active", "Showcase of student startup ideas, tech prototypes, and venture pitches."));

        events.add(new Event("EVT-109", "Cloud Architecture Masterclass", "Technology", "2026-10-12", "10:00 AM", "Lab 302", 100, 85, "Upcoming", "Masterclass on microservices, AWS cloud infrastructure, and DevOps pipelines."));
        events.add(new Event("EVT-110", "Battle of the Bands", "Cultural", "2026-10-15", "06:00 PM", "Open Air Theatre", 1000, 620, "Upcoming", "Live music contest featuring rock, pop, and indie college bands."));
        events.add(new Event("EVT-111", "Inter-Department Basketball", "Sports", "2026-10-18", "03:00 PM", "Indoor Sports Complex", 300, 180, "Upcoming", "Knockout basketball tournament between university departments."));
        events.add(new Event("EVT-112", "Quantum Computing Seminar", "Academic", "2026-10-20", "11:00 AM", "Seminar Hall B", 150, 95, "Upcoming", "Guest lecture on quantum algorithms, qubits, and future computing paradigms."));
        events.add(new Event("EVT-113", "Data Science Career Fair", "Academic", "2026-10-22", "09:00 AM", "Main Auditorium", 600, 410, "Upcoming", "Networking event connecting analytics students with industry recruiters."));
        events.add(new Event("EVT-114", "Photography & Short Film Fest", "Cultural", "2026-10-25", "01:00 PM", "Media Lab", 120, 90, "Upcoming", "Exhibition of creative photography and screening of original short films."));
        events.add(new Event("EVT-115", "Web Development Bootcamp", "Workshop", "2026-10-28", "10:00 AM", "Lab 105", 70, 65, "Upcoming", "Full-stack web creation bootcamp covering HTML, CSS, JavaScript, and React."));
        events.add(new Event("EVT-116", "E-Sports Championship (Valorant)", "Gaming", "2026-11-01", "12:00 PM", "E-Sports Arena", 250, 230, "Upcoming", "Multi-player tactical shooter tournament for university gaming enthusiasts."));
        events.add(new Event("EVT-117", "Model United Nations (MUN)", "Academic", "2026-11-04", "09:00 AM", "Conference Hall A", 180, 140, "Upcoming", "Diplomatic simulation debate on international security and global policy."));
        events.add(new Event("EVT-118", "Chess & Strategy Tournament", "Sports", "2026-11-08", "10:00 AM", "Student Center", 60, 50, "Upcoming", "Rapid chess competition testing strategic thinking and tactical play."));
        events.add(new Event("EVT-119", "Environmental Sustainability Talk", "Academic", "2026-11-12", "02:30 PM", "Seminar Hall A", 200, 110, "Upcoming", "Seminar on renewable energy, carbon footprint reduction, and green tech."));
        events.add(new Event("EVT-120", "Drama Club Annual Play", "Cultural", "2026-11-15", "06:30 PM", "Main Auditorium", 500, 320, "Upcoming", "Theatrical production presented by the campus drama society."));

        events.add(new Event("EVT-121", "Freshers Welcome Party 2026", "Cultural", "2026-09-01", "05:00 PM", "Open Air Theatre", 1200, 1200, "Completed", "Welcome ceremony and evening celebration for incoming first-year students."));
        events.add(new Event("EVT-122", "Alumni Networking Dinner", "Academic", "2026-09-10", "07:00 PM", "Banquet Hall", 200, 195, "Completed", "Annual alumni gathering for mentorship and professional networking."));
        events.add(new Event("EVT-123", "Orientation & Campus Tour", "Academic", "2026-09-15", "09:00 AM", "Main Auditorium", 800, 780, "Completed", "Guided campus tour and academic orientation program for new batches."));
        events.add(new Event("EVT-124", "Inter-College Debate Cup", "Academic", "2026-09-20", "11:00 AM", "Seminar Hall A", 150, 145, "Completed", "Parliamentary style debate cup on technology ethics and society."));
        events.add(new Event("EVT-125", "Code Debugging Contest", "Technology", "2026-09-25", "02:00 PM", "Lab 302", 90, 88, "Completed", "Timed competition to identify and fix algorithmic bugs in complex code."));

        // 3. 20 Sample Participants (P-1001 to P-1020) with valid event affiliations
        participants.add(new Participant("P-1001", "Alice Vance", "alice.v@univ.edu", "Computer Science", "Hackathon 2026 - CodeFest", "2026-09-28", "Confirmed"));
        participants.add(new Participant("P-1002", "Bob Martinez", "bob.m@univ.edu", "Electrical Engg", "Hackathon 2026 - CodeFest", "2026-09-28", "Confirmed"));
        participants.add(new Participant("P-1003", "Charlie Davis", "charlie.d@univ.edu", "Mechanical Engg", "Annual Cultural Fest - Rhythm 26", "2026-09-29", "Confirmed"));
        participants.add(new Participant("P-1004", "Diana Prince", "diana.p@univ.edu", "Information Tech", "AI & Deep Learning Workshop", "2026-09-29", "Confirmed"));
        participants.add(new Participant("P-1005", "Ethan Hunt", "ethan.h@univ.edu", "Cyber Security", "Cyber Security Symposium", "2026-09-30", "Waitlisted"));
        participants.add(new Participant("P-1006", "Fiona Gallagher", "fiona.g@univ.edu", "Business Studies", "Startup & Innovation Expo", "2026-09-30", "Confirmed"));
        participants.add(new Participant("P-1007", "George Clark", "george.c@univ.edu", "Civil Engg", "Annual Athletics Championship", "2026-09-30", "Confirmed"));
        participants.add(new Participant("P-1008", "Hannah Abbott", "hannah.a@univ.edu", "Biotechnology", "AI & Deep Learning Workshop", "2026-10-01", "Confirmed"));
        participants.add(new Participant("P-1009", "Ian Wright", "ian.w@univ.edu", "Computer Science", "Cloud Architecture Masterclass", "2026-10-01", "Waitlisted"));
        participants.add(new Participant("P-1010", "Julia Roberts", "julia.r@univ.edu", "Media & Arts", "Design Thinking & UI/UX Summit", "2026-10-01", "Confirmed"));
        participants.add(new Participant("P-1011", "Kevin Owens", "kevin.o@univ.edu", "Computer Science", "Battle of the Bands", "2026-10-02", "Confirmed"));
        participants.add(new Participant("P-1012", "Laura Croft", "laura.c@univ.edu", "Sports & Physical Ed", "Inter-Department Basketball", "2026-10-02", "Confirmed"));
        participants.add(new Participant("P-1013", "Michael Chang", "michael.c@univ.edu", "Physics & Computing", "Quantum Computing Seminar", "2026-10-03", "Confirmed"));
        participants.add(new Participant("P-1014", "Nina Patel", "nina.p@univ.edu", "Data Science", "Data Science Career Fair", "2026-10-03", "Confirmed"));
        participants.add(new Participant("P-1015", "Oscar Wilde", "oscar.w@univ.edu", "Media & Arts", "Photography & Short Film Fest", "2026-10-04", "Confirmed"));
        participants.add(new Participant("P-1016", "Paula Deen", "paula.d@univ.edu", "Information Tech", "Web Development Bootcamp", "2026-10-04", "Confirmed"));
        participants.add(new Participant("P-1017", "Quinn Hughes", "quinn.h@univ.edu", "Computer Science", "E-Sports Championship (Valorant)", "2026-10-05", "Confirmed"));
        participants.add(new Participant("P-1018", "Rachel Adams", "rachel.a@univ.edu", "Political Science", "Model United Nations (MUN)", "2026-10-05", "Waitlisted"));
        participants.add(new Participant("P-1019", "Samir Khan", "samir.k@univ.edu", "Mathematics", "Chess & Strategy Tournament", "2026-10-06", "Confirmed"));
        participants.add(new Participant("P-1020", "Tina Fey", "tina.f@univ.edu", "Environmental Science", "Environmental Sustainability Talk", "2026-10-06", "Confirmed"));

        // 4. 30 Sample Registrations linking Participants to Events
        registrations.add(new Registration("REG-1001", "P-1001", "EVT-101", "2026-09-28", "Confirmed"));
        registrations.add(new Registration("REG-1002", "P-1002", "EVT-101", "2026-09-28", "Confirmed"));
        registrations.add(new Registration("REG-1003", "P-1003", "EVT-103", "2026-09-29", "Confirmed"));
        registrations.add(new Registration("REG-1004", "P-1004", "EVT-102", "2026-09-29", "Confirmed"));
        registrations.add(new Registration("REG-1005", "P-1005", "EVT-105", "2026-09-30", "Waitlisted"));
        registrations.add(new Registration("REG-1006", "P-1006", "EVT-108", "2026-09-30", "Confirmed"));
        registrations.add(new Registration("REG-1007", "P-1007", "EVT-107", "2026-09-30", "Confirmed"));
        registrations.add(new Registration("REG-1008", "P-1008", "EVT-102", "2026-10-01", "Confirmed"));
        registrations.add(new Registration("REG-1009", "P-1009", "EVT-109", "2026-10-01", "Waitlisted"));
        registrations.add(new Registration("REG-1010", "P-1010", "EVT-106", "2026-10-01", "Confirmed"));
        registrations.add(new Registration("REG-1011", "P-1011", "EVT-110", "2026-10-02", "Confirmed"));
        registrations.add(new Registration("REG-1012", "P-1012", "EVT-111", "2026-10-02", "Confirmed"));
        registrations.add(new Registration("REG-1013", "P-1013", "EVT-112", "2026-10-03", "Confirmed"));
        registrations.add(new Registration("REG-1014", "P-1014", "EVT-113", "2026-10-03", "Confirmed"));
        registrations.add(new Registration("REG-1015", "P-1015", "EVT-114", "2026-10-04", "Confirmed"));
        registrations.add(new Registration("REG-1016", "P-1016", "EVT-115", "2026-10-04", "Confirmed"));
        registrations.add(new Registration("REG-1017", "P-1017", "EVT-116", "2026-10-05", "Confirmed"));
        registrations.add(new Registration("REG-1018", "P-1018", "EVT-117", "2026-10-05", "Waitlisted"));
        registrations.add(new Registration("REG-1019", "P-1019", "EVT-118", "2026-10-06", "Confirmed"));
        registrations.add(new Registration("REG-1020", "P-1020", "EVT-119", "2026-10-06", "Confirmed"));
        registrations.add(new Registration("REG-1021", "P-1001", "EVT-102", "2026-09-29", "Confirmed"));
        registrations.add(new Registration("REG-1022", "P-1002", "EVT-104", "2026-09-30", "Confirmed"));
        registrations.add(new Registration("REG-1023", "P-1003", "EVT-110", "2026-10-01", "Confirmed"));
        registrations.add(new Registration("REG-1024", "P-1004", "EVT-101", "2026-09-30", "Confirmed"));
        registrations.add(new Registration("REG-1025", "P-1005", "EVT-101", "2026-10-01", "Confirmed"));
        registrations.add(new Registration("REG-1026", "P-1006", "EVT-106", "2026-10-01", "Confirmed"));
        registrations.add(new Registration("REG-1027", "P-1008", "EVT-108", "2026-10-02", "Confirmed"));
        registrations.add(new Registration("REG-1028", "P-1010", "EVT-114", "2026-10-02", "Confirmed"));
        registrations.add(new Registration("REG-1029", "P-1013", "EVT-101", "2026-10-03", "Confirmed"));
        registrations.add(new Registration("REG-1030", "P-1016", "EVT-102", "2026-10-04", "Confirmed"));

        // 5. Sample Activities (with Duration in Minutes and Priority Value)
        activities.add(new Activity("ACT-1", "EVT-101", "Keynote: Future of AI in Software", "09:30 AM", "10:30 AM", "Dr. Alan Turing", "Main Auditorium", 60, 95));
        activities.add(new Activity("ACT-2", "EVT-101", "Coding Phase 1: Problem Solving", "10:45 AM", "01:00 PM", "Mentors Team", "Main Auditorium", 135, 85));
        activities.add(new Activity("ACT-3", "EVT-101", "Final Project Demos & Jury Evaluation", "02:00 PM", "04:30 PM", "Industry Judges", "Main Auditorium", 150, 90));
        activities.add(new Activity("ACT-4", "EVT-102", "Deep Learning Hands-On Lab", "10:00 AM", "11:30 AM", "Prof. Andrew Ng", "Lab 302", 90, 80));
        activities.add(new Activity("ACT-5", "EVT-102", "Neural Network Architecture Workshop", "11:45 AM", "01:15 PM", "Dr. Yann LeCun", "Lab 302", 90, 88));
        activities.add(new Activity("ACT-6", "EVT-103", "Cultural Music & Band Performance", "05:00 PM", "06:30 PM", "Campus Band", "Open Air Theatre", 90, 75));
        activities.add(new Activity("ACT-7", "EVT-105", "Ethical Hacking Live Demo", "09:30 AM", "10:15 AM", "Security Team", "Seminar Hall A", 45, 70));
        activities.add(new Activity("ACT-8", "EVT-106", "UI/UX Interactive Design Sprint", "02:00 PM", "03:00 PM", "Lead Designer", "Design Studio", 60, 65));

        // 6. Sample Volunteers assigned to active events
        volunteers.add(new Volunteer("VOL-1", "Sarah Jenkins", "Registration Lead", "EVT-101", "+1 555-0143"));
        volunteers.add(new Volunteer("VOL-2", "Marcus Brody", "Technical Setup Coordinator", "EVT-101", "+1 555-0188"));
        volunteers.add(new Volunteer("VOL-3", "Elena Rostova", "Stage Manager", "EVT-103", "+1 555-0192"));
        volunteers.add(new Volunteer("VOL-4", "David Miller", "Hospitality & Logistics", "EVT-104", "+1 555-0176"));
        volunteers.add(new Volunteer("VOL-5", "Rachel Green", "Lab Technical Assistant", "EVT-102", "+1 555-0199"));
        volunteers.add(new Volunteer("VOL-6", "Alex Mercer", "Cyber Security Helper", "EVT-105", "+1 555-0211"));
        volunteers.add(new Volunteer("VOL-7", "Jessica Alba", "Design Studio Mentor", "EVT-106", "+1 555-0233"));
        volunteers.add(new Volunteer("VOL-8", "Chris Evans", "General Event Coordinator", "EVT-102", "+1 555-0255"));

        // 7. 12 Sample Schedules connecting Events/Activities to Venues
        schedules.add(new Schedule("SCH-101", "EVT-101", "ACT-1", "2026-10-01", "09:30 AM", "10:30 AM", "V-101"));
        schedules.add(new Schedule("SCH-102", "EVT-101", "ACT-2", "2026-10-01", "10:45 AM", "01:00 PM", "V-101"));
        schedules.add(new Schedule("SCH-103", "EVT-101", "ACT-3", "2026-10-01", "02:00 PM", "04:30 PM", "V-101"));
        schedules.add(new Schedule("SCH-104", "EVT-102", "ACT-4", "2026-10-02", "10:00 AM", "11:30 AM", "V-105"));
        schedules.add(new Schedule("SCH-105", "EVT-102", "ACT-5", "2026-10-02", "11:45 AM", "01:15 PM", "V-105"));
        schedules.add(new Schedule("SCH-106", "EVT-103", "ACT-6", "2026-10-03", "05:00 PM", "06:30 PM", "V-102"));
        schedules.add(new Schedule("SCH-107", "EVT-105", "ACT-7", "2026-10-05", "09:30 AM", "10:15 AM", "V-104"));
        schedules.add(new Schedule("SCH-108", "EVT-106", "ACT-8", "2026-10-06", "02:00 PM", "03:00 PM", "V-107"));
        schedules.add(new Schedule("SCH-109", "EVT-104", "", "2026-10-04", "11:00 AM", "04:00 PM", "V-103"));
        schedules.add(new Schedule("SCH-110", "EVT-107", "", "2026-10-07", "08:00 AM", "05:00 PM", "V-108"));
        schedules.add(new Schedule("SCH-111", "EVT-108", "", "2026-10-08", "10:30 AM", "04:00 PM", "V-106"));
        schedules.add(new Schedule("SCH-112", "EVT-109", "", "2026-10-12", "10:00 AM", "02:00 PM", "V-105"));
    }

    public List<Event> getEvents() { return events; }
    public List<Participant> getParticipants() { return participants; }
    public List<Venue> getVenues() { return venues; }
    public List<Activity> getActivities() { return activities; }
    public List<Volunteer> getVolunteers() { return volunteers; }
    public List<Registration> getRegistrations() { return registrations; }
    public List<Schedule> getSchedules() { return schedules; }

    // Helper lookup methods
    public Event getEventById(String id) {
        if (id == null) return null;
        for (Event e : events) {
            if (e.getId().equalsIgnoreCase(id)) return e;
        }
        return null;
    }

    public Participant getParticipantById(String id) {
        if (id == null) return null;
        for (Participant p : participants) {
            if (p.getId().equalsIgnoreCase(id)) return p;
        }
        return null;
    }

    public Venue getVenueById(String id) {
        if (id == null) return null;
        for (Venue v : venues) {
            if (v.getId().equalsIgnoreCase(id)) return v;
        }
        return null;
    }

    public Activity getActivityById(String id) {
        if (id == null) return null;
        for (Activity a : activities) {
            if (a.getId().equalsIgnoreCase(id)) return a;
        }
        return null;
    }

    public List<Registration> getRegistrationsByParticipantId(String participantId) {
        List<Registration> list = new ArrayList<>();
        if (participantId == null) return list;
        for (Registration r : registrations) {
            if (r.getParticipantId().equalsIgnoreCase(participantId)) list.add(r);
        }
        return list;
    }

    public List<Registration> getRegistrationsByEventId(String eventId) {
        List<Registration> list = new ArrayList<>();
        if (eventId == null) return list;
        for (Registration r : registrations) {
            if (r.getEventId().equalsIgnoreCase(eventId)) list.add(r);
        }
        return list;
    }

    public List<Schedule> getSchedulesByEventId(String eventId) {
        List<Schedule> list = new ArrayList<>();
        if (eventId == null) return list;
        for (Schedule s : schedules) {
            if (s.getEventId().equalsIgnoreCase(eventId)) list.add(s);
        }
        return list;
    }

    public List<Schedule> getSchedulesByVenueId(String venueId) {
        List<Schedule> list = new ArrayList<>();
        if (venueId == null) return list;
        for (Schedule s : schedules) {
            if (s.getVenueId().equalsIgnoreCase(venueId)) list.add(s);
        }
        return list;
    }

    // Dashboard Statistics dynamically calculated from actual in-memory data
    public int getTotalEventsCount() {
        return events.size();
    }

    public int getParticipantsCount() {
        return participants.size();
    }

    public int getRegistrationsCount() {
        return registrations.size();
    }

    public int getActiveEventsCount() {
        int count = 0;
        for (Event event : events) {
            if ("Active".equalsIgnoreCase(event.getStatus())) {
                count++;
            }
        }
        return count;
    }
}
