package com.example.finde;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<ServiceItem> implements View.OnClickListener{

    private ArrayList<ServiceItem> items;
    private int lastPosition=-1;
    Context mContext;

    public static class ViewHolder {
        TextView placeName;
        TextView placeDistance;
        ImageButton placeCall;
        ImageButton placeDirection;
    }

    public CustomListAdapter(ArrayList<ServiceItem> data, Context context){
        super(context, R.layout.row_item, data);
        this.items = data;
        this.mContext = context;
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        ServiceItem serviceItem = (ServiceItem)getItem(position);

        // TODO: Select location market on Google Map
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ServiceItem serviceItem = getItem(position);
        ViewHolder viewHolder;

        final View result;
        if( convertView==null ) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.placeName = (TextView)convertView.findViewById(R.id.placeName);
            viewHolder.placeDistance = (TextView)convertView.findViewById(R.id.placeDistance);
            viewHolder.placeCall = (ImageButton) convertView.findViewById(R.id.imageButton);
            viewHolder.placeDirection = (ImageButton) convertView.findViewById(R.id.imageButton2);

            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        lastPosition = position;
        viewHolder.placeName.setText(serviceItem.getName());
        viewHolder.placeDistance.setText(serviceItem.getDistance());

        // TODO: Implement call and map "onclick" event handlers

        return convertView;

    }
}
