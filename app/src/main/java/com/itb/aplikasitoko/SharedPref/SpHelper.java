package com.itb.aplikasitoko.SharedPref;

import android.content.Context;
import android.content.SharedPreferences;

public class SpHelper {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor; //in buat edit share pef
    private Context context;

    public SpHelper(Context context) {
        this.context = context;
        sp = context.getSharedPreferences("apiku", Context.MODE_PRIVATE); //inisiasi sharedpref
        editor = sp.edit();


    }
    //gettoken
    public String getToken(){
//        return sp.getString("token", "");
            return getValue("token");
    }

    //ini set token dan disimpan di share preference
    public void setToken(String token){
        editor.putString("token", token);
        editor.commit(); //share pref hrs di commit dulu
    }

    public void setEmail(String email){
        editor.putString("email", email);
        editor.commit();
    }

    public String getEmail(){
        return getValue("email");
    }

    public void setUsername(String username){
        editor.putString("username", username);
        editor.commit();
    }

    public void setIdPegawai(String id){
        editor.putString("idpegawai", id);
        editor.commit();
    }

    public String getIdPegawai(){
        return getValue("idpegawai");
    }

    public void setKembali(double kembali){
        editor.putInt("kembali", (int) kembali);
        editor.commit();
    }

    public String getKembali(){
        return getValue("kembali");
    }

    public String getUsername(){
        return getValue("username");
    }

    public void setBoolean(String key, boolean value){
        editor.putBoolean(key,value);
        editor.commit();
    }

    //logout toko di click nnti manggil sp.clearAll() trs diintent ke login
    public void clearAll(){
        editor.clear();
        editor.commit();
    }

    public void clearPegawai(){
        sp.edit().remove("idpegawai").commit();
    }

    public void setPrinter(String printer){
        editor.putString("printer", printer);
        editor.commit();
    }

    public String getPrinter(){
        return getValue("printer", "56mm");
    }

    public boolean getBoolean(String key){
        return sp.getBoolean(key,false);
    }

    public String getValue(String key){
        return sp.getString(key, "");
    }

    public String getValue(String key, String def){
        return sp.getString(key, def);
    }

    public void setValue(String key, String value) {
        editor.putString(key, value);
        editor.commit(); //share pref hrs di commit dulu
    }
}
