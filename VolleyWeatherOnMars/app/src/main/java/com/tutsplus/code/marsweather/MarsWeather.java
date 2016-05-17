package com.tutsplus.code.marsweather;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class MarsWeather extends Application {

    public static final String TAG = MarsWeather.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private static MarsWeather mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
    }

    /**
     * Singleton main method. Provides the global static instance of the helper class.
     * @return The MarsWeather instance.
     */
    public static synchronized MarsWeather getInstance() {
        return mInstance;
    }

    /**
     * Provides the general Volley request queue.
     */
    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    /**
     * Adds the request to the general queue.
     * @param req The object Request
     * @param <T> The type of the request result.
     */
    public <T> void add(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    /**
     * Cancels all the pending requests.
     */
    public void cancel() {
        mRequestQueue.cancelAll(TAG);
    }
    
}