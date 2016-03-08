package com.tudor.rotarus.unibuc.metme.rest;

import com.tudor.rotarus.unibuc.metme.pojos.CountryGetBody;
import com.tudor.rotarus.unibuc.metme.pojos.TestGetBody;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Tudor on 04.03.2016.
 */
public interface RestAPI {

    @GET("test.php")
    Call<TestGetBody> TEST_GET_BODY_CALL();

    @GET("countries.php")
    Call<CountryGetBody> COUNTRY_GET_BODY_CALL(@Query("iso") String iso);
}
