package com.tudor.rotarus.unibuc.metme.pojos.interfaces.network;

import com.tudor.rotarus.unibuc.metme.pojos.responses.get.UserGetBody;

/**
 * Created by Tudor on 09.06.2016.
 */
public interface GetProfileListener {
    void onGetProfileSuccess(UserGetBody response);
    void onGetProfileFailed();
}
