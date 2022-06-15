package com.itb.aplikasitoko.Database.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.itb.aplikasitoko.Model.ModelSatuan;
import com.itb.aplikasitoko.Model.ModelToko;

import java.util.List;

@Dao
public interface TokoDao {
    @Insert(onConflict  = OnConflictStrategy.REPLACE)
    void insert(ModelToko satuan);

//    get toko by id
    @Query("select * from tbltoko where idtoko=:idtoko")
    LiveData<ModelToko> getToko(String idtoko);

}
