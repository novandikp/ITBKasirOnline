package com.itb.aplikasitoko.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.opencsv.CSVWriter;

import jxl.CellView;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ModulExcel {
    private static WritableCellFormat times;
    private static WritableCellFormat timesBold;
    private static WritableCellFormat timesBoldUnderline;
    public static int row=0;
    public static Boolean csvNextLine(CSVWriter csvWriter){
        try {
            String[] header = {
                    "",
                    "",
                    "",
            };
            csvWriter.writeNext(header);
            return true ;
        }catch (Exception e){
            return false ;
        }
    }

    public static Boolean csvNextLine(CSVWriter csvWriter, int total){
        try {
            String[] header = {
                    "",
                    "",
                    "",
            };
            for (int i = 0 ; i < total ; i++){
                csvWriter.writeNext(header);
            }
            return true ;
        }catch (Exception e){
            return false ;
        }
    }

    public static Boolean excelNextLine(WritableSheet sheet, int total){
        try {
            for (int i = 0 ; i < total ; i++){
                addLabel(sheet,0,row++,"");
            }
            return true ;
        }catch (Exception e){
            return false ;
        }
    }


    public static void addLabel(WritableSheet sheet, int column, int row, String s)
            throws WriteException, RowsExceededException {
        Label label;
        label = new Label(column, row, s, times);
        if(!sheet.getColumnView(column).isAutosize()){
            CellView cell = sheet.getColumnView(column);
            cell.setAutosize(true);
            sheet.setColumnView(2, cell);
        }

        sheet.addCell(label);
    }

    public static void setJudul(WritableSheet sheet, String[] val) throws WriteException {
        int col = 0 ;
        for (int i = 0 ; i < val.length ; i++){
            addCaption(sheet,col++,row,val[i]);
        }
        row++ ;
    }



    public static void askForPermission(String permission, Integer requestCode, Context context, Activity activity) {

        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
            }
        }
    }

    private static void addCaption(WritableSheet sheet, int column, int row, String s)
            throws RowsExceededException, WriteException {
        Label label;
        label = new Label(column, row, s, timesBold);
        sheet.addCell(label);
    }

    public static void createLabel(WritableSheet sheet)
            throws WriteException {
        row = 0;
        // Lets create a times font
        WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
        // Define the cell format
        times = new WritableCellFormat(times10pt);
        // Lets automaticall                y wrap the cells
        times.setWrap(true);

        // create create a bold font with unterlines
        WritableFont times10ptBoldUnderline = new WritableFont(
                WritableFont.TIMES, 10, WritableFont.BOLD, false,
                UnderlineStyle.SINGLE);
        timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
        // Lets automatically wrap the cells
        timesBoldUnderline.setWrap(true);

        WritableFont times10ptBold = new WritableFont(
                WritableFont.TIMES, 12, WritableFont.BOLD, false);
        timesBold = new WritableCellFormat(times10ptBold);
        // Lets automatically wrap the cells
        timesBold.setWrap(true);

        CellView cv = new CellView();
        cv.setFormat(timesBold);

//        cv.setAutosize(true);
    }

    public static Boolean setCenter(CSVWriter csvWriter, int JumlahKolom, String value){
        try {
            String[] item = {} ;
            int baru = JumlahKolom/2 - 1 ;
            int i ;
            for (i = 0 ; i < baru ; i++){
                item[i] = "" ;
            }
            item[i] = value ;
            csvWriter.writeNext(item);
            return true ;
        }catch (Exception e){
            return false ;
        }
    }
}

