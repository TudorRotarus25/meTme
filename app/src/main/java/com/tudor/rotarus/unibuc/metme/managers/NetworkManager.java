package com.tudor.rotarus.unibuc.metme.managers;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.tudor.rotarus.unibuc.metme.MyApplication;
import com.tudor.rotarus.unibuc.metme.activities.NavigationDrawerActivity;
import com.tudor.rotarus.unibuc.metme.activities.login.LoginConfirmActivity;
import com.tudor.rotarus.unibuc.metme.pojos.interfaces.ActivateUserListener;
import com.tudor.rotarus.unibuc.metme.pojos.interfaces.CountriesListener;
import com.tudor.rotarus.unibuc.metme.pojos.interfaces.CreateMeetingListener;
import com.tudor.rotarus.unibuc.metme.pojos.interfaces.LoginListener;
import com.tudor.rotarus.unibuc.metme.pojos.interfaces.MeetingListListener;
import com.tudor.rotarus.unibuc.metme.pojos.requests.get.CountryGetBody;
import com.tudor.rotarus.unibuc.metme.pojos.requests.get.MeetingsListGetBody;
import com.tudor.rotarus.unibuc.metme.pojos.requests.post.ActivateUserPostBody;
import com.tudor.rotarus.unibuc.metme.rest.RestAPI;
import com.tudor.rotarus.unibuc.metme.rest.RestClient;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Tudor on 30.03.2016.
 */
public class NetworkManager {

    private final String TAG = getClass().getSimpleName();

    private static NetworkManager instance = null;
    private RestAPI requestAPI;

    public NetworkManager() {
    }

    public static NetworkManager getInstance() {
        if (instance == null) {
            instance = new NetworkManager();
            RestClient restClient = new RestClient();
            instance.requestAPI = restClient.getApiService();
        }
        return instance;
    }

    public void login(String phoneNumber, String firstName, String lastName, final LoginListener callback) {
        Call<Void> userPostCall = requestAPI.USER_POST_BODY_CALL(phoneNumber, firstName, lastName);
        userPostCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Response<Void> response, Retrofit retrofit) {
                if(response != null){
                    if(response.code() == 200) {

                        callback.onLoginSuccess();

                    } else {
                        Log.e(TAG, response.code() + " - " + response.message());
                        callback.onLoginFailed();
                    }
                } else {
                    callback.onLoginFailed();
                    Log.e(TAG, "Login Empty response");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                callback.onLoginFailed();
                Log.e(TAG, "Login failed");
            }
        });
    }

    public void populateCoutryField(String locale, final CountriesListener callback) {
        Call<CountryGetBody> getCountryDetailsCall = requestAPI.COUNTRY_GET_BODY_CALL(locale);
        getCountryDetailsCall.enqueue(new Callback<CountryGetBody>() {
            @Override
            public void onResponse(Response<CountryGetBody> response, Retrofit retrofit) {
                if(response.body() != null && response.code() == 200){
                    callback.onCountryPopulateSuccess(response.body());
                } else {
                    callback.onCountryPopulateFailure();
                    Log.e(TAG, "Country population error");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                callback.onCountryPopulateFailure();
                Log.e(TAG, "Country population failed: " + t.getMessage());
            }
        });
    }

    public void activateUser(String phoneNumber, String code, final ActivateUserListener callback) {
        Call<ActivateUserPostBody> call = requestAPI.ACTIVATE_USER_POST_BODY(phoneNumber, code);
        call.enqueue(new Callback<ActivateUserPostBody>() {
            @Override
            public void onResponse(Response<ActivateUserPostBody> response, Retrofit retrofit) {
                if (response != null) {
                    if (response.code() == 200) {
                        if(response.body() != null){
                            callback.onActivateUserSuccess(response.body());

                        } else {
                            callback.onActivateUserFailed();
                        }
                    } else {
                        callback.onActivateUserFailed();
                    }
                } else {
                    callback.onActivateUserFailed();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                callback.onActivateUserFailed();
                Log.e(TAG, "Activate call failed - " + t.getMessage());
            }
        });
    }

    public void createMeeting(String name, String fromTime, String toTime, int notifyTime, Double locationLat, Double locationLon, String locationName, String locationAddress, int transportMethod, String phoneNumber, int meetingType, ArrayList<Integer> members, final CreateMeetingListener callback) {
        // TODO: add members, hardcoded type
        Call<Void> createMeetingCall = requestAPI.MEETING_POST_BODY_CALL(name, fromTime, toTime, notifyTime, locationLat, locationLon, locationName, locationAddress, transportMethod, phoneNumber, meetingType, members);
        createMeetingCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Response<Void> response, Retrofit retrofit) {
                if(response != null && response.code() == 200) {
                    callback.onCreateMeetingSuccess();
                } else {
                    Log.e(TAG, "Create meeting failed");
                    callback.onCreateMeetingFailed();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.getMessage().toString());
                callback.onCreateMeetingFailed();
            }
        });
    }

    public void listAllMeetings(String phoneNumber, final int callType, final MeetingListListener callback) {
        Call<MeetingsListGetBody> call = requestAPI.MEETINGS_LIST_GET_BODY_CALL(phoneNumber);
        call.enqueue(new Callback<MeetingsListGetBody>() {
            @Override
            public void onResponse(Response<MeetingsListGetBody> response, Retrofit retrofit) {
                if (response != null && response.code() == 200 && response.body() != null) {

                    callback.onListAllMeetingsSuccess(response.body(), callType);

                } else {
                    callback.onListAllMeetingsFailed(callType);
                    Log.e(TAG, response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                callback.onListAllMeetingsFailed(callType);
                Log.e(TAG, t.getMessage());
            }
        });
    }
}
