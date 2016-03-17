package com.tudor.rotarus.unibuc.metme.activities.login;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tudor.rotarus.unibuc.metme.MyApplication;
import com.tudor.rotarus.unibuc.metme.R;
import com.tudor.rotarus.unibuc.metme.activities.NavigationDrawerActivity;
import com.tudor.rotarus.unibuc.metme.pojos.post.ActivateUserPostBody;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class LoginConfirmActivity extends AppCompatActivity {

    EditText codeEditText;
    Button continueButton;

    MyApplication app;

    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_confirm);

        initLayout();
    }

    private void initLayout() {
        codeEditText = (EditText) findViewById(R.id.activity_login_confirm_editText_code);
        continueButton = (Button) findViewById(R.id.activity_login_confirm_button_ok);

        phoneNumber = getIntent().getStringExtra(LoginActivity.LOGIN_EXTRA_PHONE_NUMBER);

        app = (MyApplication) getApplication();

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = codeEditText.getText().toString();
                if (!code.isEmpty()) {
                    Call<ActivateUserPostBody> call = app.getRestClient().getApiService().ACTIVATE_USER_POST_BODY(phoneNumber, code);
                    call.enqueue(new Callback<ActivateUserPostBody>() {
                        @Override
                        public void onResponse(Response<ActivateUserPostBody> response, Retrofit retrofit) {
                            if (response != null) {
                                if (response.code() == 200) {
                                    if(response.body() != null){
                                        writeTokenToSharedPreferences(response.body().getId(), response.body().getToken());
                                        Intent intent = new Intent(LoginConfirmActivity.this, NavigationDrawerActivity.class);
                                        startActivity(intent);

                                    } else {
                                        Log.e("BE error", "200 code, but empty response");
                                    }
                                } else {
                                    Log.e("BE error", response.code() + " - " + response.message());
                                }
                            } else {
                                Log.e("BE error", "Empty response");
                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            Log.e("BE error", "Call failed - " + t.getMessage());
                        }
                    });
                } else {
                    codeEditText.setError("Code required");
                }
            }
        });
    }

    private void writeTokenToSharedPreferences(int id, String token) {
        SharedPreferences.Editor editor = getSharedPreferences(MyApplication.METME_SHARED_PREFERENCES, MODE_PRIVATE).edit();
        editor.putInt("id", id);
        editor.putString("token", token);
        editor.commit();
    }
}
