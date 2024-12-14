package com.example.onlinecoachingmanagement;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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


public class ProfileFragment extends Fragment {

    PieChart pieChart;
    BarChart barChart;
    String nameOfUser;
    TextView userName,userEmail,allExam,allExamScore,average;
    private ListView listView;
    private Button btnUpdateProfile;
    private DatabaseReference dbReference;
    List<DetailItem> detailItems;

    public String emailOfUser="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
         pieChart = view.findViewById(R.id.pieChart);
         barChart = view.findViewById(R.id.barChart);
         userName = view.findViewById(R.id.user_name);
         userEmail = view.findViewById(R.id.user_email);
         allExam = view.findViewById(R.id.all_exams);
         allExamScore = view.findViewById(R.id.total_points);
         average = view.findViewById(R.id.average_points);
         listView = view.findViewById(R.id.listViewDetails);
         btnUpdateProfile = view.findViewById(R.id.btnUpdateProfile);
         btnUpdateProfile.setOnClickListener(v -> showUpdateDialog());

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
                        emailOfUser = userString;
                        userName.setText(nameOfUser);
                        userEmail.setText(userString);
                        addUserDetails(nameOfUser, userString);
                        loadUserDetails(userString);
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
    private void addUserDetails(String name,String email) {
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference("Details");

        // Example user data

        String className = "";
        String address = "";
        String dob = "";
        String mobile = "";

        // Query to check if the email already exists
        dbReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Email already exists
                    Toast.makeText(getActivity(), "User found in database!", Toast.LENGTH_SHORT).show();
                } else {
                    // Email does not exist, proceed to add the new user
                    String userId = dbReference.push().getKey();
                    if (userId != null) {
                        UserDetails user = new UserDetails(email, name, className, address, dob, mobile);
                        dbReference.child(userId).setValue(user).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "User details added to to Database!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Failed to add user details: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    // Method to load user details from Firebase and display them in the ListView
    private void loadUserDetails(String email) {
        dbReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    detailItems = new ArrayList<>();

                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        UserDetails user = childSnapshot.getValue(UserDetails.class);
                        if (user != null) {
                            detailItems.add(new DetailItem(R.drawable.name_icon, "Name", user.name));
                            detailItems.add(new DetailItem(R.drawable.ic_mobile, "Mobile", user.mobile));
                            detailItems.add(new DetailItem(R.drawable.ic_email, "Email", user.email));
                            detailItems.add(new DetailItem(R.drawable.ic_address, "Address", user.address));
                            detailItems.add(new DetailItem(R.drawable.ic_dob, "D.O.B", user.dob));
                            detailItems.add(new DetailItem(R.drawable.ic_class, "Class", user.className));
                        }
                    }

                    DetailsAdapter adapter = new DetailsAdapter(getActivity(), detailItems);
                    listView.setAdapter(adapter);

                } else {
                    Toast.makeText(getActivity(), "User details not found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_update_profile, null);
        builder.setView(dialogView);

        EditText etClass = dialogView.findViewById(R.id.etClass);
        EditText etAddress = dialogView.findViewById(R.id.etAddress);
        EditText etDob = dialogView.findViewById(R.id.etDob);
        EditText etMobile = dialogView.findViewById(R.id.etMobile);
        Button btnSave = dialogView.findViewById(R.id.btnSave);

        // Pre-fill fields with current data
        if (detailItems != null && detailItems.size() >= 5) {
            etClass.setText(detailItems.get(5).getContent());
            etAddress.setText(detailItems.get(3).getContent());
            etDob.setText(detailItems.get(4).getContent());
            etMobile.setText(detailItems.get(1).getContent());

        }

        AlertDialog dialog = builder.create();
        dialog.show();

        btnSave.setOnClickListener(v -> {
            String updatedClass = etClass.getText().toString();
            String updatedAddress = etAddress.getText().toString();
            String updatedDob = etDob.getText().toString();
            String updatedMobile = etMobile.getText().toString();

            dbReference.orderByChild("email").equalTo(emailOfUser).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        childSnapshot.getRef().child("className").setValue(updatedClass);
                        childSnapshot.getRef().child("address").setValue(updatedAddress);
                        childSnapshot.getRef().child("dob").setValue(updatedDob);
                        childSnapshot.getRef().child("mobile").setValue(updatedMobile);
                    }
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                    loadUserDetails(emailOfUser);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(), "Error updating profile: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }


}
 class SubjectPerformance {
    private String subjectName;
    private double performance;

    public SubjectPerformance() {}

    public SubjectPerformance(String subjectName, double performance) {
        this.subjectName = subjectName;
        this.performance = performance;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public double getPerformance() {
        return performance;
    }
}
 class UserDetails {
    public String email, name, className, address, dob, mobile;

    public UserDetails() { } // Default constructor required for Firebase

    public UserDetails(String email, String name, String className, String address, String dob, String mobile) {
        this.email = email;
        this.name = name;
        this.className = className;
        this.address = address;
        this.dob = dob;
        this.mobile = mobile;
    }
}
 class DetailsAdapter extends ArrayAdapter<DetailItem> {
    private Context context;
    private List<DetailItem> detailItems;

    public DetailsAdapter(Context context, List<DetailItem> detailItems) {
        super(context, R.layout.item_detail, detailItems);
        this.context = context;
        this.detailItems = detailItems;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        // Reuse views for efficiency
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_detail, parent, false);
            holder = new ViewHolder();
            holder.icon = convertView.findViewById(R.id.detail_icon);
            holder.title = convertView.findViewById(R.id.detail_title);
            holder.content = convertView.findViewById(R.id.detail_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Bind data to views
        DetailItem item = detailItems.get(position);
        holder.icon.setImageResource(item.getIconResId());
        holder.title.setText(item.getTitle());
        holder.content.setText(item.getContent());

        return convertView;
    }

    // ViewHolder for optimization
    static class ViewHolder {
        ImageView icon;
        TextView title;
        TextView content;
    }
}

class DetailItem {
    private int iconResId;
    private String title;
    private String content;

    public DetailItem(int iconResId, String title, String content) {
        this.iconResId = iconResId;
        this.title = title;
        this.content = content;
    }

    public int getIconResId() {
        return iconResId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
