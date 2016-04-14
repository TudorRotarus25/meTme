package com.tudor.rotarus.unibuc.metme.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.criapp.circleprogresscustomview.CircleProgressView;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.tudor.rotarus.unibuc.metme.R;

public class HomeFragment extends Fragment {

    private ArcProgress arcProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        init(view);

        return view;
    }

    private void init(View view) {

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Next meeting");

        arcProgress = (ArcProgress) view.findViewById(R.id.fragment_home_progress_bar);
        arcProgress.setProgress(65);
        arcProgress.setBottomText("ETA");
        arcProgress.setSuffixText("m");
//        arcProgress.setDrawingCacheBackgroundColor(Color.WHITE);
//        arcProgress.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//        arcProgress.setTextColor(R.color.fragment_home_top_text_color);
//        arcProgress.setFinishedStrokeColor(R.color.colorAccent);
//        arcProgress.setUnfinishedStrokeColor(R.color.fragment_home_top_text_color);

    }

}
