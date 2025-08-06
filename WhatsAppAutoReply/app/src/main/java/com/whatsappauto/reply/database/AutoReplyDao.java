package com.whatsappauto.reply.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.whatsappauto.reply.models.AutoReply;

import java.util.List;

@Dao
public interface AutoReplyDao {
    @Insert
    void insert(AutoReply autoReply);

    @Update
    void update(AutoReply autoReply);

    @Delete
    void delete(AutoReply autoReply);

    @Query("SELECT * FROM auto_replies WHERE isEnabled = 1 ORDER BY createdAt DESC")
    LiveData<List<AutoReply>> getEnabledAutoReplies();

    @Query("SELECT * FROM auto_replies ORDER BY createdAt DESC")
    LiveData<List<AutoReply>> getAllAutoReplies();

    @Query("SELECT * FROM auto_replies WHERE keyword LIKE '%' || :keyword || '%' AND isEnabled = 1")
    List<AutoReply> findByKeyword(String keyword);
}