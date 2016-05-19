package me.jimmyshaw.realtutorialopengles;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity
{

    // Our OpenGL surface view.
    private GLSurfaceView glSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Turn off the window's title bar.
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        // Fullscreen mode.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // We create our SurfaceView for our OpenGL here.
        glSurfaceView = new GLSurf(this);

        setContentView(R.layout.activity_main);

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.gamelayout);

        // Attach our surface view to our relative layout from our main layout.
        RelativeLayout.LayoutParams glParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        layout.addView(glSurfaceView, glParams);

    }

    @Override
    public void onPause()
    {
        super.onPause();
        glSurfaceView.onPause();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        glSurfaceView.onResume();
    }
}
