package com.example.onlinecoachingmanagement;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BranchesActivity extends AppCompatActivity {

    private ListView branchesListView;
    private List<Branch> branches;
    private BranchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branches);

        branchesListView = findViewById(R.id.branchesListView);
        branches = new ArrayList<>();
        adapter = new BranchAdapter(this, branches);
        branchesListView.setAdapter(adapter);

        fetchBranchData();

        branchesListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(BranchesActivity.this, BranchDetailsActivity.class);
            intent.putExtra("branch", branches.get(position));
            startActivity(intent);
        });
    }

    private void fetchBranchData() {
        String url = "https://api.myjson.online/v1/records/fd808143-2df9-4e72-9359-38126e1b15d9";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        // Access the "data" object first
                        JSONObject dataObject = response.getJSONObject("data");
                        JSONArray branchesArray = dataObject.getJSONArray("branches");

                        // Parse each branch
                        for (int i = 0; i < branchesArray.length(); i++) {
                            JSONObject branchObject = branchesArray.getJSONObject(i);

                            String name = branchObject.getString("name");
                            String imageUrl = branchObject.getString("image_url");
                            String details = branchObject.getString("details");

                            Branch branch = new Branch(name, imageUrl, details);
                            branches.add(branch);
                        }

                        // Notify adapter of data change
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Toast.makeText(BranchesActivity.this, "Error parsing JSON", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(BranchesActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show()
        );

        queue.add(jsonObjectRequest);
    }

}
 class BranchAdapter extends ArrayAdapter<Branch> {

    public BranchAdapter(Context context, List<Branch> branches) {
        super(context, android.R.layout.simple_list_item_1, branches);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        Branch branch = getItem(position);
        textView.setText(branch.getName());
        return convertView;
    }
}

class Branch implements Serializable {
    private String name;
    private String imageUrl;
    private String details;

    public Branch(String name, String imageUrl, String details) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.details = details;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDetails() {
        return details;
    }
}
