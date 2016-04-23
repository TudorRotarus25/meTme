package com.tudor.rotarus.unibuc.metme.pojos.interfaces.network;

import com.tudor.rotarus.unibuc.metme.pojos.responses.post.ActivateUserPostBody;

/**
 * Created by Tudor on 30.03.2016.
 */
public interface ActivateUserListener {
    void onActivateUserSuccess(ActivateUserPostBody response);
    void onActivateUserFailed();
}
