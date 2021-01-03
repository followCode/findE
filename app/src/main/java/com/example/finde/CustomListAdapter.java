package com.example.finde;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Locale;

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

    public boolean sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    protected void getContacts(final String msg) {
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        final CollectionReference complaintsRef = rootRef.collection("userContacts");
        complaintsRef.whereEqualTo("email",email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //contactButton1.setText(document.get("contact1").toString());
                        //contactButton2.setText(document.get("contact2").toString());
                        sendSMS(document.get("contact1").toString(), msg);
                        sendSMS(document.get("contact2").toString(), msg);
                        Log.d("SMS", "Sent SMS");
                    }
                }
            }
        });
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final ServiceItem serviceItem = getItem(position);
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

        viewHolder.placeDirection.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("Direction", "SHowing toast message");
                Toast.makeText(v.getContext(), "Clicked on Direction", Toast.LENGTH_SHORT).show();

                final Context context = v.getContext();

                AlertDialog alert = (new AlertDialog.Builder(v.getRootView().getContext()))
                        .setMessage("Do you want to send your contacts this location?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Send SMS
                                getContacts("Hello! I'm going to: "+
                                        "http://maps.google.com/maps?q=loc:"+serviceItem.getLatLong());
                                // Load Google Maps
                                String uri = "http://maps.google.com/maps?q=loc:"+serviceItem.getLatLong();
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                context.startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String uri = "http://maps.google.com/maps?q=loc:"+serviceItem.getLatLong();
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                context.startActivity(intent);
                            }
                        }).create();
                alert.setTitle("Send SMS");
                alert.show();
            }
        });

        viewHolder.placeCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobileNumber = serviceItem.getPhoneNumber();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+mobileNumber));
                view.getContext().startActivity(intent);
            }
        });

        // TODO: Implement call and map "onclick" event handlers

        return convertView;

    }

    public void updateServiceItems(ArrayList<ServiceItem> new_items) {
        this.items = new_items;
    }
}
