package com.tudor.rotarus.unibuc.metme.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tudor.rotarus.unibuc.metme.R;
import com.tudor.rotarus.unibuc.metme.managers.MySharedPreferencesManager;
import com.tudor.rotarus.unibuc.metme.managers.NetworkManager;
import com.tudor.rotarus.unibuc.metme.pojos.interfaces.network.AcceptMeetingListener;
import com.tudor.rotarus.unibuc.metme.pojos.interfaces.network.MeetingDetailsListener;
import com.tudor.rotarus.unibuc.metme.pojos.responses.get.MeetingGetBody;
import com.tudor.rotarus.unibuc.metme.utils.Utilities;
import com.tudor.rotarus.unibuc.metme.views.dialogs.AddMeetingNotificationDialog;
import com.tudor.rotarus.unibuc.metme.views.dialogs.AddMeetingTransportDialog;

public class AcceptMeetingActivity extends AppCompatActivity implements MeetingDetailsListener, AcceptMeetingListener {

    private final String TAG = getClass().getSimpleName();

    public final static String MEETING_ID_BUNDLE_KEY = "MEETING_ID_BUNDLE_KEY";
    public final static String MEETING_MESSAGE_BUNDLE_KEY = "MEETING_MESSAGE_BUNDLE_KEY";

    private TextView messageTextView;
    private TextView titleTextView;
    private TextView placeTextView;
    private TextView placeAddressTextView;
    private TextView timeTextView;
    private TextView transportationMethodTextView;
    private TextView notifyTimeTextView;
    private LinearLayout transportationContainer;
    private LinearLayout notifyContainer;
    private Button acceptButton;
    private Button declineButton;

    private AddMeetingTransportDialog transportDialog;
    private AddMeetingNotificationDialog notificationDialog;

    private int transportationMethod;
    private int notificationTime;
    private int meetingId;

    private NetworkManager networkManager;
    private MySharedPreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_meeting);

        initLayout();
    }

    private void initLayout() {

        networkManager = NetworkManager.getInstance();
        preferencesManager = MySharedPreferencesManager.getInstance();
        Intent creatorIntent = getIntent();
        meetingId = creatorIntent.getIntExtra(MEETING_ID_BUNDLE_KEY, -1);
        String meetingMessage = creatorIntent.getStringExtra(MEETING_MESSAGE_BUNDLE_KEY);
        Log.i(TAG, meetingId + "");

        if(meetingId == -1) {
            Log.e(TAG, "meeting id is missing");
            Toast.makeText(this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, NavigationDrawerActivity.class));
            finish();
            return;
        }

        messageTextView = (TextView) findViewById(R.id.activity_accept_meeting_message_textView);
        titleTextView = (TextView) findViewById(R.id.activity_accept_meeting_title_textView);
        placeTextView = (TextView) findViewById(R.id.activity_accept_meeting_place_textView);
        placeAddressTextView = (TextView) findViewById(R.id.activity_accept_meeting_place_address_textView);
        timeTextView = (TextView) findViewById(R.id.activity_accept_meeting_time_textView);
        transportationMethodTextView = (TextView) findViewById(R.id.activity_accept_meeting_transport_text);
        notifyTimeTextView = (TextView) findViewById(R.id.activity_accept_meeting_notification_text);
        transportationContainer = (LinearLayout) findViewById(R.id.activity_accept_meeting_transport_container);
        notifyContainer = (LinearLayout) findViewById(R.id.activity_accept_meeting_notification_container);
        acceptButton = (Button) findViewById(R.id.activity_accept_meeting_accept_button);
        declineButton = (Button) findViewById(R.id.activity_accept_meeting_reject_button);

        transportDialog = new AddMeetingTransportDialog();
        transportDialog.setDialogListener(new AddMeetingTransportDialog.DialogListener() {
            @Override
            public void onItemClick(int selectedIndex) {
                transportationMethodTextView.setText(getResources().getStringArray(R.array.transport_choices)[selectedIndex]);
                transportationMethod = selectedIndex;
            }
        });

        notificationDialog = new AddMeetingNotificationDialog();
        notificationDialog.setDialogListener(new AddMeetingNotificationDialog.DialogListener() {
            @Override
            public void onItemClick(int selectedIndex) {
                notifyTimeTextView.setText(getResources().getStringArray(R.array.notification_choices)[selectedIndex]);
                notificationTime = Integer.valueOf(getResources().getStringArray(R.array.notification_value_choices)[selectedIndex]);
            }
        });

        transportationContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transportDialog.show(getFragmentManager(), TAG);
            }
        });

        notifyContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationDialog.show(getFragmentManager(), TAG);
            }
        });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MySharedPreferencesManager preferencesManager = MySharedPreferencesManager.getInstance();
                int userId = preferencesManager.readId(AcceptMeetingActivity.this);

                networkManager.acceptMeeting(userId, meetingId, transportationMethod, notificationTime, AcceptMeetingActivity.this);
            }
        });

        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AcceptMeetingActivity.this, NavigationDrawerActivity.class));
                finish();
            }
        });

        if(meetingMessage != null) {
            messageTextView.setText(meetingMessage);
        } else {
            messageTextView.setText("New notification");
        }

        int userId = preferencesManager.readId(this);
        if(userId > 0) {
            networkManager.getMeetingDetails(userId, meetingId, this);
        } else {
            Log.e(TAG, "user id is missing");
        }
    }

    @Override
    public void onFetchMeetingDetailsSuccess(MeetingGetBody response) {
        if(response != null) {
            titleTextView.setText(response.getName());
            placeTextView.setText(response.getPlaceName());
            placeAddressTextView.setText(response.getPlaceAddress());
            timeTextView.setText(Utilities.getPrettyFullDateTime(response.getFromTime()));

        }
    }

    @Override
    public void onFetchMeetingDetailsEmptyResponse() {
        Toast.makeText(this, "Meeting does not exist", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFetchMeetingDetailsFailed() {
        Toast.makeText(this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAcceptMeetingSuccess() {
        Intent intent = new Intent(this, NavigationDrawerActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onAcceptMeetingFailed() {
        Toast.makeText(this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
    }
}
