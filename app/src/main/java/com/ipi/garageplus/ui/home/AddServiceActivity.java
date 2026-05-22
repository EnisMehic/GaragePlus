package com.ipi.garageplus.ui.home;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.textfield.TextInputEditText;
import com.ipi.garageplus.R;
import com.ipi.garageplus.model.ServiceRecord;
import com.ipi.garageplus.viewmodel.ServiceRecordViewModel;
import java.util.Calendar;

public class AddServiceActivity extends AppCompatActivity {

    public static final String EXTRA_VEHICLE_ID = "vehicle_id";

    private TextInputEditText etOpis, etCijena, etKilometraza, etDatum;
    private AutoCompleteTextView etTipServisa;
    private Button btnSave;
    private ServiceRecordViewModel viewModel;
    private int vehicleId;

    private final String[] tipoviServisa = {
            "Zamjena ulja", "Tehnički pregled", "Registracija",
            "Zamjena guma", "Kočnice", "Akumulator",
            "Zamjena filtera", "Klima servis", "Ostalo"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);

        vehicleId = getIntent().getIntExtra(EXTRA_VEHICLE_ID, -1);
        if (vehicleId == -1) { finish(); return; }

        viewModel = new ViewModelProvider(this).get(ServiceRecordViewModel.class);

        etTipServisa = findViewById(R.id.etTipServisa);
        etOpis = findViewById(R.id.etOpis);
        etCijena = findViewById(R.id.etCijena);
        etKilometraza = findViewById(R.id.etKilometraza);
        etDatum = findViewById(R.id.etDatum);
        btnSave = findViewById(R.id.btnSave);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Dodaj servisni zapis");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, tipoviServisa);
        etTipServisa.setAdapter(adapter);

        etDatum.setOnClickListener(v -> showDatePicker());
        etDatum.setFocusable(false);

        btnSave.setOnClickListener(v -> saveServiceRecord());
    }

    private void showDatePicker() {
        Calendar cal = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, day) -> {
            String datum = String.format("%02d.%02d.%d", day, month + 1, year);
            etDatum.setText(datum);
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void saveServiceRecord() {
        String tip = etTipServisa.getText().toString().trim();
        String opis = etOpis.getText().toString().trim();
        String cijenaStr = etCijena.getText().toString().trim();
        String kmStr = etKilometraza.getText().toString().trim();
        String datum = etDatum.getText().toString().trim();

        if (TextUtils.isEmpty(tip)) { etTipServisa.setError("Odaberite tip servisa"); return; }
        if (TextUtils.isEmpty(cijenaStr)) { etCijena.setError("Unesite cijenu"); return; }
        if (TextUtils.isEmpty(kmStr)) { etKilometraza.setError("Unesite kilometražu"); return; }
        if (TextUtils.isEmpty(datum)) { etDatum.setError("Odaberite datum"); return; }

        ServiceRecord record = new ServiceRecord(
                vehicleId, tip,
                opis.isEmpty() ? "-" : opis,
                Double.parseDouble(cijenaStr),
                datum,
                Integer.parseInt(kmStr)
        );

        viewModel.insertServiceRecord(record);
        Toast.makeText(this, "Servisni zapis dodan!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}