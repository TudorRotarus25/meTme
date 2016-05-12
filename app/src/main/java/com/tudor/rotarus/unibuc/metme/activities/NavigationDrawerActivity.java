package com.tudor.rotarus.unibuc.metme.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.tudor.rotarus.unibuc.metme.MyApplication;
import com.tudor.rotarus.unibuc.metme.R;
import com.tudor.rotarus.unibuc.metme.activities.login.LoginNameActivity;
import com.tudor.rotarus.unibuc.metme.fragments.AllMeetingsFragment;
import com.tudor.rotarus.unibuc.metme.fragments.CalendarFragment;
import com.tudor.rotarus.unibuc.metme.fragments.FriendsFragment;
import com.tudor.rotarus.unibuc.metme.fragments.HomeFragment;
import com.tudor.rotarus.unibuc.metme.managers.SharedPreferencesManager;

import static android.support.v4.app.ActivityCompat.requestPermissions;

public class NavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "NavigationDrawerActivity";

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    private SharedPreferencesManager sharedPreferencesManager;

    private FloatingActionMenu fam;
    private FloatingActionButton createMeetingFab;
    private FloatingActionButton createPickupFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkAuthentication();
        checkPermissions();
        initLayout();

    }

    private void checkPermissions() {
        //read contacts permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            ((MyApplication) getApplication()).refreshFriendList();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                checkPermissions();
            } else {
                Toast.makeText(this, "Until you grant the permission, we cannot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkAuthentication() {
        SharedPreferencesManager sharedPreferencesManager = SharedPreferencesManager.getInstance();
        String token = sharedPreferencesManager.readToken(this);
        if(token != null && !token.equals("")) {
            Log.i(TAG, token);
        } else {
            Intent intent = new Intent(NavigationDrawerActivity.this, LoginNameActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void initLayout() {
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fam = (FloatingActionMenu) findViewById(R.id.fab);
        createMeetingFab = (FloatingActionButton) findViewById(R.id.fab_add_meeting);
        createPickupFab = (FloatingActionButton) findViewById(R.id.fab_add_pickup);

        createMeetingFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        sharedPreferencesManager = SharedPreferencesManager.getInstance();
        TextView drawerTitleTextView = (TextView) header.findViewById(R.id.drawer_nav_header_title);
        drawerTitleTextView.setText(sharedPreferencesManager.readFirstName(this) + " " + sharedPreferencesManager.readLastName(this));
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

        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.action_profile:
                intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                break;
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

        switch (viewId) {
            case R.id.nav_home:
                fragment = new HomeFragment();
                break;
            case R.id.nav_all_meetings:
                fragment = new AllMeetingsFragment();
                break;
            case R.id.nav_calendar:
                fragment = new CalendarFragment();
                break;
            case R.id.nav_friends:
                fragment = new FriendsFragment();
                break;
        }

        if(fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.navigation_drawer_fragment_container, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
}
