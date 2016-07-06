package me.jimmyshaw.dropbucketlist.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

// Our app's Notification Service will be disabled whenever the user's device restarts. The service
// needs to be triggered again in the background, without ever forcing the user to re-open our app.
// This broadcast receiver listens for device restarts and upon bootup will trigger the service.
public class BootupReceiver extends BroadcastReceiver {
    public BootupReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
