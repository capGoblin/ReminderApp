package com.example.reminderapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.reminderapp.databinding.ActivityMainBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import com.example.reminderapp.ReminderModel;
//import com.example.reminderapp.ReminderDatabase;
//import com.example.reminderapp.Toaster;

public class CreateReminderActivity extends AppCompatActivity {
    public static ArrayList<ReminderModel> reminders = new ArrayList<>();
    private EditText titleEditText;
    private EditText contentEditText;
    private EditText dateEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_reminder);


        titleEditText = findViewById(R.id.title);
        contentEditText = findViewById(R.id.content);
        dateEditText = findViewById(R.id.date);


        ((Button)findViewById(R.id.button2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createReminder();
            }
        });
    }

    private void createReminder() {
        // Get the data entered by the user
        String title = titleEditText.getText().toString();
        String content = contentEditText.getText().toString();
        String dateString = dateEditText.getText().toString();

        // Validate input
        if (title.isEmpty() || content.isEmpty() || dateString.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert date string to Date object
        Date date = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Invalid date format", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create the reminder object
        ReminderModel reminder = new ReminderModel(title, content, date);

        // Save the reminder (You'll need to implement this method)
        saveReminder(reminder);
    }

    // Method to save the reminder to database or other storage
    private void saveReminder(ReminderModel reminder) {
        // Implement this method to save the reminder to database or other storage
        // Show confirmation message
        reminders.add(reminder);
        Toast.makeText(this, "Reminder created successfully", Toast.LENGTH_SHORT).show();
        finish();
        // Optionally, schedule notification
        // NotificationHelper.scheduleNotification(this, reminder);
    }
}
