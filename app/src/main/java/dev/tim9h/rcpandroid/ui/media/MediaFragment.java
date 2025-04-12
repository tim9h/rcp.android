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

import dev.tim9h.rcpandroid.R;
import dev.tim9h.rcpandroid.databinding.FragmentMediaBinding;
import dev.tim9h.rcpandroid.ui.utils.BindingUtils;

public class MediaFragment extends Fragment {

    private FragmentMediaBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        var viewModel = new ViewModelProvider(this).get(MediaViewModel.class);

        binding = FragmentMediaBinding.inflate(inflater, container, false);
        var root = binding.getRoot();

        BindingUtils.setDynamicTextColor(binding.txtTitle, com.google.android.material.R.attr.colorPrimary);

        viewModel.getTrack().observe(getViewLifecycleOwner(), track -> {
            if (track.isPlaying()) {
                binding.txtAlbum.setText(track.album());
                binding.txtArtist.setText(track.artist());
                binding.txtTitle.setText(track.title());
            } else {
                binding.txtAlbum.setText("");
                binding.txtArtist.setText("");
                binding.txtTitle.setText(R.string.not_playing);

            }
        });

        binding.btnPlaypause.setOnClickListener(_ -> viewModel.play());
        binding.btnNext.setOnClickListener(_ -> viewModel.next());
        binding.btnPrevious.setOnClickListener(_ -> viewModel.previous());

        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            Log.e("RCP", error);
            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        });

        viewModel.isLoading().observe(getViewLifecycleOwner(), loading -> Log.d("RCP", "Loading: " + loading));

        binding.swiperefreshTrack.setOnRefreshListener(() -> {
            Log.d("RCP", "Refreshing current track");
            viewModel.nowPlaying();
            binding.swiperefreshTrack.setRefreshing(false);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}