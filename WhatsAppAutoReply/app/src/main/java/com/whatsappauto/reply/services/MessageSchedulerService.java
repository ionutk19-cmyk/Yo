package com.whatsappauto.reply.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.whatsappauto.reply.database.AppDatabase;
import com.whatsappauto.reply.database.ScheduledMessageDao;
import com.whatsappauto.reply.models.ScheduledMessage;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MessageSchedulerService extends Service {
    private static final String TAG = "MessageScheduler";
    private ScheduledExecutorService scheduler;
    private ScheduledMessageDao messageDao;
    private Handler mainHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        AppDatabase db = AppDatabase.getInstance(this);
        messageDao = db.scheduledMessageDao();
        mainHandler = new Handler(Looper.getMainLooper());
        
        scheduler = Executors.newScheduledThreadPool(1);
        startScheduler();
    }

    private void startScheduler() {
        scheduler.scheduleAtFixedRate(() -> {
            checkAndSendScheduledMessages();
        }, 0, 30, TimeUnit.SECONDS); // Check every 30 seconds
    }

    private void checkAndSendScheduledMessages() {
        long currentTime = System.currentTimeMillis();
        
        Executors.newSingleThreadExecutor().execute(() -> {
            List<ScheduledMessage> messagesToSend = messageDao.getMessagesToSend(currentTime);
            
            for (ScheduledMessage message : messagesToSend) {
                sendScheduledMessage(message);
                messageDao.markAsSent(message.getId());
            }
        });
    }

    private void sendScheduledMessage(ScheduledMessage message) {
        Intent intent = new Intent("com.whatsappauto.reply.SEND_SCHEDULED_MESSAGE");
        intent.putExtra("contact_name", message.getContactName());
        intent.putExtra("message_text", message.getMessage());
        sendBroadcast(intent);
        
        Log.d(TAG, "Scheduled message sent to " + message.getContactName());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (scheduler != null) {
            scheduler.shutdown();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}