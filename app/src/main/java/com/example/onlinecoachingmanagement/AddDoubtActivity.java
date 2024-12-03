package com.example.onlinecoachingmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddDoubtActivity extends AppCompatActivity {

    private EditText editTextQuestion;
    private Button buttonSubmit;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doubt);

        editTextQuestion = findViewById(R.id.editTextQuestion);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        databaseRef = FirebaseDatabase.getInstance().getReference("doubts");

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDoubt();
            }
        });

    }

    private void addDoubt() {
        String question = editTextQuestion.getText().toString().trim();

        if (TextUtils.isEmpty(question)) {
            Toast.makeText(this, "Please enter a doubt", Toast.LENGTH_SHORT).show();
            return;
        }

        String id = databaseRef.push().getKey();
        Doubt doubt = new Doubt(id, question, "Pending answer", "");

        databaseRef.child(id).setValue(doubt).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(AddDoubtActivity.this, "Doubt submitted successfully", Toast.LENGTH_SHORT).show();
                editTextQuestion.setText("");
            } else {
                Toast.makeText(AddDoubtActivity.this, "Failed to add doubt", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
 class Doubt {
    private String id;
    private String question;
    private String status;
    private String answer;

    public Doubt() {
        // Default constructor required for Firebase
    }

    public Doubt(String id, String question, String status, String answer) {
        this.id = id;
        this.question = question;
        this.status = status;
        this.answer = answer;
    }

    public String getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getStatus() {
        return status;
    }

    public String getAnswer() {
        return answer;
    }
}
