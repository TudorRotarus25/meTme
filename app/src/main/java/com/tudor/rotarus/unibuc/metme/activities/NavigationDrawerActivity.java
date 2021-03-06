package com.tudor.rotarus.unibuc.metme.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.tudor.rotarus.unibuc.metme.MyApplication;
import com.tudor.rotarus.unibuc.metme.R;
import com.tudor.rotarus.unibuc.metme.activities.login.LoginNameActivity;
import com.tudor.rotarus.unibuc.metme.fragments.AllMeetingsFragment;
import com.tudor.rotarus.unibuc.metme.fragments.FriendsFragment;
import com.tudor.rotarus.unibuc.metme.fragments.HomeFragment;
import com.tudor.rotarus.unibuc.metme.fragments.ProfileFragment;

public class NavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "NavigationDrawerAct";
    private MyApplication app = (MyApplication) getApplication();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkAuthentication();
        initLayout();

    }

    private void checkAuthentication() {
        SharedPreferences prefs = getSharedPreferences(MyApplication.METME_SHARED_PREFERENCES, MODE_PRIVATE);
        if(prefs.contains("token")) {
            Log.i(TAG, prefs.getString("first_name", ""));
        } else {
            Intent intent = new Intent(NavigationDrawerActivity.this, LoginNameActivity.class);
            startActivity(intent);
        }
    }

    private void initLayout() {
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NavigationDrawerActivity.this, AddMeetingActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
//        drawer.setDrawerTitle(Gravity.LEFT, prefs.getString("first_name", "") + " " + prefs.getString("last_name", ""));
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        displayView(R.id.nav_home);

        View header = navigationView.getHeaderView(0);

        SharedPreferences prefs = getSharedPreferences(app.METME_SHARED_PREFERENCES, MODE_PRIVATE);
        TextView drawerTitleTextView = (TextView) header.findViewById(R.id.drawer_nav_header_title);
        drawerTitleTextView.setText(prefs.getString("first_name", "") + " " + prefs.getString("last_name", ""));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        displayView(item.getItemId());
        return true;
    }

    public void displayView(int viewId) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);

        switch (viewId) {
            case R.id.nav_home:
                fragment = new HomeFragment();
                title = "Home";
                break;
            case R.id.nav_all_meetings:
                fragment = new AllMeetingsFragment();
                title = "All meetings";
                break;
            case R.id.nav_friends:
                fragment = new FriendsFragment();
                title = "Friends";
                break;
            case R.id.nav_profile:
                fragment = new ProfileFragment();
                title = "Profile";
                break;
            case R.id.nav_settings:

                break;
        }

        if(fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.navigation_drawer_fragment_container, fragment);
            ft.commit();
        }

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(title);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
}
