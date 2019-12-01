package org.example.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DatabaseRepo {
    private String db_name = "BloodBank";
    private BloodBankDatabase ourDatabase;
    public List<Signup> listret;
    public boolean check=false;
    public DatabaseRepo(Context context) {
        getInstance(context);
    }

    private BloodBankDatabase getInstance(Context context) {
        if (ourDatabase == null) {
            listret = new ArrayList<>();
            ourDatabase = Room.databaseBuilder(context, BloodBankDatabase.class, db_name).fallbackToDestructiveMigration().build();
        }
        return ourDatabase;
    }

    @SuppressLint("StaticFieldLeak")
    public void insertDonor(final Donor donor) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                ourDatabase.donorDao().insert(donor);
                Log.e("Donor Added", donor.getName());
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void insertReceiver(final Receiver receiver) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                ourDatabase.receiverDao().insert(receiver);
                Log.e("Receiver Added", receiver.getName());
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void insertUser(final Signup user) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                ourDatabase.userDao().insert(user);
                Log.e("User Added", user.getName());
                return null;
            }
        }.execute();
    }

    public void insertDonor(String name, String address, String contact, int age, String bloodGroup) {
        Donor donor = new Donor(name, address, contact, age, bloodGroup);
        insertDonor(donor);
    }
    public void insertreceiver(String name, String address, String contact, int age, String bloodGroup) {
        Receiver receiver = new Receiver(name, address, contact, age, bloodGroup);
        insertReceiver(receiver);
    }

    public void insertUser(String name, String email, String password, char gender) {
        Signup user = new Signup(name, email, password, gender);
        insertUser(user);
    }
    public List<Signup> getAllUsers() throws ExecutionException, InterruptedException {
        return new GetUsersAsyncTask().execute().get();
    }
    private class GetUsersAsyncTask extends AsyncTask<Void, Void,List<Signup>>
    {
        @Override
        protected List<Signup> doInBackground(Void... url) {
            return ourDatabase.userDao().allUsers();
        }
    }
    public List<Donor> getAllDonors() throws ExecutionException, InterruptedException {
        return new GetDonorsAsyncTask().execute().get();
    }
    private class GetDonorsAsyncTask extends AsyncTask<Void, Void,List<Donor>>
    {
        @Override
        protected List<Donor> doInBackground(Void... url) {
            return ourDatabase.donorDao().allDonors();
        }
    }

    public List<Receiver> getAllReceivers() throws ExecutionException, InterruptedException {
        return new GetReceiversAsyncTask().execute().get();
    }
    private class GetReceiversAsyncTask extends AsyncTask<Void, Void,List<Receiver>>
    {
        @Override
        protected List<Receiver> doInBackground(Void... url) {
            return ourDatabase.receiverDao().allReceivers();
        }
    }

}