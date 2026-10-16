package model;

/**
 * Venue model representing an event hosting facility.
 */
public class Venue {
    private String id;
    private String name;
    private String location;
    private int capacity;
    private String facilities;
    private boolean available;

    public Venue(String id, String name, String location, int capacity, String facilities, boolean available) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.facilities = facilities;
        this.available = available;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public String getFacilities() { return facilities; }
    public void setFacilities(String facilities) { this.facilities = facilities; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}
