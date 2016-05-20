package com.tudor.rotarus.unibuc.metme.managers;

import android.util.Log;

import com.tudor.rotarus.unibuc.metme.pojos.interfaces.network.MeetingDetailsListener;
import com.tudor.rotarus.unibuc.metme.pojos.requests.FriendsBody;
import com.tudor.rotarus.unibuc.metme.pojos.interfaces.network.ActivateUserListener;
import com.tudor.rotarus.unibuc.metme.pojos.interfaces.network.CountriesListener;
import com.tudor.rotarus.unibuc.metme.pojos.interfaces.network.CreateMeetingListener;
import com.tudor.rotarus.unibuc.metme.pojos.interfaces.network.FriendsListListener;
import com.tudor.rotarus.unibuc.metme.pojos.interfaces.network.LoginListener;
import com.tudor.rotarus.unibuc.metme.pojos.interfaces.network.MeetingListListener;
import com.tudor.rotarus.unibuc.metme.pojos.interfaces.network.RefreshGcmTokenListener;
import com.tudor.rotarus.unibuc.metme.pojos.responses.get.CountryGetBody;
import com.tudor.rotarus.unibuc.metme.pojos.responses.get.MeetingGetBody;
import com.tudor.rotarus.unibuc.metme.pojos.responses.get.MeetingsListGetBody;
import com.tudor.rotarus.unibuc.metme.pojos.responses.post.ActivateUserPostBody;
import com.tudor.rotarus.unibuc.metme.pojos.responses.post.CreateUserPostBody;
import com.tudor.rotarus.unibuc.metme.pojos.responses.post.FriendsPostBody;
import com.tudor.rotarus.unibuc.metme.rest.RestAPI;
import com.tudor.rotarus.unibuc.metme.rest.RestClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        Call<CreateUserPostBody> userPostCall = requestAPI.createUser(phoneNumber, firstName, lastName);
        userPostCall.enqueue(new Callback<CreateUserPostBody>() {
            @Override
            public void onResponse(Call<CreateUserPostBody> call, Response<CreateUserPostBody> response) {
                if (response != null) {
                    if (response.body() != null && response.code() == 200) {

                        callback.onLoginSuccess(response.body());

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
            public void onFailure(Call<CreateUserPostBody> call, Throwable t) {
                callback.onLoginFailed();
                Log.e(TAG, "Login failed");
            }
        });
    }

    public void populateCountryField(String locale, final CountriesListener callback) {
        Call<CountryGetBody> getCountryDetailsCall = requestAPI.getCountries(locale);
        getCountryDetailsCall.enqueue(new Callback<CountryGetBody>() {
            @Override
            public void onResponse(Call<CountryGetBody> call, Response<CountryGetBody> response) {
                if (response.body() != null && response.code() == 200) {
                    callback.onCountryPopulateSuccess(response.body());
                } else {
                    callback.onCountryPopulateFailure();
                    Log.e(TAG, "Country population error");
                }
            }

            @Override
            public void onFailure(Call<CountryGetBody> call, Throwable t) {
                callback.onCountryPopulateFailure();
                Log.e(TAG, "Country population failed: " + t.getMessage());
            }
        });
    }

    public void activateUser(int userId, String code, final ActivateUserListener callback) {
        Call<ActivateUserPostBody> call = requestAPI.activateUser(userId, code);
        call.enqueue(new Callback<ActivateUserPostBody>() {
            @Override
            public void onResponse(Call<ActivateUserPostBody> call, Response<ActivateUserPostBody> response) {
                if (response != null) {
                    if (response.code() == 200) {
                        if (response.body() != null) {
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
            public void onFailure(Call<ActivateUserPostBody> call, Throwable t) {
                callback.onActivateUserFailed();
                Log.e(TAG, "Activate call failed - " + t.getMessage());
            }
        });
    }

    public void listFriends(FriendsBody contacts, final FriendsListListener callback) {
        Call<FriendsPostBody> call = requestAPI.getFriends(contacts);
        call.enqueue(new Callback<FriendsPostBody>() {
            @Override
            public void onResponse(Call<FriendsPostBody> call, Response<FriendsPostBody> response) {
                if (response != null && response.body() != null && response.code() == 200) {
                    callback.onFriendsListSuccess(response.body());
                } else {
                    Log.e(TAG, "List friends failed: " + (response != null ? response.message() : null));
                    callback.onFriendsListFailed();
                }
            }

            @Override
            public void onFailure(Call<FriendsPostBody> call, Throwable t) {
                Log.e(TAG, "List friends failed: " + t.getMessage());
                callback.onFriendsListFailed();
            }
        });
    }

    public void createMeeting(String name, String fromTime, String toTime, int notifyTime, Double locationLat, Double locationLon, String locationName, String locationAddress, int transportMethod, int authorId, int meetingType, ArrayList<Integer> members, final CreateMeetingListener callback) {
        // TODO: add members, hardcoded type
        Call<Void> createMeetingCall = requestAPI.createMeeting(name, fromTime, toTime, notifyTime, locationLat, locationLon, locationName, locationAddress, transportMethod, authorId, meetingType, members);
        createMeetingCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response != null && response.code() == 200) {
                    callback.onCreateMeetingSuccess();
                } else {
                    Log.e(TAG, "Create meeting failed");
                    callback.onCreateMeetingFailed();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, t.getMessage().toString());
                callback.onCreateMeetingFailed();
            }
        });
    }

    public void getNextMeetingDetails(int userId, final MeetingDetailsListener callback) {
        Call<MeetingGetBody> call = requestAPI.getNextMeetingDetails(userId);
        call.enqueue(new Callback<MeetingGetBody>() {
            @Override
            public void onResponse(Call<MeetingGetBody> call, Response<MeetingGetBody> response) {
                if (response != null && response.body() != null && response.code() == 200) {
                    callback.onFetchMeetingDetailsSuccess(response.body());
                } else if (response != null && response.code() == 204) {
                    callback.onFetchMeetingDetailsEmptyResponse();
                } else {
                    Log.e(TAG, "getNextMeetingDetails failed: " + response.code());
                    callback.onFetchMeetingDetailsFailed();

                }
            }

            @Override
            public void onFailure(Call<MeetingGetBody> call, Throwable t) {
                Log.e(TAG, "getNextMeetingDetails failed: " + t.getMessage());
                callback.onFetchMeetingDetailsFailed();
            }
        });
    }

    public void getMeetingDetails(int meetingId, final MeetingDetailsListener callback) {
        Call<MeetingGetBody> call = requestAPI.getMeetingDetails(meetingId);
        call.enqueue(new Callback<MeetingGetBody>() {
            @Override
            public void onResponse(Call<MeetingGetBody> call, Response<MeetingGetBody> response) {
                if (response != null && response.body() != null && response.code() == 200) {
                    callback.onFetchMeetingDetailsSuccess(response.body());
                } else {
                    Log.e(TAG, "getNextMeetingDetails failed: " + response.code());
                    callback.onFetchMeetingDetailsFailed();
                }
            }

            @Override
            public void onFailure(Call<MeetingGetBody> call, Throwable t) {
                Log.e(TAG, "getNextMeetingDetails failed: " + t.getMessage());
                callback.onFetchMeetingDetailsFailed();
            }
        });
    }

    public void listAllMeetings(int userId, final MeetingListListener callback) {
        Call<MeetingsListGetBody> call = requestAPI.getAllMeetings(userId);
        call.enqueue(new Callback<MeetingsListGetBody>() {
            @Override
            public void onResponse(Call<MeetingsListGetBody> call, Response<MeetingsListGetBody> response) {
                if (response != null && response.code() == 200 && response.body() != null) {

                    callback.onListAllMeetingsSuccess(response.body());

                } else {
                    callback.onListAllMeetingsFailed();
                    Log.e(TAG, response.message().toString());
                }
            }

            @Override
            public void onFailure(Call<MeetingsListGetBody> call, Throwable t) {
                callback.onListAllMeetingsFailed();
                Log.e(TAG, t.getMessage());
            }
        });
    }

    public void refreshGcmToken(int userId, String token, final RefreshGcmTokenListener callback) {
        Call<Void> call = requestAPI.refreshGcmToken(userId, token);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response != null && response.code() == 200) {
                    callback.onTokenRefreshSuccess();
                } else {
                    callback.onTokenRefreshFailed();
                    Log.e(TAG, "Refresh gcm token failed: " + response.code() + " " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onTokenRefreshFailed();
                Log.e(TAG, "Refresh gcm token failed: " + t.getMessage());
            }
        });
    }
}
