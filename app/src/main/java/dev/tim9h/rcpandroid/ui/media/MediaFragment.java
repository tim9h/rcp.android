package dev.tim9h.rcpandroid.ui.media;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import dev.tim9h.rcpandroid.R;
import dev.tim9h.rcpandroid.databinding.FragmentMediaBinding;
import dev.tim9h.rcpandroid.model.Track;
import dev.tim9h.rcpandroid.preferences.PrefsHelper;
import dev.tim9h.rcpandroid.ui.utils.ColorUtils;

public class MediaFragment extends Fragment {

    private FragmentMediaBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        var viewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                var preferences = new PrefsHelper(requireContext());
                //noinspection unchecked
                return (T) new MediaViewModel(preferences);
            }
        }).get(MediaViewModel.class);

        binding = FragmentMediaBinding.inflate(inflater, container, false);
        var root = binding.getRoot();

        viewModel.getTrack().observe(getViewLifecycleOwner(), this::handleTrackChanged);

        binding.btnTitle.setOnClickListener(_ -> viewModel.openLastFmTrack());
        binding.btnArtist.setOnClickListener(_ -> viewModel.openLastFmArtist());
        binding.btnAlbum.setOnClickListener(_ -> viewModel.openLastFmAlbum());

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

        binding.swiperefreshTrack.setFadingEdgeLength(200);
        binding.swiperefreshTrack.setVerticalFadingEdgeEnabled(true);

        binding.swiperefreshTrack.setColorSchemeColors(ColorUtils.getPrimary(getContext()));
        binding.swiperefreshTrack.setProgressBackgroundColorSchemeColor(ColorUtils.getSurfaceVariant(getContext()));

        viewModel.getOpenBrowserIntent().observe(getViewLifecycleOwner(), intent -> {
            if (intent != null) {
                startActivity(intent);
                viewModel.resetOpenBrowserIntent();
            }
        });

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@org.jspecify.annotations.NonNull Menu menu, @org.jspecify.annotations.NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.media_menu, menu);
                var icon = menu.findItem(R.id.action_lastfm_profile_icon_button).getIcon();
                if (icon != null) {
                    var drawable = DrawableCompat.wrap(icon).mutate();
                    DrawableCompat.setTint(drawable, ColorUtils.getColorOnSurface(requireContext()));
                }

            }

            @Override
            public boolean onMenuItemSelected(@org.jspecify.annotations.NonNull MenuItem menuItem) {
                var id = menuItem.getItemId();
                if (id == R.id.action_lastfm_profile_icon_button) {
                    viewModel.openLastFmProfile();
                    return true;
                }
                return false;
            }
        });

        return root;
    }

    private void handleTrackChanged(Track track) {
        if (track.isPlaying()) {
            binding.btnTitle.setText(track.title());
            binding.btnArtist.setText(track.artist());
            binding.btnAlbum.setText(track.album());

            binding.btnTitle.setEnabled(true);
            binding.btnArtist.setEnabled(true);
            binding.btnAlbum.setEnabled(true);
        } else {
            binding.btnTitle.setText(R.string.not_playing);
            binding.btnArtist.setText("");
            binding.btnAlbum.setText("");

            binding.btnTitle.setEnabled(false);
            binding.btnArtist.setEnabled(false);
            binding.btnAlbum.setEnabled(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}