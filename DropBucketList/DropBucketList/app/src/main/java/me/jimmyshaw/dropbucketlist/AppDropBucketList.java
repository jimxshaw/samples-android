package me.jimmyshaw.dropbucketlist;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import me.jimmyshaw.dropbucketlist.adapters.Filter;

// This application configuration class is needed to setup Realm's configuration. If we don't
// configure Realm on start up then we'll have to configure Realm every time we want to use it
// in any of our activities or fragments.
// We add the methods to be able to work with shared preferences for the same reasons.
public class AppDropBucketList extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    public static void saveFilterToSharePreferences(Context context, int filterOption) {
        // The difference between getPreferences and getSharedPreferences is the later is meant for
        // multiple shared preferences instances and it forces you to provide a file name to specify
        // the one you want.
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor Editor = sharedPreferences.edit();
        Editor.putInt("Filter", filterOption);
        // We can commit to shared preferences using commit or apply. Apply is asynchronous and is
        // recommended.
        Editor.apply();
    }

    public static int extractFilterFromSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        // Our default filter option would be none.
        int filterOption = sharedPreferences.getInt("Filter", Filter.NONE);

        return filterOption;
    }
}
