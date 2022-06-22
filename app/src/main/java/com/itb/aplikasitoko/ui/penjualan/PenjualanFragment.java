package com.itb.aplikasitoko.ui.penjualan;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.itb.aplikasitoko.Adapter.PrintPenjulanAdapter;
import com.itb.aplikasitoko.Api;
import com.itb.aplikasitoko.Component.LoadingDialog;
import com.itb.aplikasitoko.Database.Repository.JualRepository;
import com.itb.aplikasitoko.R;
import com.itb.aplikasitoko.Response.PendapatanGetResp;
import com.itb.aplikasitoko.ViewModel.ViewModelJual;
import com.itb.aplikasitoko.databinding.FragmentPenjualanBinding;
import com.itb.aplikasitoko.util.Modul;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PenjualanFragment extends Fragment {

    private FragmentPenjualanBinding bind;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private PrintPenjulanAdapter adapter;
    private List<ViewModelJual> data = new ArrayList<>();
    JualRepository jualRepository;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PenjualanViewModel galleryViewModel =
                new ViewModelProvider(this).get(PenjualanViewModel.class);
        jualRepository = new JualRepository(getActivity().getApplication());
        bind = FragmentPenjualanBinding.inflate(inflater, container, false);
        View root = bind.getRoot();

        init();

        //inisiasi recyclerview
        bind.itemPenjualan.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PrintPenjulanAdapter(getContext(), data);
        bind.itemPenjualan.setAdapter(adapter);

        refreshData(true);

        bind.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                refreshData(false);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()){
                    refreshData(false);
                }
                return false;
            }
        });

//        final TextView textView = binding.textPenjualan;
//        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    public void init(){
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        bind.dateFrom.setFocusable(false);
        bind.dateFrom.setClickable(true);
        bind.dateTo.setFocusable(false);
        bind.dateTo.setClickable(true);

        bind.searchView.setFocusable(false);
        bind.searchView.setClickable(true);

        //mengset ke tgl hari ini
        bind.dateFrom.setText(Modul.getDate("yyyy-MM-dd"));
        bind.dateTo.setText(Modul.getDate("yyyy-MM-dd"));

        bind.dateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateFrom();
            }
        });

        bind.dateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTo();
            }
        });

    }

    public void refreshData(boolean fetch){
        String cari = bind.searchView.getQuery().toString();
        String mulai = bind.dateFrom.getText().toString();
        String sampai = bind.dateTo.getText().toString();

        jualRepository.getPendapatan(cari,mulai,sampai).observe(getActivity(), new Observer<List<ViewModelJual>>() {
            @Override
            public void onChanged(List<ViewModelJual> viewModelJuals) {
                if(viewModelJuals.size()>0){
                    LoadingDialog.close();
                    data.clear();
                    data.addAll(viewModelJuals);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        if (true){
            Call<PendapatanGetResp> pendapatanGetRespCall = Api.Pendapatan(getContext()).getPendapatan(mulai, sampai, cari);
            pendapatanGetRespCall.enqueue(new Callback<PendapatanGetResp>() {
                @Override
                public void onResponse(Call<PendapatanGetResp> call, Response<PendapatanGetResp> response) {
                    data.clear();
                    if (response.isSuccessful()){

                        data.addAll(response.body().getData());

                    } else {
                        bind.itemPenjualan.setVisibility(View.GONE);
                        bind.txtKosong.setVisibility(View.VISIBLE);
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<PendapatanGetResp> call, Throwable t) {
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void showDateFrom(){
        Calendar kalender = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);

                bind.dateFrom.setText(dateFormatter.format(newDate.getTime()));
                refreshData(true);
            }
        }, kalender.get(Calendar.YEAR), kalender.get(Calendar.MONTH), kalender.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    public void showDateTo(){
        Calendar kalender = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);

                bind.dateTo.setText(dateFormatter.format(newDate.getTime()));
                refreshData(true);
            }
        }, kalender.get(Calendar.YEAR), kalender.get(Calendar.MONTH), kalender.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();

    }

//    public void PrintStruk(String idjual){
//        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
//        alert.setTitle("Konfirmasi");
//        alert.setMessage("Apakah anda yakin untuk menghapus data ini ?");
//        alert.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                LoadingDialog.load(getContext());
//
//                startActivity(new Intent(getContext(), PrintStruk.class));
//
//            }
//        }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//        alert.show();
//    }
}