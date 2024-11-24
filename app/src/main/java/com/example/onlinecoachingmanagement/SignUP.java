package com.example.onlinecoachingmanagement;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUP extends AppCompatActivity {

    private TextView signIn;
    private Button signUpButton;
    private EditText name,email,password,secretId;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        this.setTitle("Sign Up");
       signIn=findViewById(R.id.SignInRedirect);
       databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        signUpButton = findViewById(R.id.SignUpButton);
        name=findViewById(R.id.nameEditTextID);
        email=findViewById(R.id.semailEditTextID);
        password=findViewById(R.id.spasswordEditTextID);
        secretId=findViewById(R.id.secretEditTextID);
        mAuth = FirebaseAuth.getInstance();

        signUpButton.setOnClickListener(view -> {
            // Convert EditText to string
            String nameString = name.getText().toString();
            String emailString = email.getText().toString();
            String passwordString = password.getText().toString();
            String secretString = secretId.getText().toString();

            if (!nameString.isEmpty() && !emailString.isEmpty() && !passwordString.isEmpty() && !secretString.isEmpty()) {
                String key = databaseReference.push().getKey();

                if (key != null) {
                    mAuth.createUserWithEmailAndPassword(emailString, passwordString)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    // Save user data in Firebase Realtime Database
                                    databaseReference.child(key).child("name").setValue(nameString);
                                    databaseReference.child(key).child("email").setValue(emailString);
                                    databaseReference.child(key).child("secretId").setValue(secretString);
                                   // FirebaseAuth.getInstance().signOut();
                                    Intent intent = new Intent(SignUP.this, Login.class);
                                    intent.putExtra("signup","signUpSuccess");
                                    startActivity(intent);
                                   // startActivity(new Intent(SignUP.this, MainActivity.class));

                                    Toast.makeText(this, "Account Created Successfully!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(this, "Account creation failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(this, "Database error. Please try again.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUP.this,Login.class);
                startActivity(intent);
            }
        });
    }
}