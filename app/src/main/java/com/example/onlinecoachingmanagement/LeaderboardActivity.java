package com.example.onlinecoachingmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
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

public class LeaderboardActivity extends AppCompatActivity {

    private ListView subjectListView;
    private DatabaseReference leaderboardRef;
    private ArrayList<String> subjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        subjectListView = findViewById(R.id.subjectListView);
        leaderboardRef = FirebaseDatabase.getInstance().getReference("Leaderboard");
        subjects = new ArrayList<>();

        // Fetch subjects from Firebase
        leaderboardRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                subjects.clear();
                for (DataSnapshot subjectSnapshot : snapshot.getChildren()) {
                    subjects.add(subjectSnapshot.getKey()); // Add subject names
                }

                // Populate ListView
                ArrayAdapter<String> adapter = new ArrayAdapter<>(LeaderboardActivity.this, android.R.layout.simple_list_item_1, subjects);
                subjectListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(LeaderboardActivity.this, "Failed to load subjects.", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle subject click
        subjectListView.setOnItemClickListener((AdapterView<?> parent, android.view.View view, int position, long id) -> {
            String selectedSubject = subjects.get(position);

            Intent intent = new Intent(LeaderboardActivity.this, SubjectLeaderboardActivity.class);
            intent.putExtra("subject", selectedSubject);
            startActivity(intent);
        });
    }
}
