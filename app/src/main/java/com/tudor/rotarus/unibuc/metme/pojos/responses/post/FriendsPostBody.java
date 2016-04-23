package com.tudor.rotarus.unibuc.metme.pojos.responses.post;

import java.util.ArrayList;

/**
 * Created by Tudor on 21.04.2016.
 */
public class FriendsPostBody {

    ArrayList<Friend> friends;

    public FriendsPostBody(ArrayList<Friend> friends) {
        this.friends = friends;
    }

    public FriendsPostBody() {
        this.friends = new ArrayList<>();
    }

    public ArrayList<Friend> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<Friend> friends) {
        this.friends = friends;
    }

    public class Friend {

        int id;
        String name;
        String initials;
        String phoneNumber;

        public Friend() {
        }

        public Friend(int id, String name, String initials, String phoneNumber) {
            this.id = id;
            this.name = name;
            this.initials = initials;
            this.phoneNumber = phoneNumber;
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
