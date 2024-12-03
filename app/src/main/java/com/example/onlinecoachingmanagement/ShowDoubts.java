package com.example.onlinecoachingmanagement;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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

public class ShowDoubts extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference databaseRef;
    private List<Doubt> doubtList;
    private StudentDoubtAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_doubts);

        recyclerView = findViewById(R.id.studentRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseRef = FirebaseDatabase.getInstance().getReference("doubts");
        doubtList = new ArrayList<>();
        adapter = new StudentDoubtAdapter(this, doubtList);
        recyclerView.setAdapter(adapter);

        fetchDoubts();
    }

    private void fetchDoubts() {
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                doubtList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Doubt doubt = dataSnapshot.getValue(Doubt.class);
                    if (doubt != null) {
                        doubtList.add(doubt);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ShowDoubts.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

 class StudentDoubtAdapter extends RecyclerView.Adapter<StudentDoubtAdapter.StudentDoubtViewHolder> {

    private Context context;
    private List<Doubt> doubtList;

    public StudentDoubtAdapter(Context context, List<Doubt> doubtList) {
        this.context = context;
        this.doubtList = doubtList;
    }

    @NonNull
    @Override
    public StudentDoubtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.show_doubt_item, parent, false);
        return new StudentDoubtViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentDoubtViewHolder holder, int position) {
        Doubt doubt = doubtList.get(position);
        holder.questionTextView.setText("Q" + (position + 1) + ": " + doubt.getQuestion());

        // Show "Pending answer" if no answer is provided
        if (doubt.getAnswer() == null || doubt.getAnswer().isEmpty()) {
            holder.answerTextView.setText("Answer: Pending");
        } else {
            holder.answerTextView.setText("Answer: \n    " + doubt.getAnswer());
        }
    }

    @Override
    public int getItemCount() {
        return doubtList.size();
    }

    public static class StudentDoubtViewHolder extends RecyclerView.ViewHolder {
        TextView questionTextView, answerTextView;

        public StudentDoubtViewHolder(@NonNull View itemView) {
            super(itemView);

            questionTextView = itemView.findViewById(R.id.studentQuestion);
            answerTextView = itemView.findViewById(R.id.studentAnswer);
        }
    }
}
