package com.example.reminderapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    Context context;
    ArrayList<ReminderModel> reminders;
    public RecyclerViewAdapter(Context context, ArrayList<ReminderModel> reminders) {
        this.context = context;
        this.reminders = reminders;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        ReminderModel reminder = reminders.get(position);
        holder.title.setText(reminder.getTitle());
        holder.content.setText(reminder.getContent());
        holder.date.setText(reminder.getDate().toString());
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageButton editButton;
        TextView title;
        TextView content;
        TextView date;
        public ViewHolder(@NonNull View view) {
            super(view);
            editButton = view.findViewById(R.id.imageButton);
            title = view.findViewById(R.id.rvTitle);
            content = view.findViewById(R.id.rvContent);
            date = view.findViewById(R.id.rvDate);

        }
    }
}
