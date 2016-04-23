package com.tudor.rotarus.unibuc.metme.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.tudor.rotarus.unibuc.metme.R;
import com.tudor.rotarus.unibuc.metme.pojos.responses.get.MeetingGetBody;
import com.tudor.rotarus.unibuc.metme.views.adapters.HomeParticipantsListAdapter;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private TextView titleTextView;
    private TextView locationTextView;
    private FloatingActionButton transportationButton;
    private FloatingActionButton optionsButton;
    private ArcProgress arcProgress;
    private RecyclerView recyclerView;

    private ArrayList<MeetingGetBody.Participant> participantsList;
    private HomeParticipantsListAdapter participantsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        init(view);

        return view;
    }

    private void init(View view) {

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Next meeting");

        titleTextView = (TextView) view.findViewById(R.id.fragment_home_title_textView);
        locationTextView = (TextView) view.findViewById(R.id.fragment_home_location_textView);
        transportationButton = (FloatingActionButton) view.findViewById(R.id.fragment_home_transportation_button);
        optionsButton = (FloatingActionButton) view.findViewById(R.id.fragment_home_options_button);
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_home_participants_recyclerView);

        arcProgress = (ArcProgress) view.findViewById(R.id.fragment_home_progress_bar);
        arcProgress.setProgress(65);
        arcProgress.setBottomText("ETA");
        arcProgress.setSuffixText("m");
//        arcProgress.setDrawingCacheBackgroundColor(Color.WHITE);
//        arcProgress.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//        arcProgress.setTextColor(R.color.fragment_home_top_text_color);
//        arcProgress.setFinishedStrokeColor(R.color.colorAccent);
//        arcProgress.setUnfinishedStrokeColor(R.color.fragment_home_top_text_color);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        participantsList = new ArrayList<>();
        for(int i=0;i<3;i++) {
            MeetingGetBody.Participant newParticipant = new MeetingGetBody().new Participant(i, "Tudor Rotarus " + i, 30);
            participantsList.add(newParticipant);
        }
        participantsAdapter = new HomeParticipantsListAdapter(participantsList);
        recyclerView.setAdapter(participantsAdapter);

        getActivity().findViewById(R.id.fab).setVisibility(View.GONE);

    }
}
