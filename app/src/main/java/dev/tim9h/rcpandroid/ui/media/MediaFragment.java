package dev.tim9h.rcpandroid.ui.media;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import dev.tim9h.rcpandroid.databinding.FragmentMediaBinding;
import dev.tim9h.rcpandroid.ui.utils.BindingUtils;

public class MediaFragment extends Fragment {

    private FragmentMediaBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        var viewModel =
                new ViewModelProvider(this).get(MediaViewModel.class);

        binding = FragmentMediaBinding.inflate(inflater, container, false);
        var root = binding.getRoot();

        final var textViewTitle = binding.txtTitle;
        BindingUtils.setDynamicTextColor(textViewTitle, com.google.android.material.R.attr.colorPrimary);
        viewModel.getTitle().observe(getViewLifecycleOwner(), textViewTitle::setText);

        final var textViewArtist = binding.txtArtist;
        viewModel.getArtist().observe(getViewLifecycleOwner(), textViewArtist::setText);

        final var textViewAlbum = binding.txtAlbum;
        viewModel.getAlbum().observe(getViewLifecycleOwner(), textViewAlbum::setText);

        binding.btnPlaypause.setOnClickListener(_ -> viewModel.play());
        binding.btnNext.setOnClickListener(_ -> viewModel.next());
        binding.btnPrevious.setOnClickListener(_ -> viewModel.previous());

        viewModel.getData().observe(getViewLifecycleOwner(), data -> {
            // TODO: handle data
        });
        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            Log.e("RCP", error);
            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        });

        viewModel.isLoading().observe(getViewLifecycleOwner(), loading -> {
            // TODO: add loading spinner
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}