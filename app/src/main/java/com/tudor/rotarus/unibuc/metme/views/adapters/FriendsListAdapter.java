package com.tudor.rotarus.unibuc.metme.views.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tudor.rotarus.unibuc.metme.R;
import com.tudor.rotarus.unibuc.metme.pojos.FriendsBody;

import java.util.List;

/**
 * Created by Tudor on 04.04.2016.
 */
public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.FriendsViewHolder> {

    private List<FriendsBody> contacts;

    public static class FriendsViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private ImageView icon;
        private TextView name;
        private Button pickMe;

        public FriendsViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.list_friends_cardView);
            icon = (ImageView) itemView.findViewById(R.id.list_friends_icon);
            name = (TextView) itemView.findViewById(R.id.list_friends_name_textView);
            pickMe = (Button) itemView.findViewById(R.id.list_friends_pickup_button);
        }
    }

    public FriendsListAdapter(List<FriendsBody> contacts) {
        this.contacts = contacts;
    }

    @Override
    public FriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_friends, parent, false);
        FriendsViewHolder viewHolder = new FriendsViewHolder(v);
        return viewHolder;
    }

    public List<FriendsBody> getContacts() {
        return contacts;
    }

    public void setContacts(List<FriendsBody> contacts) {
        this.contacts = contacts;
    }

    @Override
    public void onBindViewHolder(FriendsViewHolder holder, int position) {
        holder.name.setText(contacts.get(position).getName());
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
