package com.tudor.rotarus.unibuc.metme.rest;

import com.tudor.rotarus.unibuc.metme.pojos.get.MeetingsListGetBody;
import com.tudor.rotarus.unibuc.metme.pojos.post.ActivateUserPostBody;
import com.tudor.rotarus.unibuc.metme.pojos.get.CountryGetBody;
import com.tudor.rotarus.unibuc.metme.pojos.get.TestGetBody;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by Tudor on 04.03.2016.
 */
public interface RestAPI {

    @GET("countries.php")
    Call<CountryGetBody> COUNTRY_GET_BODY_CALL(@Query("iso") String iso);

    @FormUrlEncoded
    @POST("users.php")
    Call<Void> USER_POST_BODY_CALL(@Field("phone_number") String phoneNumber, @Field("first_name") String firstName, @Field("last_name") String lastName);

    @FormUrlEncoded
    @POST("activate_user.php")
    Call<ActivateUserPostBody> ACTIVATE_USER_POST_BODY(@Field("phone_number") String phoneNumber, @Field("sms_code") String smsCode);

    @FormUrlEncoded
    @POST("meetings.php")
    Call<Void> MEETING_POST_BODY_CALL(@Field("name") String name, @Field("from_time") String fromTime, @Field("to_time") String toTime, @Field("notify_time") int notifyTime, @Field("place_lat") Double placeLat, @Field("place_lon") Double placeLon, @Field("place_name") String placeName, @Field("place_address") String placeAddress, @Field("transportation_method") int transportationMethod, @Field("author_phone_number") String authorPhoneNumber, @Field("type") int type, @Field("members") ArrayList<Integer> members);

    @GET("all_meetings.php")
    Call<MeetingsListGetBody> MEETINGS_LIST_GET_BODY_CALL(@Query("phone_number") String phoneNumber);

}
