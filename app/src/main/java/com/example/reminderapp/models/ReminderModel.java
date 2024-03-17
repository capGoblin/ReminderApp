package com.example.reminderapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Date;

public class ReminderModel implements Parcelable {
    private static int id = 0;
    private String title;
    private String content;
    private Date date;

    public ReminderModel(String title, String content, Date date) {
        this.title = title;
        this.content = content;
        this.date = date;
        id++;
    }

    protected ReminderModel(Parcel in) {
        title = in.readString();
        content = in.readString();
        date = new Date(in.readLong());
    }

    public static final Creator<ReminderModel> CREATOR = new Creator<ReminderModel>() {
        @Override
        public ReminderModel createFromParcel(Parcel in) {
            return new ReminderModel(in);
        }

        @Override
        public ReminderModel[] newArray(int size) {
            return new ReminderModel[size];
        }
    };
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(content);
        dest.writeLong(date.getTime());
    }
}
