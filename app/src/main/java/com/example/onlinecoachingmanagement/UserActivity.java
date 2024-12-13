package com.example.onlinecoachingmanagement;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {

    private ListView subjectListView;
    private ArrayList<String> subjects;
    private DatabaseReference databaseReference;
    private CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        setTitle("Subject List");

        subjectListView = findViewById(R.id.subjectListView);
        subjects = new ArrayList<>();
        adapter = new CustomAdapter(subjects, this);
        subjectListView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Subjects");

        // Listen for changes in the database and update the list
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
                // Handle database error
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

class CustomAdapter extends BaseAdapter {
    private final ArrayList<String> names;
    private final Context context;
    private LayoutInflater inflater;

    CustomAdapter(ArrayList<String> names, Context context) {
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
            convertView = inflater.inflate(R.layout.simple_layout, parent, false);
        }
        TextView textView = convertView.findViewById(R.id.textId);
        textView.setText(names.get(position));

        return convertView;
    }
}
