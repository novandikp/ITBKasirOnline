package com.itb.aplikasitoko.ViewModel;

import androidx.room.DatabaseView;

@DatabaseView(viewName = "view_detailjual", value = "SELECT tbldetailjual.iddetailjual,\n" +
        "    tbljual.idjual,\n" +
        "    tblbarang.idbarang,\n" +
        "    tbldetailjual.jumlahjual,\n" +
        "    tbldetailjual.hargajual,\n" +
        "    tbldetailjual.hargajual - tblbarang.hargabeli as laba,\n" +
        "    tbljual.fakturjual,\n" +
        "    tbljual.bayar,\n" +
        "    tbljual.total,\n" +
        "    tbljual.kembali,\n" +
        "    tbljual.potongan,\n" +
        "    COALESCE(tbljual.idpelanggan, 0) AS idpelanggan,\n" +
        "    tbljual.idpegawai,\n" +
        "    tblbarang.idkategori,\n" +
        "    tblbarang.idsatuan,\n" +
        "    tblbarang.barang,\n" +
        "    tblbarang.harga,\n" +
        "    tblbarang.hargabeli,\n" +
        "    tblbarang.stok,\n" +
        "    tblbarang.flag_stok,\n" +
        "    COALESCE(tblpelanggan.nama_pelanggan, 'Umum') AS nama_pelanggan,\n" +
        "    COALESCE(tblpelanggan.alamat_pelanggan, '-') AS alamat_pelanggan,\n" +
        "    COALESCE(tblpelanggan.no_telepon, '-') AS no_telepon,\n" +
        "    tbljual.tanggal_jual,\n" +
        "    tblsatuan.nama_satuan,\n" +
        "    tblkategori.nama_kategori,\n" +
        "   tblpegawai.nama_pegawai," +
        "   tblpegawai.alamat_pegawai," +
        "   tblpegawai.no_pegawai" +
        "   FROM tbldetailjual inner join tbljual on tbljual.idjual = tbldetailjual.idjual\n" +
        "   left join tblpelanggan on tblpelanggan.idpelanggan = tbljual.idpelanggan\n" +
        "   inner join tblbarang on tblbarang.idbarang = tbldetailjual.idbarang\n" +
        "   inner join tblkategori on tblkategori.idkategori = tblbarang.idkategori\n" +
        "   inner join tblsatuan on tblsatuan.idsatuan = tblbarang.idsatuan\n" +
        "   inner join tblpegawai on tblpegawai.idpegawai = tbljual.idpegawai")
public class ModelViewStruk {
    public void setIddetailjual(int iddetailjual) {
        this.iddetailjual = iddetailjual;
    }

    public void setIdjual(int idjual) {
        this.idjual = idjual;
    }

    public void setIdbarang(String idbarang) {
        this.idbarang = idbarang;
    }

    public void setJumlahjual(double jumlahjual) {
        this.jumlahjual = jumlahjual;
    }

    public void setHargajual(double hargajual) {
        this.hargajual = hargajual;
    }

    public void setFakturjual(String fakturjual) {
        this.fakturjual = fakturjual;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setBayar(double bayar) {
        this.bayar = bayar;
    }

    public void setKembali(double kembali) {
        this.kembali = kembali;
    }

    public void setPotongan(double potongan) {
        this.potongan = potongan;
    }

    public void setIdpelanggan(int idpelanggan) {
        this.idpelanggan = idpelanggan;
    }

    public void setIdpegawai(int idpegawai) {
        this.idpegawai = idpegawai;
    }

    public void setIdkategori(int idkategori) {
        this.idkategori = idkategori;
    }

    public void setIdsatuan(int idsatuan) {
        this.idsatuan = idsatuan;
    }

    public void setBarang(String barang) {
        this.barang = barang;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    public void setHargabeli(double hargabeli) {
        this.hargabeli = hargabeli;
    }

    public void setStok(double stok) {
        this.stok = stok;
    }

    public void setFlag_stok(int flag_stok) {
        this.flag_stok = flag_stok;
    }

    public void setNama_pelanggan(String nama_pelanggan) {
        this.nama_pelanggan = nama_pelanggan;
    }

    public void setAlamat_pelanggan(String alamat_pelanggan) {
        this.alamat_pelanggan = alamat_pelanggan;
    }

    public void setNo_telepon(String no_telepon) {
        this.no_telepon = no_telepon;
    }

    public void setTanggal_jual(String tanggal_jual) {
        this.tanggal_jual = tanggal_jual;
    }

    public void setNama_satuan(String nama_satuan) {
        this.nama_satuan = nama_satuan;
    }

    public void setNama_kategori(String nama_kategori) {
        this.nama_kategori = nama_kategori;
    }

    private int iddetailjual;
    private int idjual;
    private String idbarang;
    private double jumlahjual;
    private double hargajual;
    private String fakturjual;
    private double total;
    private double bayar;
    private double kembali;
    private double potongan;
    private  int idpelanggan;
    private int idpegawai;
    private int idkategori;
    private int idsatuan;
    private String barang;
    private double harga;
    private double hargabeli;
    private double stok;
    private int flag_stok;
    private String nama_pelanggan;
    private String alamat_pelanggan;
    private String no_telepon;
    private String tanggal_jual;
    private String nama_satuan;
    private String nama_kategori;
    private double laba;
    private String nama_pegawai;
    private String alamat_pegawai;
    private String no_pegawai;

    public double getLaba() {
        return laba;
    }

    public String getAlamat_pegawai() {
        return alamat_pegawai;
    }

    public void setAlamat_pegawai(String alamat_pegawai) {
        this.alamat_pegawai = alamat_pegawai;
    }

    public String getNo_pegawai() {
        return no_pegawai;
    }

    public void setNo_pegawai(String no_pegawai) {
        this.no_pegawai = no_pegawai;
    }

    public String getNama_pegawai() {
        return nama_pegawai;
    }

    public void setNama_pegawai(String nama_pegawai) {
        this.nama_pegawai = nama_pegawai;
    }

    public void setLaba(double laba) {
        this.laba = laba;
    }

    public int getIddetailjual() {
        return iddetailjual;
    }

    public int getIdjual() {
        return idjual;
    }

    public String getIdbarang() {
        return idbarang;
    }

    public double getJumlahjual() {
        return jumlahjual;
    }

    public double getHargajual() {
        return hargajual;
    }

    public String getFakturjual() {
        return fakturjual;
    }

    public double getTotal() {
        return total;
    }

    public double getBayar() {
        return bayar;
    }

    public double getKembali() {
        return kembali;
    }

    public double getPotongan() {
        return potongan;
    }

    public int getIdpelanggan() {
        return idpelanggan;
    }

    public int getIdpegawai() {
        return idpegawai;
    }

    public int getIdkategori() {
        return idkategori;
    }

    public int getIdsatuan() {
        return idsatuan;
    }

    public String getBarang() {
        return barang;
    }

    public double getHarga() {
        return harga;
    }

    public double getHargabeli() {
        return hargabeli;
    }

    public double getStok() {
        return stok;
    }

    public int getFlag_stok() {
        return flag_stok;
    }

    public String getNama_pelanggan() {
        return nama_pelanggan;
    }

    public String getAlamat_pelanggan() {
        return alamat_pelanggan;
    }

    public String getNo_telepon() {
        return no_telepon;
    }

    public String getTanggal_jual() {
        return tanggal_jual;
    }

    public String getNama_satuan() {
        return nama_satuan;
    }

    public String getNama_kategori() {
        return nama_kategori;
    }
}