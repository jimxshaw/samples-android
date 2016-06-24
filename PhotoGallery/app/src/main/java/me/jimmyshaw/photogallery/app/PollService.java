package me.jimmyshaw.photogallery.app;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/*
    Services are like activities in that they are contexts and responds to intents. A service's intents
    are called commands. Each command is an instruction to the service to do something. An intent
    service pulls its commands from a queue. When it receives a first command, it fires up a
    background thread and puts the command on a queue. The intent service processes each command in
    sequence. New commands go to the back of the queue. When there are no commands left in the queue,
    the services stops and is destroyed.
*/
public class PollService extends IntentService {

    private static final String TAG = "PollService";

    public static Intent newIntent(Context context) {
        return new Intent(context, PollService.class);
    }

    public PollService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Received an intent: " + intent);
    }
}
