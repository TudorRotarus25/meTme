package com.tudor.rotarus.unibuc.metme.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tudor.rotarus.unibuc.metme.R;

public class FriendsFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_friends, container, false);

        init(v);

        return v;
    }

    private void init(View v) {

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Friends");

    }

}
