package com.tudor.rotarus.unibuc.metme.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.tudor.rotarus.unibuc.metme.R;
import com.tudor.rotarus.unibuc.metme.activities.login.LoginNameActivity;
import com.tudor.rotarus.unibuc.metme.managers.NetworkManager;
import com.tudor.rotarus.unibuc.metme.managers.MySharedPreferencesManager;
import com.tudor.rotarus.unibuc.metme.pojos.interfaces.network.MeetingListListener;
import com.tudor.rotarus.unibuc.metme.pojos.responses.get.MeetingsListGetBody;

import java.util.ArrayList;

public class CalendarFragment extends Fragment implements MeetingListListener {

    private CalendarView calendarView;

    private MySharedPreferencesManager sharedPreferencesManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_calendar, container, false);

        initLayout(view);

        return view;
    }

    private void initLayout(View view) {

        sharedPreferencesManager = MySharedPreferencesManager.getInstance();

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Calendar");

        calendarView = (CalendarView) view.findViewById(R.id.fragment_calendar_calendar);

        getActivity().findViewById(R.id.fab).setVisibility(View.VISIBLE);

        int id = sharedPreferencesManager.readId(getContext());

        if(id < 0) {
            Intent intent = new Intent(getActivity(), LoginNameActivity.class);
            startActivity(intent);
            getActivity().finish();
        }

        NetworkManager networkManager = NetworkManager.getInstance();
        networkManager.listAllMeetings(id, this);

    }

    @Override
    public void onListAllMeetingsSuccess(MeetingsListGetBody response) {
        ArrayList<MeetingsListGetBody.Meeting> meetings = response.getMeetings();

        for (int i=0;i<meetings.size();i++) {
//            calendarView.addEv
        }
    }

    @Override
    public void onListAllMeetingsFailed() {

    }
}
