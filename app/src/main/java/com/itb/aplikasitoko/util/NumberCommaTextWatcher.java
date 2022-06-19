package com.itb.aplikasitoko.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class NumberCommaTextWatcher implements TextWatcher {
    private EditText editText;

    public NumberCommaTextWatcher(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
//        allow TWO NUMBER BEHIND COMMA

    }
}