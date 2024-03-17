package com.example.reminderapp.activities;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.reminderapp.receivers.AlarmReceiver;
import com.example.reminderapp.R;
import com.example.reminderapp.interfaces.RecyclerViewInterface;
import com.example.reminderapp.models.ReminderModel;
import com.example.reminderapp.adapters.RecyclerViewAdapter;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;

import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reminderapp.databinding.ActivityMainBinding;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface {
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
                        assert reminder != null;
                        setAlarm(reminder);
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
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        createNotificationChannel();

        setSupportActionBar(binding.toolbar);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        populateInitialData();
        adapter = new RecyclerViewAdapter(this, this, reminders);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        binding.fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CreateReminderActivity.class);
            createReminderActivityResultLauncher.launch(intent);
        });
    }

    private void populateInitialData() {
        reminders.add(new ReminderModel("Meeting", "Prepare presentation", new Date()));
        reminders.add(new ReminderModel("Call Mom", "Wish her happy birthday", new Date()));
        reminders.add(new ReminderModel("Grocery Shopping", "Buy milk, eggs, and bread", new Date()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateEditButtonStates();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateEditButtonStates() {
        adapter.notifyDataSetChanged();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("reminder_channel", "Reminder Channel", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("This is the reminder channel");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private int reqCode = 0;

    @SuppressLint("ScheduleExactAlarm")
    private void setAlarm(ReminderModel reminder) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
        intent.putExtra("title", reminder.getTitle());
        intent.putExtra("content", reminder.getContent());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, reqCode++, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(reminder.getDate().getTime());
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        Toast.makeText(this, "Reminder set successfully", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onEditClick(int position) {
        this.position = position;
        Intent intent = new Intent(MainActivity.this, EditReminderActivity.class);
        intent.putExtra("editReminder", reminders.get(position));
        editReminderActivityResultLauncher.launch(intent);
    }
}