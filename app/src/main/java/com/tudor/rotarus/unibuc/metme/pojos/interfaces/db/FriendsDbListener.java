package com.tudor.rotarus.unibuc.metme.pojos.interfaces.db;

import com.tudor.rotarus.unibuc.metme.pojos.responses.post.FriendsPostBody;

/**
 * Created by Tudor on 21.04.2016.
 */
public interface FriendsDbListener {
    void onFriendsDbGetSuccess(FriendsPostBody response);
    void onFriendsDbGetFailed();
}
