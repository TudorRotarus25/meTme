package com.tudor.rotarus.unibuc.metme.rest;

import com.tudor.rotarus.unibuc.metme.pojos.requests.FriendsBody;
import com.tudor.rotarus.unibuc.metme.pojos.responses.get.CountryGetBody;
import com.tudor.rotarus.unibuc.metme.pojos.responses.get.MeetingsListGetBody;
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
    Call<CountryGetBody> COUNTRY_GET_CALL(@Path("iso") String iso);

    @FormUrlEncoded
    @POST("users/create")
    Call<CreateUserPostBody> USER_POST_CALL(@Field("phone_number") String phoneNumber, @Field("first_name") String firstName, @Field("last_name") String lastName);

    @FormUrlEncoded
    @POST("users/activate")
    Call<ActivateUserPostBody> ACTIVATE_USER_POST_CALL(@Field("user_id") int userId, @Field("sms_code") String smsCode);

    @POST("users/friends")
    Call<FriendsPostBody> FRIENDS_LIST_POST_CALL(@Body FriendsBody body);

    @FormUrlEncoded
    @POST("meetings/create")
    Call<Void> MEETING_POST_CALL(@Field("name") String name, @Field("from_time") String fromTime, @Field("to_time") String toTime, @Field("notify_time") int notifyTime, @Field("place_lat") Double placeLat, @Field("place_lon") Double placeLon, @Field("place_name") String placeName, @Field("place_address") String placeAddress, @Field("transportation_method") int transportationMethod, @Field("author_id") int authorId, @Field("type") int type, @Field("members") ArrayList<Integer> members);

    @GET("meetings/all/{user_id}")
    Call<MeetingsListGetBody> MEETINGS_LIST_GET_CALL(@Path("user_id") int userId);

    @FormUrlEncoded
    @POST("gcm/refresh")
    Call<Void> GCM_REFRESH_TOKEN_POST_CALL(@Field("user_id") int userId, @Field("gcm_token") String gcmToken);

}
