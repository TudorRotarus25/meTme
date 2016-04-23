package com.tudor.rotarus.unibuc.metme.pojos.requests;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Tudor on 04.04.2016.
 */
public class FriendsBody {

    @SerializedName("contacts")
    private ArrayList<FriendBody> contacts;

    public FriendsBody(ArrayList<FriendBody> contacts) {
        this.contacts = contacts;
    }

    public FriendsBody() {
        this.contacts = new ArrayList<>();
    }

    public ArrayList<FriendBody> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<FriendBody> contacts) {
        this.contacts = contacts;
    }

    public class FriendBody {

        @SerializedName("contact_id")
        private String contactId;

        @SerializedName("name")
        private String name;

        @SerializedName("phone_numbers")
        private ArrayList<String> phoneNumbers;

        public FriendBody(String contactId, String name) {
            this.contactId = contactId;
            this.name = name;
            this.phoneNumbers = new ArrayList<>();
        }

        public FriendBody(String contactId, String name, ArrayList<String> phoneNumbers) {
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
}


