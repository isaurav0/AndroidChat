package com.example.chat;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import static android.media.tv.TvContract.Programs.Genres.encode;
import static java.lang.Thread.sleep;


public class Login extends AppCompatActivity {

    Button btn_login;
    EditText input_username, input_password;
    String username, password, response="sauravpathak";
    URL url;
    HttpURLConnection connection = null;
    DataOutputStream dataOut;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            // Google Play will install latest OpenSSL
            SSLContext sslContext;
            sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, null, null);
            sslContext.createSSLEngine();
        } catch (Exception e) {
            e.printStackTrace();
        }


        btn_login= (Button) findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_username = (EditText) findViewById(R.id.edit_email);
                input_password = (EditText) findViewById(R.id.edit_password);
                username = input_username.getText().toString();
                password = input_password.getText().toString();

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL("http://192.168.10.13:3000/login");
//                            String urlstr = url.toString();
                            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                            urlConnection.setRequestMethod("POST");
                            urlConnection.setUseCaches(true);
                            urlConnection.setDoOutput(true);
                            urlConnection.setDoInput(true);
                            urlConnection.setAllowUserInteraction(false);
                            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                            dataOut = new DataOutputStream(urlConnection.getOutputStream());
                            dataOut.writeUTF("&username="+username+"&password="+password);
                            try {
                                int code = urlConnection.getResponseCode();
//                                BufferedReader breader;
//                                if(code >=200 && code<=299) {
//                                    breader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
//                                }
//                                else{
//                                    breader = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
//                                }
//
//                                StringBuffer stringBuffer = new StringBuffer();
//                                String line;
//                                while ((line = breader.readLine()) != null)
//                                {
//                                    stringBuffer.append(line);
//                                }
//                                response = stringBuffer.toString();
                                Log.d("muji", String.valueOf(code));
                            } finally {
                                urlConnection.disconnect();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                t.start();
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    Toast.makeText(getApplicationContext(), jsonResponse.getString("msg"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}