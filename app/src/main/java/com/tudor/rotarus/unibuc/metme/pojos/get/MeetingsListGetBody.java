package com.tudor.rotarus.unibuc.metme.pojos.get;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Tudor on 27.03.2016.
 */
public class MeetingsListGetBody {

    ArrayList<Meeting> meetings;

    public MeetingsListGetBody(ArrayList<Meeting> meetings) {
        this.meetings = meetings;
    }

    public ArrayList<Meeting> getMeetings() {
        return meetings;
    }

    public void setMeetings(ArrayList<Meeting> meetings) {
        this.meetings = meetings;
    }

    public Meeting getMeeting(int index) {
        return meetings.get(index);
    }

    public void setMeeting(int index, Meeting meeting) {
        meetings.set(index, meeting);
    }

    public class Meeting {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd H:mm", Locale.US);

        int id;
        String name;
        String fromTime;
        String toTime;
        String placeName;
        int transportationMethod;
        int type;

        public Meeting(int id, String name, String fromTime, String toTime, String placeName, int transportationMethod, int type) {
            this.id = id;
            this.name = name;
            this.fromTime = fromTime;
            this.toTime = toTime;
            this.placeName = placeName;
            this.transportationMethod = transportationMethod;
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFromTime() {
            return fromTime;
        }

        public void setFromTime(String fromTime) {
            this.fromTime = fromTime;
        }

        public String getToTime() {
            return toTime;
        }

        public void setToTime(String toTime) {
            this.toTime = toTime;
        }

        public String getPlaceName() {
            return placeName;
        }

        public void setPlaceName(String placeName) {
            this.placeName = placeName;
        }

        public int getTransportationMethod() {
            return transportationMethod;
        }

        public void setTransportationMethod(int transportationMethod) {
            this.transportationMethod = transportationMethod;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
