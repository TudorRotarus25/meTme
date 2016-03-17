package com.tudor.rotarus.unibuc.metme.rest;

import com.tudor.rotarus.unibuc.metme.pojos.post.ActivateUserPostBody;
import com.tudor.rotarus.unibuc.metme.pojos.get.CountryGetBody;
import com.tudor.rotarus.unibuc.metme.pojos.get.TestGetBody;

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

    @GET("test.php")
    Call<TestGetBody> TEST_GET_BODY_CALL();

    @GET("countries.php")
    Call<CountryGetBody> COUNTRY_GET_BODY_CALL(@Query("iso") String iso);

    @FormUrlEncoded
    @POST("users.php")
    Call<Void> USER_POST_BODY_CALL(@Field("phone_number") String phoneNumber, @Field("first_name") String firstName, @Field("last_name") String lastName);

    @FormUrlEncoded
    @POST("activate_user.php")
    Call<ActivateUserPostBody> ACTIVATE_USER_POST_BODY(@Field("phone_number") String phoneNumber, @Field("sms_code") String smsCode);
}
