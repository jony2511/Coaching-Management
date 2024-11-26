package com.example.onlinecoachingmanagement;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AdminRoutineActivity extends AppCompatActivity {

    private EditText dateField, classField, routineField;
    private Button updateRoutineButton;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_routine);

        dateField = findViewById(R.id.dateField);
        classField = findViewById(R.id.classField);
        routineField = findViewById(R.id.routineField);
        updateRoutineButton = findViewById(R.id.updateRoutineButton);

        databaseReference = FirebaseDatabase.getInstance().getReference("Routine");

        updateRoutineButton.setOnClickListener(view -> {
            String date = dateField.getText().toString();
            String className = classField.getText().toString();
            String routine = routineField.getText().toString();

            if (!date.isEmpty() && !className.isEmpty() && !routine.isEmpty()) {
                Map<String, Object> routineData = new HashMap<>();
                routineData.put(className, routine);

                databaseReference.child(date).updateChildren(routineData)
                        .addOnSuccessListener(aVoid -> Toast.makeText(this, "Routine updated!", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(this, "Failed to update: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
