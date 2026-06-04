package dev.tim9h.rcpandroid.ui.system;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;
import dev.tim9h.rcpandroid.R;
import dev.tim9h.rcpandroid.databinding.DialogTextfieldBinding;
import dev.tim9h.rcpandroid.databinding.FragmentSystemBinding;

@AndroidEntryPoint
public class SystemFragment extends Fragment {

    private FragmentSystemBinding binding;

    private SystemViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(SystemViewModel.class);

        binding = FragmentSystemBinding.inflate(inflater, container, false);

        binding.btnLockWorkstation.setOnClickListener(v -> lockWorkstation());
        binding.btnShutdownCustom.setOnClickListener(v -> showShutdownDialog());
        binding.btnShutdownNow.setOnClickListener(v -> viewModel.shutdownNow());
        binding.btnSendNotification.setOnClickListener(v -> showSendNotificationDialog());

        viewModel.getSuccess().observe(getViewLifecycleOwner(), resId -> {
            if (resId != null) {
                Toast.makeText(requireContext(), resId, Toast.LENGTH_SHORT).show();
                viewModel.clearSuccess();
            }
        });
        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show();
                viewModel.clearError();
            }
        });

        return binding.getRoot();
    }

    private void lockWorkstation() {
        viewModel.lockWorkstation();
    }

    private void showSendNotificationDialog() {
        var dialogBinding = DialogTextfieldBinding.inflate(getLayoutInflater());
        dialogBinding.tilInput.setHint(R.string.message);
        var dialog = new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.send_notification)
                .setView(dialogBinding.getRoot())
                .setPositiveButton(R.string.send, (d, which) -> {
                    var message = Objects.requireNonNull(dialogBinding.etInput.getText()).toString();
                    if (!message.isEmpty()) {
                        viewModel.sendNotification(message);
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
        Objects.requireNonNull(dialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
        dialogBinding.etInput.requestFocus();
    }

    private void showShutdownDialog() {
        var dialogBinding = DialogTextfieldBinding.inflate(getLayoutInflater());
        dialogBinding.tilInput.setHint(R.string.when);
        var dialog = new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.shutdown_custom)
                .setView(dialogBinding.getRoot())
                .setPositiveButton(R.string.shutdown, (d, which) -> {
                    var when = Objects.requireNonNull(dialogBinding.etInput.getText()).toString();
                    if (!when.isEmpty()) {
                        viewModel.shutdownLater(when);
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
        Objects.requireNonNull(dialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
        dialogBinding.etInput.requestFocus();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}