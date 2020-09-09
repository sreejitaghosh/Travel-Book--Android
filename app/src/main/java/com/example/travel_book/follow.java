package com.example.travel_book;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class follow extends AppCompatActivity {

    public String WebLink_Follower = "https://travelbook-287802.nw.r.appspot.com/followerApi";
    String email,otherUserEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);

        Bundle post = getIntent().getExtras();
        if(post != null){
            email = post.getString("email_address");
            otherUserEmail = post.getString("otherUserEmail");
            follower follow;
            if(otherUserEmail.compareTo("") == 0)
            {
                follow = new follower(email,email);
            }
            else
            {
                follow = new follower(otherUserEmail,email);
            }
            Thread followTh = new Thread(follow);
            followTh.start();
        }
    }

    private class follower implements Runnable {
        String Email,CLEmail;
        String line;
        ListView followerLV;
        ArrayList<String> followerList = new ArrayList<>();
        follower_following[] FF;

        public follower(String email, String CLEmail) {
            this.CLEmail = CLEmail;
            Email = email;
        }

        @Override
        public void run() {
            followerLV = findViewById(R.id.followerListView);
            FF = new follower_following[1];
            try{
                String data = "{\"email_address\":\"" + email + "\",\"newUsersEmail\":\"\"}";
                URL Api = new URL(WebLink_Follower);

                HttpURLConnection ApiConnection = (HttpURLConnection) Api.openConnection();
                ApiConnection.setRequestMethod("POST");
                ApiConnection.setRequestProperty("Content-Type","application/json");
                ApiConnection.setRequestProperty("Accept","application/json");
                ApiConnection.setDoOutput(true);
                ApiConnection.setConnectTimeout(10000);
                System.out.println("Connection" +ApiConnection);
                System.out.println("data"+data);

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
                    followApi F_API = map.readValue(line, followApi.class);
                    for(int i=0; i<F_API.getFollowerList().length; i++)
                    {
                        System.out.println(F_API.getFollowerList()[i]);
                        followerList.add(F_API.getFollowerList()[i]);
                    }
                    System.out.println("Cap:"+followerList);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            FF[0] = new follower_following(follow.this, followerList,CLEmail);
                            followerLV.setAdapter(FF[0]);
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