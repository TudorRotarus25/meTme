package com.tudor.rotarus.unibuc.metme.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tudor.rotarus.unibuc.metme.R;

public class MeetingDetailsActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    public static final String BUNDLE_MEETING_DETAILS_ID = "BUNDLE_MEETING_DETAILS_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_details);
    }
}
