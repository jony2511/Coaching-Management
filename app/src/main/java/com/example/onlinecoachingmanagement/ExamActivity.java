package com.example.onlinecoachingmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class ExamActivity extends AppCompatActivity {

    private TextView timerTextView;

    private Button submitButton;

    private DatabaseReference databaseReference;
    public ArrayList<Question1> questions;
    private String subject;
    private long timeLimit=0; // Time in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        timerTextView = findViewById(R.id.timerTextView);

        submitButton = findViewById(R.id.submitButton);

        subject = getIntent().getStringExtra("subject");
        databaseReference = FirebaseDatabase.getInstance().getReference("Questions").child(subject);

        questions = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                questions.clear();
                for (DataSnapshot questionSnapshot : snapshot.getChildren()) {
                    Question1 question = questionSnapshot.getValue(Question1.class);
                    questions.add(question);
                }
                // Display questions dynamically
                displayQuestions();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ExamActivity.this, "Failed to load questions.", Toast.LENGTH_SHORT).show();
            }
        });

        // Timer
        int questionCount = questions.size();
        timeLimit=1*60000;
        startTimer(timeLimit);

        submitButton.setOnClickListener(view -> submitExam());
    }

    private void startTimer(long duration) {

        new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Used for formatting digits to be in 2 digits only
                NumberFormat f = new DecimalFormat("00");
                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                timerTextView.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
            }

            @Override
            public void onFinish() {
                // When the task is over it will print 00:00:00
                timerTextView.setText("00:00:00");
                submitExam();
            }
        }.start();

    }

    private void displayQuestions() {
        LinearLayout layout = findViewById(R.id.questionContainer);

        for (int i = 0; i < questions.size(); i++) {
            Question1 question = questions.get(i);

            // Question Text
            TextView questionText = new TextView(this);
            questionText.setText((i + 1) + ". " + question.getQuestionText());
            layout.addView(questionText);

            // RadioGroup for options
            RadioGroup optionsGroup = new RadioGroup(this);
            optionsGroup.setOrientation(RadioGroup.VERTICAL);

            RadioButton optionA = new RadioButton(this);
            optionA.setText(question.getOptionA());
            optionsGroup.addView(optionA);

            RadioButton optionB = new RadioButton(this);
            optionB.setText(question.getOptionB());
            optionsGroup.addView(optionB);

            RadioButton optionC = new RadioButton(this);
            optionC.setText(question.getOptionC());
            optionsGroup.addView(optionC);

            RadioButton optionD = new RadioButton(this);
            optionD.setText(question.getOptionD());
            optionsGroup.addView(optionD);

            layout.addView(optionsGroup);

            // Store selected answer for result calculation
            int finalI = i;
            optionsGroup.setOnCheckedChangeListener((group, checkedId) -> {
                RadioButton selectedOption = findViewById(checkedId);
                questions.get(finalI).setSelectedAnswer(selectedOption.getText().toString());
            });
        }
    }


    private void submitExam() {
        Intent intent = new Intent(ExamActivity.this, ResultActivity.class);
        intent.putExtra("questions", questions);
        intent.putExtra("subject", subject);
        startActivity(intent);
    }
}
