package com.example.travel_book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class search extends AppCompatActivity {

    public String WebLink_search = "https://travelbook-287802.nw.r.appspot.com/searchApi";
    String email,otherUserEmail;
    Button SearchBtn;
    EditText From, To;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        From = findViewById(R.id.SearchFrom);
        To = findViewById(R.id.SearchTo);
        SearchBtn = findViewById(R.id.SearchButton);



        Bundle post = getIntent().getExtras();
        if(post != null){
            email = post.getString("email_address");
            search_post sp = new search_post(email);
            Thread spThread = new Thread(sp);
            spThread.start();
            Thread search_postThread = new Thread (sp);
            search_postThread.start();
        }
    }
    public void SearchButton(View view)
    {
        Intent SearchButton = new Intent(this, search.class);
        startActivity(SearchButton);
    }

    private class search_post implements Runnable {
        String Email;
        String line;

        public search_post(String email) {
            Email = email;
        }

        @Override
        public void run() {
            try{
                String data = "{\"email_address\":\"" + email + "\"}";
                URL Api = new URL(WebLink_search);
                final ListView SearchListLV = findViewById(R.id.SearchList);
                final ArrayList<String> Result_email = new ArrayList<String>();
                final ArrayList<String>  Result_to_location= new ArrayList<String>();
                ArrayList<String> Search_KeyWord = new ArrayList<String>();
                final searchCustomVIew[] sv = new searchCustomVIew[1];

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
                    followApi follow = map.readValue(line, followApi.class);
                    for(int i=0; i<follow.getSearch_KeyWord().length; i++){
                        Search_KeyWord.add(follow.getSearch_KeyWord()[i]);
                        Result_email.add(follow.getResult_email()[i]);
                        Result_to_location.add(follow.getResult_to_location()[i]);
                    }


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            /*custom view constructor*/
                            sv[0] = new searchCustomVIew(search.this,Result_email, Result_to_location);
                            SearchListLV.setAdapter(sv[0]);
                        }
                    });
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