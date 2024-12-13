package com.example.onlinecoachingmanagement;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        EditText etTitle = findViewById(R.id.etNoteTitle);
        EditText etContent = findViewById(R.id.etNoteContent);
        Button btnSave = findViewById(R.id.btnSaveNote);

        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Notes");

        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String content = etContent.getText().toString().trim();

            if (!title.isEmpty() && !content.isEmpty()) {
                String noteId = database.push().getKey();
                if (noteId != null) {
                    database.child(noteId).setValue(new NoteModel(title, content))
                            .addOnSuccessListener(unused ->
                                    Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show()
                            )
                            .addOnFailureListener(e ->
                                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                            );
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
 class NoteModel {
    private String title;
    private String content;

    public NoteModel() {}

    public NoteModel(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
