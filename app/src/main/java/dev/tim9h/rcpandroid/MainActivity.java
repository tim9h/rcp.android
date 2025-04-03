package dev.tim9h.rcpandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.color.DynamicColors;

import dev.tim9h.rcpandroid.databinding.ActivityMainBinding;
import dev.tim9h.rcpandroid.ui.settings.SettingsActivity;

public class MainActivity extends AppCompatActivity {

    private NavController navController;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DynamicColors.applyToActivityIfAvailable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        var toolbar = binding.toolbar.findViewById(R.id.toolbar);
        setSupportActionBar((Toolbar) toolbar);

        var appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_media, R.id.navigation_system, R.id.navigation_lighting)
                .build();
        var navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
        if (navHostFragment == null) {
            throw new IllegalStateException("NavHostFragment not found");
        }
        navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        disableMenu(binding, R.id.navigation_system);
        disableMenu(binding, R.id.navigation_lighting);

        // fix AppBar hiding content
        var navHost = findViewById(R.id.nav_host_fragment_activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(navHost, (view, windowInsets) -> {
            var insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            var navViewHeight = binding.navView.getHeight();
            view.setPadding(0, insets.top, 0, insets.bottom + navViewHeight);
            return WindowInsetsCompat.CONSUMED;
        });
    }

    private static void disableMenu(ActivityMainBinding binding, int id) {
        var menuItem = binding.navView.getMenu().findItem(id);
        menuItem.setEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        var id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

}