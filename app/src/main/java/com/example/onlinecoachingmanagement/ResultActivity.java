package com.example.onlinecoachingmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    private TextView resultSummaryTextView;
    FirebaseAuth mAuth;
    FirebaseUser user;
    private LinearLayout questionContainer;
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

//    private void calculateResult(ArrayList<Question1> questions) {
//        int correctAnswers = 0;
//        int totalQuestions = 0;
//        int incorrectAnswers = 0;
//
//
//        for (Question1 question : questions) {
//            if (question.getCorrectOption().equals(question.getSelectedAnswer())) {
//                correctAnswers++;
//            }else
//                incorrectAnswers++;
//            totalQuestions++;
//        }
//        double percdntage= (correctAnswers * 100f / totalQuestions);
//        String resultSummary = "Total Questions: " + totalQuestions +
//                "\nCorrect Answers: " + correctAnswers +
//                "\nIncorrect Answers: " + incorrectAnswers +
//                "\nScore: " + percdntage + "%";
//
//        resultSummaryTextView.setText(resultSummary);
//
//        // Save score to Firebase
//        saveScoreToFirebase(percdntage);
//    }
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

//    private void saveScoreToFirebase(double performance) {
//        DatabaseReference leaderboardRef = FirebaseDatabase.getInstance().getReference("Leaderboard");
//        String userId = "student_" + System.currentTimeMillis(); // Replace with actual student ID or username
//        leaderboardRef.child(userId).setValue(new LeaderboardEntry(performance));
//    }
private void saveScoreToFirebase(double performance) {
    String subject = getIntent().getStringExtra("subject"); // Get the subject name
    DatabaseReference leaderboardRef = FirebaseDatabase.getInstance().getReference("Leaderboard").child(subject);
    String safeKey = user.getEmail().replace(".", "_").replace("@", "_");
   // String userId = "student_" + System.currentTimeMillis();
    // Use user's email as a unique key
    leaderboardRef.child(safeKey).setValue(performance);
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
