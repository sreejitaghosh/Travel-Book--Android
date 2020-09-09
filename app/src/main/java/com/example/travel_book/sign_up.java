package com.example.travel_book;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class sign_up extends AppCompatActivity {

    public static String WebLink_Registration = "https://travelbook-287802.nw.r.appspot.com/RegistrationApi";
    EditText Name, EmailId, Password, RepeatPassword, Answer;
    Button sign_upBtn,ResetBtn;
    static Spinner Question ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Name = findViewById(R.id.name);
        EmailId = findViewById(R.id.emailId);
        Password = findViewById(R.id.password);
        RepeatPassword = findViewById(R.id.repeat_password);
        Question = (Spinner)findViewById(R.id.question);
        Answer = findViewById(R.id.answer);
        sign_upBtn = findViewById(R.id.sign_up);
        ResetBtn = findViewById(R.id.Reset);

        sign_upBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Email = EmailId.getText().toString();
                String Pwd = Password.getText().toString();
                String name = Name.getText().toString();
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
                    Register register = new Register();
                    register.execute(view);
                }
            }
        });
    }
    public void Sign_upBtn(View view)
    {
        Intent sign_upBtn = new Intent(this, sign_up.class);
        startActivity(sign_upBtn);
    }

    class Register {
        protected void execute(View v) {
            Name = findViewById(R.id.name);
            EmailId = findViewById(R.id.emailId);
            Password = findViewById(R.id.password);
            Question = (Spinner)findViewById(R.id.question);
            Answer = findViewById(R.id.answer);
            String Email = EmailId.getText().toString();
            String Pwd = Password.getText().toString();
            String name = Name.getText().toString();
            String Ques = Question.getSelectedItem().toString();
            String Ans = Answer.getText().toString();

            Res res = new Res(Email, Pwd, name,Ques, Ans);
            Thread RegisterThread = new Thread(res);
            RegisterThread.start();

        }
    }


    private static class Res implements Runnable {
        String Email = "";
        String Pwd = "";
        String name = "";
        String Ques = "";
        String Ans = "";
        String line;

        public Res(String Email, String Pwd, String name,String Ques, String Ans) {
            this.Email = Email;
            this.Pwd = Pwd;
            this.name = name;
            this.Ques = Ques;
            this.Ans = Ans;
        }

        @Override
        public void run() {
                try{
                    String data = "{\"perform\":\"Register\",\"email_address\":\"" + Email + "\",\"user_password\":\"" + Pwd + "\",\"user_name\":\"" + name + "\",\"hint_question\":\"" + Ques + "\",\"hint_answer\":\"" + Ans + "\"}";
                    URL Api = new URL(WebLink_Registration);
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
                        while((line = br.readLine()) != null)
                        {
                            System.out.println(line);
                        }
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