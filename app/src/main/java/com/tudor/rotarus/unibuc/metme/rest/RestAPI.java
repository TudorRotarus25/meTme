package com.tudor.rotarus.unibuc.metme.rest;

import com.tudor.rotarus.unibuc.metme.pojos.requests.FriendsBody;
import com.tudor.rotarus.unibuc.metme.pojos.responses.get.CountryGetBody;
import com.tudor.rotarus.unibuc.metme.pojos.responses.get.MeetingGetBody;
import com.tudor.rotarus.unibuc.metme.pojos.responses.get.MeetingsListGetBody;
import com.tudor.rotarus.unibuc.metme.pojos.responses.get.UserGetBody;
import com.tudor.rotarus.unibuc.metme.pojos.responses.post.ActivateUserPostBody;
import com.tudor.rotarus.unibuc.metme.pojos.responses.post.CreateUserPostBody;
import com.tudor.rotarus.unibuc.metme.pojos.responses.post.FriendsPostBody;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Tudor on 04.03.2016.
 */
public interface RestAPI {

    @GET("countries/{iso}")
    Call<CountryGetBody> getCountries(@Path("iso") String iso);

    @FormUrlEncoded
    @POST("users/create")
    Call<CreateUserPostBody> createUser(@Field("phone_number") String phoneNumber, @Field("first_name") String firstName, @Field("last_name") String lastName);

    @FormUrlEncoded
    @POST("users/activate")
    Call<ActivateUserPostBody> activateUser(@Field("user_id") int userId, @Field("sms_code") String smsCode);

    @FormUrlEncoded
    @POST("users/edit")
    Call<Void> editUser(@Field("user_id") int userId, @Field("first_name") String firstName, @Field("last_name") String lastName);

    @GET("users/profile/{id}")
    Call<UserGetBody> getProfile(@Path("id") int id);

    @POST("users/friends")
    Call<FriendsPostBody> getFriends(@Body FriendsBody body);

    @FormUrlEncoded
    @POST("meetings/create")
    Call<Void> createMeeting(@Field("name") String name, @Field("from_time") String fromTime, @Field("to_time") String toTime, @Field("notify_time") int notifyTime, @Field("place_lat") Double placeLat, @Field("place_lon") Double placeLon, @Field("place_name") String placeName, @Field("place_address") String placeAddress, @Field("transportation_method") int transportationMethod, @Field("author_id") int authorId, @Field("type") int type, @Field("members") String members);

    @FormUrlEncoded
    @POST("meetings/update/transportation")
    Call<Void> updateTransportation(@Field("user_id") int userId, @Field("meeting_id") int meetingId, @Field("transportation_method") int transportationMethod);

    @GET("meetings/next/{user_id}")
    Call<MeetingGetBody> getNextMeetingDetails(@Path("user_id") int userId);

    @GET("meetings/details/{user_id}/{meeting_id}")
    Call<MeetingGetBody> getMeetingDetails(@Path("user_id") int userId, @Path("meeting_id") int meetingId);

    @GET("meetings/all/{user_id}")
    Call<MeetingsListGetBody> getAllMeetings(@Path("user_id") int userId);

    @FormUrlEncoded
    @POST("meetings/accept")
    Call<Void> acceptMeeting(@Field("user_id") int userId, @Field("meeting_id") int meetingId, @Field("transportation_method") int transportationMethod, @Field("notify_time") int notifyTime);

    @FormUrlEncoded
    @POST("meetings/delete")
    Call<Void> deleteMeeting(@Field("user_id") int userId, @Field("meeting_id") int meetingId);

    @FormUrlEncoded
    @POST("meetings/cancel")
    Call<Void> cancelMeeting(@Field("user_id") int userId, @Field("meeting_id") int meetingId);

    @FormUrlEncoded
    @POST("meetings/finish")
    Call<Void> finishMeeting(@Field("user_id") int userId, @Field("meeting_id") int meetingId);

    @FormUrlEncoded
    @POST("gcm/refresh")
    Call<Void> refreshGcmToken(@Field("user_id") int userId, @Field("gcm_token") String gcmToken);

    @FormUrlEncoded
    @POST("location/refresh")
    Call<Void> refreshLocation(@Field("user_id") int userId, @Field("meeting_id") int meetingId, @Field("lat") Double lat, @Field("lon") Double lon);
}
