package com.tudor.rotarus.unibuc.metme.activities;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.tudor.rotarus.unibuc.metme.R;
import com.tudor.rotarus.unibuc.metme.managers.MySharedPreferencesManager;
import com.tudor.rotarus.unibuc.metme.managers.NetworkManager;
import com.tudor.rotarus.unibuc.metme.pojos.interfaces.network.CreatePickupListener;
import com.tudor.rotarus.unibuc.metme.views.dialogs.AddMeetingTransportDialog;
import com.tudor.rotarus.unibuc.metme.views.dialogs.AddMeetingTypeDialog;

import java.util.ArrayList;

public class AddPickupActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, CreatePickupListener {

    private final String TAG = getClass().getSimpleName();

    public static final String BUNDLE_FRIEND_NAME = "BUNDLE_FRIEND_NAME";
    public static final String BUNDLE_FRIEND_ID = "BUNDLE_FRIEND_ID";
    private static final int PLACE_PICKER_REQUEST = 2;

    private EditText titleEditText;
    private TextView placeTextView;
    private EditText placeAliasEditText;
    private TextView transportTextView;
    private TextView typeTextView;
    private LinearLayout transportContainer;
    private LinearLayout typeContainer;

    private AddMeetingTransportDialog transportDialog;
    private AddMeetingTypeDialog typeDialog;

    private String friendName;
    private int friendId;

    private int transportationMethod;
    private int type;
    private String meetingName;
    private String locationName;
    private String locationAddress;
    private Double locationLat;
    private Double locationLon;
    private Double authorLat;
    private Double authorLon;

    private GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_pickup);

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
                createPickup();
                break;
        }
        return true;
    }

    private void createPickup() {

        if(locationName != null && !meetingName.isEmpty()){

            String locationAlias = placeAliasEditText.getText().toString();
            if(!locationAlias.isEmpty()) {
                locationName = locationAlias;
            }

            int userId = MySharedPreferencesManager.getInstance().readId(this);
            ArrayList<Integer> members = new ArrayList<>();
            members.add(friendId);

            getAuthorLocation();

            NetworkManager networkManager = NetworkManager.getInstance();
            networkManager.createPickup(meetingName, locationLat, locationLon, locationName, locationAddress, transportationMethod, userId, type, members, authorLat, authorLon, this);

        } else {
            if(meetingName.isEmpty()) {
                titleEditText.setError("Pickup name is required");
            } else {
                Toast.makeText(this, "Location is required", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void initLayout() {

        friendName = getIntent().getStringExtra(BUNDLE_FRIEND_NAME);
        friendId = getIntent().getIntExtra(BUNDLE_FRIEND_ID, -1);

        if(friendName == null || friendId == -1) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, NavigationDrawerActivity.class));
            finish();
        }

        titleEditText = (EditText) findViewById(R.id.activity_add_pickup_title_editText);
        placeTextView = (TextView) findViewById(R.id.activity_add_pickup_place_text);
        placeAliasEditText = (EditText) findViewById(R.id.activity_add_pickup_place_alias_editText);
        transportTextView = (TextView) findViewById(R.id.activity_add_pickup_transport_text);
        typeTextView = (TextView) findViewById(R.id.activity_add_pickup_type_text);
        transportContainer = (LinearLayout) findViewById(R.id.activity_add_pickup_transport_container);
        typeContainer = (LinearLayout) findViewById(R.id.activity_add_pickup_type_container);

        initPickers();
        initFields();
        initOnClickListeners();
    }

    private void initOnClickListeners() {

        placeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                    Intent intent = intentBuilder.build(AddPickupActivity.this);
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
                transportTextView.setText(getResources().getStringArray(R.array.transport_choices)[selectedIndex]);
                transportationMethod = selectedIndex;
            }
        });
        transportContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transportDialog.show(getFragmentManager(), TAG);
            }
        });

        typeDialog = new AddMeetingTypeDialog();
        typeDialog.setDialogListener(new AddMeetingTypeDialog.DialogListener() {
            @Override
            public void onItemClick(int selectedIndex) {
                typeTextView.setText(getResources().getStringArray(R.array.meeting_types)[selectedIndex]);
                type = selectedIndex;
            }
        });
        typeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeDialog.show(getFragmentManager(), TAG);
            }
        });

    }

    private void initFields() {
        meetingName = friendName + " pickup";
        titleEditText.setText(meetingName);
        type = 0;
        transportationMethod = 0;
    }

    private void initPickers() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PLACE_PICKER_REQUEST && resultCode == Activity.RESULT_OK) {

            final Place place = PlacePicker.getPlace(data, this);
            locationName = place.getName().toString();
            locationAddress = place.getAddress().toString();
            locationLat = place.getLatLng().latitude;
            locationLon = place.getLatLng().longitude;

            Log.i(TAG, locationName + " - " + locationAddress + ", (" + locationLat + ", " + locationLon + ")");

            if(!locationName.contains("(")) {
                placeTextView.setText(locationName);
            } else {
                placeTextView.setText(locationAddress);
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onCreatePickupSuccess() {
        if(mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        startActivity(new Intent(this, NavigationDrawerActivity.class));
        finish();
    }

    @Override
    public void onCreatePickupFailed() {
        if(mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        Toast.makeText(this, "Something went wrong, please try again", Toast.LENGTH_LONG).show();
    }

    public void getAuthorLocation() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getBaseContext())
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {
                            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                            if(mLastLocation != null) {

                                Double latitude = mLastLocation.getLatitude();
                                Double longitude = mLastLocation.getLongitude();

                                authorLat = latitude;
                                authorLon = longitude;

                                Log.i(TAG, latitude + " " + longitude);

                            } else {
                                Log.e(TAG, "null location");
                            }
                        }

                        @Override
                        public void onConnectionSuspended(int i) {

                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {
                            Log.e(TAG, "GoogleApiClient connection failed");
                        }
                    })
                    .addApi(LocationServices.API)
                    .build();
        }

        mGoogleApiClient.connect();
    }
}
