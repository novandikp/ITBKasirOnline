package com.itb.aplikasitoko.Database.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.itb.aplikasitoko.Database.Dao.SatuanDao;
import com.itb.aplikasitoko.Database.KasirDatabase;
import com.itb.aplikasitoko.Model.ModelSatuan;

import java.util.List;

public class SatuanRepository {
    public SatuanDao satuanDao;
    private LiveData<List<ModelSatuan>> allSatuan;
    private KasirDatabase kasirDatabase;

    public SatuanRepository(Application application){
        kasirDatabase = KasirDatabase.getInstance(application);
        satuanDao = kasirDatabase.satuanDao();
        allSatuan = satuanDao.getSatuans();
    }

    public LiveData<List<ModelSatuan>> getAllSatuan(String keyword) {
        return allSatuan;
    }

    public LiveData<List<ModelSatuan>> getAllSatuan() {
        return allSatuan;
    }

    public void insertAll(List<ModelSatuan> data, boolean truncate){
        new InsertSatuanAll(satuanDao,truncate).execute(data);
    }

    public void insert(ModelSatuan satuan){
        new InsertSatuan(satuanDao).execute(satuan);
    }

    public void delete(ModelSatuan satuan){
        new DeleteSatuan(satuanDao).execute(satuan);
    }

    private static class DeleteSatuan extends AsyncTask<ModelSatuan,Void,Void> {
        private SatuanDao satuanDao;

        public DeleteSatuan(SatuanDao satuanDao) {
            this.satuanDao = satuanDao;
        }

        @Override
        protected Void doInBackground(ModelSatuan... lists) {

            satuanDao.delete(lists[0]);
            return null;
        }
    }

    private static class InsertSatuan extends AsyncTask<ModelSatuan,Void,Void> {
        private SatuanDao satuanDao;

        public InsertSatuan(SatuanDao satuanDao) {
            this.satuanDao = satuanDao;
        }

        @Override
        protected Void doInBackground(ModelSatuan... lists) {

            satuanDao.insert(lists[0]);
            return null;
        }
    }

    private static class InsertSatuanAll extends AsyncTask<List<ModelSatuan>,Void,Void> {
        private SatuanDao satuanDao;
        private boolean truncate;

        public InsertSatuanAll(SatuanDao satuanDao, boolean truncate) {
            this.satuanDao = satuanDao;
            this.truncate = truncate;
        }

        @Override
        protected Void doInBackground(List<ModelSatuan>... lists) {
            if(truncate){
                satuanDao.deleteAll();
            }
            satuanDao.insertAll(lists[0]);
            return null;
        }
    }



    public void update(ModelSatuan satuan){
        new UpdateSatuan(satuanDao).execute(satuan);
    }

    private static class UpdateSatuan extends AsyncTask<ModelSatuan,Void,Void>{
        private SatuanDao satuanDao;

        public UpdateSatuan(SatuanDao satuanDao) {
            this.satuanDao = satuanDao;
        }

        @Override
        protected Void doInBackground(ModelSatuan... modelSatuans) {
            satuanDao.update(modelSatuans[0]);
            return null;
        }
    }

}