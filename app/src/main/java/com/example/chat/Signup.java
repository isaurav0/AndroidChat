package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.SSLContext;

public class Signup extends AppCompatActivity {

    Button btn_signup;
    EditText input_username, input_password, input_email, input_repassword, input_name;
    String username, password, email, name, repassword, response;
    URL url;
    HttpURLConnection connection = null;
    DataOutputStream dataOut;
    int code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        try {
            // Google Play will install latest OpenSSL
            SSLContext sslContext;
            sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, null, null);
            sslContext.createSSLEngine();
        } catch (Exception e) {
            e.printStackTrace();
        }


        btn_signup= (Button) findViewById(R.id.btn_signup);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_username = (EditText) findViewById(R.id.edit_username);
                input_password = (EditText) findViewById(R.id.edit_password);
                input_email = (EditText) findViewById(R.id.edit_email);
                input_repassword = (EditText) findViewById(R.id.re_password);
                input_name = (EditText) findViewById(R.id.edit_name);
                username = input_username.getText().toString();
                password = input_password.getText().toString();
                email = input_email.getText().toString();
                repassword = input_repassword.getText().toString();
                name = input_name.getText().toString();

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL("http://192.168.10.13:3000/signup");
//                            String urlstr = url.toString();
                            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                            urlConnection.setRequestMethod("POST");
                            urlConnection.setUseCaches(true);
                            urlConnection.setDoOutput(true);
                            urlConnection.setDoInput(true);
                            urlConnection.setAllowUserInteraction(false);
                            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                            dataOut = new DataOutputStream(urlConnection.getOutputStream());
                            dataOut.writeUTF("&username="+username+"&password="+password+"&email="+email+"&name="+name);
                            try {
                                code = urlConnection.getResponseCode();
                                BufferedReader breader;
                                if(code >=200 && code<=299) {
                                    breader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                                    Log.d("muji", "success");
                                }
                                else{
                                    breader = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
                                    Log.d("muji", "failure");
                                }

                                StringBuffer stringBuffer = new StringBuffer();
                                String line;
                                while ((line = breader.readLine()) != null)
                                {
                                    stringBuffer.append(line);
                                }
                                response = stringBuffer.toString();
                            }
                            finally {
                                urlConnection.disconnect();
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                if(password.equals(repassword)){
                   t.start();
                    try {
                        t.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d("muji", response);

                    if(code>=200 && code<=300){
                        Intent to_login = new Intent(v.getContext() ,Login.class);
                        startActivity(to_login);
                    }
                    else
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getApplicationContext(), "Passwords dont match", Toast.LENGTH_LONG).show();
            }
        });
    }
}
