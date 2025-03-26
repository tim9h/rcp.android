package dev.tim9h.rcpandroid.ui.system;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import dev.tim9h.rcpandroid.databinding.FragmentSystemBinding;

public class SystemFragment extends Fragment {

    private FragmentSystemBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        var dashboardViewModel =
                new ViewModelProvider(this).get(SystemViewModel.class);

        binding = FragmentSystemBinding.inflate(inflater, container, false);
        var root = binding.getRoot();

        final var textView = binding.textDashboard;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}