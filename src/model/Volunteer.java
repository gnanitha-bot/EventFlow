package model;

/**
 * Volunteer model representing event helpers and coordinators.
 */
public class Volunteer {
    private String id;
    private String name;
    private String role;
    private String assignedEvent;
    private String contact;

    public Volunteer(String id, String name, String role, String assignedEvent, String contact) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.assignedEvent = assignedEvent;
        this.contact = contact;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getAssignedEvent() { return assignedEvent; }
    public void setAssignedEvent(String assignedEvent) { this.assignedEvent = assignedEvent; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
}
