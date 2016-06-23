package me.jimmyshaw.photogallery;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/*
    The user, PhotoGalleryFragment, will use this class. It'll need to use some object to identify
    each download and to determine which UI element to update with the image once it's downloaded.
    Rather than locking the user into a specific type of object as the identifier, using a generic
    makes the implementation more flexible.
*/
public class ThumbnailDownloader<T> extends HandlerThread {

    private static final String TAG = "ThumbnailDownloader";
    // This int will be used to identify messages as download requests. ThumbnailDownloader will set
    // this as the what field on any new download messages it creates.
    private static final int MESSAGE_DOWNLOAD = 0;

    private boolean mHasQuit = false;
    // This handler will store a reference to the Handler responsible for queueing download requests
    // as messages on to the ThumbnailDownloader background thread. This handler will also be in
    // charge of processing download request messages when they are pulled off the queue.
    private Handler mRequestHandler;
    // This map is a thread-safe version of hashmap.
    private ConcurrentMap<T, String> mRequestMap = new ConcurrentHashMap<>();

    public ThumbnailDownloader() {
        super(TAG);
    }

    @Override
    public boolean quit() {
        mHasQuit = true;
        return super.quit();
    }

    // PhotoAdapter will call this method in its onBindViewHolder implementation.
    public void queueThumbnail(T target, String url) {
        Log.i(TAG, "Got a URL: " + url);

        if (url == null) {
            mRequestMap.remove(target);
        }
        else {
            mRequestMap.put(target, url);
            mRequestHandler.obtainMessage(MESSAGE_DOWNLOAD, target).sendToTarget();
        }

    }

}
