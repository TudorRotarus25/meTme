package com.tudor.rotarus.unibuc.metme.activities;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.tudor.rotarus.unibuc.metme.R;

public class AddMeetingActivity extends AppCompatActivity implements OnConnectionFailedListener {

    private static final String TAG = "AddMeetingActivity";
    private static final int PLACE_PICKER_REQUEST = 1;

    LinearLayout placeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meeting);

        initLayout();
    }

    private void initLayout() {

        placeContainer = (LinearLayout) findViewById(R.id.activity_add_meeting_place_container);
        placeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                    Intent intent = intentBuilder.build(AddMeetingActivity.this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

//        placePickerButton = (ImageButton) findViewById(R.id.activity_add_meeting_place_picker_button);
//        placePickerButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
//                    Intent intent = intentBuilder.build(AddMeetingActivity.this);
//                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
//
//                } catch (GooglePlayServicesRepairableException e) {
//                    e.printStackTrace();
//                } catch (GooglePlayServicesNotAvailableException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        timePickerButton = (ImageButton) findViewById(R.id.activity_add_meeting_time_picker_button);
//        timePickerButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                TimePickerDialog timePickerDialog = new TimePickerDialog(AddMeetingActivity.this,);
//            }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PLACE_PICKER_REQUEST && resultCode == Activity.RESULT_OK) {

            final Place place = PlacePicker.getPlace(this, data);
            final String name = place.getName().toString();
            final String address = place.getAddress().toString();
            Log.i(TAG, name + " - " + address);

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
