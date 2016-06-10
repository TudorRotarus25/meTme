package com.tudor.rotarus.unibuc.metme.views.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tudor.rotarus.unibuc.metme.R;
import com.tudor.rotarus.unibuc.metme.pojos.responses.post.FriendsPostBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tudor on 12.05.2016.
 */
public class MeetingParticipantsDialogAdapter extends RecyclerView.Adapter<MeetingParticipantsDialogAdapter.ParticipantsViewHolder> {

    private final String TAG = getClass().getSimpleName();

    private List<FriendsPostBody.Friend> contacts;
    private MeetingParticipantClickListener callback;

    public static class ParticipantsViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout container;
        private ImageView icon;
        private TextView iconText;
        private TextView name;

        public ParticipantsViewHolder(View itemView) {
            super(itemView);
            container = (RelativeLayout) itemView.findViewById(R.id.list_participants_container);
            icon = (ImageView) itemView.findViewById(R.id.list_participants_icon);
            iconText = (TextView) itemView.findViewById(R.id.list_participants_icon_inner_text);
            name = (TextView) itemView.findViewById(R.id.list_participants_name_textView);
        }
    }

    public MeetingParticipantsDialogAdapter() {
        contacts = new ArrayList<>();
    }

    public MeetingParticipantsDialogAdapter(List<FriendsPostBody.Friend> contacts) {
        this.contacts = contacts;
    }

    @Override
    public ParticipantsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_participants, parent, false);
        ParticipantsViewHolder viewHolder = new ParticipantsViewHolder(v);
        return viewHolder;
    }

    public List<FriendsPostBody.Friend> getContacts() {
        return contacts;
    }

    public void setContacts(List<FriendsPostBody.Friend> contacts) {
        if (contacts != null) {
            this.contacts = contacts;
        } else {
            this.contacts = new ArrayList<>();
        }
    }

    public void setOnMeetingParticipantClick(MeetingParticipantClickListener callback) {

        Log.i(TAG, "setOnMeetingParticipantClick");
        this.callback = callback;
    }

    @Override
    public void onBindViewHolder(ParticipantsViewHolder holder, final int position) {
        holder.name.setText(contacts.get(position).getName());
        holder.iconText.setText(contacts.get(position).getInitials());
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onClick(contacts.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public interface MeetingParticipantClickListener {
        void onClick(FriendsPostBody.Friend friend);
    }
}
