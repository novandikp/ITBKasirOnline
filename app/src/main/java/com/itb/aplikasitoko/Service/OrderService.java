package com.itb.aplikasitoko.Service;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.itb.aplikasitoko.Api;
import com.itb.aplikasitoko.Component.LoadingDialog;
import com.itb.aplikasitoko.Database.Repository.DetailJualRepository;
import com.itb.aplikasitoko.Database.Repository.JualRepository;
import com.itb.aplikasitoko.Model.ModelBarang;
import com.itb.aplikasitoko.Model.ModelDetailJual;
import com.itb.aplikasitoko.Model.ModelJual;
import com.itb.aplikasitoko.Model.ModelOrder;
import com.itb.aplikasitoko.Model.ModelPelanggan;
import com.itb.aplikasitoko.Response.OrderResponse;
import com.itb.aplikasitoko.SharedPref.SpHelper;
import com.itb.aplikasitoko.ui.home.bottom_nav.shopping.TransactionSuccess;
import com.itb.aplikasitoko.util.Modul;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderService {
    private ModelJual jual;
    private List<ModelDetailJual> detail;
    private List<ModelBarang> modelBarangs;
    private int idjual;
    private double total;
    private ModelPelanggan modelPelanggan;



    private static OrderService instance;

    public OrderService() {
        jual = new ModelJual("", 0, 0, 0, 0, 0, 24, "2022-05-17");
        detail = new ArrayList<>();
        modelBarangs = new ArrayList<>();
        idjual = 0;
        total = 0;
    }

    public void Bayar(double bayar){
        jual.setTotal(total);
        jual.setBayar(bayar);
    }

    public void save(Application application){
        jual.setTanggal_jual(Modul.getDate("yyyy-MM-dd HH:mm"));
        JualRepository jualRepository = new JualRepository(application);
        DetailJualRepository detailJualRepository = new DetailJualRepository(application);
        SpHelper sp = new SpHelper(application);
        jual.setIdpegawai(Modul.strToInt(sp.getIdPegawai()));
        ModelOrder modelOrder = new ModelOrder(jual,detail);

        LoadingDialog.load(application);
        Call<OrderResponse> orderResponseCall = Api.Order(application.getApplicationContext()).postOrder(modelOrder);
        orderResponseCall.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.isSuccessful()){
                    LoadingDialog.close();
                    jualRepository.insert(response.body().getData().getJual(), new JualRepository.onInsertJual() {
                        @Override
                        public void onComplete(Long modelJual) {
                            detailJualRepository.insertAll(response.body().getData().getDetail(), modelJual);

                            Intent intent = new Intent(application, TransactionSuccess.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("kembali", Modul.doubleToStr(jual.getKembali()));
                            intent.putExtra("total", Modul.doubleToStr(jual.getTotal()));
                            intent.putExtra("idjual", Modul.intToStr(modelJual.intValue()));
                            application.startActivity(intent);
                        }
                    });


                } else {

                }

                clearCart();
                LoadingDialog.close();
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                Toast.makeText(application, t.getMessage(), Toast.LENGTH_SHORT).show();
                LoadingDialog.close();
            }
        });

    }

    //OrderService service = OrderService.getInstance(); //ini buat sekai pengecekan / konstruksi biar gk makan memori dn gk bakal di destroy
    //

    public double getJumlah(){
        double jumlah = 0;
        for(ModelDetailJual detailJual : detail){
//            jumlah += detailJual.getJumlahjual();
            jumlah += Math.round(detailJual.getJumlahjual()*100.0)/100.0;
        }
        return jumlah;
    }
    public static OrderService getInstance(){
        if (instance == null){
            instance = new OrderService();
        }
        return instance;
    }


    public int getIndexBarang(ModelBarang barang){
        int index =0;
        for (ModelBarang produk:
             modelBarangs) {
            if(produk.getIdbarang().equals(barang.getIdbarang())){
                return index;
            }
            index++;
        }
        return -1;
    }

    //buat nambah produk/pembelian
    public void add(ModelBarang modelBarang){
        if (getIndexBarang(modelBarang) == -1){
            //yg dibawah in buat mengetahui item ap sj yg akan dipanggil item
            modelBarangs.add(modelBarang);
            ModelDetailJual detailJual = new ModelDetailJual(idjual, modelBarang.getIdbarang(), 1, modelBarang.getHarga());
            detail.add(detailJual);
            total += modelBarang.getHarga()*1;

        } else {
            ModelDetailJual detailJual = detail.get(getIndexBarang(modelBarang));
            detailJual.addJumlah();
            detail.set(getIndexBarang(modelBarang), detailJual);
            total += detailJual.getHargajual();
            //total = modelBarang.getHarga() + total
            //total = total + modelBarang.getHarga() * jumlah jual
        }

    }

    //kurangi produk
    public void relieve(ModelBarang modelBarang){
        if(getIndexBarang(modelBarang)>=0){
            ModelDetailJual detailJual = detail.get(getIndexBarang(modelBarang));
            detailJual.relieveJumlah();
            detail.set(getIndexBarang(modelBarang), detailJual);
            total -= detailJual.getHargajual();
        }
    }

    public void setPelanggan(ModelPelanggan pelanggan){
        this.modelPelanggan = pelanggan;
        this.jual.setIdpelanggan(pelanggan.getIdpelanggan());
    }

    //ganti nama
    public ModelPelanggan getPelanggan(){
        if(this.modelPelanggan == null){
            ModelPelanggan pelanggan = new ModelPelanggan("Umum","","");
            return pelanggan;
        }
        return this.modelPelanggan;
    }

    //get detail buat cart
    public List<ModelDetailJual> getDetail(){
        return this.detail;
    }

    public void setJumlahBeli(ModelBarang modelBarang,double jumlahLama, double jumlahBeli,double hargaBaru){
        jumlahBeli = Math.round(jumlahBeli*100.0)/100.0;
        int posisi = getIndexBarang(modelBarang);
        if(posisi>=0) {
            total -= modelBarang.getHarga() * jumlahLama;
            if (jumlahBeli == 0) {
                modelBarangs.remove(posisi);
                detail.remove(posisi);
            } else {

                ModelDetailJual detailJual = detail.get(posisi);


                detailJual.setHargajual(hargaBaru);
                detailJual.setJumlahjual(jumlahBeli);
                Log.d("DETAILJUAL",Modul.toString(jumlahBeli));
                detail.set(getIndexBarang(modelBarang), detailJual);
                total += detailJual.getHargajual() * jumlahBeli;
            }
        }
    }

    //get list barang buat ngecek jumlah atau counter
    public List<ModelBarang> getBarang(){
        return this.modelBarangs;

    }

    //buat detai jual
    public ModelDetailJual getDetailJual(ModelBarang modelBarang){
        if (getIndexBarang(modelBarang) == -1) {
            return null;
        }
        return detail.get(getIndexBarang(modelBarang));
    }

    public void clearCart(){
        modelBarangs.clear();
        detail.clear();
        total = 0;
    }

    public double getTotal(){
        return total;
    }
}
