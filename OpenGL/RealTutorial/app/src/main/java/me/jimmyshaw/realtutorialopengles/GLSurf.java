package me.jimmyshaw.realtutorialopengles;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class GLSurf extends GLSurfaceView
{
    // Our surface view needs a renderer, otherwise it cannot render anything
    // on the surface. The renderer is responsible for updating and rendering our game.
    private final GLRenderer mRenderer;

    public GLSurf(Context context)
    {
        super(context);

        // Create an OpenGL ES 2.0 context. It's version 2.
        setEGLContextClientVersion(2);

        // Set the Renderer for drawing on the GLSurfaceView.
        mRenderer = new GLRenderer(context);
        setRenderer(mRenderer);

        // There are two render modes: CONTINUOUSLY means the render loop is done over and over,
        // no matter what. WHEN_DIRTY means that the render function is only called when something
        // has changed.
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public void onPause()
    {
        // TODO: auto-generated method stub.
        super.onPause();
        mRenderer.onPause();
    }

    @Override
    public void onResume()
    {
        // TODO: auto-generated method stub.
        super.onResume();
        mRenderer.onResume();
    }
}
