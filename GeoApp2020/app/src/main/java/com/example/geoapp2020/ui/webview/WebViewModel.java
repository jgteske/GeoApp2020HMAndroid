package com.example.geoapp2020.ui.webview;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WebViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public WebViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is a web fragment!!!");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
