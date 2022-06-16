package com.itb.aplikasitoko.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

public class NumberTextWatcher implements TextWatcher {

    private static final String TAG = "NumberTextWatcher";

    private final int numDecimals;
    private String groupingSep;
    private String decimalSep;
    private boolean nonUsFormat;
    private DecimalFormat df;
    private DecimalFormat dfnd;
    private boolean hasFractionalPart;

    private EditText et;
    private String value;


    private String replicate(char ch, int n) {
        return new String(new char[n]).replace("\0", "" + ch);
    }

    public NumberTextWatcher(EditText et, Locale locale, int numDecimals) {

//        et.setKeyListener(DigitsKeyListener.getInstance("0123456789.,"));
        this.numDecimals = numDecimals;
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);

        char gs = symbols.getGroupingSeparator();
        char ds = symbols.getDecimalSeparator();
        groupingSep = String.valueOf(gs);
        decimalSep = String.valueOf(ds);

        String patternInt = "#,###";
        dfnd = new DecimalFormat(patternInt, symbols);

        String patternDec = patternInt + "." + replicate('#', numDecimals);
        df = new DecimalFormat(patternDec, symbols);
        df.setDecimalSeparatorAlwaysShown(true);
        df.setRoundingMode(RoundingMode.DOWN);

        this.et = et;
        hasFractionalPart = false;

        nonUsFormat = !decimalSep.equals(".");
        value = null;

    }


    @Override
    public void afterTextChanged(Editable s) {
        Log.d(TAG, "afterTextChanged");
        et.removeTextChangedListener(this);

        try {
            int inilen, endlen;
            inilen = et.getText().length();

            String v = value.replace(groupingSep, "");

            Number n = df.parse(v);

            int cp = et.getSelectionStart();
            if (hasFractionalPart) {
                int decPos = v.indexOf(decimalSep) + 1;
                int decLen = v.length() - decPos;
                if (decLen > numDecimals) {
                    v = v.substring(0, decPos + numDecimals);
                }
                int trz = countTrailingZeros(v);

                StringBuilder fmt = new StringBuilder(df.format(n));
                while (trz-- > 0) {
                    fmt.append("0");
                }
                et.setText(fmt.toString());
            } else {
                et.setText(dfnd.format(n));
            }


            endlen = et.getText().length();
            int sel = (cp + (endlen - inilen));
            if (sel > 0 && sel <= et.getText().length()) {
                et.setSelection(sel);
            } else {
                // place cursor at the end?
                et.setSelection(et.getText().length() - 1);
            }


        } catch (NumberFormatException | ParseException nfe) {
            // do nothing?
        }


        et.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Log.d(TAG, "beforeTextChanged");
        value = et.getText().toString();
    }

    private int countTrailingZeros(String str) {
        int count = 0;

        for (int i = str.length() - 1; i >= 0; i--) {
            char ch = str.charAt(i);
            if ('0' == ch) {
                count++;
            } else {
                break;
            }
        }
        return count;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.d(TAG, "onTextChanged");

        String newValue = s.toString();
        String change = newValue.substring(start, start + count);
        String prefix = value.substring(0, start);
        String suffix = value.substring(start + before);

        if (".".equals(change) && nonUsFormat) {
            change = decimalSep;
        }

        value = prefix + change + suffix;
        hasFractionalPart = value.contains(decimalSep);

        Log.d(TAG, "VALUE: " + value);


    }
}

