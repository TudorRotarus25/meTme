package com.tudor.rotarus.unibuc.metme.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tudor.rotarus.unibuc.metme.MyApplication;
import com.tudor.rotarus.unibuc.metme.R;
import com.tudor.rotarus.unibuc.metme.managers.NetworkManager;
import com.tudor.rotarus.unibuc.metme.pojos.interfaces.MeetingListListener;
import com.tudor.rotarus.unibuc.metme.pojos.requests.get.MeetingsListGetBody;
import com.tudor.rotarus.unibuc.metme.views.adapters.AllMeetingsListAdapter;

public class AllMeetingsFragment extends Fragment implements MeetingListListener {

    private static final String TAG = "AllMeetingsFragment";

    private ProgressDialog progressDialog;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private TextView contentEmptyTextView;

    private final int INITIAL_CALL = 1;
    private final int REFRESH_CALL = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_all_meetings, container, false);

        init(v);

        return v;
    }

    private void init(final View v) {

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("All meetings");

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait while loading");
        progressDialog.show();

        contentEmptyTextView = (TextView) v.findViewById(R.id.fragment_all_meetings_empty_textView);

        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.fragment_all_meetings_swipeRefreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                meetingListCall(REFRESH_CALL);

            }
        });

        recyclerView = (RecyclerView) v.findViewById(R.id.fragment_all_meetings_recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        meetingListCall(INITIAL_CALL);
    }

    private void meetingListCall(final int callType) {

        NetworkManager networkManager = NetworkManager.getInstance();
        networkManager.listAllMeetings(getPhoneNumber(), callType, this);

    }

    private String getPhoneNumber() {
        SharedPreferences sp = getActivity().getSharedPreferences(MyApplication.METME_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return sp.getString("phone_number", "");
    }

    @Override
    public void onListAllMeetingsSuccess(MeetingsListGetBody response, final int callType) {
        if(callType == INITIAL_CALL) {
            progressDialog.dismiss();
        } else {
            refreshLayout.setRefreshing(false);
        }

        if(response.getMeetings().size() == 0) {

            refreshLayout.setVisibility(View.GONE);
            contentEmptyTextView.setVisibility(View.VISIBLE);

        } else {
            refreshLayout.setVisibility(View.VISIBLE);
            contentEmptyTextView.setVisibility(View.GONE);

            AllMeetingsListAdapter adapter = new AllMeetingsListAdapter(response.getMeetings());
            recyclerView.setAdapter(adapter);

        }

    }

    @Override
    public void onListAllMeetingsFailed(final int callType) {
        if(callType == INITIAL_CALL) {
            progressDialog.dismiss();
        } else {
            refreshLayout.setRefreshing(false);
        }

        Toast.makeText(getActivity(), "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
    }
}
