package com.tudor.rotarus.unibuc.metme.pojos.interfaces;

import com.tudor.rotarus.unibuc.metme.pojos.requests.post.ActivateUserPostBody;

/**
 * Created by Tudor on 30.03.2016.
 */
public interface ActivateUserListener {
    void onActivateUserSuccess(ActivateUserPostBody response);
    void onActivateUserFailed();
}
