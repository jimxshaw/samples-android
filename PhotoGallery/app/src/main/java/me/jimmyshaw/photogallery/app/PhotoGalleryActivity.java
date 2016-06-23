package me.jimmyshaw.photogallery.app;

import android.support.v4.app.Fragment;

import me.jimmyshaw.photogallery.fragments.PhotoGalleryFragment;
import me.jimmyshaw.photogallery.fragments.SingleFragmentActivity;

public class PhotoGalleryActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return PhotoGalleryFragment.newInstance();
    }
}
