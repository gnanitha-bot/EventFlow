package model;

/**
 * Schedule model representing an event or activity schedule.
 */
public class Schedule {
    private String id;
    private String eventId;
    private String activityId;
    private String date;
    private String startTime;
    private String endTime;
    private String venueId;

    public Schedule(String id, String eventId, String activityId, String date, String startTime, String endTime, String venueId) {
        this.id = id;
        this.eventId = eventId;
        this.activityId = activityId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.venueId = venueId;
    }

    public Schedule(String id, String activityId, String date, String startTime, String endTime, String venueId) {
        this(id, "", activityId, date, startTime, endTime, venueId);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getScheduleId() { return id; }
    public void setScheduleId(String id) { this.id = id; }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public String getActivityId() { return activityId; }
    public void setActivityId(String activityId) { this.activityId = activityId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }

    public String getVenueId() { return venueId; }
    public void setVenueId(String venueId) { this.venueId = venueId; }

    @Override
    public String toString() {
        return "Schedule{" +
                "id='" + id + '\'' +
                ", eventId='" + eventId + '\'' +
                ", activityId='" + activityId + '\'' +
                ", date='" + date + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", venueId='" + venueId + '\'' +
                '}';
    }
}
