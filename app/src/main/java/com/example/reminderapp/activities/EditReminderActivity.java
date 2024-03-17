package com.example.reminderapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.reminderapp.R;
import com.example.reminderapp.models.ReminderModel;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class EditReminderActivity extends AppCompatActivity {
    private EditText titleEditText;
    private EditText contentEditText;
    private DatePicker datePicker;
    private TimePicker timePicker;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reminder);

        Toolbar tb = findViewById(R.id.eToolbar2);
        setSupportActionBar(tb);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Edit Reminder");

        titleEditText = findViewById(R.id.eTitle);
        contentEditText = findViewById(R.id.eContent);

        datePicker = findViewById(R.id.eDatePicker);
        timePicker = findViewById(R.id.eTimePicker);

        ReminderModel reminder = getIntent().getParcelableExtra("editReminder");
        if(reminder != null) {
            titleEditText.setText(reminder.getTitle());
            contentEditText.setText(reminder.getContent());
            setDateAndTimePicker(reminder.getDate());
        }

        (findViewById(R.id.eButton2)).setOnClickListener(v -> editReminder());
    }

    private void setDateAndTimePicker(Date reminderDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(reminderDate);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        datePicker.updateDate(year, month, dayOfMonth);

        timePicker.setHour(hourOfDay);
        timePicker.setMinute(minute);
    }

    private void editReminder() {
        String title = titleEditText.getText().toString();
        String content = contentEditText.getText().toString();

        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int dayOfMonth = datePicker.getDayOfMonth();

        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth, hour, minute);

        Date selectedDate = calendar.getTime();

        if (title.isEmpty() || content.isEmpty()) {
            return;
        }

        ReminderModel reminder = new ReminderModel(title, content, selectedDate);
        Toast.makeText(this, "Reminder edited successfully", Toast.LENGTH_SHORT).show();
        Intent resultIntent = new Intent();
        resultIntent.putExtra("editedReminder", reminder);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
