package com.example.reminderapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reminderapp.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface {
    // TODO: create global storage, editReminAct, some db?/animations/theme/ui?
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private RecyclerViewAdapter adapter;
    public ArrayList<ReminderModel> reminders = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    ActivityResultLauncher<Intent> createReminderActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        ReminderModel reminder = data.getParcelableExtra("newReminder");
                        reminders.add(reminder);
                        Log.d("MainActivity", "onActivityResult: " + reminders);
                        adapter.notifyDataSetChanged();
                    }
                }
            });
    private int position;
    @SuppressLint("NotifyDataSetChanged")
    ActivityResultLauncher<Intent> editReminderActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        ReminderModel reminder = data.getParcelableExtra("editedReminder");
                        reminders.set(position, reminder);
                        Log.d("MainActivity", "onActivityEdit: " + reminders);
                        adapter.notifyDataSetChanged();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());

        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        reminders.add(new ReminderModel("Meeting", "Prepare presentation", new Date()));
        reminders.add(new ReminderModel("Call Mom", "Wish her happy birthday", new Date()));
        reminders.add(new ReminderModel("Grocery Shopping", "Buy milk, eggs, and bread", new Date()));

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        adapter = new RecyclerViewAdapter(this, this, reminders);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateReminderActivity.class);
                createReminderActivityResultLauncher.launch(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onEditClick(int position) {
        this.position = position;
        Intent intent = new Intent(MainActivity.this, EditReminderActivity.class);
        intent.putExtra("editReminder", reminders.get(position));
        editReminderActivityResultLauncher.launch(intent);
    }
}