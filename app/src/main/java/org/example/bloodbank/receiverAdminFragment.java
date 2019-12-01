package org.example.bloodbank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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

public class receiverAdminFragment extends AppCompatActivity {

    DatabaseRepo db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_receiver);
        db=new DatabaseRepo(getApplicationContext());
        RecyclerView programminglist=(RecyclerView) findViewById(R.id.recyclerView3);
        programminglist.setLayoutManager(new LinearLayoutManager(this));
        String [] str;
        String [] num;
        String [] blood;
        int size=0;
        int n=0;
        int b=0;
        try {
            List<Receiver> list=db.getAllReceivers();
            List<String> strlist=new ArrayList<>();
            List<String> numlist=new ArrayList<>();
            List<String> bloodlist=new ArrayList<>();
            for(Receiver receiver: list){
                strlist.add(receiver.getName() + "\n" + receiver.getAddress() + "\n"
                        + receiver.getContact() + "\n" + receiver.getBloodGroup() + "\n");
                numlist.add(receiver.getContact());
                bloodlist.add(receiver.getBloodGroup());
            }
            if(strlist.size()>0) {
                str = new String[strlist.size()];
                for (String s : strlist) {
                    str[size] = s;
                    Log.e("receiver List : ", str[size]);
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
                programminglist.setAdapter(new receiverAdminAdapter(str,num,blood,receiverAdminFragment.this));
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}