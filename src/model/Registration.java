package model;

/**
 * Registration model representing a participant's registration for an event.
 */
public class Registration {
    private String id;
    private String participantId;
    private String eventId;
    private String registrationDate;
    private String status; // "Confirmed", "Waitlisted", "Attended", "Cancelled"

    public Registration(String id, String participantId, String eventId, String registrationDate, String status) {
        this.id = id;
        this.participantId = participantId;
        this.eventId = eventId;
        this.registrationDate = registrationDate;
        this.status = status;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getRegistrationId() { return id; }
    public void setRegistrationId(String id) { this.id = id; }

    public String getParticipantId() { return participantId; }
    public void setParticipantId(String participantId) { this.participantId = participantId; }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public String getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(String registrationDate) { this.registrationDate = registrationDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Registration{" +
                "id='" + id + '\'' +
                ", participantId='" + participantId + '\'' +
                ", eventId='" + eventId + '\'' +
                ", registrationDate='" + registrationDate + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
