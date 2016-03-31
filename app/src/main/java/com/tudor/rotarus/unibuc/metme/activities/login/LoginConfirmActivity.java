package com.tudor.rotarus.unibuc.metme.activities.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tudor.rotarus.unibuc.metme.MyApplication;
import com.tudor.rotarus.unibuc.metme.R;
import com.tudor.rotarus.unibuc.metme.activities.NavigationDrawerActivity;
import com.tudor.rotarus.unibuc.metme.managers.NetworkManager;
import com.tudor.rotarus.unibuc.metme.pojos.interfaces.ActivateUserListener;
import com.tudor.rotarus.unibuc.metme.pojos.requests.post.ActivateUserPostBody;

public class LoginConfirmActivity extends AppCompatActivity implements ActivateUserListener {

    EditText codeEditText;
    Button continueButton;

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

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = codeEditText.getText().toString();
                if (!code.isEmpty()) {

                    NetworkManager networkManager = NetworkManager.getInstance();
                    networkManager.activateUser(phoneNumber, code, LoginConfirmActivity.this);

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

    @Override
    public void onActivateUserSuccess(ActivateUserPostBody response) {
        writeTokenToSharedPreferences(response.getId(), response.getToken());
        Intent intent = new Intent(LoginConfirmActivity.this, NavigationDrawerActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onActivateUserFailed() {

    }
}
