package com.tudor.rotarus.unibuc.metme.rest;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Tudor on 04.03.2016.
 */
public class RestClient {

    private final String TAG = getClass().getSimpleName();

    private static final String BASE_URL = "http://46.101.162.51/api/v2/public/";
    private RestAPI apiService;

    public RestClient() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Response response = chain.proceed(chain.request());
                Log.i(TAG, response.code() + " - " + chain.request().url().toString());

                return response;
            }
        });
//        client.interceptors()
//                .add(new Interceptor() {
//                    @Override
//                    public Response intercept(Interceptor.Chain chain) throws IOException {
//
//                        Request request = chain.request();
//                        Log.i(TAG, request.body().toString());
//
//                        Response response = chain.proceed(chain.request());
//
//                        Log.i(TAG, response.code() + " - " + chain.request().urlString());
//
//                        return response;
//                    }
//                });
        client.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();

        apiService = retrofit.create(RestAPI.class);
    }

    public RestAPI getApiService() {
        return apiService;
    }
}
