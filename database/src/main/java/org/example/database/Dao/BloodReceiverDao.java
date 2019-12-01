package org.example.database.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import org.example.database.Receiver;

import java.util.List;
@Dao
public interface BloodReceiverDao {
    @Insert
    void insert(Receiver receiver);

    @Query("SELECT * from Receiver")
    List<Receiver> allReceivers();
}