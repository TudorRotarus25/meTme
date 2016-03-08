package com.tudor.rotarus.unibuc.metme.drawerFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tudor.rotarus.unibuc.metme.LoginActivity;
import com.tudor.rotarus.unibuc.metme.MyApplication;
import com.tudor.rotarus.unibuc.metme.R;
import com.tudor.rotarus.unibuc.metme.pojos.TestGetBody;

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

        Call<TestGetBody> testCall = app.getRestClient().getApiService().TEST_GET_BODY_CALL();
        testCall.enqueue(new Callback<TestGetBody>() {
            @Override
            public void onResponse(Response<TestGetBody> response, Retrofit retrofit) {
                if (response.body() != null) {
                    meetingNameTextView.setText(response.body().getOutput());
                } else {
                    Log.e("My parsing error", "parsing error!");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("Call error", t.getMessage());
            }
        });

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
