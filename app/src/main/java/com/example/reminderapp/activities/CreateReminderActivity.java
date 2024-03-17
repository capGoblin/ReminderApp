package com.example.reminderapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.reminderapp.R;
import com.example.reminderapp.models.ReminderModel;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class CreateReminderActivity extends AppCompatActivity {
    private EditText titleEditText;
    private EditText contentEditText;
    private DatePicker datePicker;
    private TimePicker timePicker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_reminder);

        setSupportActionBar(findViewById(R.id.toolbar2));
        Objects.requireNonNull(getSupportActionBar()).setTitle("Create Reminder");

        titleEditText = findViewById(R.id.title);
        contentEditText = findViewById(R.id.content);
        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);


        findViewById(R.id.button2).setOnClickListener(v -> createReminder());
    }

    private void createReminder() {
        String title = titleEditText.getText().toString();
        String content = contentEditText.getText().toString();

        Date selectedDate = getDate();

        ReminderModel reminder = new ReminderModel(title, content, selectedDate);
        System.out.println("selectedDate" + selectedDate);

        saveReminder(reminder);
    }

    @NonNull
    private Date getDate() {
        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int dayOfMonth = datePicker.getDayOfMonth();

        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth, hour, minute);

        return calendar.getTime();
    }

    private void saveReminder(ReminderModel reminder) {
        Toast.makeText(this, "Reminder created successfully", Toast.LENGTH_SHORT).show();
        Intent resultIntent = new Intent();
        resultIntent.putExtra("newReminder", reminder);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
