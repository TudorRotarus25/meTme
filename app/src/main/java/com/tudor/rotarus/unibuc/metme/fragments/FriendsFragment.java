package com.tudor.rotarus.unibuc.metme.fragments;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
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

import com.tudor.rotarus.unibuc.metme.R;
import com.tudor.rotarus.unibuc.metme.pojos.FriendsBody;
import com.tudor.rotarus.unibuc.metme.views.adapters.AllMeetingsListAdapter;
import com.tudor.rotarus.unibuc.metme.views.adapters.FriendsListAdapter;

import java.util.ArrayList;
import java.util.List;

public class FriendsFragment extends Fragment implements SearchView.OnQueryTextListener {

    private final String TAG = getClass().getSimpleName();

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private FriendsListAdapter adapter;

    private ProgressDialog progressDialog;

    private ArrayList<FriendsBody> contactList;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_friends, menu);

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

        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.fragment_friends_swipeRefreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new ContactsTask().execute(null, null);
            }
        });

        recyclerView = (RecyclerView) v.findViewById(R.id.fragment_friends_recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        contactList = new ArrayList<>();
        adapter = new FriendsListAdapter(contactList);
        recyclerView.setAdapter(adapter);

        new ContactsTask().execute(null, null);
    }

    private void populateList(ArrayList<FriendsBody> contacts) {

        Log.i(TAG, "" + contacts.size());

        contactList = contacts;

        progressDialog.dismiss();
        refreshLayout.setRefreshing(false);

        adapter.setContacts(contacts);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final ArrayList<FriendsBody> filteredContactList = filter(newText);
        adapter.setContacts(filteredContactList);
        adapter.notifyDataSetChanged();
        return true;
    }

    private ArrayList<FriendsBody> filter(String query) {

        ArrayList<FriendsBody> builder = new ArrayList<>();
        for (int i = 0 ; i < contactList.size() ; i++) {
            if(contactList.get(i).getName().toLowerCase().contains(query.toLowerCase())) {
                builder.add(contactList.get(i));
            }
        }

        return  builder;
    }

    private class ContactsTask extends AsyncTask<Void, Void, ArrayList<FriendsBody>> {

        @Override
        protected ArrayList<FriendsBody> doInBackground(Void... params) {

            return fetchContacts();

        }

        @Override
        protected void onPostExecute(ArrayList<FriendsBody> friendsBodies) {
            super.onPostExecute(friendsBodies);

            populateList(friendsBodies);
        }
    }

    private ArrayList<FriendsBody> fetchContacts() {

        final Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        final String _ID = ContactsContract.Contacts._ID;
        final String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        final String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        final Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        final String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        final String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        ArrayList<FriendsBody> result = new ArrayList<>();

        Cursor cursor = getActivity().getContentResolver().query(CONTENT_URI, null, null, null, null);

        if (cursor.getCount() > 0) {

            while (cursor.moveToNext()) {

                String contactId = cursor.getString(cursor.getColumnIndex(_ID));
                String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));

                if (hasPhoneNumber > 0) {

                    FriendsBody newFriend = new FriendsBody(contactId, name);
                    Cursor phoneCursor = getActivity().getContentResolver().query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contactId}, null);
                    while (phoneCursor.moveToNext()) {
                        String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        if (phoneNumber != null) {
                            newFriend.addPhoneNumber(phoneNumber);
                        }
                    }
                    phoneCursor.close();
                    result.add(newFriend);
                }
            }
            cursor.close();
        }
        return result;
    }

}
