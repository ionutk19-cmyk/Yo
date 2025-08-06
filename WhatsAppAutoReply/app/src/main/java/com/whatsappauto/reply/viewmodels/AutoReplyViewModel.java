package com.whatsappauto.reply.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.whatsappauto.reply.database.AppDatabase;
import com.whatsappauto.reply.database.AutoReplyDao;
import com.whatsappauto.reply.models.AutoReply;

import java.util.List;
import java.util.concurrent.Executors;

public class AutoReplyViewModel extends AndroidViewModel {
    private AutoReplyDao autoReplyDao;
    private LiveData<List<AutoReply>> allAutoReplies;

    public AutoReplyViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        autoReplyDao = db.autoReplyDao();
        allAutoReplies = autoReplyDao.getAllAutoReplies();
    }

    public LiveData<List<AutoReply>> getAllAutoReplies() {
        return allAutoReplies;
    }

    public void insert(AutoReply autoReply) {
        Executors.newSingleThreadExecutor().execute(() -> autoReplyDao.insert(autoReply));
    }

    public void update(AutoReply autoReply) {
        Executors.newSingleThreadExecutor().execute(() -> autoReplyDao.update(autoReply));
    }

    public void delete(AutoReply autoReply) {
        Executors.newSingleThreadExecutor().execute(() -> autoReplyDao.delete(autoReply));
    }
}