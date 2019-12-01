package org.example.database.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import org.example.database.Donor;

import java.util.List;
@Dao
public interface BloodDonorDao {
    @Insert
    void insert(Donor donor);

    @Query("SELECT * from Donor")
    List<Donor> allDonors();
}
