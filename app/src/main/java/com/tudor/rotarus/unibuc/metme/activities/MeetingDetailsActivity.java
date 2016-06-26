package com.tudor.rotarus.unibuc.metme.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.tudor.rotarus.unibuc.metme.R;
import com.tudor.rotarus.unibuc.metme.managers.MySharedPreferencesManager;
import com.tudor.rotarus.unibuc.metme.managers.NetworkManager;
import com.tudor.rotarus.unibuc.metme.pojos.interfaces.network.MeetingDetailsListener;
import com.tudor.rotarus.unibuc.metme.pojos.responses.get.MeetingGetBody;
import com.tudor.rotarus.unibuc.metme.utils.Utilities;

public class MeetingDetailsActivity extends AppCompatActivity implements MeetingDetailsListener {

    private final String TAG = getClass().getSimpleName();

    public static final String BUNDLE_MEETING_DETAILS_ID = "BUNDLE_MEETING_DETAILS_ID";

    private TextView nameTextView;
    private TextView typeTextView;
    private TextView datetimeFromTextView;
    private TextView datetimeToTextView;
    private TextView locationNameTextView;
    private TextView locationAddressTextView;
    private TextView transportationTextView;
    private TextView notifyTimeTextView;

    private MeetingGetBody meetingDetails;

    private int meetingId;
    private int userId;

    private NetworkManager networkManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_details);

        initLayout();
    }

    private void initLayout() {

        meetingId = getIntent().getIntExtra(BUNDLE_MEETING_DETAILS_ID, -1);
        userId = MySharedPreferencesManager.getInstance().readId(this);

        if (meetingId == -1) {
            Toast.makeText(this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, NavigationDrawerActivity.class);
            startActivity(intent);
            finish();
        }

        nameTextView = (TextView) findViewById(R.id.content_meeting_details_name);
        typeTextView = (TextView) findViewById(R.id.content_meeting_details_type);
        datetimeFromTextView = (TextView) findViewById(R.id.content_meeting_details_datetime_from);
        datetimeToTextView = (TextView) findViewById(R.id.content_meeting_details_datetime_to);
        locationNameTextView = (TextView) findViewById(R.id.content_meeting_details_location_name);
        locationAddressTextView = (TextView) findViewById(R.id.content_meeting_details_location_address);
        transportationTextView = (TextView) findViewById(R.id.content_meeting_details_transportation_method);
        notifyTimeTextView = (TextView) findViewById(R.id.content_meeting_details_notify_time);

        networkManager = NetworkManager.getInstance();
        networkManager.getMeetingDetails(userId, meetingId, this);
    }

    @Override
    public void onFetchMeetingDetailsSuccess(MeetingGetBody response) {
        meetingDetails = response;

        nameTextView.setText(response.getName());
        if (response.getType() != null) {
            typeTextView.setText(getResources().getStringArray(R.array.meeting_types)[response.getType()]);
        }
        datetimeFromTextView.setText(Utilities.getPrettyFullDateTime(response.getFromTime()));
        datetimeToTextView.setText(Utilities.getPrettyFullDateTime(response.getToTime()));
        locationNameTextView.setText(response.getPlaceName());
        locationAddressTextView.setText(response.getPlaceAddress());
        transportationTextView.setText(getResources().getStringArray(R.array.transport_choices)[response.getTransportationMethod()]);
        if (response.getNotifyTime() != null) {
            notifyTimeTextView.setText(response.getNotifyTime() + " minutes before departure time");
        }
    }

    @Override
    public void onFetchMeetingDetailsEmptyResponse() {

    }

    @Override
    public void onFetchMeetingDetailsFailed() {

    }
}
