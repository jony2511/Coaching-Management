package com.example.onlinecoachingmanagement;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class SubjectLeaderboardActivity extends AppCompatActivity {

    private ListView leaderboardListView;
    private DatabaseReference subjectRef;
    private ArrayList<String> leaderboardEntries;
    private CustomAdapter1 adapter1;

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
                HashMap<String, Double> leaderboardMap = new HashMap<>();

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String user = userSnapshot.getKey().replace("_", ".");
                    double score = userSnapshot.getValue(Double.class);
                    leaderboardMap.put(user, score);
                }

                // Sort entries by score in descending order
                ArrayList<Map.Entry<String, Double>> sortedEntries = new ArrayList<>(leaderboardMap.entrySet());
                Collections.sort(sortedEntries, new Comparator<Map.Entry<String, Double>>() {
                    @Override
                    public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                        return Double.compare(o2.getValue(), o1.getValue());
                    }
                });

                int pos=1;
                for (Map.Entry<String, Double> entry : sortedEntries) {
                    leaderboardEntries.add(pos+". " + entry.getKey() + " - " + entry.getValue() + "%");
                    pos++;
                }

                // Populate ListView
            //    ArrayAdapter<String> adapter = new ArrayAdapter<>(SubjectLeaderboardActivity.this, android.R.layout.simple_list_item_1, leaderboardEntries);
            //    leaderboardListView.setAdapter(adapter);
              adapter1 = new CustomAdapter1(leaderboardEntries, SubjectLeaderboardActivity.this);
                leaderboardListView.setAdapter(adapter1);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(SubjectLeaderboardActivity.this, "Failed to load leaderboard.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
class CustomAdapter1 extends BaseAdapter {
    private final ArrayList<String> names;
    private final Context context;
    private LayoutInflater inflater;

    CustomAdapter1(ArrayList<String> names, Context context) {
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