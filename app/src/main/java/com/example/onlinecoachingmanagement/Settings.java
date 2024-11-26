package com.example.onlinecoachingmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

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

    private Button adminButton;
   // BottomNavigationView bottomNavigationView;
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

        //bottomNavigationView=findViewById(R.id.bottomNavigationViewsett);
        adminButton = findViewById(R.id.adminLogin);
        adminButton.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.this, CheckAdmin.class);
            startActivity(intent);
        });

//        bottomNavigationView.setBackground(null);
//        bottomNavigationView.setOnItemSelectedListener(item -> {
//
//            if (item.getItemId() == R.id.home) {
//                replaceFragment(new HomeFragment());
//            } else if (item.getItemId() == R.id.calculator) {
//                replaceFragment(new CalculatorFragment());
//            } else if (item.getItemId() == R.id.dictionary) {
//                replaceFragment(new SubsFragment());
//            } else if (item.getItemId() == R.id.library) {
//                replaceFragment(new LibraryFragment());
//            } else {
//                return false;
//            }
//            return true;
//
//        });
    }

//    private void replaceFragment(Fragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.fragment_layout, fragment);
//        fragmentTransaction.commit();
//    }
}