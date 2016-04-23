package com.tudor.rotarus.unibuc.metme.pojos.interfaces.network;

import com.tudor.rotarus.unibuc.metme.pojos.responses.post.CreateUserPostBody;

/**
 * Created by Tudor on 30.03.2016.
 */
public interface LoginListener {
    void onLoginSuccess(CreateUserPostBody response);
    void onLoginFailed();
}
