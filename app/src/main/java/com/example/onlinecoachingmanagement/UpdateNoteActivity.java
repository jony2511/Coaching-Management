package com.example.onlinecoachingmanagement;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_note);

        EditText etUpdateContent = findViewById(R.id.etUpdateContent);
        Button btnUpdateNote = findViewById(R.id.btnUpdateNote);

        String title = getIntent().getStringExtra("title");
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Notes");
        Button btnDeleteNote = findViewById(R.id.btnDeleteNote);

        btnDeleteNote.setOnClickListener(v -> {
            if (title != null) {
                new AlertDialog.Builder(UpdateNoteActivity.this)
                        .setIcon(R.drawable.delete_note)
                        .setTitle("Delete Note")
                        .setMessage("Are you sure you want to delete this note?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            // Perform deletion
                            database.orderByChild("title").equalTo(title).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    for (DataSnapshot noteSnapshot : snapshot.getChildren()) {
                                        noteSnapshot.getRef().removeValue()
                                                .addOnSuccessListener(unused -> {
                                                    Toast.makeText(UpdateNoteActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
                                                    finish(); // Close the activity after deletion
                                                })
                                                .addOnFailureListener(e ->
                                                        Toast.makeText(UpdateNoteActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                                                );
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    Toast.makeText(UpdateNoteActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        })
                        .setNegativeButton("No", (dialog, which) -> {
                            // Dismiss the dialog
                            dialog.dismiss();
                        })
                        .show();
            } else {
                Toast.makeText(this, "Error: Note title not found", Toast.LENGTH_SHORT).show();
            }
        });



        if (title != null) {
            // Load existing note content
            database.orderByChild("title").equalTo(title).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot noteSnapshot : snapshot.getChildren()) {
                        NoteModel note = noteSnapshot.getValue(NoteModel.class);
                        if (note != null) {
                            etUpdateContent.setText(note.getContent());
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(UpdateNoteActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            // Update note content on button click
            btnUpdateNote.setOnClickListener(v -> {
                String updatedContent = etUpdateContent.getText().toString().trim();

                if (!updatedContent.isEmpty()) {
                    database.orderByChild("title").equalTo(title).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            for (DataSnapshot noteSnapshot : snapshot.getChildren()) {
                                noteSnapshot.getRef().child("content").setValue(updatedContent)
                                        .addOnSuccessListener(unused ->
                                                Toast.makeText(UpdateNoteActivity.this, "Note Updated", Toast.LENGTH_SHORT).show()
                                        )
                                        .addOnFailureListener(e ->
                                                Toast.makeText(UpdateNoteActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                                        );
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            Toast.makeText(UpdateNoteActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(this, "Content cannot be empty", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Error: Note title not found", Toast.LENGTH_SHORT).show();
        }
    }
}
