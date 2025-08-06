package com.whatsappauto.reply.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.whatsappauto.reply.models.ScheduledMessage;

import java.util.List;

@Dao
public interface ScheduledMessageDao {
    @Insert
    void insert(ScheduledMessage scheduledMessage);

    @Update
    void update(ScheduledMessage scheduledMessage);

    @Delete
    void delete(ScheduledMessage scheduledMessage);

    @Query("SELECT * FROM scheduled_messages WHERE isSent = 0 ORDER BY scheduledTime ASC")
    LiveData<List<ScheduledMessage>> getPendingMessages();

    @Query("SELECT * FROM scheduled_messages ORDER BY scheduledTime DESC")
    LiveData<List<ScheduledMessage>> getAllMessages();

    @Query("SELECT * FROM scheduled_messages WHERE scheduledTime <= :currentTime AND isSent = 0")
    List<ScheduledMessage> getMessagesToSend(long currentTime);

    @Query("UPDATE scheduled_messages SET isSent = 1 WHERE id = :id")
    void markAsSent(int id);
}