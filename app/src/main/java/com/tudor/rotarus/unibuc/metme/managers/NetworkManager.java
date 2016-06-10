package com.tudor.rotarus.unibuc.metme.managers;

import android.util.Log;

import com.tudor.rotarus.unibuc.metme.pojos.interfaces.network.AcceptMeetingListener;
import com.tudor.rotarus.unibuc.metme.pojos.interfaces.network.CancelMeetingListener;
import com.tudor.rotarus.unibuc.metme.pojos.interfaces.network.DeleteMeetingListener;
import com.tudor.rotarus.unibuc.metme.pojos.interfaces.network.EditUserListener;
import com.tudor.rotarus.unibuc.metme.pojos.interfaces.network.FinishMeetingListener;
import com.tudor.rotarus.unibuc.metme.pojos.interfaces.network.GetProfileListener;
import com.tudor.rotarus.unibuc.metme.pojos.interfaces.network.MeetingDetailsListener;
import com.tudor.rotarus.unibuc.metme.pojos.interfaces.network.RefreshLocationListener;
import com.tudor.rotarus.unibuc.metme.pojos.interfaces.network.UpdateTransportationListener;
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
import com.tudor.rotarus.unibuc.metme.pojos.responses.get.UserGetBody;
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

    public void getProfile(int userId, final GetProfileListener callback) {
        Call<UserGetBody> call = requestAPI.getProfile(userId);
        call.enqueue(new Callback<UserGetBody>() {
            @Override
            public void onResponse(Call<UserGetBody> call, Response<UserGetBody> response) {
                if(response != null && response.code() == 200 && response.body() != null) {
                    callback.onGetProfileSuccess(response.body());
                } else {
                    Log.e(TAG, "getProfile failed: " + (response != null ? response.message() : null));
                    callback.onGetProfileFailed();
                }
            }

            @Override
            public void onFailure(Call<UserGetBody> call, Throwable t) {
                Log.e(TAG, "getProfile failed: " + t.getMessage());
                callback.onGetProfileFailed();
            }
        });
    }

    public void editUser(int userId, String firstName, String lastName, final EditUserListener callback) {
        Call<Void> call = requestAPI.editUser(userId, firstName, lastName);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response != null && response.code() == 200) {
                    callback.onEditUserSuccess();
                } else {
                    Log.e(TAG, "editUser failed: " + (response != null ? response.message() : null));
                    callback.onEditUserFailed();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "editUser failed: " + t.getMessage());
                callback.onEditUserFailed();
            }
        });
    }

    public void createMeeting(String name, String fromTime, String toTime, int notifyTime, Double locationLat, Double locationLon, String locationName, String locationAddress, int transportMethod, int authorId, int meetingType, ArrayList<Integer> members, final CreateMeetingListener callback) {
        // TODO: add members, hardcoded type
        Call<Void> createMeetingCall = requestAPI.createMeeting(name, fromTime, toTime, notifyTime, locationLat, locationLon, locationName, locationAddress, transportMethod, authorId, meetingType, members.toString());
        createMeetingCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response != null && response.code() == 200) {
                    callback.onCreateMeetingSuccess();
                } else {
                    Log.e(TAG, "Create meeting failed: " + (response != null ? response.code() : "null response"));
                    callback.onCreateMeetingFailed();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                callback.onCreateMeetingFailed();
            }
        });
    }

    public void updateTransportation(int userId, int meetingId, int transportationMethod, final UpdateTransportationListener callback) {
        Call<Void> call = requestAPI.updateTransportation(userId, meetingId, transportationMethod);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response != null && response.code() == 200) {
                    callback.onUpdateTransportationSuccess();
                } else {
                    Log.e(TAG, "updateTransportation failed: " + (response != null ? response.code() : "null response"));
                    callback.onUpdateTransportationFailed();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "updateTransportation failed: " + t.getMessage());
                callback.onUpdateTransportationFailed();
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
                    Log.e(TAG, "getNextMeetingDetails failed: " + (response != null ? response.code() : "null response"));
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

    public void getMeetingDetails(int userId, int meetingId, final MeetingDetailsListener callback) {
        Call<MeetingGetBody> call = requestAPI.getMeetingDetails(userId, meetingId);
        call.enqueue(new Callback<MeetingGetBody>() {
            @Override
            public void onResponse(Call<MeetingGetBody> call, Response<MeetingGetBody> response) {
                if (response != null && response.body() != null && response.code() == 200) {
                    callback.onFetchMeetingDetailsSuccess(response.body());
                } else {
                    Log.e(TAG, "getNextMeetingDetails failed: " + (response != null ? response.code() : "null response"));
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
                    Log.e(TAG, "listAllMeetings failed: " + (response != null ? response.code() : "null response"));
                }
            }

            @Override
            public void onFailure(Call<MeetingsListGetBody> call, Throwable t) {
                callback.onListAllMeetingsFailed();
                Log.e(TAG, t.getMessage());
            }
        });
    }

    public void acceptMeeting(int userId, int meetingId, int transportationMethod, int notifyTime, final AcceptMeetingListener callback) {
        Call<Void> call = requestAPI.acceptMeeting(userId, meetingId, transportationMethod, notifyTime);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response != null && response.code() ==200) {
                    callback.onAcceptMeetingSuccess();
                } else {
                    callback.onAcceptMeetingFailed();
                    Log.e(TAG, "acceptMeeting failed: " + (response != null ? response.code() : "null response"));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onAcceptMeetingFailed();
                Log.e(TAG, "acceptMeeting failed: " + t.getMessage());
            }
        });
    }

    public void deleteMeeting(int userId, int  meetingId, final DeleteMeetingListener callback) {
        Call<Void> call = requestAPI.deleteMeeting(userId, meetingId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response != null && response.code() == 200) {
                    callback.onDeleteMeetingSuccess();
                } else {
                    callback.onDeleteMeetingFailed();
                    Log.e(TAG, "deleteMeeting failed: " + (response != null ? response.code() : "null response"));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onDeleteMeetingFailed();
                Log.e(TAG, "deleteMeeting failed: " + t.getMessage());
            }
        });
    }

    public void cancelMeeting(int userId, int  meetingId, final CancelMeetingListener callback) {
        Call<Void> call = requestAPI.cancelMeeting(userId, meetingId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response != null && response.code() == 200) {
                    callback.onCancelMeetingSuccess();
                } else {
                    callback.onCancelMeetingFailed();
                    Log.e(TAG, "cancelMeeting failed: " + (response != null ? response.code() : "null response"));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onCancelMeetingFailed();
                Log.e(TAG, "cancelMeeting failed: " + t.getMessage());
            }
        });
    }

    public void finishMeeting(int userId, int  meetingId, final FinishMeetingListener callback) {
        Call<Void> call = requestAPI.finishMeeting(userId, meetingId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response != null && response.code() == 200) {
                    callback.onFinishMeetingSuccess();
                } else {
                    callback.onFinishMeetingFailed();
                    Log.e(TAG, "finishMeeting failed: " + (response != null ? response.code() : "null response"));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFinishMeetingFailed();
                Log.e(TAG, "finishMeeting failed: " + t.getMessage());
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
                    Log.e(TAG, "Refresh gcm token failed: " + (response != null ? response.code() : "null response"));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onTokenRefreshFailed();
                Log.e(TAG, "Refresh gcm token failed: " + t.getMessage());
            }
        });
    }

    public void refreshLocation(int userId, int meetingId, Double lat, Double lon, final RefreshLocationListener callback) {
        Call<Void> call = requestAPI.refreshLocation(userId, meetingId, lat, lon);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response != null && response.code() == 200) {
                    callback.onRefreshLocationSuccess();
                } else {
                    Log.e(TAG, "refreshLocation failed: " + (response != null ? response.code() : "null response"));
                    callback.onRefreshLocationFailed();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "refreshLocation failed: " + t.getMessage());
                callback.onRefreshLocationFailed();
            }
        });
    }
}
