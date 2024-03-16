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

public class EditReminderActivity extends AppCompatActivity {
    private EditText titleEditText;
    private EditText contentEditText;
    private EditText dateEditText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reminder);

        Toolbar tb = findViewById(R.id.eToolbar2);
        setSupportActionBar(tb);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Edit Reminder");

        titleEditText = findViewById(R.id.eTitle);
        contentEditText = findViewById(R.id.eContent);
        dateEditText = findViewById(R.id.eDate);

        ReminderModel reminder = getIntent().getParcelableExtra("editReminder");
        if(reminder != null) {
            titleEditText.setText(reminder.getTitle());
            contentEditText.setText(reminder.getContent());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            dateEditText.setText(dateFormat.format(reminder.getDate()));
        }

        ((Button)findViewById(R.id.eButton2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editReminder();
            }
        });
    }

    private void editReminder() {
        String title = titleEditText.getText().toString();
        String content = contentEditText.getText().toString();
        String dateString = dateEditText.getText().toString();

        if (title.isEmpty() || content.isEmpty() || dateString.isEmpty()) {
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
        Toast.makeText(this, "Reminder edited successfully", Toast.LENGTH_SHORT).show();
        Intent resultIntent = new Intent();
        resultIntent.putExtra("editedReminder", reminder);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
