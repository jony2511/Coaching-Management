package com.example.onlinecoachingmanagement;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class AnalyticsFragment extends Fragment {

    PieChart pieChart;
    BarChart barChart;
    String nameOfUser;
    TextView allExam,allExamScore,average;

    private DatabaseReference dbReference;
    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Result Analysis");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Coaching Management");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_analytics, container, false);
        pieChart = view.findViewById(R.id.pieChart);
        barChart = view.findViewById(R.id.barChart);
        allExam = view.findViewById(R.id.all_exams);
        allExamScore = view.findViewById(R.id.total_points);
        average = view.findViewById(R.id.average_points);



        // Firebase reference to "Details" node
        dbReference = FirebaseDatabase.getInstance().getReference("Details");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userString = user.getEmail();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Leaderboard");
        databaseReference.orderByChild("email").equalTo(userString).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        nameOfUser = userSnapshot.child("name").getValue(String.class);
                        //  Toast.makeText(Dashboard.this, "Name: " + nameOfUser, Toast.LENGTH_SHORT).show();
                        //  textView.setText("HI \n" + nameOfUser + ",");
                        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                double totalScore = 0.0;
                                int subjectCount = 0;
                                ArrayList<SubjectPerformance> subjectPerformances = new ArrayList<>();

                                for (DataSnapshot subjectSnapshot : snapshot.getChildren()) {
                                    String subjectName = subjectSnapshot.getKey();
                                    if (subjectSnapshot.hasChild(nameOfUser)) {
                                        double score = subjectSnapshot.child(nameOfUser).getValue(Double.class);
                                        totalScore += score;
                                        subjectCount++;
                                        subjectPerformances.add(new SubjectPerformance(subjectName, score));
                                    }
                                }

                                double averageScore = subjectCount > 0 ? totalScore / subjectCount : 0.0;
                                average.setText("Average\n" + String.format("%.2f", averageScore)+"%");
                                allExamScore.setText("Total Points\n"+String.format("%.2f", totalScore));
                                allExam.setText("All Exams\n"+subjectCount);
                                displayPieChart(averageScore);
                                displayBarChart(subjectPerformances);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(view.getContext(), "Error fetching data!", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                } else {
                    Toast.makeText(getActivity(), "User not found.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
    private void displayPieChart(double averagePerformance) {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry((float) averagePerformance, "Performance"));
        entries.add(new PieEntry((float) (100 - averagePerformance), "Remaining"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(Color.GREEN, Color.RED);

        dataSet.setValueTextSize(14f);
        dataSet.setValueTextColor(Color.BLACK);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(14f);
        pieChart.setData(data);

        pieChart.getDescription().setEnabled(false); // Disable description
        pieChart.setUsePercentValues(true); // Show percentage values
        pieChart.setHoleRadius(40f); // Adjust the hole size
        pieChart.setTransparentCircleRadius(45f); // Adjust transparency around the hole
        pieChart.setCenterText("Average"); // Center text
        pieChart.setCenterTextSize(16f); // Increase center text size
        pieChart.setCenterTextColor(Color.BLACK);

        pieChart.invalidate(); // Refresh the chart
    }

    private void displayBarChart(ArrayList<SubjectPerformance> subjectPerformances) {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        for (int i = 0; i < subjectPerformances.size(); i++) {
            barEntries.add(new BarEntry(i, (float) subjectPerformances.get(i).getPerformance()));
            labels.add(subjectPerformances.get(i).getSubjectName());
        }

        BarDataSet dataSet = new BarDataSet(barEntries, "Subjects");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.BLACK);

        BarData barData = new BarData(dataSet);
        barData.setValueTextSize(15f);

        barChart.setData(barData);

        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setGranularityEnabled(true);
        barChart.getXAxis().setTextSize(12f); // Increase label text size

        // Additional formatting
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setTextSize(14f);
        barChart.animateY(1000);

        barChart.invalidate();
    }
}