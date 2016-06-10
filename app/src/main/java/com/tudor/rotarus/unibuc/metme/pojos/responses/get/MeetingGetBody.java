package com.tudor.rotarus.unibuc.metme.pojos.responses.get;

import java.util.ArrayList;

/**
 * Created by Tudor on 11.04.2016.
 */
public class MeetingGetBody {

    public static final int TRANS_CAR = 0;
    public static final int TRANS_PUBLIC = 1;
    public static final int TRANS_WALK = 2;

    int id;
    String name;
    String fromTime;
    String toTime;
    String placeName;
    String placeAddress;
    int transportationMethod;
    Integer eta;
    ArrayList<Participant> participants;

    public MeetingGetBody(int id, String name, String fromTime, String toTime, String placeName, String placeAddress, int transportationMethod, Integer eta, ArrayList<Participant> participants) {
        this.id = id;
        this.name = name;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.placeName = placeName;
        this.placeAddress = placeAddress;
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

    public String getPlaceAddress() {
        return placeAddress;
    }

    public void setPlaceAddress(String placeAddress) {
        this.placeAddress = placeAddress;
    }

    public int getTransportationMethod() {
        return transportationMethod;
    }

    public void setTransportationMethod(int transportationMethod) {
        this.transportationMethod = transportationMethod;
    }

    public Integer getEta() {
        return eta;
    }

    public void setEta(Integer eta) {
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
        Integer eta;
        String initials;
        String phoneNumber;

        public Participant(int id, String name, Integer eta) {
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

        public Integer getEta() {
            return eta;
        }

        public void setEta(Integer eta) {
            this.eta = eta;
        }

        public String getInitials() {
            return initials;
        }

        public void setInitials(String initials) {
            this.initials = initials;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
    }

}
