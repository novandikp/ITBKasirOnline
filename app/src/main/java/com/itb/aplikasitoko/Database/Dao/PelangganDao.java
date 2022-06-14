package com.itb.aplikasitoko.Database.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.itb.aplikasitoko.Model.ModelPelanggan;

import java.util.List;

@Dao
public interface PelangganDao {
    @Insert(onConflict  = OnConflictStrategy.REPLACE)
    void insertAll(List<ModelPelanggan> pelanggans);

    @Insert(onConflict  = OnConflictStrategy.REPLACE)
    void insert(ModelPelanggan pelanggan);

    @Query("select * from tblpelanggan where nama_pelanggan like '%' || :keyword || '%' ")
    LiveData<List<ModelPelanggan>> getPelanggans(String keyword);

    @Update
    void update(ModelPelanggan pelanggan);

    @Delete
    void delete(ModelPelanggan pelanggan);

    @Query("DELETE FROM tblpelanggan")
    void deleteAll();
}