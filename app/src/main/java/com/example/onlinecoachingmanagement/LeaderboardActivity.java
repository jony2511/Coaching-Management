package com.example.onlinecoachingmanagement;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
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
    private CustomAdapter2 adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        setTitle("Subject List");

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
              //  ArrayAdapter<String> adapter = new ArrayAdapter<>(LeaderboardActivity.this, android.R.layout.simple_list_item_1, subjects);
                //subjectListView.setAdapter(adapter);
                adapter2 = new CustomAdapter2(subjects, LeaderboardActivity.this);
                subjectListView.setAdapter(adapter2);
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
class CustomAdapter2 extends BaseAdapter {
private final ArrayList<String> names;
private final Context context;
private LayoutInflater inflater;

CustomAdapter2(ArrayList<String> names, Context context) {
    this.names = names;
    this.context = context;
}

@Override
public int getCount() {
    return names.size();
}

@Override
public Object getItem(int position) {
    return names.get(position);
}

@Override
public long getItemId(int position) {
    return position;
}

@Override
public View getView(int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.simple_layout1, parent, false);
    }
    TextView textView = convertView.findViewById(R.id.textId);
    textView.setText(names.get(position));

    return convertView;
}
}