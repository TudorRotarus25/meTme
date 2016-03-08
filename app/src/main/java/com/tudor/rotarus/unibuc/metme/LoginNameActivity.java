package com.tudor.rotarus.unibuc.metme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

    private void initLayout() {
        firstNameEditText = (EditText) findViewById(R.id.activity_login_name_editText_firstName);
        lastNameEditText = (EditText) findViewById(R.id.activity_login_name_editText_lastName);
        continueButton = (Button) findViewById(R.id.activity_login_name_button_ok);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(firstNameEditText.isDirty() && lastNameEditText.isDirty()){
                String firstName = firstNameEditText.getText().toString();
                String lastName = lastNameEditText.getText().toString();

                if(!firstName.isEmpty() && !lastName.isEmpty()){
                    Intent intent = new Intent(LoginNameActivity.this, LoginActivity.class);
                    intent.putExtra(LOGIN_EXTRA_FIRST_NAME, firstName);
                    intent.putExtra(LOGIN_EXTRA_LAST_NAME, lastName);
                    startActivity(intent);
                }
            }
            }
        });
    }
}
