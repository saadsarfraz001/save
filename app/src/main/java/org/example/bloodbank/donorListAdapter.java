package org.example.bloodbank;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

public class donorListAdapter extends RecyclerView.Adapter<donorListAdapter.ExperienceViewHolder>{
    String [] list;
    String [] number;
    String [] blood;
    AppCompatActivity act;
    public donorListAdapter(String [] str,String []num,
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
        View view=inflater.inflate(R.layout.donorlist,parent,false);
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
                }
                Uri uri = Uri.parse("smsto:"+number[position]);
                Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                it.putExtra("sms_body", "Hello I need " + blood[position] + " Blood\nKindly Help!");
                act.startActivity(it);
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
            text=itemView.findViewById(R.id.dtext);
            img=itemView.findViewById(R.id.dimg);
            ib=itemView.findViewById(R.id.dbut);
            ibc=itemView.findViewById(R.id.dcall);
        }

    }
    private void showPermissionAlert(Context context, AppCompatActivity activity){
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.SEND_SMS}, 123);
        }
    }
}
