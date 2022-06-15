package com.itb.aplikasitoko.Database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.itb.aplikasitoko.Database.Dao.BarangDao;
import com.itb.aplikasitoko.Database.Dao.DetailJualDao;
import com.itb.aplikasitoko.Database.Dao.JualDao;
import com.itb.aplikasitoko.Database.Dao.KategoriDao;
import com.itb.aplikasitoko.Database.Dao.PegawaiDao;
import com.itb.aplikasitoko.Database.Dao.PelangganDao;
import com.itb.aplikasitoko.Database.Dao.SatuanDao;
import com.itb.aplikasitoko.Model.ModelBarang;
import com.itb.aplikasitoko.Model.ModelDetailJual;
import com.itb.aplikasitoko.Model.ModelJual;
import com.itb.aplikasitoko.Model.ModelKategori;
import com.itb.aplikasitoko.Model.ModelPegawai;
import com.itb.aplikasitoko.Model.ModelPelanggan;
import com.itb.aplikasitoko.Model.ModelSatuan;
import com.itb.aplikasitoko.Model.ModelToko;
import com.itb.aplikasitoko.ViewModel.ModelViewStruk;
import com.itb.aplikasitoko.ViewModel.ViewModelJual;

@Database(entities ={
        ModelKategori.class,
        ModelSatuan.class,
        ModelBarang.class,
        ModelPegawai.class,
        ModelPelanggan.class,
        ModelJual.class,
        ModelDetailJual.class,
        ModelToko.class
}, views = {ModelViewStruk.class, ViewModelJual.class},version = 9)
public abstract class KasirDatabase extends RoomDatabase {
    private static final String name_database = "KasirDB";

//    Abstract Dao
    public abstract KategoriDao kategoriDao();
    public abstract BarangDao barangDao();
    public abstract DetailJualDao detailJualDao();
    public abstract JualDao jualDao();
    public abstract PegawaiDao pegawaiDao();
    public abstract PelangganDao pelangganDao();
    public abstract SatuanDao satuanDao();

    public static volatile KasirDatabase INSTANCE= null ;
//    SINGLETON
    public  static KasirDatabase getInstance(Context context){
        if(INSTANCE == null){
            synchronized (KasirDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context,KasirDatabase.class,name_database).fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }

}