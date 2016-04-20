package com.tudor.rotarus.unibuc.metme.pojos.requests.get;

import java.util.ArrayList;

/**
 * Created by Tudor on 11.04.2016.
 */
public class MeetingGetBody {

    int id;
    String name;
    String fromTime;
    String toTime;
    String placeName;
    int transportationMethod;
    String eta;
    ArrayList<Participant> participants;

    public MeetingGetBody(int id, String name, String fromTime, String toTime, String placeName, int transportationMethod, String eta, ArrayList<Participant> participants) {
        this.id = id;
        this.name = name;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.placeName = placeName;
        this.transportationMethod = transportationMethod;
        this.eta = eta;
        this.participants = participants;
    }

    public MeetingGetBody() {
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

    public String getEta() {
        return eta;
    }

    public void setEta(String eta) {
        this.eta = eta;
    }

    public ArrayList<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<Participant> participants) {
        this.participants = participants;
    }

    public class Participant {
        int id;
        String name;
        int eta;
        String initials;
        String phoneNumber;

        public Participant(int id, String name, int eta) {
            this.id = id;
            this.name = name;
            this.eta = eta;
        }

        public Participant() {
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

        public int getEta() {
            return eta;
        }

        public void setEta(int eta) {
            this.eta = eta;
        }
    }

}
