package com.tudor.rotarus.unibuc.metme.rest;

import android.util.Log;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;

import java.io.IOException;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Tudor on 04.03.2016.
 */
public class RestClient {

    private static final String BASE_URL = "http://46.101.162.51/api/v2/public/";
    private RestAPI apiService;

    public RestClient() {

        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());

                Log.i("Request", response.code() + " - " + chain.request().urlString());

                return response;
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        apiService = retrofit.create(RestAPI.class);
    }

    public RestAPI getApiService() {
        return apiService;
    }
}
