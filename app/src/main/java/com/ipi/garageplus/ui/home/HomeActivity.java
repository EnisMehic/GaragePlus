package com.ipi.garageplus.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ipi.garageplus.R;
import com.ipi.garageplus.model.Vehicle;
import com.ipi.garageplus.ui.auth.LoginActivity;
import com.ipi.garageplus.viewmodel.HomeViewModel;

public class HomeActivity extends AppCompatActivity implements VehicleAdapter.OnVehicleClickListener {

    private HomeViewModel viewModel;
    private VehicleAdapter adapter;
    private TextView tvEmptyState, tvWelcome;
    private RecyclerView recyclerView;
    private FloatingActionButton fabAddVehicle;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
                recyclerView.setVisibility(View.GONE);
                tvEmptyState.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                tvEmptyState.setVisibility(View.GONE);
                adapter.setVehicles(vehicles);
            }
        });

        fabAddVehicle.setOnClickListener(v -> {
            startActivity(new Intent(this, AddVehicleActivity.class));
        });

        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
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