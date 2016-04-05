package com.tudor.rotarus.unibuc.metme.pojos;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Tudor on 04.04.2016.
 */
public class FriendsBody {
    private String contactId;
    private String name;
    private ArrayList<String> phoneNumbers;

    public FriendsBody(String contactId, String name) {
        this.contactId = contactId;
        this.name = name;
        this.phoneNumbers = new ArrayList<>();
    }

    public FriendsBody(String contactId, String name, ArrayList<String> phoneNumbers) {
        this.contactId = contactId;
        this.name = name;
        this.phoneNumbers = phoneNumbers;
    }

    public void addPhoneNumber(String phoneNumber) {
        phoneNumbers.add(phoneNumber);
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(ArrayList<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }
}
