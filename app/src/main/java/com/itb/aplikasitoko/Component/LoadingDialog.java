package com.itb.aplikasitoko.Component;

import android.app.ProgressDialog;
import android.content.Context;

public class LoadingDialog extends ProgressDialog {
    public static LoadingDialog instance;
    public LoadingDialog(Context context) {
        super(context);
        setMessage("Loading...");
        setCancelable(false);
    }

    public static void load(Context context){
        if(instance == null){
            instance = new LoadingDialog(context);
            instance.show();
        }

    }


    public static void close(){
        if(instance!= null){
            instance.cancel();
            instance = null;
        }
    }

}