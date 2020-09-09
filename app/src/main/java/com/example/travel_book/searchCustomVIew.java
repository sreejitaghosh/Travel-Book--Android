package com.example.travel_book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class searchCustomVIew extends BaseAdapter {

    ArrayList<String> Result_email,Result_to_location;
    Context context;

    public class ViewHolder{
        private TextView to1;
        private TextView EMailId1;
        private TextView Location1;
        RelativeLayout relative1;
    }

    searchCustomVIew(Context context, ArrayList<String> Result_email, ArrayList<String>Result_to_location){
        this.Result_email = Result_email;
        this.Result_to_location = Result_to_location;
        this.context = context;
    }

    @Override
    public int getCount() {
        return Result_to_location.size();
    }

    @Override
    public Object getItem(int i) {
        return Result_to_location.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        ViewHolder hold;
        if(view != null){
            hold = (ViewHolder)view.getTag();
        }
        else{
            hold = new ViewHolder();

            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.activity_search_custom_view, viewGroup , false);
            hold.to1 = view.findViewById(R.id.to1);
            hold.EMailId1 = view.findViewById(R.id.EMailId1);
            hold.Location1 = view.findViewById(R.id.Location1);
            hold.relative1 = view.findViewById(R.id.relative1);
            view.setTag(hold);
        }

        hold.to1.setText("TO: "+Result_to_location.get(i));
        hold.EMailId1.setText("Caption: "+Result_email.get(i));
        hold.Location1.setText("From:"+Result_to_location.get(i));
        return view;
    }
}
