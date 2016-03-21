package com.tudor.rotarus.unibuc.metme.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.tudor.rotarus.unibuc.metme.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddMeetingActivity extends AppCompatActivity implements OnConnectionFailedListener {

    private static final String TAG = "AddMeetingActivity";
    private static final int PLACE_PICKER_REQUEST = 1;

    private EditText nameEditText;
    private TextView placeText;
    private EditText placeAliasEditText;
    private TextView timeStartDateText;
    private TextView timeEndDateText;
    private TextView timeStartTimeText;
    private TextView timeEndTimeText;
    private TextView notificationText;
    private TextView addMembersText;

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private TimePickerDialog fromTimePickerDialog;
    private TimePickerDialog toTimePickerDialog;

    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meeting);

        initLayout();
    }

    private void initLayout() {
        nameEditText = (EditText) findViewById(R.id.activity_add_meeting_title_editText);
        placeText = (TextView) findViewById(R.id.activity_add_meeting_place_text);
        placeAliasEditText = (EditText) findViewById(R.id.activity_add_meeting_place_alias_editText);
        timeStartDateText = (TextView) findViewById(R.id.activity_add_meeting_start_date_text);
        timeEndDateText = (TextView) findViewById(R.id.activity_add_meeting_end_date_text);
        timeStartTimeText = (TextView) findViewById(R.id.activity_add_meeting_start_time_text);
        timeEndTimeText = (TextView) findViewById(R.id.activity_add_meeting_end_time_text);
        notificationText = (TextView) findViewById(R.id.activity_add_meeting_notification_text);
        addMembersText = (TextView) findViewById(R.id.activity_add_meeting_add_people_text);

        initPickers();
        initFields();
        initOnClickListeners();
    }

    private void initOnClickListeners() {
        placeText.setOnClickListener(new View.OnClickListener() {
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

        timeStartDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDatePickerDialog.show();
            }
        });

        timeEndDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDatePickerDialog.show();
            }
        });

        timeStartTimeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromTimePickerDialog.show();
            }
        });

        timeEndTimeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toTimePickerDialog.show();
            }
        });
    }

    private void initFields() {

        timeStartDateText.setText(dateFormatter.format(Calendar.getInstance().getTime()));
        timeEndDateText.setText(dateFormatter.format(Calendar.getInstance().getTime()));
        timeStartTimeText.setText(timeFormatter.format(Calendar.getInstance().getTime()));

        Calendar toTime = Calendar.getInstance();
        toTime.add(Calendar.HOUR_OF_DAY, 1);

        timeEndTimeText.setText(timeFormatter.format(toTime.getTime()));
    }

    private void initPickers() {

        dateFormatter = new SimpleDateFormat("EEE, dd MMM yyyy", Locale.US);
        timeFormatter = new SimpleDateFormat("H:mm", Locale.US);

        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Log.i(TAG, year + " " + monthOfYear + " " + dayOfMonth);

            }
        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Log.i(TAG, year + " " + monthOfYear + " " + dayOfMonth);

            }
        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        fromTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Log.i(TAG, hourOfDay + ":" + minute);
            }
        }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), true);

        toTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Log.i(TAG, hourOfDay + ":" + minute);
            }
        }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), true);
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
