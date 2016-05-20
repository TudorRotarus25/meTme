package com.tudor.rotarus.unibuc.metme.views.adapters;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tudor.rotarus.unibuc.metme.R;
import com.tudor.rotarus.unibuc.metme.pojos.responses.post.FriendsPostBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tudor on 04.04.2016.
 */
public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.FriendsViewHolder> {

    private List<FriendsPostBody.Friend> contacts;

    public static class FriendsViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private ImageView icon;
        private TextView iconText;
        private TextView name;
        private FloatingActionButton pickMe;

        public FriendsViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.list_friends_cardView);
            icon = (ImageView) itemView.findViewById(R.id.list_friends_icon);
            iconText = (TextView) itemView.findViewById(R.id.list_friends_icon_inner_text);
            name = (TextView) itemView.findViewById(R.id.list_friends_name_textView);
            pickMe = (FloatingActionButton) itemView.findViewById(R.id.list_friends_pickup_button);
        }
    }

    public FriendsListAdapter(List<FriendsPostBody.Friend> contacts) {
        this.contacts = contacts;
    }

    @Override
    public FriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_friends, parent, false);
        FriendsViewHolder viewHolder = new FriendsViewHolder(v);
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

    @Override
    public void onBindViewHolder(FriendsViewHolder holder, int position) {
        holder.name.setText(contacts.get(position).getName());
        holder.iconText.setText(contacts.get(position).getInitials());
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
