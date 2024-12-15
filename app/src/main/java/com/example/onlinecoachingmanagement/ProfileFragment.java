package com.example.onlinecoachingmanagement;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
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


    private Button btnDeleteProfile;
    private FirebaseAuth mAuth;
    String nameOfUser;
    TextView userName,userEmail;
    private ListView listView;
    private Button btnUpdateProfile;
    private DatabaseReference dbReference;
    List<DetailItem> detailItems;

    public String emailOfUser="";
    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Profile");
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


         userName = view.findViewById(R.id.user_name);
         userEmail = view.findViewById(R.id.user_email);
         listView = view.findViewById(R.id.listViewDetails);
         btnUpdateProfile = view.findViewById(R.id.btnUpdateProfile);
         btnUpdateProfile.setOnClickListener(v -> showUpdateDialog());
        mAuth = FirebaseAuth.getInstance();
        //btnDeleteProfile = view.findViewById(R.id.btnDeleteProfile);

      //  btnDeleteProfile.setOnClickListener(v -> showDeleteConfirmationDialog());


        // Firebase reference to "Details" node
        dbReference = FirebaseDatabase.getInstance().getReference("Details");


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userString = user.getEmail();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

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

                    }
                } else {
                   // Toast.makeText(getActivity(), "User not found.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
//    private void showDeleteConfirmationDialog() {
//        new AlertDialog.Builder(getContext())
//                .setTitle("Delete Profile")
//                .setMessage("Are you sure you want to delete your profile? This action cannot be undone.")
//                .setPositiveButton("Yes", (dialog, which) -> deleteUserFromAuth())
//                .setNegativeButton("No", null)
//                .show();
//    }
    private void deleteProfile() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            // Get the user email or UID (you may want to use UID for more consistency)
            String userId = currentUser.getUid();
          //  Log.d("ProfileFragment", "User ID: " + userId);
            // Delete the user's details from the "Details" node in the database
//            dbReference.child(userId).removeValue().addOnCompleteListener(task -> {
//                if (task.isSuccessful()) {
//                    Toast.makeText(getActivity(), "Profile data deleted from database.", Toast.LENGTH_SHORT).show();
//                   // deleteUserFromAuth();  // Proceed to delete the user from Firebase Auth
//                } else {
//                    Toast.makeText(getActivity(), "Error deleting profile data.", Toast.LENGTH_SHORT).show();
//                }
//            });
        }
    }

    // Method to delete the user from Firebase Authentication
//    private void deleteUserFromAuth() {
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//
//        if (currentUser != null) {
//            // Delete the user's account from Firebase Authentication
//            currentUser.delete().addOnCompleteListener(task -> {
//                if (task.isSuccessful()) {
//                    // Inform the user the deletion was successful
//                    Toast.makeText(getActivity(), "Profile deleted successfully.", Toast.LENGTH_SHORT).show();
//
//                    // Optionally, redirect to the login screen or home screen
//                    Intent intent = new Intent(getActivity(), Login.class);  // or main screen
//                    startActivity(intent);
//                    getActivity().finish();
//                } else {
//                    Toast.makeText(getActivity(), "Error deleting user account.", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    }
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

                } else {
                    // Email does not exist, proceed to add the new user
                    String userId = dbReference.push().getKey();
                    if (userId != null) {
                        UserDetails user = new UserDetails(email, name, className, address, dob, mobile);
                        dbReference.child(userId).setValue(user).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                             //   Toast.makeText(getActivity(), "User details added to to Database!", Toast.LENGTH_SHORT).show();
                            } else {
                              //  Toast.makeText(getActivity(), "Failed to add user details: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
                            detailItems.add(new DetailItem(R.drawable.ic_dob, "Date of Birth", user.dob));
                            detailItems.add(new DetailItem(R.drawable.ic_class, "Class", user.className));
                        }
                    }

                    DetailsAdapter adapter = new DetailsAdapter(getActivity(), detailItems);
                    listView.setAdapter(adapter);

                } else {
                   // Toast.makeText(getActivity(), "User details not found!", Toast.LENGTH_SHORT).show();
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
