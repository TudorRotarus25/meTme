package com.tudor.rotarus.unibuc.metme.pojos.post;

/**
 * Created by Tudor on 15.03.2016.
 */
public class ActivateUserPostBody {
    int id;
    String token;

    public ActivateUserPostBody(int id, String token) {
        this.id = id;
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
