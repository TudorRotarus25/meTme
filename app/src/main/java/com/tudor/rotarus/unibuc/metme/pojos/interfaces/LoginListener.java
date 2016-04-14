package com.tudor.rotarus.unibuc.metme.pojos.interfaces;

import com.tudor.rotarus.unibuc.metme.pojos.requests.post.CreateUserPostBody;

/**
 * Created by Tudor on 30.03.2016.
 */
public interface LoginListener {
    void onLoginSuccess(CreateUserPostBody response);
    void onLoginFailed();
}
