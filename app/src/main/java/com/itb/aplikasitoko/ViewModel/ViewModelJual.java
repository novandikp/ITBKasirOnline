package com.itb.aplikasitoko.ViewModel;

import androidx.room.DatabaseView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@DatabaseView(value =
       "SELECT tbljual.idjual,\n" +
                "    tbljual.fakturjual,\n" +
                "    tbljual.bayar,\n" +
                "    tbljual.total,\n" +
                "    tbljual.kembali,\n" +
                "    tbljual.potongan,\n" +
                "    tblpelanggan.idpelanggan,\n" +
                "    tbljual.tanggal_jual,\n" +
                "    tblpelanggan.nama_pelanggan,\n" +
                "    tblpelanggan.alamat_pelanggan,\n" +
                "    tblpelanggan.no_telepon\n" +
                "    \n" +
                "   FROM (tbljual\n" +
                "     JOIN tblpelanggan ON (tbljual.idpelanggan = tblpelanggan.idpelanggan)\n" +
                "     );\n" +
                "     ",viewName = "viewJual"
)
public class ViewModelJual {
    private String fakturjual;
    private double bayar;
    private double total;
    private double kembali;
    private double potongan;
    private String nama_pelanggan;
    private String nama_pegawai;
    private String tanggal_jual;

    public ViewModelJual(String fakturjual, double bayar, double total, double kembali, double potongan, String nama_pelanggan, String nama_pegawai, String tanggal_jual) {
        this.fakturjual = fakturjual;
        this.bayar = bayar;
        this.total = total;
        this.kembali = kembali;
        this.potongan = potongan;
        this.nama_pelanggan = nama_pelanggan;
        this.nama_pegawai = nama_pegawai;
        this.tanggal_jual = tanggal_jual;
    }

    public String getFakturjual() {
        return fakturjual;
    }

    public void setFakturjual(String fakturjual) {
        this.fakturjual = fakturjual;
    }

    public double getBayar() {
        return bayar;
    }

    public void setBayar(double bayar) {
        this.bayar = bayar;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getKembali() {
        return kembali;
    }

    public void setKembali(double kembali) {
        this.kembali = kembali;
    }

    public double getPotongan() {
        return potongan;
    }

    public void setPotongan(double potongan) {
        this.potongan = potongan;
    }

    public String getNama_pelanggan() {
        return nama_pelanggan;
    }

    public void setNama_pelanggan(String nama_pelanggan) {
        this.nama_pelanggan = nama_pelanggan;
    }

    public String getNama_pegawai() {
        return nama_pegawai;
    }

    public void setNama_pegawai(String nama_pegawai) {
        this.nama_pegawai = nama_pegawai;
    }

    public String getTanggal_jual() {
        return tanggal_jual;
    }

    public String tanggalku() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date tanggal = format.parse(getTanggal_jual());
        SimpleDateFormat formatBaru = new SimpleDateFormat("yyyy-MM-dd", new Locale("id", "ID"));
        return formatBaru.format(tanggal);
        //return tanggal;
    }


    public void setTanggal_jual(String tanggal_jual) {
        this.tanggal_jual = tanggal_jual;
    }
}