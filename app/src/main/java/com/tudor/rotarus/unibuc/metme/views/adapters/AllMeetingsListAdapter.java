package com.tudor.rotarus.unibuc.metme.views.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tudor.rotarus.unibuc.metme.R;
import com.tudor.rotarus.unibuc.metme.pojos.requests.get.MeetingsListGetBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Tudor on 27.03.2016.
 */
public class AllMeetingsListAdapter extends RecyclerView.Adapter<AllMeetingsListAdapter.MeetingViewHolder> {

    private List<MeetingsListGetBody.Meeting> meetings;

    private SimpleDateFormat fromResponse = new SimpleDateFormat("yyyy-MM-dd H:mm:ss", Locale.US);
    private SimpleDateFormat toDate = new SimpleDateFormat("EEE, dd MMM yyyy", Locale.US);
    private SimpleDateFormat toTimeHour = new SimpleDateFormat("HH", Locale.US);
    private SimpleDateFormat toTimeMin = new SimpleDateFormat("mm", Locale.US);

    public static class MeetingViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView name;
        TextView place;
        TextView date;
        TextView timeHour;
        TextView timeMin;

        public MeetingViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.list_all_meetings_cardView);
            name = (TextView) itemView.findViewById(R.id.list_all_meetings_name_textView);
            place = (TextView) itemView.findViewById(R.id.list_all_meetings_place_textView);
            date = (TextView) itemView.findViewById(R.id.list_all_meetings_date_textView);
            timeHour = (TextView) itemView.findViewById(R.id.list_all_meetings_time_hour_textView);
            timeMin = (TextView) itemView.findViewById(R.id.list_all_meetings_time_min_textView);
        }
    }

    public AllMeetingsListAdapter(List<MeetingsListGetBody.Meeting> meetings) {
        this.meetings = meetings;
    }

    @Override
    public MeetingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_all_meetings, parent, false);
        MeetingViewHolder viewHolder = new MeetingViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MeetingViewHolder holder, int position) {
        holder.name.setText(meetings.get(position).getName());
        holder.place.setText("at " + meetings.get(position).getPlaceName());

        try {

            Calendar fromTime = Calendar.getInstance();
            fromTime.setTime(fromResponse.parse(meetings.get(position).getFromTime()));

            holder.date.setText(toDate.format(fromTime.getTime()));
            holder.timeHour.setText(toTimeHour.format(fromTime.getTime()));
            holder.timeMin.setText(toTimeMin.format(fromTime.getTime()));

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return meetings.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
