package com.itb.aplikasitoko.util;

import android.text.TextUtils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Modul {
    public static String intToStr(int a){
        return String.valueOf(a) ;
    }

    public static int strToInt(String v){
        try {
            return Integer.parseInt(v) ;
        } catch (Exception e){
            return 0 ;
        }
    }

    public static double strToDouble(String v){
        try {
            return Double.parseDouble(v) ;
        }catch (Exception e){
            return 0 ;
        }
    }

    public static String doubleToStr(double d){
        try {
            return String.valueOf(d) ;
        }catch (Exception e){
            return "" ;
        }
    }

    public static String getDate(String type){ //Random time type : HHmmssddMMyy
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(type);
        String formattedDate = df.format(c.getTime());
        return formattedDate ;
    }

    //buat memperjelas angka/menambah format
    public static String removeE(double value){
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(8);
        return Modul.numberFormat(df.format(value)) ;
    }
    public static String removeE(String value){
        double hasil = Modul.strToDouble(value) ;
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(8);
        return Modul.numberFormat(df.format(hasil)) ;
    }


    public  static String toString(double doubleValue){
        String rounded = String.format("%.0f", doubleValue);
        return rounded;
    }

    public static String unnumberFormat(String df){
        Locale locale=new Locale("in","ID");
        DecimalFormat format=(DecimalFormat) DecimalFormat.getInstance(locale);
        DecimalFormatSymbols symbols=format.getDecimalFormatSymbols();
        char separator=symbols.getDecimalSeparator();
        char grouping=symbols.getGroupingSeparator();

        String a=df.replace(String.valueOf(grouping),"");
        String b=a.replace(separator,'.');

        return b;
    }



    //buat membuat huruf pertama jd kapital
    public static String upperCaseFirst(String value) {

        // Convert String to char array.
        char[] array = value.toCharArray();
        // Modify first element in array.
        array[0] = Character.toUpperCase(array[0]);
        // Return string.
        return new String(array);
    }


    public static String numberFormat(String number){ // Rp. 1,000,000.00
        try{
            String hasil = "";
            String[] b = number.split("\\.") ;

            if(b.length == 1){
                String[] a = number.split("") ;
                int c=0 ;
                for(int i=a.length-1;i>=0;i--){
                    if(c == 3 && !TextUtils.isEmpty(a[i])){
                        hasil = a[i] + "." + hasil ;
                        c=1;
                    } else {
                        hasil = a[i] + hasil ;
                        c++ ;
                    }
                }
            } else {
                String[] a = b[0].split("") ;
                int c=0 ;
                for(int i=a.length-1;i>=0;i--){
                    if(c == 3 && !TextUtils.isEmpty(a[i])){
                        hasil = a[i] + "." + hasil ;
                        c=1;
                    } else {
                        hasil = a[i] + hasil ;
                        c++ ;
                    }
                }
                hasil+=","+b[1] ;
            }
            return  hasil ;
        }catch (Exception e){
            return  "" ;
        }
    }

    public static String tanggalku(String tanggal) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date tanggal1 = format.parse(tanggal);
        SimpleDateFormat formatBaru = new SimpleDateFormat("yyyy-MM-dd", new Locale("id", "ID"));
        return formatBaru.format(tanggal1);
    }

    public static String PhoneFormat(String telp) {
        String telpon = telp.substring(0,1).replace("0", "62")+telp.substring(1);
        return telpon;
    }
}