package com.whatsappauto.reply.services;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import com.whatsappauto.reply.database.AppDatabase;
import com.whatsappauto.reply.database.AutoReplyDao;
import com.whatsappauto.reply.models.AutoReply;
import com.whatsappauto.reply.utils.WhatsAppHelper;

import java.util.List;
import java.util.concurrent.Executors;

public class WhatsAppAccessibilityService extends AccessibilityService {
    private static final String TAG = "WhatsAppAutoReply";
    private static final String WHATSAPP_PACKAGE = "com.whatsapp";
    
    private AutoReplyDao autoReplyDao;
    private String lastMessageSender = "";
    private String lastMessageText = "";

    @Override
    public void onCreate() {
        super.onCreate();
        AppDatabase db = AppDatabase.getInstance(this);
        autoReplyDao = db.autoReplyDao();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getPackageName() == null || !event.getPackageName().toString().equals(WHATSAPP_PACKAGE)) {
            return;
        }

        switch (event.getEventType()) {
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                handleNotification(event);
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                handleWindowContentChanged();
                break;
        }
    }

    private void handleNotification(AccessibilityEvent event) {
        Notification notification = (Notification) event.getParcelableData();
        if (notification != null) {
            Bundle extras = notification.extras;
            String title = extras.getString(Notification.EXTRA_TITLE);
            String text = extras.getString(Notification.EXTRA_TEXT);
            
            if (title != null && text != null) {
                checkAndReply(title, text, notification);
            }
        }
    }

    private void handleWindowContentChanged() {
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        if (rootNode == null) return;

        // Extract messages from the chat window
        List<AccessibilityNodeInfo> messageNodes = rootNode.findAccessibilityNodeInfosByViewId("com.whatsapp:id/conversation_text_row");
        
        for (AccessibilityNodeInfo messageNode : messageNodes) {
            if (messageNode.getText() != null) {
                String messageText = messageNode.getText().toString();
                // Process the message
                processIncomingMessage(messageText);
            }
        }
    }

    private void processIncomingMessage(String messageText) {
        if (!messageText.equals(lastMessageText)) {
            lastMessageText = messageText;
            
            Executors.newSingleThreadExecutor().execute(() -> {
                List<AutoReply> autoReplies = autoReplyDao.findByKeyword(messageText);
                if (!autoReplies.isEmpty()) {
                    AutoReply autoReply = autoReplies.get(0);
                    sendReply(autoReply.getReplyMessage());
                }
            });
        }
    }

    private void checkAndReply(String sender, String message, Notification notification) {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<AutoReply> autoReplies = autoReplyDao.findByKeyword(message);
            if (!autoReplies.isEmpty()) {
                AutoReply autoReply = autoReplies.get(0);
                
                // Open the notification
                try {
                    PendingIntent pendingIntent = notification.contentIntent;
                    if (pendingIntent != null) {
                        pendingIntent.send();
                        Thread.sleep(1000); // Wait for WhatsApp to open
                        
                        // Send the reply
                        sendReply(autoReply.getReplyMessage());
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error sending reply", e);
                }
            }
        });
    }

    private void sendReply(String replyMessage) {
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        if (rootNode == null) return;

        // Find the message input field
        List<AccessibilityNodeInfo> messageFields = rootNode.findAccessibilityNodeInfosByViewId("com.whatsapp:id/entry");
        
        if (!messageFields.isEmpty()) {
            AccessibilityNodeInfo messageField = messageFields.get(0);
            
            // Set the text
            Bundle arguments = new Bundle();
            arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, replyMessage);
            messageField.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
            
            // Find and click the send button
            List<AccessibilityNodeInfo> sendButtons = rootNode.findAccessibilityNodeInfosByViewId("com.whatsapp:id/send");
            if (!sendButtons.isEmpty()) {
                sendButtons.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
    }

    public void sendScheduledMessage(String contactName, String message) {
        // This method will be called by the scheduler service
        WhatsAppHelper.openWhatsAppChat(this, contactName);
        
        // Wait for WhatsApp to open
        try {
            Thread.sleep(2000);
            sendReply(message);
        } catch (InterruptedException e) {
            Log.e(TAG, "Error in scheduled message", e);
        }
    }

    @Override
    public void onInterrupt() {
        Log.d(TAG, "Accessibility Service Interrupted");
    }
}