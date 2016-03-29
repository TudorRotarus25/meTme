package com.tudor.rotarus.unibuc.metme.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.tudor.rotarus.unibuc.metme.MyApplication;
import com.tudor.rotarus.unibuc.metme.R;
import com.tudor.rotarus.unibuc.metme.pojos.get.MeetingsListGetBody;
import com.tudor.rotarus.unibuc.metme.views.adapters.AllMeetingsListAdapter;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class AllMeetingsFragment extends Fragment {

    private static final String TAG = "AllMeetingsFragment";

    MyApplication app;

    ProgressDialog progressDialog;
    SwipeRefreshLayout refreshLayout;
    RecyclerView recyclerView;

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

        app = (MyApplication) getActivity().getApplication();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait while loading");
        progressDialog.show();

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

        Call<MeetingsListGetBody> call = app.getRestClient().getApiService().MEETINGS_LIST_GET_BODY_CALL(getPhoneNumber());
        call.enqueue(new Callback<MeetingsListGetBody>() {
            @Override
            public void onResponse(Response<MeetingsListGetBody> response, Retrofit retrofit) {
                if(callType == INITIAL_CALL) {
                    progressDialog.dismiss();
                } else {
                    refreshLayout.setRefreshing(false);
                }

                if (response.code() == 200 && response.body() != null) {
                    AllMeetingsListAdapter adapter = new AllMeetingsListAdapter(response.body().getMeetings());
                    recyclerView.setAdapter(adapter);

                } else {
                    Log.e(TAG, response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, t.getMessage());
                Toast.makeText(getActivity(), "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
            }
        });

    }

    private String getPhoneNumber() {
        SharedPreferences sp = getActivity().getSharedPreferences(app.METME_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return sp.getString("phone_number", "");
    }
}
