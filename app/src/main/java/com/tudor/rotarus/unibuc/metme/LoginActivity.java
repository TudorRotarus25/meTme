package com.tudor.rotarus.unibuc.metme;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.tudor.rotarus.unibuc.metme.pojos.CountryGetBody;

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
                Log.i("prefix:", "/" + prefixEditText.getText().toString() + "/");
                Log.i("phone_number:", "/" + phoneNumberEditText.getText().toString() + "/");
                Intent intent = new Intent(LoginActivity.this, LoginConfirmActivity.class);
                intent.putExtra(LOGIN_EXTRA_PHONE_NUMBER, prefixEditText.getText().toString() + phoneNumberEditText.getText().toString());
                startActivity(intent);
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
