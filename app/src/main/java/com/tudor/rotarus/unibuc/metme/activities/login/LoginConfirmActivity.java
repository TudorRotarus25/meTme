package com.tudor.rotarus.unibuc.metme.activities.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tudor.rotarus.unibuc.metme.R;
import com.tudor.rotarus.unibuc.metme.activities.NavigationDrawerActivity;
import com.tudor.rotarus.unibuc.metme.managers.NetworkManager;
import com.tudor.rotarus.unibuc.metme.managers.MySharedPreferencesManager;
import com.tudor.rotarus.unibuc.metme.pojos.interfaces.network.ActivateUserListener;
import com.tudor.rotarus.unibuc.metme.pojos.responses.post.ActivateUserPostBody;

public class LoginConfirmActivity extends AppCompatActivity implements ActivateUserListener {

    private EditText codeEditText;
    private Button continueButton;

    private MySharedPreferencesManager sharedPreferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_confirm);

        initLayout();
    }

    private void initLayout() {

        sharedPreferencesManager = MySharedPreferencesManager.getInstance();

        codeEditText = (EditText) findViewById(R.id.activity_login_confirm_editText_code);
        continueButton = (Button) findViewById(R.id.activity_login_confirm_button_ok);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = codeEditText.getText().toString();
                if (!code.isEmpty()) {

                    int id = sharedPreferencesManager.readId(LoginConfirmActivity.this);

                    if(id < 0) {
                        Intent intent = new Intent(LoginConfirmActivity.this, LoginNameActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    NetworkManager networkManager = NetworkManager.getInstance();
                    networkManager.activateUser(id, code, LoginConfirmActivity.this);

                } else {
                    codeEditText.setError("Code required");
                }
            }
        });
    }

    @Override
    public void onActivateUserSuccess(ActivateUserPostBody response) {

        sharedPreferencesManager.writeToken(this, response.getToken());
        sharedPreferencesManager.writeRefreshToken(this, response.getRefreshToken());

        Intent intent = new Intent(LoginConfirmActivity.this, NavigationDrawerActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onActivateUserFailed() {
        Toast.makeText(this, "Activation failed, please try again", Toast.LENGTH_SHORT).show();
    }
}
