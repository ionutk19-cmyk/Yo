package com.whatsappauto.reply.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.whatsappauto.reply.services.MessageSchedulerService;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            // Start the message scheduler service
            Intent serviceIntent = new Intent(context, MessageSchedulerService.class);
            context.startService(serviceIntent);
        }
    }
}