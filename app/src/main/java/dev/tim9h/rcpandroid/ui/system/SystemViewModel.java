package dev.tim9h.rcpandroid.ui.system;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dev.tim9h.rcpandroid.backend.service.RcpService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class SystemViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    private final RcpService rcpService;

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    private final MutableLiveData<String> error = new MutableLiveData<>();

    @Inject
    public SystemViewModel(RcpService rcpService) {
        this.rcpService = rcpService;
        mText = new MutableLiveData<>();
        mText.setValue("This is system fragment");
    }

    public LiveData<Boolean> isLoading() {
        return isLoading;
    }

    public LiveData<String> getText() {
        return mText;
    }

    public <T> Callback<T> createCallback() {
        return new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
                isLoading.setValue(false);
            }

            @Override
            public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
                isLoading.setValue(false);
                Log.e("RCP", "Error while calling API", t);
                error.postValue(t.getMessage());
            }
        };
    }

}