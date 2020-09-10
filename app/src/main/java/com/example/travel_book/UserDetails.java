package com.example.travel_book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Time;
import java.util.ArrayList;

public class UserDetails extends AppCompatActivity {

    public String WebLink_UserDetails = "https://travelbook-287802.nw.r.appspot.com/newUserApi";
    Button  userFollowBtn, userFollowingBtn;
    String email, otherUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        userFollowBtn = findViewById(R.id.userFollow);
        userFollowingBtn = findViewById(R.id.userFollowing);

        Bundle post = getIntent().getExtras();
        if(post != null){
            email = post.getString("email_address");
            otherUserEmail = post.getString("otherUserEmail");
            System.out.println("CL : "+email);
            System.out.println("NU : "+otherUserEmail);
            if(email.compareTo(otherUserEmail) == 0)
            {
                Intent GoToProfile = new Intent(this, Timeline.class);
                GoToProfile.putExtra("email_address",email);
                startActivity(GoToProfile);
                this.finish();
            }
            else
            {
                User_Details ud = new User_Details(otherUserEmail, email);
                Thread udThread = new Thread(ud);
                udThread.start();
            }
        }
    }


    private class User_Details implements Runnable{
        String Email, CLEmail;
        String line;
        public User_Details(String email, String currentlyLoggedInEmail) {
            Email = email; CLEmail = currentlyLoggedInEmail;
        }
        @Override
        public void run() {

            try{
                String data = "{\"email_address\":\"" + CLEmail + "\",\"newUsersEmail\":\""+ Email+"\"}";
                URL Api = new URL(WebLink_UserDetails);
                final ListView UsersDetails_LV = findViewById(R.id.UsersDetailsListView);
                final ArrayList<String> Caption = new ArrayList<String>();
                final ArrayList<Bitmap> photo_url = new ArrayList<Bitmap>();
                final ArrayList<String> experience = new ArrayList<String>();
                final ArrayList<String> hotel = new ArrayList<String>();
                final ArrayList<String> flight = new ArrayList<String>();
                final ArrayList<String> visa = new ArrayList<String>();
                final ArrayList<String> from_location = new ArrayList<String>();
                final ArrayList<String> to_location = new ArrayList<String>();
                final UserDetailsCustomView[] udcv = new UserDetailsCustomView[1];

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
                    Users_details_api udapi = map.readValue(line, Users_details_api.class);
                    if(udapi.getResponseStatus().compareTo("GivingNewUserData") == 0) {
                        for (int i = 0; i < udapi.getFrom_location().length; i++) {
                            Caption.add(udapi.getCaption()[i]);
                            IB pic = new IB();
                            Bitmap collectionBitmap = pic.image(udapi.getPhoto_url()[i]);
                            photo_url.add(collectionBitmap);
                            experience.add(udapi.getExperience()[i]);
                            hotel.add(udapi.getHotel()[i]);
                            visa.add(udapi.getVisa()[i]);
                            flight.add(udapi.getFlight()[i]);
                            from_location.add(udapi.getFrom_location()[i]);
                            to_location.add(udapi.getTo_location()[i]);
                        }
                        System.out.println("Cap:" + Caption);
                        System.out.println("Image:" + photo_url);
                        System.out.println("exp:" + experience);
                        System.out.println("hotel:" + hotel);
                        System.out.println("Visa:" + visa);
                        System.out.println("Flight:" + flight);
                        System.out.println("From:" + from_location);
                        System.out.println("to" + to_location);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                /*custom view constructor*/
                                udcv[0] = new UserDetailsCustomView(UserDetails.this, Caption, photo_url, experience, hotel, flight, visa, from_location, to_location);
                                UsersDetails_LV.setAdapter(udcv[0]);
                            }
                        });
                    }
                    else
                    {
                        Intent GoToProfile = new Intent(UserDetails.this,Timeline.class);
                        GoToProfile.putExtra("email_address",email);
                        startActivity(GoToProfile);
                        finish();
                    }
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

    public class IB{

        public Bitmap image(String PassedURL) throws Exception{
            Bitmap collectionBitmap;
            URL url = new URL(PassedURL);
            HttpURLConnection connect = (HttpURLConnection) url.openConnection();
            connect.setDoInput(true);
            connect.connect();
            InputStream inst = connect.getInputStream();
            collectionBitmap = BitmapFactory.decodeStream(inst);
            return collectionBitmap;
        }
    }
}