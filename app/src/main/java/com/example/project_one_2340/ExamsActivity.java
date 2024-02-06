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

public class ExamsActivity extends AppCompatActivity {
    private List<GenericItem> myExams = new ArrayList<>();
    private GenericAdapter adapter;

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "examPrefs";
    private static final String EXAM_LIST_KEY = "examList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exams_activity);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        loadData();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GenericAdapter(myExams);
        recyclerView.setAdapter(adapter);

        Button addExamButton = findViewById(R.id.btn_add_exam);
        addExamButton.setOnClickListener(view -> showDialogToAddExam());

        adapter.setOnDeleteClickListener(this::showDialogToDeleteExam);
        adapter.setOnEditClickListener(this::showEditExamDialog);
        setupBottomNavigation();
    }

    private void showDialogToAddExam() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.add_exams, null);
        builder.setView(dialogView);

        final EditText editTitle = dialogView.findViewById(R.id.edit_exam_title);
        final EditText editDateTime = dialogView.findViewById(R.id.edit_exam_datetime);
        final EditText editLocation = dialogView.findViewById(R.id.edit_exam_location);
        final Button addButton = dialogView.findViewById(R.id.button_add_exam);

        AlertDialog dialog = builder.create();

        addButton.setOnClickListener(v -> {
            String title = editTitle.getText().toString().trim();
            String dateTime = editDateTime.getText().toString().trim();
            String location = editLocation.getText().toString().trim();

            if (!title.isEmpty() && !dateTime.isEmpty() && !location.isEmpty()) {
                myExams.add(new GenericItem(title, dateTime, location));
                adapter.notifyDataSetChanged();
                dialog.dismiss();
                saveData();
            } else {
                Toast.makeText(ExamsActivity.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void showDialogToDeleteExam(int position) {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to delete this exam?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    myExams.remove(position);
                    adapter.notifyItemRemoved(position);
                    saveData();
                    Toast.makeText(ExamsActivity.this, "Exam deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void showEditExamDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.add_exams, null);
        builder.setView(dialogView);

        final EditText editTitle = dialogView.findViewById(R.id.edit_exam_title);
        final EditText editDateTime = dialogView.findViewById(R.id.edit_exam_datetime);
        final EditText editLocation = dialogView.findViewById(R.id.edit_exam_location);
        final Button updateButton = dialogView.findViewById(R.id.button_add_exam);
        updateButton.setText("Update");

        // Pre-populate the fields
        GenericItem item = myExams.get(position);
        editTitle.setText(item.getTitle());
        editDateTime.setText(item.getSubtitle1());
        editLocation.setText(item.getSubtitle2());

        AlertDialog dialog = builder.create();

        updateButton.setOnClickListener(v -> {
            String title = editTitle.getText().toString().trim();
            String dateTime = editDateTime.getText().toString().trim();
            String location = editLocation.getText().toString().trim();

            if (!title.isEmpty() && !dateTime.isEmpty() && !location.isEmpty()) {
                myExams.set(position, new GenericItem(title, dateTime, location));
                adapter.notifyDataSetChanged();
                dialog.dismiss();
                saveData();
            } else {
                Toast.makeText(ExamsActivity.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
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
        String json = gson.toJson(myExams);
        editor.putString(EXAM_LIST_KEY, json);
        editor.apply();
    }

    private void loadData() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(EXAM_LIST_KEY, null);
        Type type = new TypeToken<ArrayList<GenericItem>>() {
        }.getType();
        myExams = gson.fromJson(json, type);
        if (myExams == null) {
            myExams = new ArrayList<>();
        }
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.navigation);
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.classes) {
                // Navigate to ClassesActivity
                startActivity(new Intent(ExamsActivity.this, ClassesActivity.class));
            } else if (itemId == R.id.assignments) {
                // Navigate to AssignmentsActivity
                startActivity(new Intent(ExamsActivity.this, AssignmentsActivity.class));
            } else if (itemId == R.id.exams) {
                // Already in ExamsActivity, so do nothing
            }
            // Returning true here to indicate that the item has been selected
            return true;
        });

        // Highlight the current tab (Exams)
        bottomNav.setSelectedItemId(R.id.exams);
    }
}


