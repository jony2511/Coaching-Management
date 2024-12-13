package com.example.onlinecoachingmanagement;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;



import com.bumptech.glide.Glide;

public class BranchDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_details);

        ImageView branchImage = findViewById(R.id.branchImage);
        TextView branchName = findViewById(R.id.branchName);
        TextView branchDetails = findViewById(R.id.branchDetails);

        Branch branch = (Branch) getIntent().getSerializableExtra("branch");

        if (branch != null) {
            branchName.setText(branch.getName());
            branchDetails.setText(branch.getDetails());
            Glide.with(this).load(branch.getImageUrl()).into(branchImage);
        }
    }
}


