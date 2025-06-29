package dev.tim9h.rcpandroid.ui.media;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

import dev.tim9h.rcpandroid.R;
import dev.tim9h.rcpandroid.databinding.FragmentMediaBinding;
import dev.tim9h.rcpandroid.model.Track;
import dev.tim9h.rcpandroid.model.lastfm.TrackInfoResponse;
import dev.tim9h.rcpandroid.preferences.PrefsHelper;
import dev.tim9h.rcpandroid.ui.utils.ColorUtils;

public class MediaFragment extends Fragment {

    private FragmentMediaBinding binding;

    private MediaViewModel viewModel;

    private Runnable nowPlayingRunnable;

    private final Handler handler = new Handler(Looper.getMainLooper());

    private static final long NP_REFRESH_INTERVAL_MS = 5000;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
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

        viewModel.getTrackInfo().observe(getViewLifecycleOwner(), this::handleTrackChanged);

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

    private void handleTrackChanged(TrackInfoResponse trackInfo) {
        var track = trackInfo.getTrack();
        if (track == null) {
            setDefaultAlbumArt();
            return;
        }
        var album = track.getAlbum();
        if (album == null) {
            setDefaultAlbumArt();
            return;
        }
        var image = album.getImage();
        if (image == null || image.isEmpty()) {
            setDefaultAlbumArt();
            return;
        }
        var url = trackInfo.getTrack().getAlbum().getImage().getLast().getText();
        Glide.with(this).load(url).into(binding.albumArtImageview);
    }

    private void setDefaultAlbumArt() {
        binding.albumArtImageview.setImageResource(R.drawable.defaultalbumcover);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nowPlayingRunnable = () -> {
            if (isAdded() && getView() != null && viewModel != null) {
                Log.d("RCP", "Polling current track");
                viewModel.nowPlaying();
                handler.postDelayed(nowPlayingRunnable, NP_REFRESH_INTERVAL_MS);
            }
        };

        getViewLifecycleOwner().getLifecycle().addObserver((LifecycleEventObserver) (_, event) -> {
            if (event == Lifecycle.Event.ON_START) {
                npPeriodically();
            } else if (event == Lifecycle.Event.ON_STOP) {
                stopNpPeriodically();
            }
        });
    }

    private void npPeriodically() {
        handler.removeCallbacks(nowPlayingRunnable);
        handler.post(nowPlayingRunnable);
        Log.d("RCP", "Started repeated nowPlaying task");
    }

    private void stopNpPeriodically() {
        handler.removeCallbacks(nowPlayingRunnable);
        Log.d("RCP", "Stopped repeated nowPlaying task");
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