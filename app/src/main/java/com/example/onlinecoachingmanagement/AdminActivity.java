package com.example.onlinecoachingmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminActivity extends AppCompatActivity {

    private EditText subjectNameField;
    private Button addSubjectButton, addQuestionSetButton,setRoutine,doubtSolve,addmaterials;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        subjectNameField = findViewById(R.id.subjectNameField);
        addSubjectButton = findViewById(R.id.addSubjectButton);
        addQuestionSetButton = findViewById(R.id.addQuestionSetButton);
        setRoutine=findViewById(R.id.routineId);
        doubtSolve=findViewById(R.id.solveDoubt);
        addmaterials=findViewById(R.id.addMaterials);

        databaseReference = FirebaseDatabase.getInstance().getReference("Subjects");

        addSubjectButton.setOnClickListener(view -> {
            String subjectName = subjectNameField.getText().toString();
            if (!subjectName.isEmpty()) {
                databaseReference.child(subjectName).setValue(true);
                Toast.makeText(this, "Subject added!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please enter a subject name.", Toast.LENGTH_SHORT).show();
            }
        });

        addQuestionSetButton.setOnClickListener(view ->
                startActivity(new Intent(AdminActivity.this, AddQuestionActivity.class))
        );
        setRoutine.setOnClickListener(view ->
                startActivity(new Intent(AdminActivity.this, AdminRoutineActivity.class))
        );
        doubtSolve.setOnClickListener(view ->
                startActivity(new Intent(AdminActivity.this, DoubtListActivity.class))
        );
        addmaterials.setOnClickListener(view ->
                startActivity(new Intent(AdminActivity.this, AddMaterialActivity.class))
        );
    }
}
