package dev.tim9h.rcpandroid.ui.media;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import dev.tim9h.rcpandroid.databinding.FragmentMediaBinding;
import dev.tim9h.rcpandroid.ui.utils.BindingUtils;

public class MediaFragment extends Fragment {

    private FragmentMediaBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        var homeViewModel =
                new ViewModelProvider(this).get(MediaViewModel.class);

        binding = FragmentMediaBinding.inflate(inflater, container, false);
        var root = binding.getRoot();

        final var textViewTitle = binding.txtTitle;
        BindingUtils.setDynamicTextColor(textViewTitle, com.google.android.material.R.attr.colorPrimary);
        homeViewModel.getTitle().observe(getViewLifecycleOwner(), textViewTitle::setText);

        final var textViewArtist = binding.txtArtist;
        homeViewModel.getArtist().observe(getViewLifecycleOwner(), textViewArtist::setText);

        final var textViewAlbum = binding.txtAlbum;
        homeViewModel.getAlbum().observe(getViewLifecycleOwner(), textViewAlbum::setText);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}