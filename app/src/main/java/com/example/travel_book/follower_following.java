package com.example.travel_book;



import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class follower_following extends BaseAdapter {

    String CurrentlyLoggedInEmail;
    ArrayList<String> EmailIdList;
    Context context;

    public class ViewHolder{

        private TextView EMail_Id;
        RelativeLayout relative1;

    }
    follower_following(Context context, ArrayList<String> EmailIdList, String currentlyLoggedInEmail){
        this.CurrentlyLoggedInEmail = currentlyLoggedInEmail;
        this.EmailIdList = EmailIdList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return EmailIdList.size();
    }

    @Override
    public Object getItem(int i) {
        return EmailIdList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder hold;
        if (view != null){
            hold = (ViewHolder)view.getTag();
        }
        else{
            hold = new ViewHolder();
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.activity_follower_following,viewGroup,false);
            hold.EMail_Id = view.findViewById(R.id.EMailId);
            hold.relative1 = view.findViewById(R.id.relative);
            view.setTag(hold);
        }
        hold.EMail_Id.setText("User: "+EmailIdList.get(i));
        hold.relative1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent UserDetailsPage = new Intent(context, UserDetails.class);
                UserDetailsPage.putExtra("email_address",CurrentlyLoggedInEmail );
                UserDetailsPage.putExtra("otherUserEmail",EmailIdList.get(i));
                context.startActivity(UserDetailsPage);
            }
        });

        return view;
    }
}