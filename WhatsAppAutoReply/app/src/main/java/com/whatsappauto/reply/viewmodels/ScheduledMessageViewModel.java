package com.whatsappauto.reply.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.whatsappauto.reply.database.AppDatabase;
import com.whatsappauto.reply.database.ScheduledMessageDao;
import com.whatsappauto.reply.models.ScheduledMessage;

import java.util.List;
import java.util.concurrent.Executors;

public class ScheduledMessageViewModel extends AndroidViewModel {
    private ScheduledMessageDao messageDao;
    private LiveData<List<ScheduledMessage>> allMessages;
    private LiveData<List<ScheduledMessage>> pendingMessages;

    public ScheduledMessageViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        messageDao = db.scheduledMessageDao();
        allMessages = messageDao.getAllMessages();
        pendingMessages = messageDao.getPendingMessages();
    }

    public LiveData<List<ScheduledMessage>> getAllMessages() {
        return allMessages;
    }

    public LiveData<List<ScheduledMessage>> getPendingMessages() {
        return pendingMessages;
    }

    public void insert(ScheduledMessage message) {
        Executors.newSingleThreadExecutor().execute(() -> messageDao.insert(message));
    }

    public void update(ScheduledMessage message) {
        Executors.newSingleThreadExecutor().execute(() -> messageDao.update(message));
    }

    public void delete(ScheduledMessage message) {
        Executors.newSingleThreadExecutor().execute(() -> messageDao.delete(message));
    }
}