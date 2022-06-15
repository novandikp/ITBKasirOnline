package com.itb.aplikasitoko.Database.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.itb.aplikasitoko.Database.Dao.TokoDao;
import com.itb.aplikasitoko.Database.KasirDatabase;
import com.itb.aplikasitoko.Model.ModelToko;

public class TokoRepository {

    TokoDao tokoDao;
    KasirDatabase kasirDatabase;
    public TokoRepository(Application application){
        kasirDatabase = KasirDatabase.getInstance(application);
        tokoDao = kasirDatabase.tokoDao();
    }

//    get toko by id
    public LiveData<ModelToko> getToko(){
        return tokoDao.getToko("1");
    }


    public void insert(ModelToko toko){
        new InsertToko(tokoDao).execute(toko);
    }

    private static class InsertToko extends AsyncTask<ModelToko,Void,Void> {
        private TokoDao tokoDao;

        public InsertToko(TokoDao tokoDao) {
            this.tokoDao = tokoDao;
        }

        @Override
        protected Void doInBackground(ModelToko... modelTokos) {
            ModelToko toko =modelTokos[0];
            toko.setIdtoko("1");
            tokoDao.insert(toko);
            return null;
        }
    }
}