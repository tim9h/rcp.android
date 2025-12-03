package dev.tim9h.rcpandroid.ui.lighting;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import dev.tim9h.rcpandroid.databinding.FragmentLightingBinding;

public class LightingFragment extends Fragment {

    private FragmentLightingBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        var viewModel = new ViewModelProvider(this).get(LightingViewModel.class);

        binding = FragmentLightingBinding.inflate(inflater, container, false);
        var root = binding.getRoot();

        binding.toggleButton.addOnCheckedChangeListener((_, checked) -> viewModel.toggleLed(checked));

        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            Log.e("RCP", error);
            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        });

        viewModel.initButtonState();
        viewModel.getLogiledStatus().observe(getViewLifecycleOwner(), status -> {
            binding.toggleButton.setChecked(status.enabled());
            binding.colorPickerView.setColor(status.color());
            binding.brightnessSlider.setValue(getBrightnessFromHexColor(status.color()));
        });

        binding.colorPickerView.setOnColorChangedListener(color -> {
            var hex = String.format("#%06X", (0xFFFFFF & color));
            if (binding.toggleButton.isChecked()) {
                viewModel.updateLedColor(hex);
//              binding.toggleButton.setBackgroundColor(color);
            }
        });

        binding.brightnessSlider.addOnChangeListener((_, brightness, _) -> binding.colorPickerView.setBrightness(brightness));

        return root;
    }

    private static float getBrightnessFromHexColor(String hexColor) {
        var color = Color.parseColor(hexColor);
        var hsv = new float[3];
        Color.colorToHSV(color, hsv);
        var df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return Float.parseFloat(df.format(hsv[2]));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}