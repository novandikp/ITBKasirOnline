package com.itb.aplikasitoko.Database.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.itb.aplikasitoko.Database.Dao.PegawaiDao;
import com.itb.aplikasitoko.Database.KasirDatabase;
import com.itb.aplikasitoko.Model.ModelPegawai;

import java.util.List;

public class PegawaiRepository {
    public PegawaiDao pegawaiDao;
    private LiveData<List<ModelPegawai>> allPegawai;
    private KasirDatabase kasirDatabase;

    public PegawaiRepository(Application application){
        kasirDatabase = KasirDatabase.getInstance(application);
        pegawaiDao = kasirDatabase.pegawaiDao();
        allPegawai = pegawaiDao.getPegawai("");
    }



    public LiveData<List<ModelPegawai>> getAllPegawai(String keyword){
        allPegawai = pegawaiDao.getPegawai(keyword);
        return allPegawai;
    }

    public LiveData<List<ModelPegawai>> getAllPegawai() {
        return allPegawai;
    }

    public void insertAll(List<ModelPegawai> data, boolean truncate){
        new InsertPegawaiAll(pegawaiDao,truncate).execute(data);
    }

    public void insert(ModelPegawai pegawai){
        new InsertPegawai(pegawaiDao).execute(pegawai);
    }

    public void delete(ModelPegawai pegawai){
        new DeletePegawai(pegawaiDao).execute(pegawai);
    }

    private static class DeletePegawai extends AsyncTask<ModelPegawai,Void,Void> {
        private PegawaiDao pegawaiDao;

        public DeletePegawai(PegawaiDao pegawaiDao) {
            this.pegawaiDao = pegawaiDao;
        }

        @Override
        protected Void doInBackground(ModelPegawai... lists) {

            pegawaiDao.delete(lists[0]);
            return null;
        }
    }

    private static class InsertPegawai extends AsyncTask<ModelPegawai,Void,Void> {
        private PegawaiDao pegawaiDao;

        public InsertPegawai(PegawaiDao pegawaiDao) {
            this.pegawaiDao = pegawaiDao;
        }

        @Override
        protected Void doInBackground(ModelPegawai... lists) {

            pegawaiDao.insert(lists[0]);
            return null;
        }
    }

    private static class InsertPegawaiAll extends AsyncTask<List<ModelPegawai>,Void,Void> {
        private PegawaiDao pegawaiDao;
        private boolean truncate;

        public InsertPegawaiAll(PegawaiDao pegawaiDao, boolean truncate) {
            this.pegawaiDao = pegawaiDao;
            this.truncate = truncate;
        }

        @Override
        protected Void doInBackground(List<ModelPegawai>... lists) {
            if(truncate){
                pegawaiDao.deleteAll();
            }
            pegawaiDao.insertAll(lists[0]);
            return null;
        }
    }


    public void update(ModelPegawai pegawai){
        new UpdatePegawai(pegawaiDao).execute(pegawai);
    }

    private static class UpdatePegawai extends AsyncTask<ModelPegawai,Void,Void>{
        private PegawaiDao pegawaiDao;

        public UpdatePegawai(PegawaiDao pegawaiDao) {
            this.pegawaiDao = pegawaiDao;
        }

        @Override
        protected Void doInBackground(ModelPegawai... modelPegawais) {
            pegawaiDao.update(modelPegawais[0]);
            return null;
        }
    }

}