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

    private final MutableLiveData<Integer> success = new MutableLiveData<>();

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

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<Integer> getSuccess() {
        return success;
    }

    public void clearSuccess() {
        success.setValue(null);
    }

    public void clearError() {
        error.setValue(null);
    }

    public void sendNotification(String message) {
        isLoading.setValue(true);
        rcpService.sendNotification(message).enqueue(createCallback(dev.tim9h.rcpandroid.R.string.notification_sent));
    }

    public <T> Callback<T> createCallback(int successMessageResId) {
        return new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
                isLoading.setValue(false);
                if (response.isSuccessful()) {
                    success.setValue(successMessageResId);
                } else {
                    error.setValue("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
                isLoading.setValue(false);
                Log.e("RCP", "Error while calling API", t);
                error.postValue(t.getMessage());
            }
        };
    }

    public void lockWorkstation() {
        rcpService.lockWorkstation().enqueue(createCallback(dev.tim9h.rcpandroid.R.string.workstation_locked));
    }

    public void shutdownNow() {
        rcpService.shutdownNow().enqueue(createCallback(dev.tim9h.rcpandroid.R.string.shutdown_scheduled));
    }

    public void shutdownLater(String when) {
        rcpService.shutdownLater(when).enqueue(createCallback(dev.tim9h.rcpandroid.R.string.shutdown_scheduled));
    }

}