package me.jimmyshaw.dropbucketlist.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class NotificationService extends IntentService {

    public static final String TAG = "Jim";

    public NotificationService() {
        super("NotificationService");
        Log.d(TAG, "NotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent");
    }

}
