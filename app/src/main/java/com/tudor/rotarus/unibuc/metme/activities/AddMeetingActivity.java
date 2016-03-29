package com.tudor.rotarus.unibuc.metme.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.tudor.rotarus.unibuc.metme.MyApplication;
import com.tudor.rotarus.unibuc.metme.R;
import com.tudor.rotarus.unibuc.metme.activities.login.LoginNameActivity;
import com.tudor.rotarus.unibuc.metme.views.dialogs.AddMeetingNotificationDialog;
import com.tudor.rotarus.unibuc.metme.views.dialogs.AddMeetingTransportDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class AddMeetingActivity extends AppCompatActivity implements OnConnectionFailedListener {

    private static final String TAG = "AddMeetingActivity";
    private static final int PLACE_PICKER_REQUEST = 1;

    private MyApplication app;

    private EditText nameEditText;
    private TextView placeText;
    private EditText placeAliasEditText;
    private TextView transportText;
    private TextView timeStartDateText;
    private TextView timeEndDateText;
    private TextView timeStartTimeText;
    private TextView timeEndTimeText;
    private TextView notificationText;
    private TextView addMembersText;

    private Calendar fromTime;
    private Calendar toTime;

    private String locationName;
    private String locationAddress;
    private Double locationLat;
    private Double locationLon;

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private TimePickerDialog fromTimePickerDialog;
    private TimePickerDialog toTimePickerDialog;

    private AddMeetingNotificationDialog notificationDialog;
    private AddMeetingTransportDialog transportDialog;

    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;
    private SimpleDateFormat callDateTimeFormatter;

    private int transportMethod = 0;
    private int notifyTime = 15;


    private void createMeeting() {
        String name = nameEditText.getText().toString();

        if(locationName != null && !name.isEmpty()) {

            String locationAlias = placeAliasEditText.getText().toString();
            if(!locationAlias.isEmpty()) {
                locationName = locationAlias;
            }

            // TODO: add members, hardcoded type
            Call<Void> createMeetingCall = app.getRestClient().getApiService().MEETING_POST_BODY_CALL(name, callDateTimeFormatter.format(fromTime.getTime()), callDateTimeFormatter.format(toTime.getTime()), notifyTime, locationLat, locationLon, locationName, locationAddress, transportMethod, getUserPhoneNumber(), 15, new ArrayList<Integer>());
            createMeetingCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Response<Void> response, Retrofit retrofit) {
                    if(response.code() == 200) {
                        Intent intent = new Intent(AddMeetingActivity.this, NavigationDrawerActivity.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.e(TAG, t.getMessage().toString());
                    Toast.makeText(AddMeetingActivity.this, "Something went wrong, please try again", Toast.LENGTH_LONG).show();
                }
            });

        } else {
            if(name.isEmpty()) {
                nameEditText.setError("Meeting name is required");
            } else {
                Toast.makeText(this, "Location is required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meeting);

        initLayout();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_meeting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_meeting_create_button:
                createMeeting();
                break;
        }

        return true;
    }

    private void initLayout() {
        app = (MyApplication) getApplication();

        nameEditText = (EditText) findViewById(R.id.activity_add_meeting_title_editText);
        placeText = (TextView) findViewById(R.id.activity_add_meeting_place_text);
        placeAliasEditText = (EditText) findViewById(R.id.activity_add_meeting_place_alias_editText);
        transportText = (TextView) findViewById(R.id.activity_add_meeting_transport_text);
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

        transportDialog = new AddMeetingTransportDialog();
        transportDialog.setDialogListener(new AddMeetingTransportDialog.DialogListener() {
            @Override
            public void onItemClick(int selectedIndex) {
                transportText.setText(getResources().getStringArray(R.array.transport_choices)[selectedIndex]);
                transportMethod = selectedIndex;
            }
        });
        transportText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transportDialog.show(getFragmentManager(), TAG);
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

        notificationDialog = new AddMeetingNotificationDialog();
        notificationDialog.setDialogListener(new AddMeetingNotificationDialog.DialogListener() {
            @Override
            public void onItemClick(int selectedIndex) {
                notificationText.setText(getResources().getStringArray(R.array.notification_choices)[selectedIndex]);
                notifyTime = Integer.valueOf(getResources().getStringArray(R.array.notification_value_choices)[selectedIndex]);
            }
        });
        notificationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationDialog.show(getFragmentManager(), TAG);
            }
        });
    }

    private void initFields() {
        fromTime = Calendar.getInstance();
        toTime = Calendar.getInstance();
        toTime.add(Calendar.HOUR_OF_DAY, 1);

        timeStartDateText.setText(dateFormatter.format(fromTime.getTime()));
        timeEndDateText.setText(dateFormatter.format(toTime.getTime()));
        timeStartTimeText.setText(timeFormatter.format(fromTime.getTime()));
        timeEndTimeText.setText(timeFormatter.format(toTime.getTime()));
    }

    private void initPickers() {

        dateFormatter = new SimpleDateFormat("EEE, dd MMM yyyy", Locale.US);
        timeFormatter = new SimpleDateFormat("H:mm", Locale.US);
        callDateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd H:mm", Locale.US);

        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Log.i(TAG, year + " " + monthOfYear + " " + dayOfMonth);
                fromTime.set(Calendar.YEAR, year);
                fromTime.set(Calendar.MONTH, monthOfYear);
                fromTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                timeStartDateText.setText(dateFormatter.format(fromTime.getTime()));

                toTime.set(Calendar.YEAR, year);
                toTime.set(Calendar.MONTH, monthOfYear);
                toTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                timeEndDateText.setText(dateFormatter.format(toTime.getTime()));

            }
        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Log.i(TAG, year + " " + monthOfYear + " " + dayOfMonth);

                toTime.set(Calendar.YEAR, year);
                toTime.set(Calendar.MONTH, monthOfYear);
                toTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                timeEndDateText.setText(dateFormatter.format(toTime.getTime()));

            }
        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        fromTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Log.i(TAG, hourOfDay + ":" + minute);

                fromTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                fromTime.set(Calendar.MINUTE, minute);

                timeStartTimeText.setText(timeFormatter.format(fromTime.getTime()));

                toTime.set(Calendar.HOUR_OF_DAY, hourOfDay + 1);
                toTime.set(Calendar.MINUTE, minute);

                timeEndTimeText.setText(timeFormatter.format(toTime.getTime()));

            }
        }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), true);

        toTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Log.i(TAG, hourOfDay + ":" + minute);

                toTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                toTime.set(Calendar.MINUTE, minute);

                timeEndTimeText.setText(timeFormatter.format(toTime.getTime()));

            }
        }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), true);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PLACE_PICKER_REQUEST && resultCode == Activity.RESULT_OK) {

            final Place place = PlacePicker.getPlace(this, data);
            locationName = place.getName().toString();
            locationAddress = place.getAddress().toString();
            locationLat = place.getLatLng().latitude;
            locationLon = place.getLatLng().longitude;

            Log.i(TAG, locationName + " - " + locationAddress + ", (" + locationLat + ", " + locationLon + ")");

            if(!locationName.contains("(")) {
                placeText.setText(locationName);
            } else {
                placeText.setText(locationAddress);
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private String getUserPhoneNumber() {
        SharedPreferences prefs = getSharedPreferences(MyApplication.METME_SHARED_PREFERENCES, MODE_PRIVATE);
        if(prefs.contains("phone_number")) {
            return prefs.getString("phone_number", null);
        } else {
            Toast.makeText(this, "Something went wrong, please login again", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, LoginNameActivity.class);
            startActivity(intent);
            return null;
        }
    }
}
