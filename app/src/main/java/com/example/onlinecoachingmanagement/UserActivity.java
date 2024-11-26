package com.example.onlinecoachingmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {

    private ListView subjectListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> subjects;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        subjectListView = findViewById(R.id.subjectListView);
        subjects = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, subjects);
        subjectListView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Subjects");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                subjects.clear();
                for (DataSnapshot subjectSnapshot : snapshot.getChildren()) {
                    String subjectName = subjectSnapshot.getKey();
                    subjects.add(subjectName);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle error
            }
        });

            subjectListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedSubject = subjects.get(position);
            Intent intent = new Intent(UserActivity.this, ExamActivity.class);
            intent.putExtra("subject", selectedSubject);
            startActivity(intent);
        });
    }
}
