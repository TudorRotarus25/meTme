package com.tudor.rotarus.unibuc.metme.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.tudor.rotarus.unibuc.metme.R;
import com.tudor.rotarus.unibuc.metme.activities.NavigationDrawerActivity;
import com.tudor.rotarus.unibuc.metme.activities.login.LoginNameActivity;
import com.tudor.rotarus.unibuc.metme.managers.NetworkManager;
import com.tudor.rotarus.unibuc.metme.managers.MySharedPreferencesManager;
import com.tudor.rotarus.unibuc.metme.pojos.interfaces.network.CancelMeetingListener;
import com.tudor.rotarus.unibuc.metme.pojos.interfaces.network.FinishMeetingListener;
import com.tudor.rotarus.unibuc.metme.pojos.interfaces.network.MeetingDetailsListener;
import com.tudor.rotarus.unibuc.metme.pojos.interfaces.network.PostponeMeetingListener;
import com.tudor.rotarus.unibuc.metme.pojos.interfaces.network.UpdateTransportationListener;
import com.tudor.rotarus.unibuc.metme.pojos.responses.get.MeetingGetBody;
import com.tudor.rotarus.unibuc.metme.views.adapters.HomeParticipantsListAdapter;
import com.tudor.rotarus.unibuc.metme.views.dialogs.PostponeMeetingDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class HomeFragment extends Fragment implements MeetingDetailsListener, UpdateTransportationListener, CancelMeetingListener, FinishMeetingListener, PostponeMeetingListener {

    private final String TAG = getClass().getSimpleName();

    private RelativeLayout wrapper;
    private TextView emptyRespnseMessageTextView;

    private TextView titleTextView;
    private TextView locationTextView;
    private FloatingActionMenu transportationButton;
    private FloatingActionMenu optionsButton;
    private FloatingActionButton transportationCar;
    private FloatingActionButton transportationBus;
    private FloatingActionButton transportationWalk;
    private FloatingActionButton finishMeetingFab;
    private FloatingActionButton postponeMeetingFab;
    private FloatingActionButton cancelMeetingFab;
    private ArcProgress arcProgress;
    private RecyclerView recyclerView;

    private ArrayList<MeetingGetBody.Participant> participantsList;
    private HomeParticipantsListAdapter participantsAdapter;

    private Integer userId;
    private Integer meetingId;
    private int currentTransportationMethod;

    private AlertDialog cancelConfirmDialog;
    private AlertDialog finishConfirmDialog;

    private NetworkManager networkManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (arcProgress != null) {
            arcProgress.setBottomText("ETA");
        }

        if(userId != null && networkManager != null) {
            networkManager.getNextMeetingDetails(userId, this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initLayout(view);
        initDialogs();

        return view;
    }

    private void initDialogs() {

        AlertDialog.Builder cancelBuilder = new AlertDialog.Builder(getActivity());

        cancelBuilder.setMessage("Are you sure you want to exit this meeting?")
                .setTitle("Cancel meeting")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        networkManager.cancelMeeting(userId, meetingId, HomeFragment.this);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        cancelConfirmDialog = cancelBuilder.create();

        AlertDialog.Builder finishBuilder = new AlertDialog.Builder(getActivity());

        finishBuilder.setMessage("Are you sure you want to finish this meeting? This will remove it from the other participants as well")
                .setTitle("Finish meeting")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        networkManager.finishMeeting(userId, meetingId, HomeFragment.this);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        finishConfirmDialog = finishBuilder.create();
    }

    private void initLayout(View view) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Next meeting");

        wrapper = (RelativeLayout) view.findViewById(R.id.fragment_home_wrapper);
        emptyRespnseMessageTextView = (TextView) view.findViewById(R.id.fragment_home_empty_textView);

        titleTextView = (TextView) view.findViewById(R.id.fragment_home_title_textView);
        locationTextView = (TextView) view.findViewById(R.id.fragment_home_location_textView);
        transportationButton = (FloatingActionMenu) view.findViewById(R.id.fragment_home_transportation_button);
        optionsButton = (FloatingActionMenu) view.findViewById(R.id.fragment_home_options_button);
        transportationCar = (FloatingActionButton) view.findViewById(R.id.fragment_home_transportation_car_fab);
        transportationBus = (FloatingActionButton) view.findViewById(R.id.fragment_home_transportation_bus_fab);
        transportationWalk = (FloatingActionButton) view.findViewById(R.id.fragment_home_transportation_walk_fab);
        finishMeetingFab = (FloatingActionButton) view.findViewById(R.id.fragment_home_fab_finish_meeting_button);
        postponeMeetingFab = (FloatingActionButton) view.findViewById(R.id.fragment_home_fab_postpone_meeting_button);
        cancelMeetingFab = (FloatingActionButton) view.findViewById(R.id.fragment_home_fab_cancel_meeting_button);
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_home_participants_recyclerView);

        arcProgress = (ArcProgress) view.findViewById(R.id.fragment_home_progress_bar);
//        arcProgress.setFinishedStrokeColor(R.color.colorAccent);
//        arcProgress.setUnfinishedStrokeColor(R.color.fragment_home_top_text_color);
        arcProgress.setProgress(10);
        arcProgress.setSuffixText("m");
//        arcProgress.setDrawingCacheBackgroundColor(Color.WHITE);
//        arcProgress.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//        arcProgress.setTextColor(R.color.fragment_home_top_text_color);

        transportationCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTransportationButtonsClicked(MeetingGetBody.TRANS_CAR);
            }
        });

        transportationBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTransportationButtonsClicked(MeetingGetBody.TRANS_PUBLIC);
            }
        });

        transportationWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTransportationButtonsClicked(MeetingGetBody.TRANS_WALK);
            }
        });

        finishMeetingFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionsButton.close(true);
                finishConfirmDialog.show();
            }
        });

        postponeMeetingFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionsButton.close(true);
                PostponeMeetingDialog dialog = PostponeMeetingDialog.newInstance();
                dialog.setOnClickListener(new PostponeMeetingDialog.PostponeMeetingOnClick() {
                    @Override
                    public void onClick(int number) {
                        networkManager.postponeMeeting(userId, meetingId, number, HomeFragment.this);
                    }
                });
                dialog.show(getActivity().getFragmentManager(), TAG);
            }
        });

        cancelMeetingFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionsButton.close(true);
                cancelConfirmDialog.show();
            }
        });

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        participantsList = new ArrayList<>();
        participantsAdapter = new HomeParticipantsListAdapter(participantsList);
        recyclerView.setAdapter(participantsAdapter);

        getActivity().findViewById(R.id.fab).setVisibility(View.GONE);

        userId = MySharedPreferencesManager.getInstance().readId(getContext());
        networkManager = NetworkManager.getInstance();
    }

    private void onTransportationButtonsClicked(int transMethod) {
        if (userId == null) {
            startActivity(new Intent(getActivity(), LoginNameActivity.class));
            getActivity().finish();
        }
        if (meetingId != null) {
            networkManager.updateTransportation(userId, meetingId, transMethod, this);
            changeTransportationIcons(transMethod);
        }
    }

    private void changeTransportationIcons(int transMethod) {
        switch (transMethod) {
            case MeetingGetBody.TRANS_CAR: {
                transportationCar.setEnabled(false);
                transportationBus.setEnabled(true);
                transportationWalk.setEnabled(true);
                break;
            }
            case MeetingGetBody.TRANS_PUBLIC: {
                transportationCar.setEnabled(true);
                transportationBus.setEnabled(false);
                transportationWalk.setEnabled(true);
                break;
            }
            case MeetingGetBody.TRANS_WALK: {
                transportationCar.setEnabled(true);
                transportationBus.setEnabled(true);
                transportationWalk.setEnabled(false);
                break;
            }
        }
        if (transportationButton.isOpened()) {
            transportationButton.close(true);
        }
    }

    @Override
    public void onFetchMeetingDetailsSuccess(MeetingGetBody response) {

        SimpleDateFormat fromResponse = new SimpleDateFormat("yyyy-MM-dd H:mm:ss", Locale.US);
        SimpleDateFormat toDate = new SimpleDateFormat("EEE, dd MMM yyyy, HH:mm", Locale.US);

        wrapper.setVisibility(View.VISIBLE);
        emptyRespnseMessageTextView.setVisibility(View.GONE);

        if(response.getAuthorId() == userId) {
            finishMeetingFab.setVisibility(View.VISIBLE);
        } else {
            finishMeetingFab.setVisibility(View.GONE);
        }

        meetingId = response.getId();
        if (response.getEta() != null) {
            arcProgress.setProgress(response.getEta());
        }
        currentTransportationMethod = response.getTransportationMethod();
        changeTransportationIcons(response.getTransportationMethod());
        participantsAdapter.setParticipants(response.getParticipants());
        titleTextView.setText(response.getName());

        Calendar fromTime = Calendar.getInstance();
        try {
            fromTime.setTime(fromResponse.parse(response.getFromTime()));
        } catch (ParseException e) {
            Log.e(TAG, "Could not parse date");
        }

        locationTextView.setText(String.format("at %s - %s", response.getPlaceName(), toDate.format(fromTime.getTime())));
        participantsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFetchMeetingDetailsEmptyResponse() {
        wrapper.setVisibility(View.GONE);
        emptyRespnseMessageTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFetchMeetingDetailsFailed() {
        Toast.makeText(getContext(), "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpdateTransportationSuccess() {

    }

    @Override
    public void onUpdateTransportationFailed() {
        Toast.makeText(getContext(), "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancelMeetingSuccess() {
        startActivity(new Intent(getActivity(), NavigationDrawerActivity.class));
    }

    @Override
    public void onCancelMeetingFailed() {
        Toast.makeText(getContext(), "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFinishMeetingSuccess() {
        startActivity(new Intent(getActivity(), NavigationDrawerActivity.class));
    }

    @Override
    public void onFinishMeetingFailed() {
        Toast.makeText(getContext(), "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPostponeMeetingSuccess() {
        startActivity(new Intent(getActivity(), NavigationDrawerActivity.class));
    }

    @Override
    public void onPostponeMeetingFailed() {
        Toast.makeText(getContext(), "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
    }
}
