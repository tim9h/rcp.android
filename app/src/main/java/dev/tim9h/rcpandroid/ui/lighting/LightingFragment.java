package dev.tim9h.rcpandroid.ui.lighting;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import dagger.hilt.android.AndroidEntryPoint;
import dev.tim9h.rcpandroid.R;
import dev.tim9h.rcpandroid.databinding.FragmentLightingBinding;

@AndroidEntryPoint
public class LightingFragment extends Fragment {

    private FragmentLightingBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        var viewModel = new ViewModelProvider(this).get(LightingViewModel.class);

        binding = FragmentLightingBinding.inflate(inflater, container, false);
        var root = binding.getRoot();

        binding.toggleButton.addOnCheckedChangeListener((btn, checked) -> {
            viewModel.toggleLed(checked);
            if (!checked) {
                binding.toggleButton.setBackgroundColor(Color.TRANSPARENT);
                binding.toggleButton.setIconTintResource(R.color.white);
            } else {
                binding.toggleButton.setBackgroundColor(binding.colorPickerView.getCurrentColor());
                binding.toggleButton.setIconTintResource(isColorDark(binding.colorPickerView.getCurrentColor()) ? R.color.white : R.color.black);
            }
        });
//        binding.toggleButton.setOnClickListener(_ -> animateToggleButton(binding.toggleButton));

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

        binding.brightnessSlider.addOnChangeListener((slider, brightness, b) -> binding.colorPickerView.setBrightness(brightness));

        binding.colorPickerView.setOnColorChangedListener(color -> {
            var hex = String.format("#%06X", (0xFFFFFF & color));
            if (binding.toggleButton.isChecked()) {
                viewModel.updateLedColor(hex);
                binding.toggleButton.setBackgroundColor(color);
                binding.toggleButton.setIconTintResource(isColorDark(color) ? android.R.color.white : android.R.color.black);
            }
        });

        return root;
    }

    private static boolean isColorDark(int color) {
        return ColorUtils.calculateLuminance(color) < 0.3;
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
