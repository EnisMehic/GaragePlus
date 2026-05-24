package com.ipi.garageplus.ui.home;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ipi.garageplus.R;
import com.ipi.garageplus.model.ServiceRecord;
import com.ipi.garageplus.viewmodel.ServiceRecordViewModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ServiceScheduleActivity extends AppCompatActivity {

    public static final String EXTRA_VEHICLE_ID = "vehicle_id";
    public static final String EXTRA_VEHICLE_NAME = "vehicle_name";

    private ServiceRecordViewModel viewModel;
    private ServiceScheduleAdapter adapter;
    private TextView tvVehicleName, tvTotalCost, tvEmptyState;
    private RecyclerView recyclerView;
    private int vehicleId;
    private String vehicleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_schedule);

        vehicleId = getIntent().getIntExtra(EXTRA_VEHICLE_ID, -1);
        vehicleName = getIntent().getStringExtra(EXTRA_VEHICLE_NAME);

        if (vehicleId == -1) {
            finish();
            return;
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvVehicleName = findViewById(R.id.tvVehicleName);
        tvTotalCost = findViewById(R.id.tvTotalCost);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        recyclerView = findViewById(R.id.recyclerView);

        tvVehicleName.setText(vehicleName);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(vehicleName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        adapter = new ServiceScheduleAdapter();
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
                adapter.setGroups(groupAndSum(records));
            }
        });

        viewModel.getTotalCost(vehicleId).observe(this, total -> {
            if (total != null) {
                tvTotalCost.setText(String.format("Ukupni troškovi: %.2f KM", total));
            } else {
                tvTotalCost.setText("Ukupni troškovi: 0.00 KM");
            }
        });
    }

    private List<ServiceGroup> groupAndSum(List<ServiceRecord> records) {
        Map<String, Map<String, Double>> grouped = new LinkedHashMap<>();

        for (ServiceRecord record : records) {
            String tip = record.getTipServisa() == null ? "" : record.getTipServisa().trim();
            String category = tip;
            String subcategory = tip;

            if (tip.contains("/")) {
                String[] parts = tip.split("/");
                category = parts[0].trim();
                subcategory = parts.length > 1 ? parts[1].trim() : parts[0].trim();
            }

            if (!grouped.containsKey(category)) {
                grouped.put(category, new LinkedHashMap<>());
            }

            Map<String, Double> subMap = grouped.get(category);
            double current = subMap.containsKey(subcategory) ? subMap.get(subcategory) : 0.0;
            subMap.put(subcategory, current + record.getCijena());
        }

        List<ServiceGroup> result = new ArrayList<>();
        for (Map.Entry<String, Map<String, Double>> categoryEntry : grouped.entrySet()) {
            String category = categoryEntry.getKey();
            Map<String, Double> subMap = categoryEntry.getValue();

            List<SubcategoryTotal> totals = new ArrayList<>();
            double categoryTotal = 0.0;

            for (Map.Entry<String, Double> subEntry : subMap.entrySet()) {
                totals.add(new SubcategoryTotal(subEntry.getKey(), subEntry.getValue()));
                categoryTotal += subEntry.getValue();
            }

            result.add(new ServiceGroup(category, categoryTotal, totals));
        }

        return result;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}