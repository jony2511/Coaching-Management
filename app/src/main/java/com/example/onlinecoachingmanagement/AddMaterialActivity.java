package com.example.onlinecoachingmanagement;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class AddMaterialActivity extends AppCompatActivity {

    private EditText etMaterialTitle, etMaterialContent;
    private Button btnAddMaterial;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_material);

        etMaterialTitle = findViewById(R.id.et_material_title);
        etMaterialContent = findViewById(R.id.et_material_content);
        btnAddMaterial = findViewById(R.id.btn_add_material);

        databaseReference = FirebaseDatabase.getInstance().getReference("materials");

        btnAddMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etMaterialTitle.getText().toString();
                String content = etMaterialContent.getText().toString();
                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                if (!title.isEmpty() && !content.isEmpty()) {
                    HashMap<String, String> material = new HashMap<>();
                    material.put("title", title);
                    material.put("content", content);
                    material.put("date", date);
                    databaseReference.push().setValue(material);
                    Toast.makeText(AddMaterialActivity.this, "Material added", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddMaterialActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
