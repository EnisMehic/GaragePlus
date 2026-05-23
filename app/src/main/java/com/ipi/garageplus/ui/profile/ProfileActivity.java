package com.ipi.garageplus.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ipi.garageplus.R;
import com.ipi.garageplus.ui.auth.LoginActivity;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvName, tvEmail, tvUid, tvVehicleCount, tvLogoutInfo;
    private Button btnLogout;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        auth = FirebaseAuth.getInstance();

        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvUid = findViewById(R.id.tvUid);
        tvVehicleCount = findViewById(R.id.tvVehicleCount);
        tvLogoutInfo = findViewById(R.id.tvLogoutInfo);
        btnLogout = findViewById(R.id.btnLogout);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Profil");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        String displayName = user.getDisplayName();
        String email = user.getEmail();
        String uid = user.getUid();

        tvName.setText(displayName != null && !displayName.isEmpty() ? displayName : "Korisnik");
        tvEmail.setText(email != null ? email : "-");
        tvUid.setText(uid);
        tvVehicleCount.setText("Broj vozila: " + getIntent().getIntExtra("vehicle_count", 0));
        tvLogoutInfo.setText("Odjavom se briše trenutna sesija na uređaju.");

        btnLogout.setOnClickListener(v -> {
            auth.signOut();
            Toast.makeText(this, "Uspješno ste odjavljeni", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}