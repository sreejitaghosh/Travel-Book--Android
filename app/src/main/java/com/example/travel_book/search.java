package com.example.travel_book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class search extends AppCompatActivity {

    public String WebLink_search = "https://travelbook-287802.nw.r.appspot.com/searchApi";
    String CLEmail;
    EditText EmailET, SourceET, DestinationET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        EmailET = findViewById(R.id.EmailET);
        SourceET = findViewById(R.id.SourceET);
        DestinationET = findViewById(R.id.DestinationET);

        Bundle post = getIntent().getExtras();
        if(post != null)
        {
            CLEmail = post.getString("CLEmail");
            System.out.println("CLEmail : "+CLEmail);
        }
    }
    public void SearchButton(View view)
    {
        String SEmail, SSource, SDestination;
        SEmail = EmailET.getText().toString();
        if(SEmail.compareTo("") == 0)
        {
            SSource = SourceET.getText().toString();
            SDestination = DestinationET.getText().toString();
            if(SSource.compareTo("") == 0 && SDestination.compareTo("") != 0)
            {
                SourceET.setError("Cannot be blank.");
                SourceET.requestFocus();
            }
            else if(SSource.compareTo("") != 0 && SDestination.compareTo("") == 0)
            {
                DestinationET.setError("Cannot be blank.");
                DestinationET.requestFocus();
            }
            else if(SSource.compareTo("") == 0 && SDestination.compareTo("") == 0)
            {
                EmailET.setError("Cannot be blank.");
                EmailET.requestFocus();
            }
        }
        else
        {
            SSource = "";
            SDestination = "";
        }
        System.out.println("CLEmail : "+CLEmail);
        System.out.println("SEmail : "+SEmail);
        System.out.println("SSource : "+SSource);
        System.out.println("SDestination : "+SDestination);
        search_post SearchPage = new search_post(CLEmail, SEmail.toLowerCase(), SSource.toLowerCase(), SDestination.toLowerCase());
        Thread SearchThread = new Thread(SearchPage);
        SearchThread.start();
    }

    private class search_post implements Runnable {
        String CLEmail, SEmail, SSource, SDestination;
        String line;

        public search_post(String CLEmail, String SEmail, String SSource, String SDestination)
        {
            this.CLEmail = CLEmail;
            this.SEmail = SEmail;
            this.SSource = SSource;
            this.SDestination = SDestination;
            System.out.println("CLEmail again : "+CLEmail);
            System.out.println("SEmail again : "+SEmail);
            System.out.println("SSource again : "+SSource);
            System.out.println("SDestination : "+SDestination);
        }

        @Override
        public void run() {
            try{
                String data = "{\"email_address\":\"" + SEmail + "\",\"from_location\":\""+SSource+"\",\"to_location\":\""+SDestination+"\"}";
                System.out.println("JSON String is : "+data);
                URL Api = new URL(WebLink_search);
                final ListView SearchListLV = findViewById(R.id.SearchList);
                final ArrayList<String> Result_email = new ArrayList<String>();
                final ArrayList<String>  Result_to_location= new ArrayList<String>();
                final searchCustomVIew[] sv = new searchCustomVIew[1];

                HttpURLConnection ApiConnection = (HttpURLConnection) Api.openConnection();
                ApiConnection.setRequestMethod("POST");
                ApiConnection.setRequestProperty("Content-Type","application/json");
                ApiConnection.setRequestProperty("Accept","application/json");
                ApiConnection.setDoOutput(true);
                ApiConnection.setConnectTimeout(10000);
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
                    System.out.println("Inside Try");
                    BufferedReader br = new BufferedReader(new InputStreamReader(ApiConnection.getInputStream()));
                    StringBuilder data1 = new StringBuilder();
                    while((line = br.readLine()) != null)
                    {
                        System.out.println("Inside While");
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
                System.out.println("Response Line is : "+line);
                ObjectMapper map = new ObjectMapper();
                try
                {
                    followApi RData = map.readValue(line, followApi.class);
                    if(RData.getResult_email().length != 0)
                    {
                        for (int i = 0; i < RData.getResult_email().length; i++)
                        {
                            Result_email.add(RData.getResult_email()[i]);
                            if(RData.getResult_to_location().length == 0)
                            {
                                Result_to_location.add(" ");
                            }
                            else
                            {
                                Result_to_location.add(RData.getResult_to_location()[i]);
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                /*custom view constructor*/
                                sv[0] = new searchCustomVIew(search.this, Result_email, Result_to_location, CLEmail);
                                SearchListLV.setAdapter(sv[0]);
                            }
                        });
                    }
                    else
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView ResultTV = findViewById(R.id.ResultTV);
                                ResultTV.setText("Sorry! No results found for your entered keyword. Please try with another keyword.");
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            catch(Exception e)
            {
                System.out.println("Exception3 have occurred : "+e);
            }
        }
    }
}