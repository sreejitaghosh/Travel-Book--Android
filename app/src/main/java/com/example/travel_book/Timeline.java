package com.example.travel_book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class Timeline extends AppCompatActivity {

    public String WebLink_Timeline = "https://travelbook-287802.nw.r.appspot.com/TimelineApi";
    Button followBtn, followingBtn, searchBtn;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        followBtn = findViewById(R.id.follow);
        followingBtn = findViewById(R.id.following);
        searchBtn = findViewById(R.id.Search);


        Bundle post = getIntent().getExtras();
        if(post != null){
            email = post.getString("email_address");
            timeline_post tp = new timeline_post(email);
            Thread tpThread = new Thread(tp);
            tpThread.start();
            Thread create_postThread = new Thread (tp);
            create_postThread.start();
        }
    }
    public void follow(View view)
    {
        Intent follow = new Intent(this, follow.class);
        follow.putExtra("email_address",email);
        follow.putExtra("otherUserEmail","");
        startActivity(follow);
    }

    public void following(View view)
    {
        Intent following = new Intent(this, following.class);
        following.putExtra("email_address",email);
        following.putExtra("otherUserEmail","");
        startActivity(following);
    }

    public void Search (View view)
    {
        Intent Search = new Intent(this, search.class);
        System.out.println("Sending Currently loggedin email to search page : "+email);
        Search.putExtra("CLEmail",email);
        startActivity(Search);
    }

    private class timeline_post implements Runnable {
        String Email;
        String line;

        public timeline_post(String email) {
            Email = email;
        }

        @Override
        public void run() {
            try{
                String data = "{\"email_address\":\"" + email + "\"}";
                URL Api = new URL(WebLink_Timeline);
                final ListView Timeline_Post_LV = findViewById(R.id.Timeline_PostListView);
                final ArrayList<String> Caption = new ArrayList<String>();
                final ArrayList<Bitmap> photo_url = new ArrayList<Bitmap>();
                final ArrayList<String> experience = new ArrayList<String>();
                final ArrayList<String> hotel = new ArrayList<String>();
                final ArrayList<String> flight = new ArrayList<String>();
                final ArrayList<String> visa = new ArrayList<String>();
                final ArrayList<String> from_location = new ArrayList<String>();
                final ArrayList<String> to_location = new ArrayList<String>();
                final create_post[] tl = new create_post[1];

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
                    Timeline_api timeline = map.readValue(line, Timeline_api.class);
                    for(int i=0; i<timeline.getFrom_location().length; i++){
                        Caption.add(timeline.getCaption()[i]);
                        IB pic = new IB();
                        Bitmap collectionBitmap = pic.image(timeline.getPhoto_url()[i]);
                        photo_url.add(collectionBitmap);
                        experience.add(timeline.getExperience()[i]);
                        hotel.add(timeline.getHotel()[i]);
                        visa.add(timeline.getVisa()[i]);
                        flight.add(timeline.getFlight()[i]);
                        from_location.add(timeline.getFrom_location()[i]);
                        to_location.add(timeline.getTo_location()[i]);
                    }
                    System.out.println("Cap:"+Caption);
                    System.out.println("Image:"+photo_url);
                    System.out.println("exp:"+experience);
                    System.out.println("hotel:" +hotel);
                    System.out.println("Visa:"+visa);
                    System.out.println("Flight:" +flight);
                    System.out.println("From:"+from_location);
                    System.out.println("to"+to_location);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            /*custom view constructor*/
                            tl[0] = new create_post(Timeline.this, Caption, photo_url, experience, hotel, flight, visa, from_location, to_location);
                            Timeline_Post_LV.setAdapter(tl[0]);
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

    public class IB{

        public Bitmap image(String PassedURL) throws Exception{
            Bitmap collectionBitmap;
            java.net.URL url = new URL(PassedURL);
            HttpURLConnection connect = (HttpURLConnection) url.openConnection();
            connect.setDoInput(true);
            connect.connect();
            InputStream inst = connect.getInputStream();
            collectionBitmap = BitmapFactory.decodeStream(inst);
            return collectionBitmap;
        }
    }
}




