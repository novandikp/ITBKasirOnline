package com.itb.aplikasitoko.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
//changes
import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.itb.aplikasitoko.Adapter.StrukAdapter;
import com.itb.aplikasitoko.Api;
import com.itb.aplikasitoko.Component.ErrorDialog;
import com.itb.aplikasitoko.Component.LoadingDialog;
import com.itb.aplikasitoko.Database.Repository.DetailJualRepository;
import com.itb.aplikasitoko.Database.Repository.JualRepository;
import com.itb.aplikasitoko.Database.Repository.TokoRepository;
import com.itb.aplikasitoko.HomePage;
import com.itb.aplikasitoko.Model.ModelJual;
import com.itb.aplikasitoko.Model.ModelToko;
import com.itb.aplikasitoko.R;
import com.itb.aplikasitoko.Response.DetailOrderResponse;
import com.itb.aplikasitoko.Response.IdentitasGetResp;
import com.itb.aplikasitoko.SharedPref.SpHelper;
import com.itb.aplikasitoko.ViewModel.ModelViewStruk;
import com.itb.aplikasitoko.databinding.ActivityPrintStrukBinding;
import com.itb.aplikasitoko.ui.laporan.RekapKategori;
import com.itb.aplikasitoko.util.Modul;
import com.google.android.material.textfield.TextInputEditText;
import com.itb.aplikasitoko.util.ModulExcel;
import com.novandikp.simplethermalprinter.AlignColumn;
import com.novandikp.simplethermalprinter.Bluetooth.PrinterBTContext;
import com.novandikp.simplethermalprinter.ColumnPrinter;
import com.novandikp.simplethermalprinter.PrintTextBuilder;
import com.novandikp.simplethermalprinter.Type.Printer58mm;
import com.novandikp.simplethermalprinter.Type.Printer76mm;
import com.novandikp.simplethermalprinter.TypePrinter;
import com.novandikp.simplethermalprinter.USB.PrinterUSBContext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrintStruk extends AppCompatActivity {

    ActivityPrintStrukBinding bind;
    Toolbar toolbar;
    private JualRepository jualRepository;
    private DetailJualRepository detailJualRepository;
    private ModelJual modelJual;
    private StrukAdapter adapter;
    private List<ModelViewStruk> modelDetailJualList = new ArrayList<>();

    final int PERMISSION_BLUETOOTH = 10;
    final int PERMISSION_BLUETOOTH_ADMIN = 11;
    final int PERMISSION_BLUETOOTH_CONNECT = 12;
    final int PERMISSION_BLUETOOTH_SCAN = 13;
    final int PERMISSION_BLUETOOTH_ENABLED = 14;

    PrintTextBuilder resultPrint;
    PrinterUSBContext printerUSBContext;
    PrinterBTContext printerBTContext;
    SpHelper spHelper;

    TokoRepository tokoRepository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LoadingDialog.close();
        bind = ActivityPrintStrukBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(bind.getRoot());
        setTitle("Print Struk");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        tokoRepository = new TokoRepository(getApplication());
        spHelper = new SpHelper(this);
        printerUSBContext = PrinterUSBContext.getInstance(this);
        printerBTContext = PrinterBTContext.getInstance(this);
        requestPermission();
        jualRepository = new JualRepository(getApplication());
        detailJualRepository = new DetailJualRepository(getApplication());

        bind.struk.setVisibility(View.GONE);

        refreshData();

        ModulExcel.askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,0, PrintStruk.this,PrintStruk.this);

        bind.cari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertPermission();

            }
        });


        bind.itemBarangJmlHarga.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StrukAdapter(PrintStruk.this, modelDetailJualList);
        bind.itemBarangJmlHarga.setAdapter(adapter);


        detailJualRepository.getDetailStruk(getIntent().getIntExtra("idjual",0)).observe(this, new Observer<List<ModelViewStruk>>() {
            @Override
            public void onChanged(List<ModelViewStruk> modelViewStruks) {
                if(modelViewStruks.size()>0){
                    setData(modelViewStruks);
                }

            }
        });


        bind.btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bind.spinnerMode.getSelectedItemPosition() == 0) {
                    if(printerBTContext.isConnectedDevice()){
                        printerBTContext.print();
                    }else{
                        Toast.makeText(PrintStruk.this, "Printer belum terhubung", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if(printerUSBContext.isConnectedDevice()){
                        printerUSBContext.print();
                    }else{
                        Toast.makeText(PrintStruk.this, "Printer belum terhubung", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });



    }


    public void alertPermission(){
        startActivity(new Intent(PrintStruk.this, ScanDevice.class));

    }

    public void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, PERMISSION_BLUETOOTH);
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, PERMISSION_BLUETOOTH_ADMIN);
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, PERMISSION_BLUETOOTH_CONNECT);
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, PERMISSION_BLUETOOTH_SCAN);
        } else {
            startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), PERMISSION_BLUETOOTH_ENABLED);
        }
        setModePrinter();
    }


    public void setModePrinter(){
        bind.spinnerMode.setSelection(0);
//        getApplicationContext().registerReceiver(receiverConnected, new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED));
        bind.cari.setVisibility(View.VISIBLE);
        bind.tvPrinter.setHint("Printer");
        bind.tvPrinter.setText("Menyambung...");
        new GetPrinterBTTask(printerBTContext).execute(bind.tvPrinter);
        bind.spinnerMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                LoadingDialog.load(PrintStruk.this);
                if(i == 0){
                    bind.tvPrinter.setText("Menyambung...");
                    new GetPrinterBTTask(printerBTContext).execute(bind.tvPrinter);
                    getApplicationContext().registerReceiver(receiverConnected, new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED));
                    bind.cari.setVisibility(View.VISIBLE);
                }else{
                    bind.tvPrinter.setText("Menyambung...");
                    getApplicationContext().unregisterReceiver(receiverConnected);
                    new GetPrinterUSBTask(printerUSBContext).execute(bind.tvPrinter);
                    bind.cari.setVisibility(View.GONE);

                }
                LoadingDialog.close();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    public void setPrinter(){
        String ukuran=  spHelper.getPrinter();
        switch (ukuran){
            case "76mm":
                PrinterUSBContext.setTextBuilder(resultPrint,new Printer76mm());
                printerBTContext.setTypePrinter(new Printer76mm());
                break;
            case "80mm":
                PrinterUSBContext.setTextBuilder(resultPrint,new Printer80mm());
                printerBTContext.setTypePrinter(new Printer80mm());
                break;
            default:
                PrinterUSBContext.setTextBuilder(resultPrint,new Printer58mm());
                printerBTContext.setTypePrinter(new Printer58mm());
        }
        printerBTContext.setPrinterText(resultPrint);

    }



    public static Bitmap getBitmapFromView(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        int x= (int) view.getLeft();
        int y = (int) view.getTop();
        int h = view.getBottom();
        int w = view.getRight();
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
//
        view.draw(canvas);
        view.layout(x, y, w, h);

        return bitmap;
    }

    private File saveBitMap(Context context, View drawView) {
        File pictureFileDir;
        if(Build.VERSION.SDK_INT >= 29) {
            pictureFileDir = new File(this.getExternalFilesDir("Struk").toString()+"/");
            //only api 21 above
        }else{
            pictureFileDir = new File(Environment.getExternalStorageDirectory().toString() + "/Download/");
            //only api 21 down
        }
        if (!pictureFileDir.exists()) {
            boolean isDirectoryCreated = pictureFileDir.mkdirs();
            if (!isDirectoryCreated)
                Log.i("ATG", "Can't create directory to save the image");
            return null;
        }
        String filename = pictureFileDir.getPath() + File.separator + System.currentTimeMillis() +bind.txtNoOrder.getText().toString()+ ".jpg";
        File pictureFile = new File(filename);
        Bitmap bitmap = getBitmapFromView(drawView);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        bitmap = getBitmapFromView(drawView);
        try {
            pictureFile.createNewFile();
            FileOutputStream oStream = new FileOutputStream(pictureFile);

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream);
            oStream.flush();
            oStream.close();

            Toast.makeText(context, "Gambar tersimpan :\n"+filename, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("TAG", "There was an issue saving the image.");
        }
        scanGallery(context, pictureFile.getAbsolutePath());
        return pictureFile;
    }

    private void scanGallery(Context cntx, String path) {
        try {
            MediaScannerConnection.scanFile(cntx, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        if (bind.struk.getVisibility() == View.VISIBLE) {
            saveBitMap(this, bind.struk);
        }else{
            ErrorDialog.message(this,"Struk masih diproses",bind.getRoot());
        }
    }


    void shareImage() {
        if (bind.struk.getVisibility() == View.VISIBLE) {
            Bitmap bitmap = getBitmapFromView(bind.struk);

            try {
                String filename = System.currentTimeMillis() + ".jpg";
                File file = new File(this.getCacheDir(), filename + ".png");
                FileOutputStream fOut = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                fOut.flush();
                fOut.close();
                Uri contentUri = FileProvider.getUriForFile(this, "com.itb.aplikasitoko.fileprovider", file);
                if (contentUri != null) {
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
                    shareIntent.setDataAndType(contentUri, getContentResolver().getType(contentUri));
                    shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                    startActivity(Intent.createChooser(shareIntent, "Choose an app"));
                }
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }else{
            ErrorDialog.message(this,"Struk masih diproses",bind.getRoot());
        }
    }
    ModelToko modelToko;
    public void refreshData(){
        tokoRepository.getToko().observe(this, new Observer<ModelToko>() {
            @Override
            public void onChanged(ModelToko res) {
                if(res!=null){
                    modelToko = res;

                    bind.txtTitle1.setText(modelToko.getNama_toko());
                    bind.txtTitle2.setText(modelToko.getAlamat_toko() +"\n"+ modelToko.getNomer_toko());
                }
            }
        });
        Call<IdentitasGetResp> identitasGetRespCall = Api.Identitas(PrintStruk.this).getIdentitas();
        identitasGetRespCall.enqueue(new Callback<IdentitasGetResp>() {
            @Override
            public void onResponse(Call<IdentitasGetResp> call, Response<IdentitasGetResp> response) {
                if (response.isSuccessful()){
                    modelToko = response.body().getData();
                    bind.txtTitle1.setText(modelToko.getNama_toko());
                    bind.txtTitle2.setText(modelToko.getAlamat_toko() +"\n"+ modelToko.getNomer_toko());
                    getData();
                }

            }

            @Override
            public void onFailure(Call<IdentitasGetResp> call, Throwable t) {
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(bind.spinnerMode.getSelectedItemPosition() == 0){
            bind.tvPrinter.setText("Menyambung...");
            new GetPrinterBTTask(printerBTContext).execute(bind.tvPrinter);
            bind.cari.setVisibility(View.VISIBLE);
        }else{
            bind.tvPrinter.setText("Menyambung...");
            new GetPrinterUSBTask(printerUSBContext).execute(bind.tvPrinter);
            bind.cari.setVisibility(View.GONE);

        }
    }

    public void getData(){
        Call<DetailOrderResponse> call = Api.Order(this).getOrderDetail(getIntent().getStringExtra("idjual"));
        call.enqueue(new Callback<DetailOrderResponse>() {
            @Override
            public void onResponse(Call<DetailOrderResponse> call, Response<DetailOrderResponse> response) {
                if (response.isSuccessful()) {
                    if(modelDetailJualList.size() == 0){

                        setData(response.body().getData());
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailOrderResponse> call, Throwable t) {

            }
        });
    }
    public  void setData(List<ModelViewStruk> data){
        modelDetailJualList.clear();
        modelDetailJualList.addAll(data);
        if(modelDetailJualList.size() > 0){
            bind.struk.setVisibility(View.VISIBLE);
            ModelViewStruk struk = modelDetailJualList.get(0);
            bind.txtNoOrder.setText("Faktur : "+struk.getFakturjual());
            bind.jmlTotalOrder.setText(Modul.removeE(struk.getTotal()));
            bind.jmlTunai.setText(Modul.removeE(struk.getBayar()));
            bind.jmlKembalian.setText(Modul.removeE(struk.getKembali()));
            //bind.txtDate.setText("Tanggal : "+struk.getTanggal_jual().substring(0,10));
            try {
                bind.txtDate.setText("Tanggal Jual: "+Modul.tanggalku(struk.getTanggal_jual()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            bind.txtPelanggan.setText("Pelanggan : "+struk.getNama_pelanggan());
            if(resultPrint==null || modelToko==null) {
//            atur struk
                resultPrint = new PrintTextBuilder();
                // Alamat bisnis
                if(modelToko!=null){

                    resultPrint.addText(modelToko.getNama_toko(), AlignColumn.CENTER);
                    resultPrint.addText(modelToko.getAlamat_toko(), AlignColumn.CENTER);
                    resultPrint.addText(modelToko.getNomer_toko(), AlignColumn.CENTER);
                }
                resultPrint.addDivider();
//            Identitas pembeli
                try {
                    resultPrint.addTextPair("Tanggal Jual", Modul.tanggalku(struk.getTanggal_jual()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                resultPrint.addTextPair("Pelanggan", struk.getNama_pelanggan());
                resultPrint.addTextPair("Faktur", struk.getFakturjual());

                resultPrint.addDivider();
//            detail
                for (ModelViewStruk detail : modelDetailJualList) {
                    resultPrint.addText(detail.getBarang());
                    ColumnPrinter jumlahColumn = new ColumnPrinter(Modul.toString(detail.getJumlahjual())+"x"+Modul.removeE(detail.getHarga()));
                    ColumnPrinter subColum = new ColumnPrinter(Modul.removeE(detail.getJumlahjual()*detail.getHargajual()), AlignColumn.RIGHT);

                    resultPrint.addColumn(jumlahColumn,subColum);
                }
                resultPrint.addDivider();
                resultPrint.addTextPair("Total", "Rp."+Modul.removeE(struk.getTotal()), AlignColumn.RIGHT);
                resultPrint.addTextPair("Tunai", "Rp."+Modul.removeE(struk.getBayar()), AlignColumn.RIGHT);
                resultPrint.addTextPair("Kembali", "Rp."+Modul.removeE(struk.getKembali()), AlignColumn.RIGHT);
//                resultPrint.addDivider();
//                resultPrint.addText("Halo", AlignColumn.CENTER);
//                resultPrint.addText("Halo", AlignColumn.CENTER);
                setPrinter();
                requestPermission();
            }

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(printerUSBContext!=null){
            printerUSBContext.removeReceiver();
        }

    }


    private BroadcastReceiver receiverConnected = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action))
            {
                bind.tvPrinter.setText(printerBTContext.getDeviceName());
            }

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, HomePage.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }else  if (id == R.id.share) {
            shareImage();
        } else if (id == R.id.unduh) {
             save();
        } else if (id == R.id.print) {

        } return true;
    }


    private class GetPrinterUSBTask extends AsyncTask<TextInputEditText,Void,String>{

        PrinterUSBContext printerBTContext;
        TextInputEditText txtPrinter;
        public GetPrinterUSBTask(PrinterUSBContext printerBTContext) {
            this.printerBTContext = printerBTContext;
        }

        @Override
        protected String doInBackground(TextInputEditText... textInputEditTexts) {
            txtPrinter = textInputEditTexts[0];
            return printerBTContext.getDeviceName();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            txtPrinter.setText(s);
        }
    }
    private class GetPrinterBTTask extends AsyncTask<TextInputEditText,Void,String>{

        PrinterBTContext printerBTContext;
        TextInputEditText txtPrinter;
        public GetPrinterBTTask(PrinterBTContext printerBTContext) {
            this.printerBTContext = printerBTContext;
        }

        @Override
        protected String doInBackground(TextInputEditText... textInputEditTexts) {
            txtPrinter = textInputEditTexts[0];
            return printerBTContext.getDeviceName();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            txtPrinter.setText(s);
        }
    }
}

class Printer80mm implements TypePrinter
{
    public static Printer80mm printer76mm;

    public static Printer80mm device(){
        if(Printer80mm.printer76mm == null){
            Printer80mm.printer76mm = new Printer80mm();
        }
        return  Printer80mm.printer76mm;
    }

    @Override
    public float getPrinterWidth() {
        return 70f;
    }

    @Override
    public int getPrinterDPI() {
        return 231;
    }

    @Override
    public int getMaxCharColumns() {
        return 40;
    }
}