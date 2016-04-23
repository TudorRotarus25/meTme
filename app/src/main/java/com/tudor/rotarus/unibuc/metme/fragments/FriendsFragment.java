package com.tudor.rotarus.unibuc.metme.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tudor.rotarus.unibuc.metme.R;
import com.tudor.rotarus.unibuc.metme.managers.LocalDbManager;
import com.tudor.rotarus.unibuc.metme.pojos.interfaces.db.FriendsDbListener;
import com.tudor.rotarus.unibuc.metme.pojos.responses.post.FriendsPostBody;
import com.tudor.rotarus.unibuc.metme.utils.FriendsComparator;
import com.tudor.rotarus.unibuc.metme.views.adapters.FriendsListAdapter;

import java.util.ArrayList;
import java.util.Collections;

public class FriendsFragment extends Fragment implements SearchView.OnQueryTextListener, FriendsDbListener {

    private final String TAG = getClass().getSimpleName();

    private RecyclerView recyclerView;
    private FriendsListAdapter adapter;

    private ProgressDialog progressDialog;

    private ArrayList<FriendsPostBody.Friend> contactList;

    private LocalDbManager dbManager;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_search, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_friends, container, false);

        init(v);

        return v;
    }

    private void init(View v) {

        setHasOptionsMenu(true);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Friends");

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait while loading");
        progressDialog.show();

        recyclerView = (RecyclerView) v.findViewById(R.id.fragment_friends_recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        contactList = new ArrayList<>();
        adapter = new FriendsListAdapter(contactList);
        recyclerView.setAdapter(adapter);

        getActivity().findViewById(R.id.fab).setVisibility(View.VISIBLE);

        dbManager = LocalDbManager.getInstance();
        dbManager.getFriends(getActivity().getApplicationContext(), this);
    }

    private void populateList(ArrayList<FriendsPostBody.Friend> contacts) {

        Log.i(TAG, "" + contacts.size());

        contactList = contacts;
        Collections.sort(contactList, new FriendsComparator());

        progressDialog.dismiss();

        adapter.setContacts(contacts);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final ArrayList<FriendsPostBody.Friend> filteredContactList = filter(newText);
        adapter.setContacts(filteredContactList);
        adapter.notifyDataSetChanged();
        return true;
    }

    private ArrayList<FriendsPostBody.Friend> filter(String query) {

        ArrayList<FriendsPostBody.Friend> builder = new ArrayList<>();
        for (int i = 0 ; i < contactList.size() ; i++) {
            if(contactList.get(i).getName().toLowerCase().contains(query.toLowerCase())) {
                builder.add(contactList.get(i));
            }
        }

        return  builder;
    }


    @Override
    public void onFriendsDbGetSuccess(FriendsPostBody response) {
        populateList(response.getFriends());
    }

    @Override
    public void onFriendsDbGetFailed() {
        Toast.makeText(getActivity(), "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
    }
}
