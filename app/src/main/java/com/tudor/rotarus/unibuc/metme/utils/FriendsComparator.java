package com.tudor.rotarus.unibuc.metme.utils;

import com.tudor.rotarus.unibuc.metme.pojos.FriendsBody;

import java.util.Comparator;

/**
 * Created by Tudor on 06.04.2016.
 */
public class FriendsComparator implements Comparator<FriendsBody> {

    @Override
    public int compare(FriendsBody lhs, FriendsBody rhs) {
        return lhs.getName().compareTo(rhs.getName());
    }

}
