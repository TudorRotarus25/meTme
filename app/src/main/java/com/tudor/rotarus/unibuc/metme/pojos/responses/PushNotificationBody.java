package com.tudor.rotarus.unibuc.metme.pojos.responses;

/**
 * Created by Tudor on 24.04.2016.
 */
public class PushNotificationBody {

    private int notificationId;
    private int type;
    private String title;
    private String body;
    private int meetingId;

    public PushNotificationBody() {
    }

    public PushNotificationBody(int notificationId, int type, String title, String body, int taskId) {
        this.notificationId = notificationId;
        this.type = type;
        this.title = title;
        this.body = body;
        this.meetingId = taskId;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(int meetingId) {
        this.meetingId = meetingId;
    }
}
