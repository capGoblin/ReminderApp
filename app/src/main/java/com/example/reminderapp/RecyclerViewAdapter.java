package com.example.reminderapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    ArrayList<ReminderModel> reminders;
    public RecyclerViewAdapter(RecyclerViewInterface recyclerViewInterface, Context context, ArrayList<ReminderModel> reminders) {
        this.recyclerViewInterface = recyclerViewInterface;
        this.context = context;
        this.reminders = reminders;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row, parent, false);
        return new ViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        ReminderModel reminder = reminders.get(position);
        holder.title.setText(reminder.getTitle());
        holder.content.setText(reminder.getContent());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy EEE", Locale.getDefault());
        holder.date.setText(dateFormat.format(reminder.getDate()));
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
        public ViewHolder(@NonNull View view, RecyclerViewInterface recyclerViewInterface) {
            super(view);
            editButton = view.findViewById(R.id.imageButton);
            title = view.findViewById(R.id.rvTitle);
            content = view.findViewById(R.id.rvContent);
            date = view.findViewById(R.id.rvDate);
            ((ImageButton) view.findViewById(R.id.imageButton)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewInterface.onEditClick(getAdapterPosition());
                }
            });
        }
    }
}
