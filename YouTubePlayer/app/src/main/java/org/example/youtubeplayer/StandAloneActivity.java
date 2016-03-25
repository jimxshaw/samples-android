package org.example.youtubeplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.android.youtube.player.YouTubeStandalonePlayer;

public class StandAloneActivity extends AppCompatActivity
                                implements View.OnClickListener
{

    // The google api key is given to us after we sign up for its api service.
    private String GOOGLE_API_KEY = "AIzaSyCyiGx1ais9yhfwyCniAnJ4tNwM-W8ZvYI";
    // Every video has an id, an unique identifier.
    private String YOUTUBE_VIDEO_ID = "ObiqJzfyACM";
    private String YOUTUBE_PLAYLIST = "PL8dPuuaLjXtPAJr1ysd5yGIyiSFuh0mIL";

    private Button btnPlayVideo;
    private Button btnPlayPlaylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standalone);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnPlayVideo = (Button) findViewById(R.id.btnPlayVideo);
        btnPlayPlaylist = (Button) findViewById(R.id.btnPlayPlaylist);

        // After we cast the retrieved views to the button type above, we'll set event handlers
        // on them. The set event handlers will take the this keyword, meaning they'll use the on
        // click event method below. That on click method is defined as part of the implementation
        // of the View.OnClickListener interface.
        btnPlayVideo.setOnClickListener(this);
        btnPlayPlaylist.setOnClickListener(this);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        // Android app are a collection of activities. How the user uses one activity to the
        // next is based on intent.
        Intent intent = null;

        // This activity has two buttons, Play Video and Play Playlist. We add a switch statement
        // that captures the id of the button that's pressed and then executes code based on that
        // button.
        switch(v.getId()) {
            case R.id.btnPlayVideo:
                intent = YouTubeStandalonePlayer.createVideoIntent(StandAloneActivity.this, GOOGLE_API_KEY, YOUTUBE_VIDEO_ID);
                break;
            case R.id.btnPlayPlaylist:
                intent = YouTubeStandalonePlayer.createPlaylistIntent(StandAloneActivity.this, GOOGLE_API_KEY, YOUTUBE_PLAYLIST);
                break;
            default:
        }

        // If intent is not null, meaning it has an action assigned to it, we'll execute the
        // activity with said non-null intent as the argument.
        if (intent != null) {
            startActivity(intent);
        }
    }
}
