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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.example.database.DatabaseRepo;
import org.example.database.Donor;
import org.example.database.Receiver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class donorListFragment extends AppCompatActivity {

    DatabaseRepo db;
    ImageButton ib;
    DatabaseReference myRef;
    FirebaseDatabase database;
    GetDonorFromDatabase donors=GetDonorFromDatabase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycle);
        ib=findViewById(R.id.dbut);
        db=new DatabaseRepo(getApplicationContext());
        RecyclerView programminglist=(RecyclerView) findViewById(R.id.recycledonor1);
        programminglist.setLayoutManager(new LinearLayoutManager(this));
        String [] str;
        String [] num;
        String [] blood;
        int size=0;
        int n=0;
        int b=0;
        try {
            //List<Donor> list=db.getAllDonors();
            List<DonorData> list=donors.list;
            List<Receiver> listr=db.getAllReceivers();
            List<String> strlist=new ArrayList<>();
            List<String> numlist=new ArrayList<>();
            List<String> bloodlist=new ArrayList<>();
            for(DonorData donor: list){
                if(donor.getBloodGroup().equals(listr.get(listr.size()-1).getBloodGroup())) {
                    strlist.add(donor.getName() + "\n" + donor.getAddress() + "\n"
                            + donor.getContact() + "\n" + donor.getBloodGroup() + "\n");
                    numlist.add(donor.getContact());
                    bloodlist.add(donor.getBloodGroup());
                }
            }
            Log.e(Integer.toString(strlist.size()), "Length");
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
                programminglist.setAdapter(new donorListAdapter(str,num,blood,donorListFragment.this));
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}