package com.itb.aplikasitoko.Database.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.itb.aplikasitoko.Database.Dao.BarangDao;
import com.itb.aplikasitoko.Database.KasirDatabase;
import com.itb.aplikasitoko.Model.ModelBarang;

import java.util.List;

public class BarangRepository {
    public BarangDao barangDao;
    private LiveData<List<ModelBarang>> allBarang;
    private KasirDatabase kasirDatabase;

    public BarangRepository(Application application){
        kasirDatabase = KasirDatabase.getInstance(application);
        barangDao = kasirDatabase.barangDao();
        allBarang = barangDao.getBarang("");
    }



    public LiveData<List<ModelBarang>> getAllBarang(String keyword){
        allBarang = barangDao.getBarang(keyword);
        return allBarang;
    }

    public LiveData<List<ModelBarang>> getAllBarang() {
        return allBarang;
    }


    public void get(String idproduk, OnSearch onSearch ){
        new GetBarang(onSearch,barangDao).execute(idproduk);
    }

    public interface OnSearch{
        void findResult(ModelBarang modelBarang);
        void notFound();
    }

    private static class GetBarang extends  AsyncTask<String,Void,ModelBarang>{
        OnSearch onSearch;
        BarangDao barangDao;

        public GetBarang(OnSearch onSearch, BarangDao barangDao) {
            this.onSearch = onSearch;
            this.barangDao = barangDao;
        }

        @Override
        protected ModelBarang doInBackground(String... idbarang) {

            return barangDao.get(idbarang[0]);
        }


        @Override
        protected void onPostExecute(ModelBarang modelBarang) {
            super.onPostExecute(modelBarang);
            if(modelBarang!=null){
                onSearch.findResult(modelBarang);
            }else{
                onSearch.notFound();
            }
        }
    }

    public void insertAll(List<ModelBarang> data, boolean truncate){
        new InsertBarangAll(barangDao,truncate).execute(data);
    }

    public void insert(ModelBarang barang){
        new InsertBarang(barangDao).execute(barang);
    }

    public void delete(ModelBarang barang){
        new DeleteBarang(barangDao).execute(barang);
    }

    private static class DeleteBarang extends AsyncTask<ModelBarang,Void,Void> {
        private BarangDao barangDao;

        public DeleteBarang(BarangDao barangDao) {
            this.barangDao = barangDao;
        }

        @Override
        protected Void doInBackground(ModelBarang... lists) {

            barangDao.delete(lists[0]);
            return null;
        }
    }


    public void update(ModelBarang barang){
        new UpdateBarang(barangDao).execute(barang);
    }


    private static class UpdateBarang extends  AsyncTask<ModelBarang,Void,Void>{
        BarangDao barangDao;

        public UpdateBarang(BarangDao barangDao) {
            this.barangDao = barangDao;
        }

        @Override
        protected Void doInBackground(ModelBarang... modelBarangs) {
            barangDao.update(modelBarangs[0]);
            return null;
        }
    }



    private static class InsertBarang extends AsyncTask<ModelBarang,Void,Void> {
        private BarangDao barangDao;

        public InsertBarang(BarangDao barangDao) {
            this.barangDao = barangDao;
        }

        @Override
        protected Void doInBackground(ModelBarang... lists) {

            barangDao.insert(lists[0]);
            return null;
        }
    }


    private static class InsertBarangAll extends AsyncTask<List<ModelBarang>,Void,Void> {
        private BarangDao barangDao;
        private boolean truncate;

        public InsertBarangAll(BarangDao barangDao, boolean truncate) {
            this.barangDao = barangDao;
            this.truncate = truncate;
        }

        @Override
        protected Void doInBackground(List<ModelBarang>... lists) {
            if(truncate){
                barangDao.deleteAll();
            }
            barangDao.insertAll(lists[0]);
            return null;
        }
    }


}