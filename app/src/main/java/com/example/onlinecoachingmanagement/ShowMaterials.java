package com.example.onlinecoachingmanagement;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ShowMaterials extends AppCompatActivity {

    private TextView tvTodayMaterials, tvPreviousMaterials;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_materials);

        tvTodayMaterials = findViewById(R.id.tv_today_materials);
        tvPreviousMaterials = findViewById(R.id.tv_previous_materials);

        databaseReference = FirebaseDatabase.getInstance().getReference("materials");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tvTodayMaterials.setText("");   // Clear the TextView
                tvPreviousMaterials.setText(""); // Clear the TextView

                for (DataSnapshot data : snapshot.getChildren()) {
                    String title = data.child("title").getValue(String.class);
                    String content = data.child("content").getValue(String.class);
                    String date = data.child("date").getValue(String.class);

                    String materialText = "Title: " + title + "\nContent: \n" + content + "\n\n";

                    if (isToday(date)) {
                        tvTodayMaterials.append(materialText);
                    } else {
                        tvPreviousMaterials.append(materialText);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error if needed
            }
        });
    }

    private boolean isToday(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String today = sdf.format(new Date());
        return date.equals(today);
    }
}
