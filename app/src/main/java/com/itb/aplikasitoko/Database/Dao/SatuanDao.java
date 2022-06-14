package com.itb.aplikasitoko.Database.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.itb.aplikasitoko.Model.ModelSatuan;

import java.util.List;

@Dao
public interface SatuanDao {
    @Insert(onConflict  = OnConflictStrategy.REPLACE)
    void insertAll(List<ModelSatuan> satuans);

    @Insert(onConflict  = OnConflictStrategy.REPLACE)
    void insert(ModelSatuan satuan);

    @Query("select * from tblsatuan")
    LiveData<List<ModelSatuan>> getSatuans();


    @Update
    void update(ModelSatuan satuan);

    @Delete
    void delete(ModelSatuan satuan);

    @Query("DELETE FROM tblsatuan")
    void deleteAll();

}
