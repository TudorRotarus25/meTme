package com.tudor.rotarus.unibuc.metme.fragments.drawer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tudor.rotarus.unibuc.metme.MyApplication;
import com.tudor.rotarus.unibuc.metme.R;
import com.tudor.rotarus.unibuc.metme.activities.login.LoginNameActivity;
import com.tudor.rotarus.unibuc.metme.pojos.get.TestGetBody;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class HomeFragment extends Fragment {

    private static TextView meetingNameTextView;
    private static TextView meetingTimeTextView;
    private static TextView meetingPlaceTextView;
    private static TextView meetingDepartureTimeTextView;

    private MyApplication app;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initLayout(view);

        return view;
    }

    private void initLayout(View view) {
        meetingNameTextView = (TextView) view.findViewById(R.id.content_main_textView_meeting_name);
        meetingTimeTextView = (TextView) view.findViewById(R.id.content_main_textView_meeting_time);
        meetingPlaceTextView = (TextView) view.findViewById(R.id.content_main_textView_meeting_place);
        meetingDepartureTimeTextView = (TextView) view.findViewById(R.id.content_main_textView_meeting_departure_time);

        app = (MyApplication) getActivity().getApplication();
    }

}
