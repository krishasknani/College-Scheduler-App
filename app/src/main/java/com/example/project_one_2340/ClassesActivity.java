package com.example.project_one_2340;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class ClassesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.classes_activity);
        List<ClassItem> myClassItems = new ArrayList<>();
        myClassItems.add(new ClassItem("CS 3510", "Professor Johnson", "MWF 3:30 PM"));
        myClassItems.add(new ClassItem("MATH 3215", "Professor Ayush", "MWF 12:30 PM"));
        myClassItems.add(new ClassItem("MATH 4000", "Professor Ayush", "MWF 12:30 PM"));

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ClassAdapter adapter = new ClassAdapter(myClassItems); // myClassItems is the list of your class data
        recyclerView.setAdapter(adapter);
    }
}