package com.itb.aplikasitoko.Database.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.itb.aplikasitoko.Database.Dao.KategoriDao;
import com.itb.aplikasitoko.Database.KasirDatabase;
import com.itb.aplikasitoko.Model.ModelKategori;

import java.util.List;

public class KategoriRepository {
    public KategoriDao kategoriDao;
    private LiveData<List<ModelKategori>> allKategori;
    private KasirDatabase kasirDatabase;

    public KategoriRepository(Application application){
        kasirDatabase = KasirDatabase.getInstance(application);
        kategoriDao = kasirDatabase.kategoriDao();
        allKategori = kategoriDao.getKategori();
    }

    public LiveData<List<ModelKategori>> getAllKategori(String keyword) {
        return kategoriDao.getKategori();
    }

    public LiveData<List<ModelKategori>> getAllKategori() {
        return kategoriDao.getKategori();
    }

    public void insertAll(List<ModelKategori> data, boolean truncate){
        new InsertKategoriAll(kategoriDao,truncate).execute(data);
    }

    public void insert(ModelKategori kategori){
        new InsertKategori(kategoriDao).execute(kategori);
    }

    public void delete(ModelKategori kategori){
        new DeleteKategori(kategoriDao).execute(kategori);
    }

    public void update(ModelKategori kategori){
        new UpdateKategori(kategoriDao).execute(kategori);
    }

    private static class DeleteKategori extends AsyncTask<ModelKategori,Void,Void> {
        private KategoriDao kategoriDao;

        public DeleteKategori(KategoriDao kategoriDao) {
            this.kategoriDao = kategoriDao;
        }

        @Override
        protected Void doInBackground(ModelKategori... lists) {

            kategoriDao.delete(lists[0]);
            return null;
        }
    }

    private static class InsertKategori extends AsyncTask<ModelKategori,Void,Void> {
        private KategoriDao kategoriDao;

        public InsertKategori(KategoriDao kategoriDao) {
            this.kategoriDao = kategoriDao;
        }

        @Override
        protected Void doInBackground(ModelKategori... lists) {

            kategoriDao.insert(lists[0]);
            return null;
        }
    }

    private static class UpdateKategori extends AsyncTask<ModelKategori,Void,Void> {
        private KategoriDao kategoriDao;

        public UpdateKategori(KategoriDao kategoriDao) {
            this.kategoriDao = kategoriDao;
        }

        @Override
        protected Void doInBackground(ModelKategori... lists) {

            kategoriDao.update(lists[0]);
            return null;
        }
    }


    private static class InsertKategoriAll extends AsyncTask<List<ModelKategori>,Void,Void> {
        private KategoriDao kategoriDao;
        private boolean truncate;

        public InsertKategoriAll(KategoriDao kategoriDao, boolean truncate) {
            this.kategoriDao = kategoriDao;
            this.truncate = truncate;
        }

        @Override
        protected Void doInBackground(List<ModelKategori>... lists) {
            if(truncate){
                kategoriDao.deleteAll();
            }
            kategoriDao.insertAll(lists[0]);
            return null;
        }
    }

//    public LiveData<List<ViewModelRekapKategori>> getRekapKategori(int idkategori){
//        return kategoriDao.getRekapKategori(idkategori);
//    }
}