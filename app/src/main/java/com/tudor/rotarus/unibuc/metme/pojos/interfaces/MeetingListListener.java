package com.tudor.rotarus.unibuc.metme.pojos.interfaces;

import com.tudor.rotarus.unibuc.metme.pojos.requests.get.MeetingsListGetBody;

/**
 * Created by Tudor on 30.03.2016.
 */
public interface MeetingListListener {
    void onListAllMeetingsSuccess(MeetingsListGetBody response, final int callType);
    void onListAllMeetingsFailed(final int callType);
}
