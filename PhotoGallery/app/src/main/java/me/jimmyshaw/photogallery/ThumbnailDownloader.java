package me.jimmyshaw.photogallery;

import android.os.HandlerThread;
import android.util.Log;

/*
    The user, PhotoGalleryFragment, will use this class. It'll need to use some object to identify
    each download and to determine which UI element to update with the image once it's downloaded.
    Rather than locking the user into a specific type of object as the identifier, using a generic
    makes the implementation more flexible.
*/
public class ThumbnailDownloader<T> extends HandlerThread {

    private static final String TAG = "ThumbnailDownloader";

    private boolean mHasQuit = false;

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
    }

}
