package com.itb.aplikasitoko.Database.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.itb.aplikasitoko.Database.Dao.JualDao;
import com.itb.aplikasitoko.Database.KasirDatabase;
import com.itb.aplikasitoko.Model.ModelJual;

import java.util.List;

public class JualRepository {
    public JualDao jualDao;
    private LiveData<List<ModelJual>> allJual;
    private KasirDatabase kasirDatabase;

    public JualRepository(Application application){
        kasirDatabase = KasirDatabase.getInstance(application);
        jualDao = kasirDatabase.jualDao();
        allJual = jualDao.getJual("");
    }

    public LiveData<ModelJual> getOrder(int idjual){
        return jualDao.getOrder(idjual);
    }



    public LiveData<List<ModelJual>> getAllJual(String keyword){
        allJual = jualDao.getJual(keyword);
        return allJual;
    }

    public LiveData<List<ModelJual>> getAllJual() {
        return allJual;
    }

    public void insertAll(List<ModelJual> data, boolean truncate){
        new InsertJualAll(jualDao,truncate).execute(data);
    }

    public static interface onInsertJual{
        void onComplete(Long modelJual);

    }

    public void insert(ModelJual jual, onInsertJual onInsertJual){
        new InsertJual(jualDao, onInsertJual).execute(jual);
    }

    public void delete(ModelJual jual){
        new DeleteJual(jualDao).execute(jual);
    }

    private static class DeleteJual extends AsyncTask<ModelJual,Void,Void> {
        private JualDao jualDao;

        public DeleteJual(JualDao jualDao) {
            this.jualDao = jualDao;
        }

        @Override
        protected Void doInBackground(ModelJual... lists) {

            jualDao.delete(lists[0]);
            return null;
        }
    }

    private static class InsertJual extends AsyncTask<ModelJual,Void,Long> {
        private JualDao jualDao;
        private onInsertJual onInsertJual;

        public InsertJual(JualDao jualDao, JualRepository.onInsertJual onInsertJual) {
            this.jualDao = jualDao;
            this.onInsertJual = onInsertJual;
        }

        @Override
        protected Long doInBackground(ModelJual... lists) {

            return jualDao.insert(lists[0]);

        }

        @Override
        protected void onPostExecute(Long modelJual) {
            super.onPostExecute(modelJual);
            onInsertJual.onComplete(modelJual);
        }
    }

    private static class InsertJualAll extends AsyncTask<List<ModelJual>,Void,Void> {
        private JualDao jualDao;
        private boolean truncate;

        public InsertJualAll(JualDao jualDao, boolean truncate) {
            this.jualDao = jualDao;
            this.truncate = truncate;
        }

        @Override
        protected Void doInBackground(List<ModelJual>... lists) {
            if(truncate){
                jualDao.deleteAll();
            }
            jualDao.insertAll(lists[0]);
            return null;
        }
    }


    public void update(ModelJual modelJual){
        new UpdateJual(jualDao).execute(modelJual);
    }


    private static class UpdateJual extends AsyncTask<ModelJual,Void,Void>{

        private JualDao jualDao;

        public UpdateJual(JualDao jualDao) {
            this.jualDao = jualDao;
        }

        @Override
        protected Void doInBackground(ModelJual... modelJuals) {
            jualDao.update(modelJuals[0]);
            return null;
        }
    }


}