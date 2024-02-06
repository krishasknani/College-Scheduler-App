package com.example.project_one_2340;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
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

public class ClassesActivity extends AppCompatActivity {
    private List<GenericItem> myItems = new ArrayList<>();
    private GenericAdapter adapter;

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "classPrefs";
    private static final String CLASS_LIST_KEY = "classList";
    private List<String> toDoList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.classes_activity);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        loadData(); // Load saved data

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GenericAdapter(myItems); // Initialize your adapter with the list
        recyclerView.setAdapter(adapter);

        Button addClassButton = findViewById(R.id.btn_add_class);
        addClassButton.setOnClickListener(view -> showDialogToAddClass());

        adapter.setOnDeleteClickListener(position -> showDialogToDeleteClass(position));
        adapter.setOnEditClickListener(position -> showEditClassDialog(position));
        setupBottomNavigation();

        Button toDoListButton = findViewById(R.id.btn_to_do_list);
        toDoListButton.setOnClickListener(v -> showToDoList());
    }

    private void showDialogToDeleteClass(int position) {
        new AlertDialog.Builder(this)
                .setMessage("Would you like to delete this class?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    myItems.remove(position);
                    adapter.notifyItemRemoved(position);
                    Toast.makeText(ClassesActivity.this, "Class deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void showEditClassDialog(int position) {
        // Similar structure to showDialogToAddClass but with pre-filled data for editing
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_class, null);
        builder.setView(dialogView);

        // Assuming title represents class name, subtitle1 represents professor name, and subtitle2 represents meeting time
        EditText editClassName = dialogView.findViewById(R.id.edit_class_name);
        EditText editProfessorName = dialogView.findViewById(R.id.edit_professor_name);
        EditText editMeetingTime = dialogView.findViewById(R.id.edit_meeting_time);

        GenericItem item = myItems.get(position);
        editClassName.setText(item.getTitle());
        editProfessorName.setText(item.getSubtitle1());
        editMeetingTime.setText(item.getSubtitle2());

        Button addButton = dialogView.findViewById(R.id.button_add_class);
        addButton.setText("Update"); // Change button text to "Update"

        AlertDialog dialog = builder.create();

        addButton.setOnClickListener(v -> {
            // Update the item at the given position
            String title = editClassName.getText().toString();
            String subtitle1 = editProfessorName.getText().toString();
            String subtitle2 = editMeetingTime.getText().toString();

            if (!title.isEmpty() && !subtitle1.isEmpty() && !subtitle2.isEmpty()) {
                myItems.set(position, new GenericItem(title, subtitle1, subtitle2));
                adapter.notifyDataSetChanged(); // Notify the adapter to refresh the list
                dialog.dismiss();
            } else {
                Toast.makeText(ClassesActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void showDialogToAddClass() {
        // Structure remains largely the same, but now creates GenericItem instances
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_class, null);
        builder.setView(dialogView);

        EditText editClassName = dialogView.findViewById(R.id.edit_class_name);
        EditText editProfessorName = dialogView.findViewById(R.id.edit_professor_name);
        EditText editMeetingTime = dialogView.findViewById(R.id.edit_meeting_time);

        Button addButton = dialogView.findViewById(R.id.button_add_class);
        AlertDialog dialog = builder.create();

        addButton.setOnClickListener(v -> {
            String title = editClassName.getText().toString();
            String subtitle1 = editProfessorName.getText().toString();
            String subtitle2 = editMeetingTime.getText().toString();

            if (!title.isEmpty() && !subtitle1.isEmpty() && !subtitle2.isEmpty()) {
                myItems.add(new GenericItem(title, subtitle1, subtitle2));
                adapter.notifyDataSetChanged(); // Notify the adapter to refresh the list
                dialog.dismiss();
            } else {
                Toast.makeText(ClassesActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData(); // Save data when the app is paused
    }

    private void saveData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(myItems);
        editor.putString(CLASS_LIST_KEY, json);
        editor.apply();
    }

    private void loadData() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(CLASS_LIST_KEY, null);
        Type type = new TypeToken<ArrayList<GenericItem>>() {}.getType();
        myItems = gson.fromJson(json, type);

        if (myItems == null) {
            myItems = new ArrayList<>();
        }

        // Reinitialize your adapter with the list
        adapter = new GenericAdapter(myItems);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.navigation);
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.classes) {
                // Already in ClassesActivity, so do nothing
                return true;
            } else if (itemId == R.id.assignments) {
                // Navigate to AssignmentsActivity
                startActivity(new Intent(ClassesActivity.this, AssignmentsActivity.class));
                return true;
            } else if (itemId == R.id.exams) {
                // Navigate to ExamsActivity
                startActivity(new Intent(ClassesActivity.this, ExamsActivity.class));
                return true;
            }
            return false;
        });

        // Highlight the current tab (Classes)
        bottomNav.setSelectedItemId(R.id.classes);
    }

    private void showToDoList() {
        CharSequence[] items = toDoList.toArray(new CharSequence[0]);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("To-Do List");
        builder.setItems(items, (dialog, which) -> {
            showEditDeleteOptionsDialog(which);
        });
        builder.setPositiveButton("Add Task", (dialog, which) -> {
            showAddTaskDialog();
        });
        builder.setNegativeButton("Close", null);
        builder.show();
    }
    private void showAddTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Task");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Add", (dialog, which) -> {
            toDoList.add(input.getText().toString());
            // Optionally refresh the to-do list dialog or other UI elements here
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void showEditDeleteOptionsDialog(final int taskIndex) {
        CharSequence[] items = {"Edit", "Delete"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Option");
        builder.setItems(items, (dialog, which) -> {
            if (which == 0) {
                showEditTaskDialog(taskIndex);
            } else {
                toDoList.remove(taskIndex);
                // Optionally refresh the to-do list dialog or other UI elements here
            }
        });
        builder.show();
    }

    private void showEditTaskDialog(final int taskIndex) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Task");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(toDoList.get(taskIndex));
        builder.setView(input);
        builder.setPositiveButton("Update", (dialog, which) -> {
            toDoList.set(taskIndex, input.getText().toString());
            // Optionally refresh the to-do list dialog or other UI elements here
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }



}
