package com.tudor.rotarus.unibuc.metme.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tudor.rotarus.unibuc.metme.R;
import com.tudor.rotarus.unibuc.metme.pojos.requests.get.MeetingGetBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tudor on 11.04.2016.
 */
public class HomeParticipantsListAdapter extends RecyclerView.Adapter<HomeParticipantsListAdapter.ParticipantsViewHolder>{

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
        this.participants = participants;
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
        holder.etaTextView.setText(participants.get(position).getEta());
    }

    @Override
    public int getItemCount() {
        return participants.size();
    }

    public class ParticipantsViewHolder extends RecyclerView.ViewHolder {
        
        private ImageView icon;
        private TextView nameTextView;
        private TextView etaTextView;

        public ParticipantsViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.list_home_participants_icon);
            nameTextView = (TextView) itemView.findViewById(R.id.list_home_participants_name_textView);
            etaTextView = (TextView) itemView.findViewById(R.id.list_home_participants_eta_textView);
        }
    }
}