package com.whatsappauto.reply.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "auto_replies")
public class AutoReply {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String keyword;
    private String replyMessage;
    private boolean isEnabled;
    private long createdAt;

    public AutoReply(String keyword, String replyMessage) {
        this.keyword = keyword;
        this.replyMessage = replyMessage;
        this.isEnabled = true;
        this.createdAt = System.currentTimeMillis();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getReplyMessage() {
        return replyMessage;
    }

    public void setReplyMessage(String replyMessage) {
        this.replyMessage = replyMessage;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}