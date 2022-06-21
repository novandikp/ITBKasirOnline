package com.itb.aplikasitoko.ui.home;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.itb.aplikasitoko.Adapter.HomeAdapter;
import com.itb.aplikasitoko.Api;
import com.itb.aplikasitoko.Component.ErrorDialog;
import com.itb.aplikasitoko.Database.Repository.BarangRepository;
import com.itb.aplikasitoko.Model.ModelBarang;
import com.itb.aplikasitoko.Model.ModelDetailJual;
import com.itb.aplikasitoko.R;
import com.itb.aplikasitoko.Response.BarangGetResp;
import com.itb.aplikasitoko.Service.OrderService;
import com.itb.aplikasitoko.databinding.DialogKeteranganOrderBinding;
import com.itb.aplikasitoko.databinding.FragmentHomeBinding;
import com.itb.aplikasitoko.ui.home.bottom_nav.PelangganOrder;
import com.itb.aplikasitoko.ui.home.bottom_nav.shopping.Payment;
import com.itb.aplikasitoko.ui.home.bottom_nav.shopping.ShoppingCart;
import com.itb.aplikasitoko.util.Modul;
import com.itb.aplikasitoko.util.NumberTextWatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    public FragmentHomeBinding binding;
    BarangRepository barangRepository;
    HomeAdapter produkAdapter;
    private List<ModelBarang> data = new ArrayList<>();
    private double jumlah;

    private static OrderService service;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        //ini buat order
        service = OrderService.getInstance();

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //memanggil db/repo
        barangRepository = new BarangRepository(getActivity().getApplication());

        binding.titlePelanggan.setText(service.getPelanggan().getNama_pelanggan());

        //recyclerview
        Display display = getActivity().getWindowManager(). getDefaultDisplay(); Point size = new Point(); display. getSize(size); int width = size. x;
        int span = 3;
        if(width >1200){
            span=6;
        }
        else if (width > 1000) {
            span = 5;
        }else if (width > 800) {
            span = 4;
        }
        binding.item.setLayoutManager(new GridLayoutManager(getActivity(), span)); //buat grid biar 1 row ada 3 item
        produkAdapter = new HomeAdapter(getActivity(), data, service, this);
        binding.item.setAdapter(produkAdapter);


        refreshData(true);
        setTotal();

        binding.searchView.setFocusable(false);
        binding.searchView.setClickable(true);

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                refreshData(false);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()){
                    refreshData(false);
                }
                return false;
            }
        });

        binding.clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(service.getBarang().size() == 0){
                    ErrorDialog.message(getContext(),"Keranjang masih kosong", binding.getRoot());
                }else{
                    clearCart();
                }
            }
        });

        binding.QRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity(ScanScreen.class);
            }
        });

        binding.cvPelanggan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), PelangganOrder.class));

            }
        });

        binding.cvCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ShoppingCart.class));
            }
        });

        binding.constraintLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Payment.class));
            }
        });


//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    private Class<?> mClss;
    private static final int ZBAR_CAMERA_PERMISSION = 1;
    public void launchActivity(Class<?> clss) {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            mClss = clss;
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA}, ZBAR_CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(getContext(), clss);
            startActivityForResult(intent,101);
//            startActivity(intent);
        }
    }

    @Override
    public void onResume() {//ketika buka halaman lain maka function ini berjalan/merefresh data yg sblmnya
        super.onResume();
        binding.titlePelanggan.setText(service.getPelanggan().getNama_pelanggan());
        if (produkAdapter != null){
            produkAdapter.notifyDataSetChanged();
            setTotal();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == getActivity().RESULT_OK){
            scanProduk(data.getStringExtra("idproduk"));
//            new TaskScan(modul,getActivity().getApplication(), this).execute(data.getStringExtra("idproduk"));

        }
    }


    public void scanProduk(String idproduk){
        barangRepository.get(idproduk, new BarangRepository.OnSearch() {
            @Override
            public void findResult(ModelBarang modelBarang) {
                service.add(modelBarang);
                produkAdapter.notifyDataSetChanged();
                setTotal();
            }

            @Override
            public void notFound() {
                ErrorDialog.message(getContext(),"Barang tidak ditemukan", binding.getRoot());
            }
        });

    }

    public void clearCart(){
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Konfirmasi").setMessage("Apakah anda yakin ingin mengosongkan keranjang?").setPositiveButton("Iya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                service.clearCart();
                setTotal();
                produkAdapter.notifyDataSetChanged();
            }
        }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).show();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



    public void refreshData(boolean fetch){
        //krn pake serch bar, hrs pke getquery bukan gettext
        String cari = binding.searchView.getQuery().toString();
        //get sql (buat berjalan offline krn retrofit berjalan dlm online
        barangRepository.getAllBarang(cari).observe(getViewLifecycleOwner(), new Observer<List<ModelBarang>>() {
            @Override
            public void onChanged(List<ModelBarang> modelBarangs) {
                data.clear();
                data.addAll(modelBarangs);
                produkAdapter.notifyDataSetChanged();
            }
        });
        //get retrofit
        if (fetch){
            Call<BarangGetResp> barangGetRespCall = Api.Barang(getActivity()).getBarang(cari);
            barangGetRespCall.enqueue(new Callback<BarangGetResp>() {
                @Override
                public void onResponse(Call<BarangGetResp> call, Response<BarangGetResp> response) {
                    if (response.isSuccessful()){
                        if (data.size() != response.body().getData().size() || !data.equals(response.body().getData())){

                            if (response.body().getData().size() == 0) {
                                binding.txtKosong.setVisibility(View.VISIBLE);
                                binding.item.setVisibility(View.GONE);
                            }
                            //memasukkan ke repo / db
                            barangRepository.insertAll(response.body().getData(), true);

                            //merefresh adapter
                            data.clear();
                            data.addAll(response.body().getData());
                            produkAdapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onFailure(Call<BarangGetResp> call, Throwable t) {
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }



    public void DialogTotal(ModelDetailJual modelDetailJual, ModelBarang modelBarang){

        DialogKeteranganOrderBinding binder = DialogKeteranganOrderBinding.inflate(LayoutInflater.from(getContext()));
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
        alertBuilder.setView(binder.getRoot());
        alertBuilder.setTitle("Jumlah Order");
        alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                double jumlah =Modul.strToDouble(binder.tvJumlah.getText().toString());
                Log.d("DETAILJUAL",Modul.toString(jumlah));
                double hargaBaru = Modul.strToDouble(Modul.unnumberFormat(binder.eHarga.getText().toString()));
                if(binder.tvJumlah.getText().toString().isEmpty()){
                    binder.tvJumlah.setError("Harap isi dengan benar");
                    return;
                }

                service.setJumlahBeli(modelBarang,  modelDetailJual.getJumlahjual(),jumlah,hargaBaru);
                setTotal();
                produkAdapter.notifyDataSetChanged();
            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        binder.eHarga.setText(Modul.toString(modelDetailJual.getHargajual()));
        binder.eHarga.addTextChangedListener(new NumberTextWatcher(binder.eHarga, new Locale("id","ID"),0));
        binder.cbHarga.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    binder.eHarga.setVisibility(View.VISIBLE);
                }else{
                    binder.eHarga.setVisibility(View.GONE);
                }
                binder.eHarga.setText(Modul.toString(modelDetailJual.getHargajual()));
            }
        });

        binder.tvJumlah.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               double jumlah = Modul.strToDouble(binder.tvJumlah.getText().toString());
                if (Math.floor(jumlah) == 0){
                    binder.kurang.setEnabled(false);
                    binder.kurang.setTextColor(getContext().getColor(R.color.darkgrey));
                } else {
                    binder.kurang.setEnabled(true);
                    binder.kurang.setTextColor(getContext().getColor(R.color.default1));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binder.tvJumlah.setText(Modul.toString(modelDetailJual.getJumlahjual()));
        binder.tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double jumlah = Modul.strToDouble(binder.tvJumlah.getText().toString());
                jumlah++;
                binder.tvJumlah.setText(Modul.toString(jumlah));
            }
        });
        binder.kurang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double jumlah = Modul.strToDouble(binder.tvJumlah.getText().toString());
                jumlah--;
                binder.tvJumlah.setText(Modul.toString(jumlah));
                if (Math.floor(jumlah) == 0){
                    binder.kurang.setEnabled(false);
                    binder.kurang.setTextColor(getContext().getColor(R.color.darkgrey));
                } else {
                    binder.kurang.setEnabled(true);
                    binder.kurang.setTextColor(getContext().getColor(R.color.default1));
                }
            }
        });
        AlertDialog dialog = alertBuilder.create();
        dialog.show();
    }

    public void setTotal(){
        if(service.getBarang().size() == 0){
            binding.viewTotal.setBackgroundColor(getContext().getColor(R.color.textdef));
            binding.tvJumlah.setTextColor(getContext().getColor(R.color.white));
            binding.tvTotal.setTextColor(getContext().getColor(R.color.white));
            binding.textviewtotal.setTextColor(getContext().getColor(R.color.white));
            service.clearCart();
            binding.viewTotal.setEnabled(false);
        } else {
            binding.viewTotal.setBackgroundColor(getContext().getColor(R.color.green));
            binding.tvJumlah.setTextColor(getContext().getColor(R.color.white));
            binding.tvTotal.setTextColor(getContext().getColor(R.color.white));
            binding.textviewtotal.setTextColor(getContext().getColor(R.color.white));
        }
        binding.tvJumlah.setText(Modul.toString(service.getJumlah()));
        binding.tvTotal.setText(Modul.removeE(service.getTotal()));
    }
}