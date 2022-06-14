package com.itb.aplikasitoko.Database.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.itb.aplikasitoko.Database.Dao.PelangganDao;
import com.itb.aplikasitoko.Database.KasirDatabase;
import com.itb.aplikasitoko.Model.ModelPelanggan;

import java.util.List;

public class PelangganRepository {
    public PelangganDao pelangganDao;
    private LiveData<List<ModelPelanggan>> allPelanggan;
    private KasirDatabase kasirDatabase;

    public PelangganRepository(Application application){
        kasirDatabase = KasirDatabase.getInstance(application);
        pelangganDao = kasirDatabase.pelangganDao();
        allPelanggan = pelangganDao.getPelanggans("");
    }



    public LiveData<List<ModelPelanggan>> getAllPelanggan(String keyword){
        allPelanggan = pelangganDao.getPelanggans(keyword);
        return allPelanggan;
    }

    public LiveData<List<ModelPelanggan>> getAllPelanggan() {
        return allPelanggan;
    }

    public void insertAll(List<ModelPelanggan> data, boolean truncate){
        new InsertPelangganAll(pelangganDao,truncate).execute(data);
    }

    public void insert(ModelPelanggan pelanggan){
        new InsertPelanggan(pelangganDao).execute(pelanggan);
    }

    public void delete(ModelPelanggan pelanggan){
        new DeletePelanggan(pelangganDao).execute(pelanggan);
    }

    private static class DeletePelanggan extends AsyncTask<ModelPelanggan,Void,Void> {
        private PelangganDao pelangganDao;

        public DeletePelanggan(PelangganDao pelangganDao) {
            this.pelangganDao = pelangganDao;
        }

        @Override
        protected Void doInBackground(ModelPelanggan... lists) {

            pelangganDao.delete(lists[0]);
            return null;
        }
    }

    private static class InsertPelanggan extends AsyncTask<ModelPelanggan,Void,Void> {
        private PelangganDao pelangganDao;

        public InsertPelanggan(PelangganDao pelangganDao) {
            this.pelangganDao = pelangganDao;
        }

        @Override
        protected Void doInBackground(ModelPelanggan... lists) {

            pelangganDao.insert(lists[0]);
            return null;
        }
    }

    private static class InsertPelangganAll extends AsyncTask<List<ModelPelanggan>,Void,Void> {
        private PelangganDao pelangganDao;
        private boolean truncate;

        public InsertPelangganAll(PelangganDao pelangganDao, boolean truncate) {
            this.pelangganDao = pelangganDao;
            this.truncate = truncate;
        }

        @Override
        protected Void doInBackground(List<ModelPelanggan>... lists) {
            if(truncate){
                pelangganDao.deleteAll();
            }
            pelangganDao.insertAll(lists[0]);
            return null;
        }
    }

    public void update(ModelPelanggan pelanggan){
        new UpdatePelanggan(pelangganDao).execute(pelanggan);
    }

    private static class UpdatePelanggan extends AsyncTask<ModelPelanggan,Void,Void>{
        private  PelangganDao pelangganDao;

        public UpdatePelanggan(PelangganDao pelangganDao) {
            this.pelangganDao = pelangganDao;
        }

        @Override
        protected Void doInBackground(ModelPelanggan... modelPelanggans) {
            pelangganDao.update(modelPelanggans[0]);
            return null;
        }
    }


}