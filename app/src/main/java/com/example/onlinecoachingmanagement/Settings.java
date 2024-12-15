package com.example.onlinecoachingmanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Settings extends AppCompatActivity {

    private Button adminButton,help_button;
    private Switch switchNightMode;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setTitle("Settings");

        adminButton = findViewById(R.id.adminLogin);
        adminButton.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.this, CheckAdmin.class);
            startActivity(intent);
        });

        help_button = findViewById(R.id.button_help);
        help_button.setOnClickListener(v -> {
            Toast.makeText(this,"Contact for any help",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.this, ContactUsActivity.class);
            startActivity(intent);
        });

    }
}