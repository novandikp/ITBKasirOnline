package com.itb.aplikasitoko;

//abstract biar gk bs dijadikan object
public abstract class Config {
    public static final String lastPageSign = "page";
    public static final String phoneOTP ="NoTelp";
//    Page Signed
      public abstract  class PageSigned{
        public static final String DASHBOARD ="Dashboard";
        public static final String OTP ="Verifikasi OTP";
        public static final String PROFIL ="Ubah Data Toko";
    }

}