package com.example.reminderapp.activities;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.example.reminderapp.receivers.AlarmReceiver;
import com.example.reminderapp.R;
import com.example.reminderapp.interfaces.RecyclerViewInterface;
import com.example.reminderapp.models.ReminderModel;
import com.example.reminderapp.adapters.RecyclerViewAdapter;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.Settings;
import android.util.Log;

import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reminderapp.databinding.ActivityMainBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface {
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private RecyclerViewAdapter adapter;
    private AlarmManager alarmManager;
    public ArrayList<ReminderModel> reminders = new ArrayList<>();
    public static int position;
    boolean hasNotificationPermissionGranted = false;

    @SuppressLint("NotifyDataSetChanged")
    ActivityResultLauncher<Intent> createReminderActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                System.out.println("here1");
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        ReminderModel reminder = data.getParcelableExtra("newReminder");
                        assert reminder != null;
                        System.out.println("here2)");
                        setAlarm(reminder);
                        reminders.add(reminder);
                        Log.d("MainActivity", "onActivityResult: " + reminders);
                        adapter.notifyDataSetChanged();
                    }
                }
            });


    @SuppressLint("NotifyDataSetChanged")
    ActivityResultLauncher<Intent> editReminderActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        ReminderModel reminder = data.getParcelableExtra("editedReminder");
                        assert reminder != null;

                        editAlarm(position, reminder);
                        reminders.set(position, reminder);
                        Log.d("MainActivity", "onActivityEdit: " + reminders);
                        adapter.notifyDataSetChanged();
                    }
                }
            });

    private final ActivityResultLauncher<String> notificationPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                hasNotificationPermissionGranted = isGranted;
                if (!isGranted) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (Build.VERSION.SDK_INT >= 33) {
                            if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                                showNotificationPermissionRationale();
                            } else {
                                showSettingDialog();
                            }
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "notification permission granted", Toast.LENGTH_SHORT).show();
                }
            });

    private void showSettingDialog() {
        new MaterialAlertDialogBuilder(this, com.google.android.material.R.style.MaterialAlertDialog_Material3)
                .setTitle("Notification Permission")
                .setMessage("Notification permission is required, Please allow notification permission from setting")
                .setPositiveButton("Ok", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showNotificationPermissionRationale() {
        new MaterialAlertDialogBuilder(this, com.google.android.material.R.style.MaterialAlertDialog_Material3)
                .setTitle("Alert")
                .setMessage("Notification permission is required, to show notification")
                .setPositiveButton("Ok", (dialog, which) -> {
                    if (Build.VERSION.SDK_INT >= 33) {
                        notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (Build.VERSION.SDK_INT >= 33) {
            notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);
        } else {
            hasNotificationPermissionGranted = true;
        }
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
    private int reqCode = 2;

    @SuppressLint("ScheduleExactAlarm")
    private void setAlarm(@NonNull ReminderModel reminder, boolean isEditAlarmAtPos) {
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
        intent.putExtra("title", reminder.getTitle());
        intent.putExtra("content", reminder.getContent());
        System.out.println("title form reminder" + reminder.getTitle());
        System.out.println("content form reminder" + reminder.getContent());
        PendingIntent pendingIntent = isEditAlarmAtPos ?
                PendingIntent.getBroadcast(MainActivity.this, position, intent, PendingIntent.FLAG_MUTABLE) :
                PendingIntent.getBroadcast(MainActivity.this, reqCode++, intent, PendingIntent.FLAG_MUTABLE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(reminder.getDate().getTime());
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        if (isEditAlarmAtPos) {
            Toast.makeText(this, "Reminder edited successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Reminder set successfully", Toast.LENGTH_SHORT).show();
        }
    }
    private void setAlarm(ReminderModel reminder) {
        setAlarm(reminder, false);
    }
    private void editAlarm(int position, ReminderModel reminder) {
        ReminderModel oldReminder = reminders.get(position);
        cancelAlarm(oldReminder);
        setAlarm(reminder, true);
    }
    private void cancelAlarm(ReminderModel oldReminder) {
        if (alarmManager == null) {
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        }
        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
        intent.putExtra("title", oldReminder.getTitle());
        intent.putExtra("content", oldReminder.getContent());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, reqCode, intent, PendingIntent.FLAG_MUTABLE);
        alarmManager.cancel(pendingIntent);
    }

    @Override
    public void onEditClick(int position) {
        this.position = position;
        Intent intent = new Intent(MainActivity.this, EditReminderActivity.class);
        intent.putExtra("editReminder", reminders.get(position));
        editReminderActivityResultLauncher.launch(intent);
    }
}