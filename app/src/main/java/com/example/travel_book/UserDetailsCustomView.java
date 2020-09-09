package com.example.travel_book;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class UserDetailsCustomView extends BaseAdapter {

    public static String WebLink_UserDetails= "https://travelbook-287802.nw.r.appspot.com/newUserApi";

    ArrayList<String> Caption, experience, hotel, flight, visa, from_location, to_location;
    ArrayList<Bitmap> photo_url;
    Context context;

    public class ViewHolder{
        private TextView Caption1;
        private TextView experience1;
        private TextView hotel1;
        private TextView flight1;
        private TextView visa1;
        private TextView from_location1;
        private TextView to_location1;
        public ImageView photo_url1;
        LinearLayout linear1;
    }
    UserDetailsCustomView(Context context, ArrayList<String> Caption, ArrayList<Bitmap> photo_url, ArrayList<String> experience, ArrayList<String> hotel, ArrayList<String> flight, ArrayList<String> visa, ArrayList<String> from_location,ArrayList<String>  to_location){
        this.Caption = Caption;
        this.photo_url = photo_url;
        this.experience = experience;
        this.flight = flight;
        this.from_location = from_location;
        this.hotel = hotel;
        this.to_location = to_location;
        this.visa = visa;
        this.context = context;
    }

    @Override
    public int getCount() {
        return Caption.size();
    }

    @Override
    public Object getItem(int i) {
        return Caption.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder hold;
        if(view != null){
            hold = (ViewHolder)view.getTag();
        }
        else{
            hold = new ViewHolder();

            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.activity_user_details_custom_view, viewGroup , false);
            hold.photo_url1 = view.findViewById(R.id.collection);
            hold.Caption1 = view.findViewById(R.id.Caption);
            hold.from_location1 = view.findViewById(R.id.from_location);
            hold.to_location1 = view.findViewById(R.id.to_location);
            hold.experience1 = view.findViewById(R.id.experience);
            hold.hotel1 = view.findViewById(R.id.hotel);
            hold.flight1 = view.findViewById(R.id.flight);
            hold.visa1 = view.findViewById(R.id.visa);
            hold.linear1 = view.findViewById(R.id.linear);
            view.setTag(hold);
        }

        hold.photo_url1.setImageBitmap(photo_url.get(i));
        hold.Caption1.setText("Caption: "+Caption.get(i));
        hold.from_location1.setText("From:"+from_location.get(i));
        hold.to_location1.setText("To: "+to_location.get(i));
        hold.experience1.setText("Experience: "+experience.get(i));
        hold.hotel1.setText("Hotel: "+hotel.get(i));
        hold.flight1.setText("Flight: "+flight.get(i));
        hold.visa1.setText("Visa: "+visa.get(i));
        return view;
    }
}