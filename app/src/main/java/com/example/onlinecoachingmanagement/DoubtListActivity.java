package com.example.onlinecoachingmanagement;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;


public class DoubtListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewDoubts;
    private DoubtAdapter adapter;
    private DatabaseReference databaseRef;
    private List<Doubt> doubtList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doubt_list);

        recyclerViewDoubts = findViewById(R.id.recyclerViewDoubts);
        recyclerViewDoubts.setLayoutManager(new LinearLayoutManager(this));

        databaseRef = FirebaseDatabase.getInstance().getReference("doubts");
        doubtList = new ArrayList<>();

        loadDoubts();
    }

    private void loadDoubts() {
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                doubtList.clear();
                for (DataSnapshot doubtSnapshot : snapshot.getChildren()) {
                    Doubt doubt = doubtSnapshot.getValue(Doubt.class);
                    doubtList.add(doubt);
                }

                adapter = new DoubtAdapter(DoubtListActivity.this, doubtList);
                recyclerViewDoubts.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DoubtListActivity.this, "Failed to load doubts", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

 class DoubtAdapter extends RecyclerView.Adapter<DoubtAdapter.DoubtViewHolder> {

    private Context context;
    private List<Doubt> doubtList;
    private DatabaseReference databaseRef;

    public DoubtAdapter(Context context, List<Doubt> doubtList) {
        this.context = context;
        this.doubtList = doubtList;
        this.databaseRef = FirebaseDatabase.getInstance().getReference("doubts");
    }

    @NonNull
    @Override
    public DoubtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.doubt_item, parent, false);
        return new DoubtViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoubtViewHolder holder, int position) {
        Doubt doubt = doubtList.get(position);

        holder.questionTextView.setText(doubt.getQuestion());
        holder.statusTextView.setText(doubt.getStatus().equals("Pending answer") ? doubt.getStatus() : "Answer: " + doubt.getAnswer());

        holder.answerButton.setOnClickListener(v -> showAnswerDialog(doubt));
        holder.updateButton.setOnClickListener(v -> showUpdateDialog(doubt));
        holder.deleteButton.setOnClickListener(v -> deleteDoubt(doubt));

    }

    @Override
    public int getItemCount() {
        return doubtList.size();
    }

    private void showAnswerDialog(Doubt doubt) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Answer the Doubt");

        EditText input = new EditText(context);
        input.setHint("Enter your answer");
        builder.setView(input);

        builder.setPositiveButton("Submit", (dialog, which) -> {
            String answer = input.getText().toString();
            if (!TextUtils.isEmpty(answer)) {
                databaseRef.child(doubt.getId()).child("answer").setValue(answer);
                databaseRef.child(doubt.getId()).child("status").setValue("Answered");
                Toast.makeText(context, "Answer submitted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Answer cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void showUpdateDialog(Doubt doubt) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Update Answer");

        EditText input = new EditText(context);
        input.setHint("Enter updated answer");
        input.setText(doubt.getAnswer());
        builder.setView(input);

        builder.setPositiveButton("Update", (dialog, which) -> {
            String updatedAnswer = input.getText().toString();
            if (!TextUtils.isEmpty(updatedAnswer)) {
                databaseRef.child(doubt.getId()).child("answer").setValue(updatedAnswer);
                databaseRef.child(doubt.getId()).child("status").setValue("Answered");
                Toast.makeText(context, "Answer updated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Answer cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

     private void deleteDoubt(Doubt doubt) {
         // Create an AlertDialog
         AlertDialog.Builder builder = new AlertDialog.Builder(context);
         builder.setTitle("Delete Doubt!");
         builder.setMessage("Are you sure you want to delete this doubt? This action cannot be undone.");
         builder.setIcon(R.drawable.img_delete);

         // Set positive (Yes) button action
         builder.setPositiveButton("Yes", (dialog, which) -> {
             databaseRef.child(doubt.getId()).removeValue().addOnSuccessListener(aVoid -> {
                 Toast.makeText(context, "Doubt deleted successfully", Toast.LENGTH_SHORT).show();
             }).addOnFailureListener(e -> {
                 Toast.makeText(context, "Failed to delete doubt", Toast.LENGTH_SHORT).show();
             });
         });

         // Set negative (No) button action
         builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

         // Show the AlertDialog
         builder.create().show();
     }


     static class DoubtViewHolder extends RecyclerView.ViewHolder {
        TextView questionTextView, statusTextView;
        Button answerButton, updateButton, deleteButton;

        public DoubtViewHolder(@NonNull View itemView) {
            super(itemView);
            questionTextView = itemView.findViewById(R.id.question);
            statusTextView = itemView.findViewById(R.id.status);
            answerButton = itemView.findViewById(R.id.answerButton);
            updateButton = itemView.findViewById(R.id.updateButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
