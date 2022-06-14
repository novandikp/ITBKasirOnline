package com.itb.aplikasitoko.ui.pengaturan;

public class MyKategori {
    private String Kategori;
    private String SubKategori;
    private Integer iconKategori;

    public MyKategori(String kategori, String subKategori, Integer iconKategori) {
        Kategori = kategori;
        SubKategori = subKategori;
        this.iconKategori = iconKategori;
    }

    public String getKategori() {
        return Kategori;
    }

    public void setKategori(String kategori) {
        Kategori = kategori;
    }

    public String getSubKategori() {
        return SubKategori;
    }

    public void setSubKategori(String subKategori) {
        SubKategori = subKategori;
    }

    public Integer getIconKategori() {
        return iconKategori;
    }

    public void setIconKategori(Integer iconKategori) {
        this.iconKategori = iconKategori;
    }
}
