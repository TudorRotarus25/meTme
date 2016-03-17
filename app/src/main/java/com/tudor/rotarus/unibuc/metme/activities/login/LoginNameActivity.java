package com.tudor.rotarus.unibuc.metme.activities.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tudor.rotarus.unibuc.metme.R;

public class LoginNameActivity extends AppCompatActivity {

    EditText firstNameEditText;
    EditText lastNameEditText;
    Button continueButton;

    public static final String LOGIN_EXTRA_FIRST_NAME = "LOGIN_EXTRA_FIRST_NAME";
    public static final String LOGIN_EXTRA_LAST_NAME = "LOGIN_EXTRA_LAST_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_name);

        initLayout();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void initLayout() {
        firstNameEditText = (EditText) findViewById(R.id.activity_login_name_editText_firstName);
        lastNameEditText = (EditText) findViewById(R.id.activity_login_name_editText_lastName);
        continueButton = (Button) findViewById(R.id.activity_login_name_button_ok);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = firstNameEditText.getText().toString();
                String lastName = lastNameEditText.getText().toString();

                if(!firstName.isEmpty() && !lastName.isEmpty()){
                    Intent intent = new Intent(LoginNameActivity.this, LoginActivity.class);
                    intent.putExtra(LOGIN_EXTRA_FIRST_NAME, firstName);
                    intent.putExtra(LOGIN_EXTRA_LAST_NAME, lastName);
                    startActivity(intent);
                } else {
                    if(firstName.isEmpty()){
                        firstNameEditText.setError("First name required!");
                    }
                    if (lastName.isEmpty()){
                        lastNameEditText.setError("Last name required");
                    }
                }
            }
        });
    }
}
