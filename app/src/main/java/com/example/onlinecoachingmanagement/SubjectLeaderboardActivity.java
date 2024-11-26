package com.example.onlinecoachingmanagement;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SubjectLeaderboardActivity extends AppCompatActivity {

    private ListView leaderboardListView;
    private DatabaseReference subjectRef;
    private ArrayList<String> leaderboardEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_leaderboard);

        leaderboardListView = findViewById(R.id.leaderboardListView);
        leaderboardEntries = new ArrayList<>();

        String subject = getIntent().getStringExtra("subject");
        subjectRef = FirebaseDatabase.getInstance().getReference("Leaderboard").child(subject);

        // Fetch leaderboard for the selected subject
        subjectRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                leaderboardEntries.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String user = userSnapshot.getKey().replace("_", ".");
                    double score = userSnapshot.getValue(Double.class);
                    leaderboardEntries.add(user + " - " + score + "%");
                }

                // Populate ListView
                ArrayAdapter<String> adapter = new ArrayAdapter<>(SubjectLeaderboardActivity.this, android.R.layout.simple_list_item_1, leaderboardEntries);
                leaderboardListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(SubjectLeaderboardActivity.this, "Failed to load leaderboard.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
