package com.itb.aplikasitoko.ViewModel;

//@DatabaseView(viewName = "view_kategori", value = " SELECT tblkategori.idkategori,\n" +
//        "    tblkategori.nama_kategori,\n" +
//        "    tbltoko.idtoko,\n" +
//        "    tbltoko.nama_toko,\n" +
//        "    tbltoko.alamat_toko,\n" +
//        "    tbltoko.nomer_toko,\n" +
//        "    tbltoko.nama_pemilik,\n" +
//        "    tbltoko.email_toko,\n" +
//        "    tbltoko.password_toko,\n" +
//        "    tbltoko.jenis_toko\n" +
//        "   FROM tblkategori\n" +
//        "    inner join tbltoko on tblkategori.idtoko = tbltoko.idtoko")
public class ViewModelRekapKategori {
    private int idkategori;
    private String tanggal_jual;
    private String nama_kategori;
    private String total_jual;
    private String total_pendapatan;

    public int getIdkategori() {
        return idkategori;
    }

    public void setIdkategori(int idkategori) {
        this.idkategori = idkategori;
    }

    public String getTanggal_jual() {
        return tanggal_jual;
    }

    public void setTanggal_jual(String tanggal_jual) {
        this.tanggal_jual = tanggal_jual;
    }

    public String getNama_kategori() {
        return nama_kategori;
    }

    public void setNama_kategori(String nama_kategori) {
        this.nama_kategori = nama_kategori;
    }

    public String getTotal_jual() {
        return total_jual;
    }

    public void setTotal_jual(String total_jual) {
        this.total_jual = total_jual;
    }

    public String getTotal_pendapatan() {
        return total_pendapatan;
    }

    public void setTotal_pendapatan(String total_pendapatan) {
        this.total_pendapatan = total_pendapatan;
    }
}
