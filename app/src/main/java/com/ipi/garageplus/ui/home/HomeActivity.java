package com.ipi.garageplus.ui.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ipi.garageplus.R;
import com.ipi.garageplus.model.Vehicle;
import com.ipi.garageplus.ui.auth.LoginActivity;
import com.ipi.garageplus.ui.profile.ProfileActivity;
import com.ipi.garageplus.ui.settings.SettingsActivity;
import com.ipi.garageplus.viewmodel.HomeViewModel;

public class HomeActivity extends AppCompatActivity implements VehicleAdapter.OnVehicleClickListener {

    private static final int REQUEST_NOTIFICATION_PERMISSION = 1001;

    private HomeViewModel viewModel;
    private VehicleAdapter adapter;
    private TextView tvEmptyState, tvWelcome;
    private RecyclerView recyclerView;
    private FloatingActionButton fabAddVehicle;
    private FirebaseAuth mAuth;
    private ActivityResultLauncher<String> notificationPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        notificationPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (!isGranted) {
                        Toast.makeText(
                                this,
                                "Dozvola za notifikacije nije odobrena. Podsjetnici neće biti prikazani.",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );

        requestNotificationPermissionIfNeeded();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        tvWelcome = findViewById(R.id.tvWelcome);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        recyclerView = findViewById(R.id.recyclerView);
        fabAddVehicle = findViewById(R.id.fabAddVehicle);

        String displayName = user.getDisplayName() != null ? user.getDisplayName() : "korisniče";
        tvWelcome.setText("Zdravo, " + displayName + "! 👋");

        adapter = new VehicleAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        viewModel.getVehiclesByUser(user.getUid()).observe(this, vehicles -> {
            if (vehicles == null || vehicles.isEmpty()) {
                recyclerView.setVisibility(android.view.View.GONE);
                tvEmptyState.setVisibility(android.view.View.VISIBLE);
            } else {
                recyclerView.setVisibility(android.view.View.VISIBLE);
                tvEmptyState.setVisibility(android.view.View.GONE);
                adapter.setVehicles(vehicles);
            }
        });

        fabAddVehicle.setOnClickListener(v -> {
            startActivity(new Intent(this, AddVehicleActivity.class));
        });
    }

    private void requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("vehicle_count", adapter.getItemCount());
            startActivity(intent);
            return true;
        } else if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onVehicleClick(Vehicle vehicle) {
        Intent intent = new Intent(this, VehicleDetailActivity.class);
        intent.putExtra(VehicleDetailActivity.EXTRA_VEHICLE_ID, vehicle.getId());
        intent.putExtra(VehicleDetailActivity.EXTRA_VEHICLE_NAME,
                vehicle.getMarka() + " " + vehicle.getModel());
        startActivity(intent);
    }

    @Override
    public void onVehicleLongClick(Vehicle vehicle) {
        new AlertDialog.Builder(this)
                .setTitle("Obriši vozilo")
                .setMessage("Da li ste sigurni da želite obrisati " + vehicle.getMarka() + " " + vehicle.getModel() + "?")
                .setPositiveButton("Obriši", (dialog, which) -> viewModel.deleteVehicle(vehicle))
                .setNegativeButton("Otkaži", null)
                .show();
    }
}