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

public class receiverAdminAdapter extends RecyclerView.Adapter<receiverAdminAdapter.ExperienceViewHolder>{
    String [] list;
    String [] number;
    String [] blood;
    private static final String CHANNEL_ID = "channel_01";
    AppCompatActivity act;
    public receiverAdminAdapter(String [] str,String []num,
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
        View view=inflater.inflate(R.layout.receiverlistadmin,parent,false);
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
                    it.putExtra("sms_body", "Hello  "+blood[position]+" Blood group Receiver");
                    act.startActivity(it);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.length;
    }

    public class ExperienceViewHolder extends RecyclerView.ViewHolder{
        TextView text;
        ImageView img;
        ImageButton ib;
        public ExperienceViewHolder(@NonNull View itemView,AppCompatActivity activity) {
            super(itemView);
            text=itemView.findViewById(R.id.rtext);
            img=itemView.findViewById(R.id.rimg);
            ib=itemView.findViewById(R.id.rbut);

        }

    }
    private void showPermissionAlert(Context context, AppCompatActivity activity){
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.SEND_SMS}, 123);
        }
    }
}
