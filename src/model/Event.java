package model;

/**
 * Event model representing a college event in the EventFlow platform.
 */
public class Event {
    private String id;
    private String title;
    private String category;
    private String date;
    private String time;
    private String venue;
    private int capacity;
    private int registeredCount;
    private String status; // "Active", "Upcoming", "Completed"
    private String description;

    public Event(String id, String title, String category, String date, String time, String venue, int capacity, int registeredCount, String status) {
        this(id, title, category, date, time, venue, capacity, registeredCount, status, title + " - " + category + " event at " + venue);
    }

    public Event(String id, String title, String category, String date, String time, String venue, int capacity, int registeredCount, String status, String description) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.date = date;
        this.time = time;
        this.venue = venue;
        this.capacity = capacity;
        this.registeredCount = registeredCount;
        this.status = status;
        this.description = description;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public int getRegisteredCount() { return registeredCount; }
    public void setRegisteredCount(int registeredCount) { this.registeredCount = registeredCount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDescription() { return description != null ? description : title; }
    public void setDescription(String description) { this.description = description; }
}
