package com.itb.aplikasitoko.ui.home;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itb.aplikasitoko.HomePage;
import com.itb.aplikasitoko.R;
import com.novandikp.simplethermalprinter.Bluetooth.DeviceBT;
import com.novandikp.simplethermalprinter.Bluetooth.PrinterBTContext;
import com.novandikp.simplethermalprinter.Bluetooth.State_Bluetooth;
import com.novandikp.simplethermalprinter.PrintTextBuilder;

import java.util.List;

public class ScanDevice extends AppCompatActivity {
    RecyclerView list;
    PrinterBTContext printerBTContext;
    final int PERMISSION_BLUETOOTH = 10;
    final int PERMISSION_BLUETOOTH_ADMIN = 11;
    final int PERMISSION_BLUETOOTH_CONNECT = 12;
    final int PERMISSION_BLUETOOTH_SCAN = 13;
    final int PERMISSION_BLUETOOTH_ENABLED = 14;
    final int FINE_LOCATION = 15;
    final int BACKGROUND_LOCATION = 16;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_devicebt);
        initView();
        setTitle("Cari Printer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {
          onBackPressed();
        }
        return true;
    }


    public void initView(){
        printerBTContext = PrinterBTContext.getInstance(this,new PrintTextBuilder());
        list = findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        requestPermission();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refreshData();
        if (requestCode == PERMISSION_BLUETOOTH_ENABLED && resultCode == RESULT_OK) {
            refreshData();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == FINE_LOCATION){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                refreshData();
                requestPermission();
            }
        }
    }

    public void requestPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION);
        }else if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, PERMISSION_BLUETOOTH);
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, PERMISSION_BLUETOOTH_ADMIN);
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, PERMISSION_BLUETOOTH_CONNECT);
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, PERMISSION_BLUETOOTH_SCAN);
        } else {
            if (!printerBTContext.isEnabled()) {
                startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), PERMISSION_BLUETOOTH_ENABLED);
                refreshData();
            }else{
                refreshData();
                ProgressDialog dialog= new ProgressDialog(this);
                dialog.setMessage("Menscan Device");

                printerBTContext.discoveryDevice(new PrinterBTContext.OnScanDevice() {
                    @Override
                    public void onScanStart() {
                        dialog.show();
                    }

                    @Override
                    public void onScanCompleted(List<DeviceBT> devices) {
                        dialog.cancel();
                        refreshData();
                    }

                });
            }
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void refreshData(){
        DeviceRecylerView adapter = new DeviceRecylerView(this, printerBTContext.getListBluetoothDevice(),printerBTContext);
        list.setAdapter(adapter);
    }
}


class DeviceRecylerView extends RecyclerView.Adapter<DeviceRecylerView.ViewHolder>{
    Context context;
    List<DeviceBT> deviceBTList;
    PrinterBTContext printerBTContext;


    public DeviceRecylerView(Context context, List<DeviceBT> deviceBTList, PrinterBTContext printerBTContext) {
        this.context = context;
        this.deviceBTList = deviceBTList;
        this.printerBTContext = printerBTContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DeviceBT deviceBT =deviceBTList.get(position);

        holder.tvDevice.setText(deviceBT.getName()+"\n"+deviceBT.getStatus());



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog dialog = new ProgressDialog(context);
                dialog.setMessage("Loading...");
                dialog.setCancelable(false);
                dialog.show();
                printerBTContext.connectDevice(deviceBT,new PrinterBTContext.OnConnect() {
                    @Override
                    public void onConnect(State_Bluetooth state_bt) {

                        deviceBT.setState(state_bt);
                        notifyDataSetChanged();
                        if (state_bt == State_Bluetooth.BOUNDING || state_bt == State_Bluetooth.PAIRED) {
                            if(!dialog.isShowing()){
                                dialog.show();
                            }
                        }else {
                            dialog.cancel();
                        }
                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return deviceBTList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDevice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDevice = itemView.findViewById(R.id.tvDevice);
        }
    }
}