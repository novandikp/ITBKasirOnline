package com.itb.aplikasitoko.Database.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.itb.aplikasitoko.Model.ModelDetailJual;
import com.itb.aplikasitoko.ViewModel.ModelViewStruk;

import java.util.List;

@Dao
public interface DetailJualDao {
    @Insert(onConflict  = OnConflictStrategy.REPLACE)
    void insertAll(List<ModelDetailJual> detailjuals);

    @Insert(onConflict  = OnConflictStrategy.REPLACE)
    void insert(ModelDetailJual detailjual);

    @Query("select * from tbldetailjual")
    LiveData<List<ModelDetailJual>> getAllJual();

    @Query("select * from tbldetailjual where idjual=:idjual")
    LiveData<List<ModelDetailJual>> getDetailOrder(int idjual);

    @Update
    void update(ModelDetailJual detailJual);

    @Delete
    void delete(ModelDetailJual detailjual);

    @Query("DELETE FROM tbldetailjual")
    void deleteAll();


    @Query("select * from view_detailjual where idjual=:idjual")
    LiveData<List<ModelViewStruk>> getDetailStruk(int idjual);


    //@Query("select sum(jumlahjual) total_jual,SUM(jumlahjual*hargajual)total_pendapatan, nama_kategori from view_detailjual group by idkategori, nama_kategori")
}