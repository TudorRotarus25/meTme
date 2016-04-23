package com.tudor.rotarus.unibuc.metme.pojos.interfaces.network;

import com.tudor.rotarus.unibuc.metme.pojos.responses.post.FriendsPostBody;

/**
 * Created by Tudor on 21.04.2016.
 */
public interface FriendsListListener {
    void onFriendsListSuccess(FriendsPostBody response);
    void onFriendsListFailed();
}
