package com.ipi.garageplus.ui.home;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ipi.garageplus.R;
import com.ipi.garageplus.model.ServiceRecord;
import com.ipi.garageplus.viewmodel.ServiceRecordViewModel;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddServiceActivity extends AppCompatActivity {

    public static final String EXTRA_VEHICLE_ID = "vehicle_id";

    private TextInputEditText etOpis, etCijena, etKilometraza, etDatum;
    private AutoCompleteTextView etKategorija, etPodkategorija;
    private TextInputLayout tilPodkategorija;
    private Button btnSave;
    private ServiceRecordViewModel viewModel;
    private int vehicleId;

    private View cardFilteri;
    private CheckBox cbFilterUlja, cbFilterKlime, cbFilterGoriva, cbFilterKabine, cbFilterZraka;

    private final String[] kategorije = {
            "Servis", "Troškovi vozila", "Popravke", "Estetika"
    };

    private final Map<String, String[]> podkategorije = new HashMap<String, String[]>() {{
        put("Servis", new String[]{"Ulje", "Filteri", "Veliki servis"});
        put("Troškovi vozila", new String[]{"Registracija", "Gorivo", "Parking", "Kazne"});
        put("Popravke", new String[]{"Motor", "Ovjes", "Elektronika"});
        put("Estetika", new String[]{"Kozmetika", "Detailing", "Limarija"});
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);

        vehicleId = getIntent().getIntExtra(EXTRA_VEHICLE_ID, -1);
        if (vehicleId == -1) {
            finish();
            return;
        }

        viewModel = new ViewModelProvider(this).get(ServiceRecordViewModel.class);

        etKategorija = findViewById(R.id.etTipServisa);
        etPodkategorija = findViewById(R.id.etPodkategorija);
        tilPodkategorija = findViewById(R.id.tilPodkategorija);
        etOpis = findViewById(R.id.etOpis);
        etCijena = findViewById(R.id.etCijena);
        etKilometraza = findViewById(R.id.etKilometraza);
        etDatum = findViewById(R.id.etDatum);
        btnSave = findViewById(R.id.btnSave);

        cardFilteri = findViewById(R.id.cardFilteri);
        cbFilterUlja = findViewById(R.id.cbFilterUlja);
        cbFilterKlime = findViewById(R.id.cbFilterKlime);
        cbFilterGoriva = findViewById(R.id.cbFilterGoriva);
        cbFilterKabine = findViewById(R.id.cbFilterKabine);
        cbFilterZraka = findViewById(R.id.cbFilterZraka);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Dodaj servisni zapis");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ArrayAdapter<String> kategorijaAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, kategorije);
        etKategorija.setAdapter(kategorijaAdapter);

        etKategorija.setOnItemClickListener((parent, view, position, id) -> {
            String odabranaKategorija = kategorije[position];
            String[] pod = podkategorije.get(odabranaKategorija);

            etPodkategorija.setText("");
            etPodkategorija.setAdapter(new ArrayAdapter<>(
                    this, android.R.layout.simple_dropdown_item_1line, pod));

            tilPodkategorija.setVisibility(View.VISIBLE);
            cardFilteri.setVisibility(View.GONE);
            resetFilteri();
        });

        etPodkategorija.setOnItemClickListener((parent, view, position, id) -> {
            String odabranaPodkategorija = etPodkategorija.getText().toString().trim();

            if ("Filteri".equals(odabranaPodkategorija)) {
                cardFilteri.setVisibility(View.VISIBLE);
            } else {
                cardFilteri.setVisibility(View.GONE);
                resetFilteri();
            }
        });

        etDatum.setOnClickListener(v -> showDatePicker());
        etDatum.setFocusable(false);

        btnSave.setOnClickListener(v -> saveServiceRecord());
    }

    private void resetFilteri() {
        cbFilterUlja.setChecked(false);
        cbFilterKlime.setChecked(false);
        cbFilterGoriva.setChecked(false);
        cbFilterKabine.setChecked(false);
        cbFilterZraka.setChecked(false);
    }

    private void showDatePicker() {
        Calendar cal = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, day) -> {
            String datum = String.format("%02d.%02d.%d", day, month + 1, year);
            etDatum.setText(datum);
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void saveServiceRecord() {
        String kategorija = etKategorija.getText().toString().trim();
        String podkategorija = etPodkategorija.getText().toString().trim();
        String opis = etOpis.getText().toString().trim();
        String cijenaStr = etCijena.getText().toString().trim();
        String kmStr = etKilometraza.getText().toString().trim();
        String datum = etDatum.getText().toString().trim();

        if (TextUtils.isEmpty(kategorija)) {
            etKategorija.setError("Odaberite kategoriju");
            return;
        }
        if (TextUtils.isEmpty(podkategorija)) {
            etPodkategorija.setError("Odaberite podkategoriju");
            return;
        }
        if (TextUtils.isEmpty(cijenaStr)) {
            etCijena.setError("Unesite cijenu");
            return;
        }
        if (TextUtils.isEmpty(kmStr)) {
            etKilometraza.setError("Unesite kilometražu");
            return;
        }
        if (TextUtils.isEmpty(datum)) {
            etDatum.setError("Odaberite datum");
            return;
        }

        String tipServisa = kategorija + " / " + podkategorija;

        if ("Filteri".equals(podkategorija)) {
            StringBuilder filteri = new StringBuilder();
            if (cbFilterUlja.isChecked()) filteri.append("Filter ulja, ");
            if (cbFilterKlime.isChecked()) filteri.append("Filter klime, ");
            if (cbFilterGoriva.isChecked()) filteri.append("Filter goriva, ");
            if (cbFilterKabine.isChecked()) filteri.append("Filter kabine, ");
            if (cbFilterZraka.isChecked()) filteri.append("Filter zraka, ");

            if (filteri.length() > 0) {
                String filteriStr = filteri.toString().replaceAll(", $", "");
                opis = opis.isEmpty() ? filteriStr : opis + "\n" + filteriStr;
            }
        }

        ServiceRecord record = new ServiceRecord(
                vehicleId,
                tipServisa,
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