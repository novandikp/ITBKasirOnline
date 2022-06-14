package com.itb.aplikasitoko.ui.penjualan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PenjualanViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public PenjualanViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is penjualan fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}