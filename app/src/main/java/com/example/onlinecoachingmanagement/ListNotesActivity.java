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
import java.util.List;

public class ListNotesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_notes);

        ListView lvNotes = findViewById(R.id.lvNotes);
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Notes");

        List<String> titles = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titles);
        lvNotes.setAdapter(adapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                titles.clear();
                for (DataSnapshot noteSnapshot : snapshot.getChildren()) {
                    NoteModel note = noteSnapshot.getValue(NoteModel.class);
                    if (note != null) titles.add(note.getTitle());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });

        lvNotes.setOnItemClickListener((parent, view, position, id) -> {
            String selectedTitle = titles.get(position);
            startActivity(new Intent(this, UpdateNoteActivity.class).putExtra("title", selectedTitle));
        });
    }
}
