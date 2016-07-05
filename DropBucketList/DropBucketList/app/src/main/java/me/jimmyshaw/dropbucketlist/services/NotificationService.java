package me.jimmyshaw.dropbucketlist.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import io.realm.Realm;
import io.realm.RealmResults;
import me.jimmyshaw.dropbucketlist.models.Drop;


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

        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            // Query realm and find all our incomplete goals. We don't need to use the findAllAsync
            // method because we're already in a background thread with this service.
            RealmResults<Drop> results = realm.where(Drop.class).equalTo("completed", false).findAll();

            for (Drop currentGoal : results) {
                if (isNotificationNeeded(currentGoal.getDateAdded(), currentGoal.getDateDue())) {
                    Log.d(TAG, "onHandleIntent: notification needed");
                }
            }
        }
        finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    private boolean isNotificationNeeded(long dateAdded, long dateDue) {
        long currentTime = System.currentTimeMillis();

        if (currentTime > dateDue) {
            // If we're already passed a goal's due date then it doesn't make sense send notification.
            return false;
        }
        else {
            // We'd like to notify the user when 90% of the time to reach the due date has passed.
            long ninetyPercentDifference = (long) (0.90 * (dateDue - dateAdded));

            // If the current time has surpassed the time when the goal was added plus 90% of the
            // due date then return true, otherwise return false.
            return (currentTime > (dateAdded + ninetyPercentDifference)) ? true : false;
        }

    }

}
