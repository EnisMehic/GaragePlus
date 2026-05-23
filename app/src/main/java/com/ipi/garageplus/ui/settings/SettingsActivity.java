package com.ipi.garageplus.ui.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.ipi.garageplus.R;
import com.ipi.garageplus.ui.auth.LoginActivity;

public class SettingsActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "garageplus_settings";
    private static final String KEY_NOTIFICATIONS = "notifications_enabled";
    private static final String KEY_DARK_MODE = "dark_mode_enabled";

    private SwitchCompat switchNotifications;
    private SwitchCompat switchDarkMode;
    private MaterialCardView cardAbout;
    private SharedPreferences prefs;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        auth = FirebaseAuth.getInstance();
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        switchNotifications = findViewById(R.id.switchNotifications);
        switchDarkMode = findViewById(R.id.switchDarkMode);
        cardAbout = findViewById(R.id.cardAbout);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Settings");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        boolean notificationsEnabled = prefs.getBoolean(KEY_NOTIFICATIONS, true);
        boolean darkModeEnabled = prefs.getBoolean(KEY_DARK_MODE, false);

        switchNotifications.setChecked(notificationsEnabled);
        switchDarkMode.setChecked(darkModeEnabled);

        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean(KEY_NOTIFICATIONS, isChecked).apply();
            Toast.makeText(this,
                    isChecked ? "Notifikacije uključene" : "Notifikacije isključene",
                    Toast.LENGTH_SHORT).show();
        });

        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean(KEY_DARK_MODE, isChecked).apply();
            Toast.makeText(this,
                    isChecked ? "Tamna tema sačuvana" : "Svijetla tema sačuvana",
                    Toast.LENGTH_SHORT).show();
        });

        cardAbout.setOnClickListener(v ->
                Toast.makeText(this, "GaragePlus - digitalna servisna knjiga vozila", Toast.LENGTH_LONG).show()
        );
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}