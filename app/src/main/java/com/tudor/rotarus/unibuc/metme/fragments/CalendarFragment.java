package com.tudor.rotarus.unibuc.metme.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.tudor.rotarus.unibuc.metme.MyApplication;
import com.tudor.rotarus.unibuc.metme.R;
import com.tudor.rotarus.unibuc.metme.managers.NetworkManager;
import com.tudor.rotarus.unibuc.metme.pojos.interfaces.MeetingListListener;
import com.tudor.rotarus.unibuc.metme.pojos.requests.get.MeetingsListGetBody;

import java.util.ArrayList;

public class CalendarFragment extends Fragment implements MeetingListListener {

    private CalendarView calendarView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_calendar, container, false);

        init(view);

        return view;
    }

    private void init(View view) {

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Calendar");

        calendarView = (CalendarView) view.findViewById(R.id.fragment_calendar_calendar);

        NetworkManager networkManager = NetworkManager.getInstance();
        networkManager.listAllMeetings(getPhoneNumber(), 0, this);

    }

    private String getPhoneNumber() {
        SharedPreferences sp = getActivity().getSharedPreferences(MyApplication.METME_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return sp.getString("phone_number", "");
    }

    @Override
    public void onListAllMeetingsSuccess(MeetingsListGetBody response, int callType) {
        ArrayList<MeetingsListGetBody.Meeting> meetings = response.getMeetings();

        for (int i=0;i<meetings.size();i++) {
//            calendarView.addEv
        }
    }

    @Override
    public void onListAllMeetingsFailed(int callType) {

    }
}
