package model;

/**
 * Activity model representing a session or sub-event within a larger event schedule.
 */
public class Activity {
    private String id;
    private String eventId;
    private String title;
    private String startTime;
    private String endTime;
    private String speaker;
    private String room;
    private int durationMinutes;
    private int value;

    public Activity(String id, String eventId, String title, String startTime, String endTime, String speaker, String room) {
        this(id, eventId, title, startTime, endTime, speaker, room, 60, 50);
    }

    public Activity(String id, String eventId, String title, String startTime, String endTime, String speaker, String room, int durationMinutes, int value) {
        this.id = id;
        this.eventId = eventId;
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.speaker = speaker;
        this.room = room;
        this.durationMinutes = durationMinutes;
        this.value = value;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }

    public String getSpeaker() { return speaker; }
    public void setSpeaker(String speaker) { this.speaker = speaker; }

    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }

    public int getDurationMinutes() { return durationMinutes > 0 ? durationMinutes : 60; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    public int getValue() { return value > 0 ? value : 50; }
    public void setValue(int value) { this.value = value; }
}
