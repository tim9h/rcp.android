package dev.tim9h.rcpandroid.ui.lighting;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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
        viewModel.getLogiledEnabled().observe(getViewLifecycleOwner(), enabled -> binding.toggleButton.setChecked(enabled));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}