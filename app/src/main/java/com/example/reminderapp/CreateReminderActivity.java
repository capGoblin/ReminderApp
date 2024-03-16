package com.example.reminderapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
//import com.example.reminderapp.ReminderDatabase;
//import com.example.reminderapp.Toaster;

public class CreateReminderActivity extends AppCompatActivity {
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

        Toolbar tb = findViewById(R.id.toolbar2);
        setSupportActionBar(tb);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Create Reminder");



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
        String title = titleEditText.getText().toString();
        String content = contentEditText.getText().toString();
        String dateString = dateEditText.getText().toString();

        if (title.isEmpty() || content.isEmpty() || dateString.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Date date = null;
        String[] formats = new String[] {"dd/MM/yy", "dd/MM/yyyy", "d/M/yy", "d/M/yyyy"};
        for (String format : formats) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
                date = dateFormat.parse(dateString);
                break;
            } catch (ParseException e) {
            }
        }

        if (date == null) {
            Toast.makeText(this, "Invalid date format", Toast.LENGTH_SHORT).show();
            return;
        }

        ReminderModel reminder = new ReminderModel(title, content, date);

        saveReminder(reminder);
    }

    private void saveReminder(ReminderModel reminder) {
        Toast.makeText(this, "Reminder created successfully", Toast.LENGTH_SHORT).show();
        Intent resultIntent = new Intent();
        resultIntent.putExtra("newReminder", reminder);
        setResult(RESULT_OK, resultIntent);
        finish();
        // Optionally, schedule notification
        // NotificationHelper.scheduleNotification(this, reminder);
    }
}
