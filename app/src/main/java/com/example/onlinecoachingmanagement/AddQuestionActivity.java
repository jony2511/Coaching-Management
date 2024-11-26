package com.example.onlinecoachingmanagement;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddQuestionActivity extends AppCompatActivity {

    private EditText subjectField, questionField, optionAField, optionBField, optionCField, optionDField, correctOptionField;
    private Button addQuestionButton;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        subjectField = findViewById(R.id.subjectField);
        questionField = findViewById(R.id.questionField);
        optionAField = findViewById(R.id.optionAField);
        optionBField = findViewById(R.id.optionBField);
        optionCField = findViewById(R.id.optionCField);
        optionDField = findViewById(R.id.optionDField);
        correctOptionField = findViewById(R.id.correctOptionField);
        addQuestionButton = findViewById(R.id.addQuestionButton);

        databaseReference = FirebaseDatabase.getInstance().getReference("Questions");

        addQuestionButton.setOnClickListener(view -> {
            String subject = subjectField.getText().toString();
            String question = questionField.getText().toString();
            String optionA = optionAField.getText().toString();
            String optionB = optionBField.getText().toString();
            String optionC = optionCField.getText().toString();
            String optionD = optionDField.getText().toString();
            String correctOption = correctOptionField.getText().toString();

            if (!subject.isEmpty() && !question.isEmpty() && !correctOption.isEmpty()) {
                DatabaseReference subjectRef = databaseReference.child(subject).push();
                subjectRef.child("questionText").setValue(question);
                subjectRef.child("optionA").setValue(optionA);
                subjectRef.child("optionB").setValue(optionB);
                subjectRef.child("optionC").setValue(optionC);
                subjectRef.child("optionD").setValue(optionD);
                subjectRef.child("correctOption").setValue(correctOption);

                Toast.makeText(this, "Question added!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
