package org.example.bloodbank;


import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

public class donorAdminAdapter extends RecyclerView.Adapter<donorAdminAdapter.ExperienceViewHolder>{
    String [] list;
    String [] number;
    String [] blood;
    private static final String CHANNEL_ID = "channel_01";
    AppCompatActivity act;
    public donorAdminAdapter(String [] str,String []num,
                             String [] b,AppCompatActivity activity) {
        this.list = str;
        this.number=num;
        this.blood=b;
        act=activity;
    }
    @NonNull
    @Override
    public ExperienceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.donorlistadmin,parent,false);
        return new ExperienceViewHolder(view,act);
    }

    @Override
    public void onBindViewHolder(@NonNull ExperienceViewHolder holder, final int position) {
        String str=list[position];
        holder.text.setText(str);
        holder.ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (act.checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                        showPermissionAlert(act.getApplicationContext(), act);
                    }
                    Uri uri = Uri.parse("smsto:"+number[position]);
                    Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                    it.putExtra("sms_body", "Hello I need " + blood[position] + " Blood\nKindly Help!");
                    act.startActivity(it);
                }
            }
        });
        holder.ibc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+number[position]));
                act.startActivity(intent);
            }
        });
    }
    private void sendNotification(String notificationDetails) {
        // Get an instance of the Notification manager
        NotificationManager mNotificationManager =
                (NotificationManager) act.getSystemService(Context.NOTIFICATION_SERVICE);

        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = act.getString(R.string.app_name);
            // Create the channel for the notification
            NotificationChannel mChannel =
                    new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);

            // Set the Notification Channel for the Notification Manager.
            mNotificationManager.createNotificationChannel(mChannel);
        }

        // Create an explicit content Intent that starts the main Activity.
        Intent notificationIntent = new Intent(act.getApplicationContext(), donorAdminFragment.class);

        // Construct a task stack.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(act);

        // Add the main Activity to the task stack as the parent.
        stackBuilder.addParentStack(act);

        // Push the content Intent onto the stack.
        stackBuilder.addNextIntent(notificationIntent);

        // Get a PendingIntent containing the entire back stack.
        PendingIntent notificationPendingIntent =
            stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get a notification builder that's compatible with platform versions >= 4
        NotificationCompat.Builder builder = new NotificationCompat.Builder(act);

        // Define the notification settings.
        builder.setSmallIcon(R.drawable.blood)
                // In a real app, you may want to use a library like Volley
                // to decode the Bitmap.
                .setLargeIcon(BitmapFactory.decodeResource(act.getResources(),
                        R.drawable.blood))
                .setColor(Color.RED)
                .setContentTitle(notificationDetails)
                .setContentText(act.getString(R.string.geofence_transition_notification_text))
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

    @Override
    public int getItemCount() {
        return list.length;
    }

    public class ExperienceViewHolder extends RecyclerView.ViewHolder{
        TextView text;
        ImageView img;
        ImageButton ib;
        ImageButton ibc;
        public ExperienceViewHolder(@NonNull View itemView,AppCompatActivity activity) {
            super(itemView);
            text=itemView.findViewById(R.id.text2);
            img=itemView.findViewById(R.id.img2);
            ib=itemView.findViewById(R.id.button5);
            ibc=itemView.findViewById(R.id.callbtn);
        }

    }
    private void sendSMS(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }
    private void showPermissionAlert(Context context, AppCompatActivity activity){
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.SEND_SMS}, 123);
        }
    }
}
