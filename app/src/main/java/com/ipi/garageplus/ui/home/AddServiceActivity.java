package com.ipi.garageplus.ui.home;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ipi.garageplus.R;
import com.ipi.garageplus.model.ServiceRecord;
import com.ipi.garageplus.util.AlarmScheduler;
import com.ipi.garageplus.viewmodel.ServiceRecordViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddServiceActivity extends AppCompatActivity {

    public static final String EXTRA_VEHICLE_ID = "vehicle_id";
    public static final String EXTRA_VEHICLE_NAME = "vehicle_name";

    private TextInputEditText etOpis, etCijena, etKilometraza, etDatum;
    private AutoCompleteTextView etKategorija, etPodkategorija;
    private TextInputLayout tilPodkategorija;
    private Button btnSave;
    private TextView tvReminderInfo;
    private ServiceRecordViewModel viewModel;
    private int vehicleId;
    private String vehicleName;

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
        vehicleName = getIntent().getStringExtra(EXTRA_VEHICLE_NAME);

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
        tvReminderInfo = findViewById(R.id.tvReminderInfo);

        cardFilteri = findViewById(R.id.cardFilteri);
        cbFilterUlja = findViewById(R.id.cbFilterUlja);
        cbFilterKlime = findViewById(R.id.cbFilterKlime);
        cbFilterGoriva = findViewById(R.id.cbFilterGoriva);
        cbFilterKabine = findViewById(R.id.cbFilterKabine);
        cbFilterZraka = findViewById(R.id.cbFilterZraka);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Dodaj zapis");
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
            updateReminderText();
        });

        etPodkategorija.setOnItemClickListener((parent, view, position, id) -> {
            String odabranaPodkategorija = etPodkategorija.getText().toString().trim();

            if ("Filteri".equals(odabranaPodkategorija)) {
                cardFilteri.setVisibility(View.VISIBLE);
            } else {
                cardFilteri.setVisibility(View.GONE);
                resetFilteri();
            }
            updateReminderText();
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
            String datum = String.format(Locale.getDefault(), "%02d.%02d.%d", day, month + 1, year);
            etDatum.setText(datum);
            updateReminderText();
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    private String calculateReminderDate(String registrationDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            Date date = sdf.parse(registrationDate);
            if (date == null) return null;

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.YEAR, 1);
            cal.add(Calendar.DAY_OF_YEAR, -7);
            return sdf.format(cal.getTime());
        } catch (ParseException e) {
            return null;
        }
    }

    private long getReminderTimeMillis(String registrationDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            Date date = sdf.parse(registrationDate);
            if (date == null) return -1;

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.YEAR, 1);
            cal.add(Calendar.DAY_OF_YEAR, -7);
            cal.set(Calendar.HOUR_OF_DAY, 9);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            return cal.getTimeInMillis();
        } catch (ParseException e) {
            return -1;
        }
    }

    private void updateReminderText() {
        String kategorija = etKategorija.getText() != null ? etKategorija.getText().toString().trim() : "";
        String podkategorija = etPodkategorija.getText() != null ? etPodkategorija.getText().toString().trim() : "";
        String datum = etDatum.getText() != null ? etDatum.getText().toString().trim() : "";

        if ("Troškovi vozila".equals(kategorija) && "Registracija".equals(podkategorija) && !TextUtils.isEmpty(datum)) {
            String reminderDate = calculateReminderDate(datum);
            if (reminderDate != null) {
                tvReminderInfo.setVisibility(View.VISIBLE);
                tvReminderInfo.setText("Podsjetnik za obnovu registracije bit će zakazan za " + reminderDate + ".");
                return;
            }
        }

        tvReminderInfo.setVisibility(View.GONE);
        tvReminderInfo.setText("");
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

        if ("Troškovi vozila".equals(kategorija) && "Registracija".equals(podkategorija)) {
            long reminderTime = getReminderTimeMillis(datum);
            if (reminderTime > 0) {
                AlarmScheduler.scheduleRegistrationReminder(
                        this,
                        reminderTime,
                        vehicleName != null ? vehicleName : "vozila",
                        calculateReminderDate(datum)
                );
            }
        }

        Toast.makeText(this, "Servisni zapis dodan!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}