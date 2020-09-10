package com.example.travel_book;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;

public class searchCustomVIew extends BaseAdapter {

    ArrayList<String> Result_email, Result_to_location;
    String CLEmail;
    Context context;

    public class ViewHolder{
        private TextView Email_Id;
        private TextView Destination;
        RelativeLayout RL;
    }

    searchCustomVIew(Context context, ArrayList<String> Result_email, ArrayList<String>Result_to_location, String CLEmail){
        this.context = context;
        this.Result_email = Result_email;
        this.Result_to_location = Result_to_location;
        this.CLEmail = CLEmail;
    }

    @Override
    public int getCount() {
        return Result_email.size();
    }

    @Override
    public Object getItem(int i) {
        return Result_email.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup)
    {
        ViewHolder hold;
        if(view != null){
            hold = (ViewHolder)view.getTag();
        }
        else{
            hold = new ViewHolder();
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.activity_search_custom_view, viewGroup , false);
            hold.Email_Id = view.findViewById(R.id.Email_Id);
            hold.Destination = view.findViewById(R.id.Destination);
            hold.RL = view.findViewById(R.id.RL);
            view.setTag(hold);
        }

        hold.Email_Id.setText("Email Address : "+Result_email.get(i));
        hold.Destination.setText("Destination : "+Result_to_location.get(i));
        hold.RL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent UserDetailsPage = new Intent(context, UserDetails.class);
                UserDetailsPage.putExtra("email_address", CLEmail);
                UserDetailsPage.putExtra("otherUserEmail",Result_email.get(i));
                context.startActivity(UserDetailsPage);
            }
        });
        return view;
    }
}
