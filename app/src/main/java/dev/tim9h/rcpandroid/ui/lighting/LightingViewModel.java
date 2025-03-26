package dev.tim9h.rcpandroid.ui.lighting;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LightingViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public LightingViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is lighting fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}