package me.jimmyshaw.dropbucketlist;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

// This application configuration class is needed to setup Realm's configuration. If we don't
// configure Realm on start up then we'll have to configure Realm every time we want to use it
// in any of our activities or fragments.
public class AppDropBucketList extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
