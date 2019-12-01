package org.example.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import org.example.database.Dao.BloodDonorDao;
import org.example.database.Dao.BloodReceiverDao;
import org.example.database.Dao.SignupDao;

@Database(entities ={Donor.class,Receiver.class,Signup.class},version=9,exportSchema = false)
public abstract class BloodBankDatabase extends RoomDatabase {
    public abstract BloodDonorDao donorDao();
    public abstract SignupDao userDao();
    public abstract BloodReceiverDao receiverDao();

}
