package com.tudor.rotarus.unibuc.metme.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.criapp.circleprogresscustomview.CircleProgressView;
import com.tudor.rotarus.unibuc.metme.R;
import com.tudor.rotarus.unibuc.metme.pojos.responses.get.MeetingGetBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tudor on 11.04.2016.
 */
public class HomeParticipantsListAdapter extends RecyclerView.Adapter<HomeParticipantsListAdapter.ParticipantsViewHolder> {

    private List<MeetingGetBody.Participant> participants;

    public HomeParticipantsListAdapter() {
        this.participants = new ArrayList<>();
    }

    public HomeParticipantsListAdapter(List<MeetingGetBody.Participant> participants) {
        this.participants = participants;
    }

    public List<MeetingGetBody.Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<MeetingGetBody.Participant> participants) {
        if (participants != null) {
            this.participants = participants;
        } else {
            this.participants = new ArrayList<>();
        }
    }

    @Override
    public ParticipantsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_home_participants, parent, false);
        ParticipantsViewHolder viewHolder = new ParticipantsViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ParticipantsViewHolder holder, int position) {
        holder.nameTextView.setText(participants.get(position).getName());
        holder.initialsTextView.setText(participants.get(position).getInitials());
        if (participants.get(position).getEta() != null) {
            holder.etaTextView.setText(participants.get(position).getEta() + " min");
            holder.icon.setProgress((participants.get(position).getEta() * 6));
        }
    }

    @Override
    public int getItemCount() {
        return participants.size();
    }

    public class ParticipantsViewHolder extends RecyclerView.ViewHolder {

        private CircleProgressView icon;
        private TextView nameTextView;
        private TextView etaTextView;
        private TextView initialsTextView;

        public ParticipantsViewHolder(View itemView) {
            super(itemView);
            icon = (CircleProgressView) itemView.findViewById(R.id.list_home_participants_progress);
            nameTextView = (TextView) itemView.findViewById(R.id.list_home_participants_name_textView);
            etaTextView = (TextView) itemView.findViewById(R.id.list_home_participants_eta_textView);
            initialsTextView = (TextView) itemView.findViewById(R.id.list_home_participants_progress_textView);
        }
    }
}
