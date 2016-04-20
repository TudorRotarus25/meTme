package com.tudor.rotarus.unibuc.metme.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tudor.rotarus.unibuc.metme.MyApplication;
import com.tudor.rotarus.unibuc.metme.R;
import com.tudor.rotarus.unibuc.metme.activities.login.LoginNameActivity;
import com.tudor.rotarus.unibuc.metme.managers.NetworkManager;
import com.tudor.rotarus.unibuc.metme.pojos.interfaces.MeetingListListener;
import com.tudor.rotarus.unibuc.metme.pojos.requests.get.MeetingsListGetBody;
import com.tudor.rotarus.unibuc.metme.views.adapters.AllMeetingsListAdapter;

import java.util.ArrayList;
import java.util.List;

public class AllMeetingsFragment extends Fragment implements MeetingListListener, SearchView.OnQueryTextListener{

    private static final String TAG = "AllMeetingsFragment";

    private ProgressDialog progressDialog;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private TextView contentEmptyTextView;

    private AllMeetingsListAdapter adapter;
    private ArrayList<MeetingsListGetBody.Meeting> meetingsList;

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_search, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void init(final View v) {

        setHasOptionsMenu(true);

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

                meetingListCall();

            }
        });

        recyclerView = (RecyclerView) v.findViewById(R.id.fragment_all_meetings_recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new AllMeetingsListAdapter(new ArrayList<MeetingsListGetBody.Meeting>());
        recyclerView.setAdapter(adapter);

        getActivity().findViewById(R.id.fab).setVisibility(View.VISIBLE);

        meetingListCall();
    }

    private void meetingListCall() {

        int id = ((MyApplication)getActivity().getApplication()).readId();

        if(id < 0) {
            Intent intent = new Intent(getActivity(), LoginNameActivity.class);
            startActivity(intent);
            getActivity().finish();
        }

        NetworkManager networkManager = NetworkManager.getInstance();
        networkManager.listAllMeetings(id, this);

    }

    @Override
    public void onListAllMeetingsSuccess(MeetingsListGetBody response) {
        progressDialog.dismiss();
        refreshLayout.setRefreshing(false);

        meetingsList = response.getMeetings();
        if(response.getMeetings().size() == 0) {

            refreshLayout.setVisibility(View.GONE);
            contentEmptyTextView.setVisibility(View.VISIBLE);

        } else {
            refreshLayout.setVisibility(View.VISIBLE);
            contentEmptyTextView.setVisibility(View.GONE);

            adapter.setMeetings(meetingsList);
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onListAllMeetingsFailed() {
        progressDialog.dismiss();
        refreshLayout.setRefreshing(false);

        Toast.makeText(getActivity(), "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        adapter.setMeetings(filter(newText));
        adapter.notifyDataSetChanged();
        return true;
    }

    private List<MeetingsListGetBody.Meeting> filter(String query) {
        List<MeetingsListGetBody.Meeting> builder = new ArrayList<>();
        for(int i = 0 ; i < meetingsList.size() ; i++) {
            if(meetingsList.get(i).getName().toLowerCase().contains(query.toLowerCase()) || (meetingsList.get(i).getPlaceName() != null && meetingsList.get(i).getPlaceName().toLowerCase().contains(query.toLowerCase()))) {
                builder.add(meetingsList.get(i));
            }
        }

        return builder;
    }
}
