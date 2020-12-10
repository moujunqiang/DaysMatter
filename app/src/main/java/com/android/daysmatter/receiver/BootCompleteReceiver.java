package com.android.daysmatter.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.android.daysmatter.service.NotificationService;

public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
//        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
//            context.startActivity(new Intent(context, MainActivity.class));
//        }
        Intent service = new Intent(context,NotificationService.class);
        context.startService(service);
    }

}
