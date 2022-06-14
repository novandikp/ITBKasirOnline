package com.itb.aplikasitoko.Database.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.itb.aplikasitoko.Model.ModelPegawai;

import java.util.List;

@Dao
public interface PegawaiDao {
    @Insert(onConflict  = OnConflictStrategy.REPLACE)
    void insertAll(List<ModelPegawai> pegawais);

    @Insert(onConflict  = OnConflictStrategy.REPLACE)
    void insert(ModelPegawai pegawai);

    @Query("select * from tblpegawai where nama_pegawai like '%' || :keyword || '%'")
    LiveData<List<ModelPegawai>> getPegawai(String keyword);

    @Update
    void update(ModelPegawai pegawai);

    @Delete
    void delete(ModelPegawai pegawai);

    @Query("DELETE FROM tblpegawai")
    void deleteAll();
}