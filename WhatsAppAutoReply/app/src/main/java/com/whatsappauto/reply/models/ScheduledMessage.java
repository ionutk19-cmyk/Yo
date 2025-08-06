package com.whatsappauto.reply.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "scheduled_messages")
public class ScheduledMessage {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String contactName;
    private String message;
    private long scheduledTime;
    private boolean isSent;
    private long createdAt;

    public ScheduledMessage(String contactName, String message, long scheduledTime) {
        this.contactName = contactName;
        this.message = message;
        this.scheduledTime = scheduledTime;
        this.isSent = false;
        this.createdAt = System.currentTimeMillis();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(long scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}