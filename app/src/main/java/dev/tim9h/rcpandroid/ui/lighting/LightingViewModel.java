package dev.tim9h.rcpandroid.ui.lighting;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import dev.tim9h.rcpandroid.backend.service.RcpService;
import dev.tim9h.rcpandroid.model.LogiledStatus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LightingViewModel extends ViewModel {

    private final RcpService rcpService;

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    private final MutableLiveData<String> error = new MutableLiveData<>();

    private final MutableLiveData<LogiledStatus> logiledStatus = new MutableLiveData<>();

    public LightingViewModel() {
        this.rcpService = new RcpService();
    }

    public void toggleLed(boolean on) {
        isLoading.setValue(true);
        if (on) {
            var call = rcpService.logiledOn();
            call.enqueue(createCallback());
        } else {
            var call = rcpService.logiledOff();
            call.enqueue(createCallback());
        }
    }

    public void updateLedColor(String color) {
        isLoading.setValue(true);
        var call = rcpService.logiled(color);
        call.enqueue(createCallback());
    }

    public void initButtonState() {
        isLoading.setValue(true);
        try {
            var call = rcpService.logiledStatus();
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<LogiledStatus> call, Response<LogiledStatus> response) {
                    isLoading.setValue(false);
                    if (response.isSuccessful()) {
                        logiledStatus.postValue(response.body());
                    } else {
                        Log.e("RCP", "API call for status unsuccessful");
                        error.postValue("Error fetching button state");
                    }
                }

                @Override
                public void onFailure(Call<LogiledStatus> call, Throwable t) {
                    isLoading.setValue(false);
                    Log.e("RCP", "Error while calling API", t);
                    error.postValue(t.getMessage());
                }
            });
        } catch (Throwable t) {
            Log.e("RCP", "Error while calling API", t);
        }
    }

    public <T> Callback<T> createCallback() {
        return new Callback<>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                isLoading.setValue(false);
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                isLoading.setValue(false);
                Log.e("RCP", "Error while calling API", t);
                error.postValue(t.getMessage());
            }
        };
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<LogiledStatus> getLogiledStatus() {
        return logiledStatus;
    }

}