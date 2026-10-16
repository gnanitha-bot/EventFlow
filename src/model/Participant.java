package model;

/**
 * Participant model representing an event registrant.
 */
public class Participant {
    private String id;
    private String name;
    private String email;
    private String department;
    private String registeredEvent;
    private String registrationDate;
    private String status; // "Confirmed", "Waitlisted", "Attended"

    public Participant(String id, String name, String email, String department, String registeredEvent, String registrationDate, String status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.department = department;
        this.registeredEvent = registeredEvent;
        this.registrationDate = registrationDate;
        this.status = status;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getRegisteredEvent() { return registeredEvent; }
    public void setRegisteredEvent(String registeredEvent) { this.registeredEvent = registeredEvent; }

    public String getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(String registrationDate) { this.registrationDate = registrationDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
