package com.ipi.garageplus.ui.home;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.ipi.garageplus.R;
import com.ipi.garageplus.model.Vehicle;
import com.ipi.garageplus.viewmodel.HomeViewModel;

public class AddVehicleActivity extends AppCompatActivity {

    private TextInputEditText etMarka, etModel, etGodina, etKilometraza, etRegistracija, etNapomena;
    private Button btnSave;
    private HomeViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        etMarka = findViewById(R.id.etMarka);
        etModel = findViewById(R.id.etModel);
        etGodina = findViewById(R.id.etGodina);
        etKilometraza = findViewById(R.id.etKilometraza);
        etRegistracija = findViewById(R.id.etRegistracija);
        etNapomena = findViewById(R.id.etNapomena);
        btnSave = findViewById(R.id.btnSave);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Dodaj vozilo");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        btnSave.setOnClickListener(v -> saveVehicle());
    }

    private void saveVehicle() {
        String marka = etMarka.getText().toString().trim();
        String model = etModel.getText().toString().trim();
        String godinaStr = etGodina.getText().toString().trim();
        String kmStr = etKilometraza.getText().toString().trim();
        String registracija = etRegistracija.getText().toString().trim();
        String napomena = etNapomena.getText().toString().trim();

        if (TextUtils.isEmpty(marka)) { etMarka.setError("Unesite marku"); return; }
        if (TextUtils.isEmpty(model)) { etModel.setError("Unesite model"); return; }
        if (TextUtils.isEmpty(godinaStr)) { etGodina.setError("Unesite godinu"); return; }
        if (TextUtils.isEmpty(kmStr)) { etKilometraza.setError("Unesite kilometražu"); return; }
        if (TextUtils.isEmpty(registracija)) { etRegistracija.setError("Unesite registraciju"); return; }

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Vehicle vehicle = new Vehicle(
                userId, marka, model,
                Integer.parseInt(godinaStr),
                Integer.parseInt(kmStr),
                registracija, napomena
        );

        viewModel.insertVehicle(vehicle);
        Toast.makeText(this, "Vozilo dodano!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}