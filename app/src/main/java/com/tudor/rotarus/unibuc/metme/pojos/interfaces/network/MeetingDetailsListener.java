package com.tudor.rotarus.unibuc.metme.pojos.interfaces.network;

import com.tudor.rotarus.unibuc.metme.pojos.responses.get.MeetingGetBody;

/**
 * Created by Tudor on 14.05.2016.
 */
public interface MeetingDetailsListener {
    void onFetchMeetingDetailsSuccess(MeetingGetBody response);
    void onFetchMeetingDetailsEmptyResponse();
    void onFetchMeetingDetailsFailed();
}
