package com.example.onlinecoachingmanagement;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Login extends AppCompatActivity {

    private Button loginButton;
    private TextView signUp;
    private FirebaseAuth mAuth;
    private EditText email,password;
    @Override
    public void onStart() {
        super.onStart();
        Bundle intt = getIntent().getExtras();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null && intt==null){
            Intent intent = new Intent(getApplicationContext(), Dashboard.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        this.setTitle("Sign In");
       signUp=findViewById(R.id.SignUpRedirect);
        mAuth = FirebaseAuth.getInstance();
        loginButton=findViewById(R.id.SignInButton);
        email=findViewById(R.id.emailEditTextID);
        password=findViewById(R.id.passwordEditTextID);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailString = email.getText().toString().trim();
                String passwordString = password.getText().toString().trim();

                if (!emailString.isEmpty() && !passwordString.isEmpty()) {
                    mAuth.signInWithEmailAndPassword(emailString, passwordString)
                            .addOnCompleteListener(Login.this, task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Login.this, "Sign In Successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Login.this, Dashboard.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(Login.this, "Sign In Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(Login.this, "Please fill in both fields.", Toast.LENGTH_SHORT).show();
                }
            }
        });

       signUp.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(Login.this,SignUP.class);
               startActivity(intent);
           }
       });
    }
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        new AlertDialog.Builder(Login.this)
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