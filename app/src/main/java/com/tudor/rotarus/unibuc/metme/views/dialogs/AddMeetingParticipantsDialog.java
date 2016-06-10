package com.tudor.rotarus.unibuc.metme.views.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tudor.rotarus.unibuc.metme.R;
import com.tudor.rotarus.unibuc.metme.managers.LocalDbManager;
import com.tudor.rotarus.unibuc.metme.pojos.interfaces.db.FriendsDbListener;
import com.tudor.rotarus.unibuc.metme.pojos.responses.post.FriendsPostBody;
import com.tudor.rotarus.unibuc.metme.views.adapters.MeetingParticipantsDialogAdapter;

import java.util.ArrayList;

/**
 * Created by Tudor on 12.05.2016.
 */
public class AddMeetingParticipantsDialog extends DialogFragment implements FriendsDbListener {

    private final String TAG = getClass().getSimpleName();

    private static final String BUNDLE_PARTICIPANTS_IDS = "BUNDLE_PARTICIPANTS_IDS";

    private LocalDbManager dbManager;

    private int[] participantsIds;

    private MeetingParticipantDialogClick callback;

    ArrayList<FriendsPostBody.Friend> participants;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private MeetingParticipantsDialogAdapter participantsAdapter;

    public AddMeetingParticipantsDialog() {
    }

    public void setOnDialogElementClick(MeetingParticipantDialogClick callback) {
        Log.i(TAG, "setOnDialogElementClick");
        this.callback = callback;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_add_meeting_participants, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.content_add_meeting_participants_swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUsers();
            }
        });

        recyclerView = (RecyclerView) v.findViewById(R.id.content_add_meeting_participants_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        participantsIds = getArguments().getIntArray(BUNDLE_PARTICIPANTS_IDS);

        participantsAdapter = new MeetingParticipantsDialogAdapter();
        participantsAdapter.setOnMeetingParticipantClick(new MeetingParticipantsDialogAdapter.MeetingParticipantClickListener() {
            @Override
            public void onClick(FriendsPostBody.Friend friend) {
                if(callback != null) {
                    callback.onClick(friend);
                    dismiss();
                }
            }
        });
        recyclerView.setAdapter(participantsAdapter);

        dbManager = LocalDbManager.getInstance();

        getUsers();

        return v;
    }

    private void getUsers(){

        dbManager.getFriends(getActivity(), this);
    }

    public static AddMeetingParticipantsDialog newInstance(int[] participantsIds) {
        AddMeetingParticipantsDialog frag = new AddMeetingParticipantsDialog();
        Bundle args = new Bundle();
        args.putIntArray(BUNDLE_PARTICIPANTS_IDS, participantsIds);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onFriendsDbGetSuccess(FriendsPostBody response) {

        swipeRefreshLayout.setRefreshing(false);
        if(response.getFriends() != null) {

            participants = response.getFriends();

            participantsAdapter.setContacts(participants);
            participantsAdapter.notifyDataSetChanged();

        } else {
            Log.e(TAG, "Empty response");
        }
    }

    @Override
    public void onFriendsDbGetFailed() {
        Toast.makeText(getActivity(), "Something went wrong with fetching friends. Try again please", Toast.LENGTH_LONG).show();
    }

    public interface MeetingParticipantDialogClick {
        void onClick(FriendsPostBody.Friend friend);
    }
}
