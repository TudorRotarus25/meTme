package com.tudor.rotarus.unibuc.metme.utils;

import com.tudor.rotarus.unibuc.metme.pojos.responses.post.FriendsPostBody;

import java.util.Comparator;

/**
 * Created by Tudor on 06.04.2016.
 */
public class FriendsComparator implements Comparator<FriendsPostBody.Friend> {

    @Override
    public int compare(FriendsPostBody.Friend lhs, FriendsPostBody.Friend rhs) {
        return lhs.getName().compareTo(rhs.getName());
    }

}
