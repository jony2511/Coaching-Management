package com.example.onlinecoachingmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    private TextView resultSummaryTextView;
    FirebaseAuth mAuth;
    FirebaseUser user;
    private LinearLayout questionContainer;
    String nameOfUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        resultSummaryTextView = findViewById(R.id.resultSummaryTextView);
        questionContainer = findViewById(R.id.questionContainer);
        Button leaderboardButton = findViewById(R.id.leaderboardButton);
        leaderboardButton.setOnClickListener(view -> {
            Intent intent = new Intent(ResultActivity.this, LeaderboardActivity.class);
            startActivity(intent);
        });

        ArrayList<Question1> questions = (ArrayList<Question1>) getIntent().getSerializableExtra("questions");
        calculateResult(questions);
    }
    @Override
    public void onBackPressed() {
        // Prevent going back to the previous activity
        Intent intent = new Intent(ResultActivity.this, UserActivity.class);
        startActivity(intent);
    }
private void calculateResult(ArrayList<Question1> questions) {
    int correctAnswers = 0;
    int answeredQuestions = 0;

    for (Question1 question : questions) {
        if (question.getSelectedAnswer() != null && !question.getSelectedAnswer().isEmpty()) {
            answeredQuestions++; // Count answered questions
            if (question.getCorrectOption().equals(question.getSelectedAnswer())) {
                correctAnswers++;
            }
        }
    }

    double percentage = Math.round((correctAnswers * 100.0 / questions.size()) * 100.0) / 100.0; // Round to 2 decimal places
    String resultSummary = "Total Questions: " + questions.size() +
            "\nAnswered Questions: " + answeredQuestions +
            "\nCorrect Answers: " + correctAnswers +
            "\nScore: " + percentage + "%";

    resultSummaryTextView.setText(resultSummary);

    // Save score to Firebase
    saveScoreToFirebase(percentage);

    // Display questions with answers
    displayQuestionsWithAnswers(questions);
}

private void saveScoreToFirebase(double performance) {
    String subject = getIntent().getStringExtra("subject"); // Get the subject name
    DatabaseReference leaderboardRef = FirebaseDatabase.getInstance().getReference("Leaderboard").child(subject);
    String email = user.getEmail();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
    databaseReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    nameOfUser = userSnapshot.child("name").getValue(String.class);
                    //  Toast.makeText(Dashboard.this, "Name: " + nameOfUser, Toast.LENGTH_SHORT).show();
                    leaderboardRef.child(nameOfUser).setValue(performance);
                }
            } else {
                Toast.makeText(ResultActivity.this, "User not found.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Toast.makeText(ResultActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
        }
    });
   // String safeKey = user.getEmail().replace(".", "_").replace("@", "_");
   // String userId = "student_" + System.currentTimeMillis();
    // Use user's email as a unique key

}

    private void displayQuestionsWithAnswers(ArrayList<Question1> questions) {
        for (int i = 0; i < questions.size(); i++) {
            Question1 question = questions.get(i);

            // Display question
            TextView questionTextView = new TextView(this);
            questionTextView.setText((i + 1) + ". " + question.getQuestionText());
            questionContainer.addView(questionTextView);

            // Display selected answer
            TextView selectedAnswerTextView = new TextView(this);
            String selectedAnswer = (question.getSelectedAnswer() == null || question.getSelectedAnswer().isEmpty()) ?
                    "Not Answered" : question.getSelectedAnswer();
            selectedAnswerTextView.setText("Your Answer: " + selectedAnswer);
            questionContainer.addView(selectedAnswerTextView);

            // Display correct answer
            TextView correctAnswerTextView = new TextView(this);
            correctAnswerTextView.setText("Correct Answer: " + question.getCorrectOption());
            questionContainer.addView(correctAnswerTextView);
        }
    }
}
