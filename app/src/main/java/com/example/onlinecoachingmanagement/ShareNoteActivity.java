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
import java.util.List;

public class ShareNoteActivity extends AppCompatActivity {

    private ListView lvNotesForShare;
    private List<String> noteTitles;
    private List<NoteModel> noteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_note);
        setTitle("List of Notes");

        lvNotesForShare = findViewById(R.id.lvNotesForShare);
        noteTitles = new ArrayList<>();
        noteList = new ArrayList<>();

        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Notes");

        // Fetch notes from Firebase
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot noteSnapshot : snapshot.getChildren()) {
                    NoteModel note = noteSnapshot.getValue(NoteModel.class);
                    if (note != null) {
                        noteTitles.add(note.getTitle());
                        noteList.add(note);
                    }
                }

                // Populate ListView with note titles
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ShareNoteActivity.this,
                        android.R.layout.simple_list_item_1, noteTitles);
                lvNotesForShare.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ShareNoteActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Handle note selection for sharing
        lvNotesForShare.setOnItemClickListener((AdapterView<?> parent, android.view.View view, int position, long id) -> {
            NoteModel selectedNote = noteList.get(position);
            shareNoteContent(selectedNote.getTitle(), selectedNote.getContent());
        });
    }

    private void shareNoteContent(String title, String content) {
        String noteToShare = "Title: " + title + "\n\n" + "Content:\n"+content;
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, noteToShare);
        // Show app chooser for sharing
        startActivity(Intent.createChooser(shareIntent, "Share Note via"));
    }
}
