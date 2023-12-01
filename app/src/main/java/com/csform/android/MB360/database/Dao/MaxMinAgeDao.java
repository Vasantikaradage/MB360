package com.csform.android.MB360.database.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.csform.android.MB360.insurance.enrollment.repository.responseclass.MaxMinAgeMainResponse;

@Dao
public interface MaxMinAgeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAge(MaxMinAgeMainResponse maxMinAgeMainResponse);

    @Query("SELECT * FROM AGE WHERE oeGrpBasInfoSrNo = :oeGrpBasInfoSrNo")
    MaxMinAgeMainResponse getAGE(String oeGrpBasInfoSrNo);

    @Query("DELETE FROM AGE")
    void deleteAge();
}
