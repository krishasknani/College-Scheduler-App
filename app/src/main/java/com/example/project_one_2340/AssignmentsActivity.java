package com.example.project_one_2340;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AssignmentsActivity extends AppCompatActivity {
    private List<GenericItem> myAssignments = new ArrayList<>();
    private GenericAdapter adapter;

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "assignmentPrefs";
    private static final String ASSIGNMENT_LIST_KEY = "assignmentList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignments);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        loadData();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GenericAdapter(myAssignments);
        recyclerView.setAdapter(adapter);

        Button addAssignmentButton = findViewById(R.id.btn_add_assignment);
        addAssignmentButton.setOnClickListener(view -> showDialogToAddAssignment());

        adapter.setOnDeleteClickListener(this::showDialogToDeleteAssignment);
        adapter.setOnEditClickListener(this::showEditAssignmentDialog);
        setupBottomNavigation();

    }

    private void showDialogToAddAssignment() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.add_assignments, null);
        builder.setView(dialogView);

        final EditText editTitle = dialogView.findViewById(R.id.edit_title);
        final EditText editDueDate = dialogView.findViewById(R.id.edit_due_date);
        final EditText editAssociatedClass = dialogView.findViewById(R.id.edit_associated_class);
        final Button addButton = dialogView.findViewById(R.id.button_add_assignment);

        AlertDialog dialog = builder.create();

        addButton.setOnClickListener(v -> {
            String title = editTitle.getText().toString().trim();
            String dueDate = editDueDate.getText().toString().trim();
            String associatedClass = editAssociatedClass.getText().toString().trim();

            if (!title.isEmpty() && !dueDate.isEmpty() && !associatedClass.isEmpty()) {
                myAssignments.add(new GenericItem(title, dueDate, associatedClass));
                adapter.notifyDataSetChanged();
                dialog.dismiss();
                saveData();
            } else {
                Toast.makeText(AssignmentsActivity.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void showDialogToDeleteAssignment(int position) {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to delete this assignment?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    myAssignments.remove(position);
                    adapter.notifyItemRemoved(position);
                    saveData();
                    Toast.makeText(AssignmentsActivity.this, "Assignment deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void showEditAssignmentDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.add_assignments, null);
        builder.setView(dialogView);

        final EditText editTitle = dialogView.findViewById(R.id.edit_title);
        final EditText editDueDate = dialogView.findViewById(R.id.edit_due_date);
        final EditText editAssociatedClass = dialogView.findViewById(R.id.edit_associated_class);
        final Button updateButton = dialogView.findViewById(R.id.button_add_assignment);
        updateButton.setText("Update");

        // Pre-populate the fields
        GenericItem item = myAssignments.get(position);
        editTitle.setText(item.getTitle());
        editDueDate.setText(item.getSubtitle1());
        editAssociatedClass.setText(item.getSubtitle2());

        AlertDialog dialog = builder.create();

        updateButton.setOnClickListener(v -> {
            String title = editTitle.getText().toString().trim();
            String dueDate = editDueDate.getText().toString().trim();
            String associatedClass = editAssociatedClass.getText().toString().trim();

            if (!title.isEmpty() && !dueDate.isEmpty() && !associatedClass.isEmpty()) {
                myAssignments.set(position, new GenericItem(title, dueDate, associatedClass));
                adapter.notifyDataSetChanged();
                dialog.dismiss();
                saveData();
            } else {
                Toast.makeText(AssignmentsActivity.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }

    private void saveData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(myAssignments);
        editor.putString(ASSIGNMENT_LIST_KEY, json);
        editor.apply();
    }

    private void loadData() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(ASSIGNMENT_LIST_KEY, null);
        Type type = new TypeToken<ArrayList<GenericItem>>() {}.getType();
        myAssignments = gson.fromJson(json, type);
        if (myAssignments == null) {
            myAssignments = new ArrayList<>();
        }
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.navigation);
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.classes) {
                // Navigate to ClassesActivity
                startActivity(new Intent(AssignmentsActivity.this, ClassesActivity.class));
                return true;
            } else if (itemId == R.id.assignments) {
                // Already in AssignmentsActivity, so do nothing
                return true;
            } else if (itemId == R.id.exams) {
                // Navigate to ExamsActivity
                startActivity(new Intent(AssignmentsActivity.this, ExamsActivity.class));
                return true;
            }
            return false;
        });

        // Highlight the current tab (Assignments)
        bottomNav.setSelectedItemId(R.id.assignments);
    }
}
