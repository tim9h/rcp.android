package dev.tim9h.rcpandroid.ui.lighting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import dev.tim9h.rcpandroid.databinding.FragmentLightingBinding;

public class LightingFragment extends Fragment {

    private FragmentLightingBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        var notificationsViewModel =
                new ViewModelProvider(this).get(LightingViewModel.class);

        binding = FragmentLightingBinding.inflate(inflater, container, false);
        var root = binding.getRoot();

        final var textView = binding.textNotifications;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}