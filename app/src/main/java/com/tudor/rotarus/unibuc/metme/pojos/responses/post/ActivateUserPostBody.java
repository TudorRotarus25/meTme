package com.tudor.rotarus.unibuc.metme.pojos.responses.post;

/**
 * Created by Tudor on 15.03.2016.
 */
public class ActivateUserPostBody {
    int id;
    String firstName;
    String lastName;
    String phoneNumber;
    String token;

    public ActivateUserPostBody(int id, String firstName, String lastName, String phoneNumber, String token) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
