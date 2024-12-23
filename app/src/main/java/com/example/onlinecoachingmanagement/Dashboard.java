package com.example.onlinecoachingmanagement;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;

public class Dashboard extends AppCompatActivity {

    FloatingActionButton fab;
    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    NavigationView navigationView;
    FirebaseUser user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

      //  textView = findViewById(R.id.welcomeId);
        user = FirebaseAuth.getInstance().getCurrentUser();


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fab = findViewById(R.id.fab);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
        //code added here
        navigationView.bringToFront();
        //comment
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    replaceFragment(new HomeFragment());

                } else if (item.getItemId() == R.id.nav_about) {
                   // Toast.makeText(Dashboard.this, "About is Clicked", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Dashboard.this, AboutUs.class);
                    startActivity(intent);
                } else if (item.getItemId() == R.id.nav_settings) {
                    //  Toast.makeText(Dashboard.this, "Settings is Clicked", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Dashboard.this, Settings.class);
                    startActivity(intent);

                } else if (item.getItemId() == R.id.nav_share) {
                    Toast.makeText(Dashboard.this, "Sharing App", Toast.LENGTH_SHORT).show();
                    shareAppLink();
                } else if (item.getItemId() == R.id.nav_contact) {
                   // Toast.makeText(Dashboard.this, "Contact Clicked", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Dashboard.this, ContactUsActivity.class);
                    startActivity(intent);
                }else if (item.getItemId() == R.id.nav_rate) {
                    //Toast.makeText(Dashboard.this, "Rates Clicked", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Dashboard.this, RateAppActivity.class);
                    startActivity(intent);
                } else {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(Dashboard.this, MainActivity.class));
                    Toast.makeText(Dashboard.this, "Logout  Successful", Toast.LENGTH_SHORT).show();

                }

                drawerLayout.closeDrawers();
                return true;
            }
        });
        replaceFragment(new HomeFragment());
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.calculator) {
                replaceFragment(new CalculatorFragment());
            } else if (item.getItemId() == R.id.profile) {
                replaceFragment(new ProfileFragment());
            } else if (item.getItemId() == R.id.library) {
                replaceFragment(new AnalyticsFragment());
            } else {
                return false;
            }
            return true;

        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialog();
            }
        });
    }

    //outside onCreate
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_layout, fragment);
        fragmentTransaction.commit();
    }

    private void showBottomDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_layout);

        LinearLayout createNote = dialog.findViewById(R.id.btnCreateNote);
        LinearLayout showNote = dialog.findViewById(R.id.btnShowNotes);
        LinearLayout shareNote = dialog.findViewById(R.id.btnShareNote);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        createNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
              //  Toast.makeText(Dashboard.this, "Create a note is clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Dashboard.this, CreateNoteActivity.class);
                startActivity(intent);
            }
        });

        showNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
               // Toast.makeText(Dashboard.this, "Show note is Clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Dashboard.this, ListNotesActivity.class);
                startActivity(intent);
            }
        });
        shareNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Toast.makeText(Dashboard.this, "Please Select a note", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Dashboard.this, ShareNoteActivity.class);
                startActivity(intent);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }
    private void shareAppLink() {
        String link="https://drive.google.com/file/d/1DaFlploEae5wFqQIbRiL6wyECS2lGExJ/view?usp=drive_link";
        // Create an intent for sharing the link
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out this app!");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Download the app from the link below:\n" + link);

        // Start the sharing activity
        startActivity(Intent.createChooser(shareIntent, "Share App Using"));
    }
    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        new AlertDialog.Builder(Dashboard.this)
                .setIcon(R.drawable.exit_icon)
                .setTitle("Exit!")
                .setMessage("Do you want to close app?")
                .setPositiveButton("Yes", (dialog, which) -> moveTaskToBack(true))
                .setNegativeButton("No", (dialog, which) -> {
                    // Dismiss the dialog
                    dialog.dismiss();
                })
                .show();

    }
}