package dev.tim9h.rcpandroid.ui.system;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SystemViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public SystemViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is system fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}