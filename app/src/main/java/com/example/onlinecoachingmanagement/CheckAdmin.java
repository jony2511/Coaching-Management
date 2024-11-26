package com.example.onlinecoachingmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CheckAdmin extends AppCompatActivity {

    private Button checkAdminButton;
    private EditText adminPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_check_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        adminPassword=findViewById(R.id.AdminpasswordEditTextID);
        checkAdminButton=findViewById(R.id.adminSignInButton);

       checkAdminButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String password=adminPassword.getText().toString();
               if(password.equals("admin"))
               {
                   Toast.makeText(CheckAdmin.this, "Successfully Logged In as Admin", Toast.LENGTH_SHORT).show();
                   Intent intent = new Intent(CheckAdmin.this, AdminActivity.class);
                   startActivity(intent);
               }else
                   Toast.makeText(CheckAdmin.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
           }
       });
    }
}