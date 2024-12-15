package com.example.onlinecoachingmanagement;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.LinearLayout;
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
        setTitle("Class Materials");

        tvTodayMaterials = findViewById(R.id.tv_today_materials);
        tvPreviousMaterials = findViewById(R.id.tv_previous_materials);

        databaseReference = FirebaseDatabase.getInstance().getReference("materials");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tvTodayMaterials.setText("");   // Clear the TextView
                tvPreviousMaterials.setText(""); // Clear the TextView
                GradientDrawable border = new GradientDrawable();
                border.setColor(Color.BLACK); // Background color
                border.setStroke(2, Color.BLACK); // Border thickness and color
                border.setCornerRadius(8); // Corner radius
                tvTodayMaterials.setBackground(border);
                tvPreviousMaterials.setBackground(border);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 16, 0, 16); // Left, top, right, bottom margins
                tvTodayMaterials.setLayoutParams(params);
                tvPreviousMaterials.setLayoutParams(params);


                tvTodayMaterials.setPadding(0, 16, 0, 16);
                tvTodayMaterials.setTextSize(18);
                tvTodayMaterials.setTypeface(null, Typeface.BOLD);
                tvTodayMaterials.setBackgroundColor(Color.parseColor("#E0F7FA"));

                tvPreviousMaterials.setPadding(0, 16, 0, 16);
                tvPreviousMaterials.setTextSize(16);
                tvPreviousMaterials.setTypeface(null, Typeface.ITALIC);
                tvPreviousMaterials.setBackgroundColor(Color.parseColor("#ECEFF1"));

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

            }
        });
    }

    private boolean isToday(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String today = sdf.format(new Date());
        return date.equals(today);
    }
}
