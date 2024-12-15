package com.example.onlinecoachingmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RateAppActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private Button btnSubmitRating;
    private TextView tvAverageRating;

    private DatabaseReference ratingsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_app);
        setTitle("Rate Our App");

        // Initialize Views
        ratingBar = findViewById(R.id.ratingBar);
        btnSubmitRating = findViewById(R.id.btnSubmitRating);
        tvAverageRating = findViewById(R.id.tvAverageRating);

        // Initialize Firebase reference
        ratingsRef = FirebaseDatabase.getInstance().getReference("ratings");

        // Fetch and display average rating
        fetchAverageRating();

        // Handle rating submission
        btnSubmitRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float userRating = ratingBar.getRating();
                if (userRating > 0) {
                    submitRating(userRating);
                } else {
                    Toast.makeText(RateAppActivity.this, "Please select a rating", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void submitRating(float rating) {
        // Save user rating (use unique userId, here we use a random key)
        String ratingId = ratingsRef.push().getKey();
        if (ratingId != null) {
            ratingsRef.child(ratingId).setValue(rating);
            Toast.makeText(this, "Rating submitted", Toast.LENGTH_SHORT).show();
            fetchAverageRating(); // Update average rating after submission
        }
    }

    private void fetchAverageRating() {
        ratingsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                float totalRating = 0;
                long ratingCount = snapshot.getChildrenCount();

                for (DataSnapshot ratingSnapshot : snapshot.getChildren()) {
                    Float rating = ratingSnapshot.getValue(Float.class);
                    if (rating != null) {
                        totalRating += rating;
                    }
                }

                if (ratingCount > 0) {
                    float averageRating = totalRating / ratingCount;
                    tvAverageRating.setText(String.format("Average Rating: %.1f", averageRating));
                } else {
                    tvAverageRating.setText("Average Rating: 0.0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RateAppActivity.this, "Failed to load ratings", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
