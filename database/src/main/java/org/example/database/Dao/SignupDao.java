package org.example.database.Dao;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import org.example.database.Donor;
import org.example.database.Signup;

import java.util.List;
@Dao
public interface SignupDao {
    @Insert
    void insert(Signup user);

    @Query("SELECT * from Signup")
    List<Signup> allUsers();
}
