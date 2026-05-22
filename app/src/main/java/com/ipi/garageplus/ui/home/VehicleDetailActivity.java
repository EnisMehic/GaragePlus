package com.ipi.garageplus.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ipi.garageplus.R;
import com.ipi.garageplus.model.ServiceRecord;
import com.ipi.garageplus.viewmodel.ServiceRecordViewModel;

public class VehicleDetailActivity extends AppCompatActivity
        implements ServiceRecordAdapter.OnServiceClickListener {

    public static final String EXTRA_VEHICLE_ID = "vehicle_id";
    public static final String EXTRA_VEHICLE_NAME = "vehicle_name";

    private ServiceRecordViewModel viewModel;
    private ServiceRecordAdapter adapter;
    private TextView tvVehicleName, tvTotalCost, tvEmptyState;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private FloatingActionButton fabAddService;
    private int vehicleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_detail);

        vehicleId = getIntent().getIntExtra(EXTRA_VEHICLE_ID, -1);
        String vehicleName = getIntent().getStringExtra(EXTRA_VEHICLE_NAME);

        if (vehicleId == -1) {
            finish();
            return;
        }

        tvVehicleName = findViewById(R.id.tvVehicleName);
        tvTotalCost = findViewById(R.id.tvTotalCost);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchView);
        fabAddService = findViewById(R.id.fabAddService);

        tvVehicleName.setText(vehicleName);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(vehicleName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        adapter = new ServiceRecordAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(ServiceRecordViewModel.class);

        viewModel.getServicesByVehicle(vehicleId).observe(this, records -> {
            if (records == null || records.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                tvEmptyState.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                tvEmptyState.setVisibility(View.GONE);
                adapter.setRecords(records);
            }
        });

        viewModel.getTotalCost(vehicleId).observe(this, total -> {
            if (total != null) {
                tvTotalCost.setText(String.format("Ukupni troškovi: %.2f KM", total));
            } else {
                tvTotalCost.setText("Ukupni troškovi: 0.00 KM");
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    viewModel.getServicesByVehicle(vehicleId).observe(
                            VehicleDetailActivity.this, records -> adapter.setRecords(records));
                } else {
                    viewModel.searchServices(vehicleId, newText).observe(
                            VehicleDetailActivity.this, records -> adapter.setRecords(records));
                }
                return true;
            }
        });

        fabAddService.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddServiceActivity.class);
            intent.putExtra(AddServiceActivity.EXTRA_VEHICLE_ID, vehicleId);
            startActivity(intent);
        });
    }

    @Override
    public void onServiceLongClick(ServiceRecord record) {
        new AlertDialog.Builder(this)
                .setTitle("Obriši servisni zapis")
                .setMessage("Da li ste sigurni da želite obrisati ovaj zapis?")
                .setPositiveButton("Obriši", (dialog, which) -> viewModel.deleteServiceRecord(record))
                .setNegativeButton("Otkaži", null)
                .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}