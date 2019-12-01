package org.example.bloodbank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import org.example.database.DatabaseRepo;
import org.example.database.Donor;
import org.example.database.Receiver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class donorAdminFragment extends AppCompatActivity {

    DatabaseRepo db;
    GetDonorFromDatabase donors=GetDonorFromDatabase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_donor);
        db=new DatabaseRepo(getApplicationContext());
        RecyclerView programminglist=(RecyclerView) findViewById(R.id.recyclerView2);
        programminglist.setLayoutManager(new LinearLayoutManager(this));
        String [] str;
        String [] num;
        String [] blood;
        int size=0;
        int n=0;
        int b=0;
        try {
            List<DonorData> list=donors.list;
            List<Donor> listd=db.getAllDonors();
            List<String> strlist=new ArrayList<>();
            List<String> numlist=new ArrayList<>();
            List<String> bloodlist=new ArrayList<>();
            for(DonorData donor: list){
                strlist.add(donor.getName() + "\n" + donor.getAddress() + "\n"
                        + donor.getContact() + "\n" + donor.getBloodGroup() + "\n");
                numlist.add(donor.getContact());
                bloodlist.add(donor.getBloodGroup());
            }
            if(strlist.size()>0) {
                str = new String[strlist.size()];
                for (String s : strlist) {
                    str[size] = s;
                    Log.e("Donor List : ", str[size]);
                    size++;
                }
                num=new String[numlist.size()];
                for (String s : numlist) {
                    num[n] = s;
                    Log.e("Contact List : ", num[n]);
                    n++;
                }
                blood=new String[bloodlist.size()];
                for (String s : bloodlist) {
                    blood[b] = s;
                    Log.e("Blood Group List : ", blood[b]);
                    b++;
                }
                programminglist.setAdapter(new donorAdminAdapter(str,num,blood,donorAdminFragment.this));
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}