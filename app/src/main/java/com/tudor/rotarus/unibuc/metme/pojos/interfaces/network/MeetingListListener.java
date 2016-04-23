package com.tudor.rotarus.unibuc.metme.pojos.interfaces.network;

import com.tudor.rotarus.unibuc.metme.pojos.responses.get.MeetingsListGetBody;

/**
 * Created by Tudor on 30.03.2016.
 */
public interface MeetingListListener {
    void onListAllMeetingsSuccess(MeetingsListGetBody response);
    void onListAllMeetingsFailed();
}
