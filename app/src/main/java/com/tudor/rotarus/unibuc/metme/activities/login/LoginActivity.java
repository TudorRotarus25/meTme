package com.tudor.rotarus.unibuc.metme.activities.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tudor.rotarus.unibuc.metme.MyApplication;
import com.tudor.rotarus.unibuc.metme.R;
import com.tudor.rotarus.unibuc.metme.pojos.get.CountryGetBody;

import java.util.Locale;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class LoginActivity extends AppCompatActivity {

    EditText countryEditText;
    EditText prefixEditText;
    EditText phoneNumberEditText;
    Button continueButton;

    String firstName;
    String lastName;
    String phoneNumber;

    MyApplication app;

    public static final String LOGIN_EXTRA_PHONE_NUMBER = "LOGIN_EXTRA_PHONE_NUMBER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initLayout();
        populateCountryFields();
    }

    public void initLayout(){
        countryEditText = (EditText) findViewById(R.id.activity_login_editText_country);
        prefixEditText = (EditText) findViewById(R.id.activity_login_editText_country_prefix);
        phoneNumberEditText = (EditText) findViewById(R.id.activity_login_editText_phone_number);
        continueButton = (Button) findViewById(R.id.activity_login_button_ok);

        Intent intent = getIntent();
        firstName = intent.getStringExtra(LoginNameActivity.LOGIN_EXTRA_FIRST_NAME);
        lastName = intent.getStringExtra(LoginNameActivity.LOGIN_EXTRA_LAST_NAME);

        app = (MyApplication) getApplication();

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prefix = prefixEditText.getText().toString();
                phoneNumber = phoneNumberEditText.getText().toString();
                if (!prefix.isEmpty() && !phoneNumber.isEmpty() && phoneNumber.matches("[0-9]+")) {
                    phoneNumber = prefix + phoneNumber;

                    createUser(phoneNumber, firstName, lastName);
                } else {
                    if (prefix.isEmpty()) {
                        prefixEditText.setError("Country prefix is required");
                    }
                    if (phoneNumber.isEmpty()) {
                        phoneNumberEditText.setError("Phone number is required");
                    } else if (!phoneNumber.matches("[0-9]+")) {
                        phoneNumberEditText.setError("Not a valid phone number");
                    }
                }
            }
        });
    }

    private void writeUserInSharedPreferences() {
        SharedPreferences.Editor editor = getSharedPreferences(MyApplication.METME_SHARED_PREFERENCES, MODE_PRIVATE).edit();
        editor.putString("first_name", firstName);
        editor.putString("last_name", lastName);
        editor.putString("phone_number", phoneNumber);
        editor.commit();
    }

    private void createUser(final String phoneNumber, final String firstName, final String lastName) {

        Call<Void> userPostCall = app.getRestClient().getApiService().USER_POST_BODY_CALL(phoneNumber, firstName, lastName);
        userPostCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Response<Void> response, Retrofit retrofit) {
                if(response != null){
                    if(response.code() == 200) {
                        writeUserInSharedPreferences();

                        Intent intent = new Intent(LoginActivity.this, LoginConfirmActivity.class);
                        intent.putExtra(LOGIN_EXTRA_PHONE_NUMBER, LoginActivity.this.phoneNumber);
                        startActivity(intent);

                    } else {
                        Log.e("BE error", response.code() + " - " + response.message());
                    }
                } else {
                    Log.e("BE error", "Empty response");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("BE error", "Call failed");
            }
        });

    }

    public void populateCountryFields(){
        String locale = getUserCountry(this);
        if(locale != null) {
            Call<CountryGetBody> getCountryDetailsCall = app.getRestClient().getApiService().COUNTRY_GET_BODY_CALL(locale);
            getCountryDetailsCall.enqueue(new Callback<CountryGetBody>() {
                @Override
                public void onResponse(Response<CountryGetBody> response, Retrofit retrofit) {
                    if(response.body() != null){
                        countryEditText.setText(response.body().getName());
                        prefixEditText.setText(response.body().getPhoneCode());
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.e("Call error", t.getMessage());
                }
            });
        }
    }

    public static String getUserCountry(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                return simCountry.toUpperCase(Locale.US);
            }
            else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    return networkCountry.toUpperCase(Locale.US);
                }
            }
        }
        catch (Exception e) { }
        return null;
    }
}
