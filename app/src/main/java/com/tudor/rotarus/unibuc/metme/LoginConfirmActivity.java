package com.tudor.rotarus.unibuc.metme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginConfirmActivity extends AppCompatActivity {

    EditText codeEditText;
    Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_confirm);

        initLayout();
    }

    private void initLayout() {
        codeEditText = (EditText) findViewById(R.id.activity_login_confirm_editText_code);
        continueButton = (Button) findViewById(R.id.activity_login_confirm_button_ok);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginConfirmActivity.this, LoginNameActivity.class);
                startActivity(intent);
            }
        });
    }
}
