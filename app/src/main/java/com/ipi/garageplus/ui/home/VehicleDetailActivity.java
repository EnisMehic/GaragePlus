package com.ipi.garageplus.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ipi.garageplus.R;
import com.ipi.garageplus.model.ServiceRecord;
import com.ipi.garageplus.viewmodel.ServiceRecordViewModel;

import java.util.List;

public class VehicleDetailActivity extends AppCompatActivity implements ServiceRecordAdapter.OnServiceClickListener {

    public static final String EXTRA_VEHICLE_ID = "vehicle_id";
    public static final String EXTRA_VEHICLE_NAME = "vehicle_name";

    private ServiceRecordViewModel viewModel;
    private ServiceRecordAdapter adapter;
    private TextView tvVehicleName, tvTotalCost, tvEmptyState;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private Spinner spinnerSort;
    private FloatingActionButton fabAddService;
    private MaterialCardView topCard;
    private int vehicleId;
    private String vehicleName;

    private String currentQuery = "";
    private String currentSort = "Datum";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_detail);

        vehicleId = getIntent().getIntExtra(EXTRA_VEHICLE_ID, -1);
        vehicleName = getIntent().getStringExtra(EXTRA_VEHICLE_NAME);

        if (vehicleId == -1) {
            finish();
            return;
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        topCard = findViewById(R.id.topCard);
        tvVehicleName = findViewById(R.id.tvVehicleName);
        tvTotalCost = findViewById(R.id.tvTotalCost);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchView);
        spinnerSort = findViewById(R.id.spinnerSort);
        fabAddService = findViewById(R.id.fabAddService);

        tvVehicleName.setText(vehicleName);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(vehicleName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        topCard.setOnClickListener(v -> {
            Intent intent = new Intent(this, ServiceScheduleActivity.class);
            intent.putExtra(ServiceScheduleActivity.EXTRA_VEHICLE_ID, vehicleId);
            intent.putExtra(ServiceScheduleActivity.EXTRA_VEHICLE_NAME, vehicleName);
            startActivity(intent);
        });

        adapter = new ServiceRecordAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(ServiceRecordViewModel.class);

        String[] sortOptions = {"Datum", "Tip", "Cijena"};
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sortOptions);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(sortAdapter);
        spinnerSort.setSelection(0);

        spinnerSort.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                currentSort = sortOptions[position];
                loadServices();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                currentSort = "Datum";
                loadServices();
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
            public boolean onQueryTextSubmit(String query) {
                currentQuery = query == null ? "" : query.trim();
                loadServices();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currentQuery = newText == null ? "" : newText.trim();
                loadServices();
                return true;
            }
        });

        fabAddService.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddServiceActivity.class);
            intent.putExtra(AddServiceActivity.EXTRA_VEHICLE_ID, vehicleId);
            intent.putExtra(AddServiceActivity.EXTRA_VEHICLE_NAME, vehicleName);
            startActivity(intent);
        });

        loadServices();
    }

    private void loadServices() {
        if (currentQuery != null && !currentQuery.isEmpty()) {
            viewModel.searchServices(vehicleId, currentQuery).observe(this, this::updateServices);
            return;
        }

        if ("Tip".equals(currentSort)) {
            viewModel.getServicesByVehicleSortedByType(vehicleId).observe(this, this::updateServices);
        } else if ("Cijena".equals(currentSort)) {
            viewModel.getServicesByVehicleSortedByPrice(vehicleId).observe(this, this::updateServices);
        } else {
            viewModel.getServicesByVehicle(vehicleId).observe(this, this::updateServices);
        }
    }

    private void updateServices(List<ServiceRecord> records) {
        if (records == null || records.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            tvEmptyState.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            tvEmptyState.setVisibility(View.GONE);
            adapter.setRecords(records);
        }
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