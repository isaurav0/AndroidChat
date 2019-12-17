package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



public class Login extends AppCompatActivity {

    Button btn_login;
    EditText input_username, input_password;
    String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login= (Button) findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_username = (EditText) findViewById(R.id.edit_email);
                input_password = (EditText) findViewById(R.id.edit_password);` `
                username = input_username.getText().toString();
                password = input_password.getText().toString();
                Toast.makeText(getApplicationContext(), username, Toast.LENGTH_LONG).show();
            }
        });
    }

}