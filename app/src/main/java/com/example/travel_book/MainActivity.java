package com.example.travel_book;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.StringBufferInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity
{
    public static String WebLink = "https://travelbook-287802.nw.r.appspot.com/";
    public static String WebLink_Login = "https://travelbook-287802.nw.r.appspot.com/LoginApi";
    EditText EmailId, Password;
    Button loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        EmailId = findViewById(R.id.email);
        Password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.login);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = EmailId.getText().toString();
                String pwd = Password.getText().toString();

                if (email.isEmpty()){
                    EmailId.setError("Please Enter Email Id");
                    EmailId.requestFocus();
                }
                if (pwd.isEmpty()){
                    Password.setError("Please Enter Password");
                    Password.requestFocus();
                }
                else{
                    Login login = new Login();
                    login.excute(view);
                }
            }
        });
    }
    public void signup(View view)
    {
        Intent signup = new Intent(this, sign_up.class);
        startActivity(signup);
    }
    public void ForgotPassword(View view)
    {
        Log.i("Info", "ForgotPassword Function called.");
        Intent FP = new Intent(this, forgotpassword.class);
        startActivity(FP);
    }
    public void login(View view)
    {
        Intent login = new Intent(this, Timeline.class);
        startActivity(login);
    }


    private class Login {
        public void excute(View view) {
            EmailId = findViewById(R.id.email);
            Password = findViewById(R.id.password);
            String email = EmailId.getText().toString();
            String pwd = Password.getText().toString();

            Log_in log = new Log_in(email,pwd);
            Thread LoginThread = new Thread(log);
            LoginThread.start();


        }
    }

    private class Log_in implements Runnable{
        String email = "";
        String pwd = "";
        String line;

        public Log_in(String email, String pwd){
            this.email = email;
            this.pwd = pwd;
        }

        @Override
        public void run() {
            try{
                String data = "{\"email_address\":\"" + email + "\",\"user_password\":\"" + pwd + "\"}";
                URL Api = new URL(WebLink_Login);
                HttpURLConnection ApiConnection = (HttpURLConnection) Api.openConnection();
                ApiConnection.setRequestMethod("POST");
                ApiConnection.setRequestProperty("Content-Type","application/json");
                ApiConnection.setRequestProperty("Accept","application/json");
                ApiConnection.setDoOutput(true);
                ApiConnection.setConnectTimeout(10000);
                System.out.println("Connection" +ApiConnection);
                System.out.println("data" +data);
                try
                {
                    DataOutputStream str = new DataOutputStream(ApiConnection.getOutputStream());
                    str.writeBytes(data);
                    str.flush();
                }
                catch(Exception e)
                {
                    System.out.println("Exception1 have occurred : "+e);
                }
                int httpStatus = ApiConnection.getResponseCode();
                System.out.println("httpStatus"+httpStatus);
                try
                {
                    BufferedReader br = new BufferedReader(new InputStreamReader(ApiConnection.getInputStream()));
                    StringBuilder data1 = new StringBuilder();
                    while((line = br.readLine()) != null)
                    {
                        data1.append(line);
                        System.out.println(data1);
                    }
                    line = data1.toString();
                }
                catch(Exception e)
                {
                    System.out.println("Exception2 have occurred : "+e);
                }
                ApiConnection.disconnect();
                ObjectMapper map = new ObjectMapper();

                try
                {
                    Converting_Api_Sting convert = map.readValue(line, Converting_Api_Sting.class);
                    Intent intent = new Intent();
                    if(convert.getStatus().compareTo("Logged In") == 0 ){
                        intent = new Intent(MainActivity.this, Timeline.class);
                        intent.putExtra("email_address",email );
                    }
                    else if(convert.getStatus().compareTo("UserNotRegistered") == 0){
                        intent = new Intent(MainActivity.this, MainActivity.class);
                    }
                    finish();
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            catch(Exception e)
            {
                System.out.println("Exception3 have occurred : "+e);
            }
            System.out.println(line);
        }

    }

}