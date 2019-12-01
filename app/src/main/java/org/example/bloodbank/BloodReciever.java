package org.example.bloodbank;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.example.database.DatabaseRepo;

import java.util.regex.Pattern;

public class BloodReciever extends Fragment {

    public static final Pattern contact=
            Pattern.compile("^"+
                    "(?=.*[0-9])" +
                    "(?=\\S+$)" +
                    ".{11,}" +
                    "$"
            );
    public static final Pattern age=
            Pattern.compile("^"+
                    "(?=.*[0-9])" +
                    "(?=\\S+$)" +
                    ".{2,}" +
                    "$"
            );

    EditText nm;
    EditText cn;
    EditText ag;
    Button signup;
    DatabaseRepo db;
    EditText add;
    Spinner spin;
    FirebaseDatabase database;
    GetDonorFromDatabase donors;
    DatabaseReference myRef;
    private static final String CHANNEL_ID = "channel_01";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.bloodreciever,container,false);
        db=new DatabaseRepo(getContext());
        database = FirebaseDatabase.getInstance();
        donors=GetDonorFromDatabase.getInstance();
        myRef = database.getReference("Donors");
        signup = (Button) view.findViewById(R.id.button12);
        add=view.findViewById(R.id.editText3);
        nm= (EditText) view.findViewById(R.id.editText);
        cn= (EditText) view.findViewById(R.id.editText42);
        ag= (EditText) view.findViewById(R.id.editText4);
        String[] blood = { "A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-" };
        spin = (Spinner) view.findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, blood);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(true) {
                   db.insertreceiver(nm.getText().toString(),add.getText().toString(),cn.getText().toString()
                            ,Integer.parseInt(ag.getText().toString()),spin.getSelectedItem().toString());
                    String note="Searching for Required Blood Donor";
                    sendNotification(note);
                    check();
                }
            }
        });
        return view;
    }

    private void sendNotification(String notificationDetails) {
        // Get an instance of the Notification manager
        NotificationManager mNotificationManager =
                (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getActivity().getString(R.string.app_name);
            // Create the channel for the notification
            NotificationChannel mChannel =
                    new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);

            // Set the Notification Channel for the Notification Manager.
            mNotificationManager.createNotificationChannel(mChannel);
        }

        // Create an explicit content Intent that starts the main Activity.
        Intent notificationIntent = new Intent(getActivity().getApplicationContext(), donorAdminFragment.class);

        // Construct a task stack.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity());

        // Add the main Activity to the task stack as the parent.
        stackBuilder.addParentStack(getActivity());

        // Push the content Intent onto the stack.
        stackBuilder.addNextIntent(notificationIntent);

        // Get a PendingIntent containing the entire back stack.
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get a notification builder that's compatible with platform versions >= 4
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity());

        // Define the notification settings.
        builder.setSmallIcon(R.drawable.blood)
                // In a real app, you may want to use a library like Volley
                // to decode the Bitmap.
                .setLargeIcon(BitmapFactory.decodeResource(getActivity().getResources(),
                        R.drawable.blood))
                .setColor(Color.RED)
                .setContentTitle(notificationDetails)
                .setContentText(getActivity().getString(R.string.geofence_transition_notification_text))
                .setContentIntent(notificationPendingIntent);

        // Set the Channel ID for Android O.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID); // Channel ID
        }

        // Dismiss notification once the user touches it.
        builder.setAutoCancel(true);

        // Issue the notification
        mNotificationManager.notify(0, builder.build());
    }

    private boolean validateContact() {
        String contactInput=cn.getText().toString();
        if (contactInput.isEmpty()) {
            cn.setError("Field can't be empty");
            return false;
        } else if (!contact.matcher(contactInput).matches()) {
            cn.setError("Please enter a valid contact number");
            return false;
        } else {
            cn.setError(null);
            return true;
        }
    }

    private boolean validateAge() {
        String ageInput=ag.getText().toString();
        if (ageInput.isEmpty()) {
            ag.setError("Field can't be empty");
            return false;
        } else if (!age.matcher(ageInput).matches()) {
            ag.setError("Please enter a valid age");
            return false;
        } else {
            ag.setError(null);
            return true;
        }
    }
    public boolean confirmInput(View v) {
        if (!validateContact() | validateAge()) {
            return false;
        }
        else{
            return true;
        }
    }
    public void check() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot DonorSnapshot: dataSnapshot.getChildren()) {
                    DonorData dataGot=DonorSnapshot.getValue(DonorData.class);
                    Log.e(dataGot.name,"::"+dataGot.bloodGroup);
                    donors.addDonor(new DonorData(dataGot.name,dataGot.address,dataGot.contact,dataGot.age,dataGot.bloodGroup));
                    int size= donors.list.size();
                    String sizeGot=Integer.toString(size);
                    Log.e("Donor Count : ",sizeGot);
                }
                Intent intent = new Intent(getActivity(), donorListFragment.class);
                startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

}

