package com.example.travel_book;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class forgotpassword extends AppCompatActivity {

    public static String WebLink_Forgot_Password  = "https://travelbook-287802.nw.r.appspot.com/Forgot_PasswordApi";
    EditText EmailId, Password, RepeatPassword, Answer;
    Button forgot_password_Btn,ResetBtn;
    static Spinner Question ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        EmailId = findViewById(R.id.emailId);
        Password = findViewById(R.id.password);
        RepeatPassword = findViewById(R.id.repeat_password);
        Question = (Spinner)findViewById(R.id.question);
        Answer = findViewById(R.id.answer);
        forgot_password_Btn = findViewById(R.id.forgot_password);
        ResetBtn = findViewById(R.id.Reset);

        forgot_password_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = EmailId.getText().toString();
                String Pwd = Password.getText().toString();
                String Ques = Question.getSelectedItem().toString();
                String Ans = Answer.getText().toString();

                if (Email.isEmpty()){
                    EmailId.setError("Please Enter Email Id");
                    EmailId.requestFocus();
                }
                if(Pwd.isEmpty()){
                    Password.setError("Please Enter Password");
                    Password.requestFocus();
                }
                if(Ans.isEmpty()){
                    Answer.setError("Please Enter");
                    Answer.requestFocus();
                }
                else{
                    Forgot_Password forgot_password = new Forgot_Password();
                    forgot_password.execute(view);
                }
            }
        });
    }


    private class Forgot_Password {
        public void execute(View view) {
            EmailId = findViewById(R.id.emailId);
            Password = findViewById(R.id.password);
            Question = (Spinner)findViewById(R.id.question);
            Answer = findViewById(R.id.answer);
            String Email = EmailId.getText().toString();
            String Pwd = Password.getText().toString();
            String Ques = Question.getSelectedItem().toString();
            String Ans = Answer.getText().toString();

            ForPas forPas = new ForPas(Email, Pwd, Ques, Ans);
            Thread ForPasThread = new Thread(forPas);
            ForPasThread.start();
        }
    }

    private class ForPas implements Runnable{
        String Email = "";
        String Pwd = "";
        String Ques = "";
        String Ans = "";
        String line;

        public ForPas(String Email, String Pwd,String Ques, String Ans) {
            this.Email = Email;
            this.Pwd = Pwd;
            this.Ques = Ques;
            this.Ans = Ans;
        }
        @Override
        public void run() {
            try{
                String data = "{\"perform\":\"Password Reset\",\"email_address\":\"" + Email + "\",\"user_password\":\"" + Pwd + "\",\"hint_question\":\"" + Ques + "\",\"hint_answer\":\"" + Ans + "\"}";
                URL Api = new URL(WebLink_Forgot_Password);
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
                    StringBuilder stringBuilder = new StringBuilder();
                    while(br.readLine() != null)
                    {
                        stringBuilder.append(br.readLine());
                    }
                    line = stringBuilder.toString();
                    System.out.println(line);
                }
                catch(Exception e)
                {
                    System.out.println("Exception2 have occurred : "+e);
                }
                ApiConnection.disconnect();
            }
            catch(Exception e)
            {
                System.out.println("Exception3 have occurred : "+e);
            }
            System.out.println(line);
        }

    }


}

