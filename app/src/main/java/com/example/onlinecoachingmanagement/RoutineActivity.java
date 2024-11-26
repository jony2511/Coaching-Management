package com.example.onlinecoachingmanagement;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RoutineActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private TextView routineTextView;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);

        calendarView = findViewById(R.id.calendarView);
        routineTextView = findViewById(R.id.routineTextView);

        databaseReference = FirebaseDatabase.getInstance().getReference("Routine");

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String date = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
            fetchRoutineForDate(date);
        });
    }

    private void fetchRoutineForDate(String date) {
        databaseReference.child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    StringBuilder routineBuilder = new StringBuilder();
                    for (DataSnapshot classSnapshot : snapshot.getChildren()) {
                        String className = classSnapshot.getKey();
                        String routine = classSnapshot.getValue(String.class);
                        routineBuilder.append(className).append(": ").append(routine).append("\n");
                    }
                    routineTextView.setText(routineBuilder.toString());
                } else {
                    routineTextView.setText("No routine available for this date.");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(RoutineActivity.this, "Failed to fetch routine: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
