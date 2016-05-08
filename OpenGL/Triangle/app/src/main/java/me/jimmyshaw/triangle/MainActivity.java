package me.jimmyshaw.triangle;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MainActivity extends AppCompatActivity implements GLSurfaceView.Renderer {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // In order to use OpenGL, it must be added to the Android Manifest. Don't forget.

        // Instantiate the view.
        GLSurfaceView glView = new GLSurfaceView(this);

        // Provide the view with some information to configure the OpenGL state.
        // We'll use OpenGL version 2.
        glView.setEGLContextClientVersion(2);
        // What quality of the output image, in bits, do we want. We're using some typical
        // values used by convention.
        glView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        // What is the source of the thing that's doing the rendering. We add a renderer
        // interface that contains three methods. For convenience, our activity will
        // implement this interface and act as the renderer for our view.
        glView.setRenderer(this);


        setContentView(glView);
    }

    // This is where OpenGL starts up essentially.
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.2f, 0.6f, 0.6f, 1.0f);
    }

    // If our OpenGL view ever gets resized, this method gets called.
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // The view port is the size of the thing we're drawing into. Where in the glView are
        // we rendering to. We could set the thing larger than the view if we wanted by
        // inputting the necessary values. Generally, we tie the view port to the GL surface
        // view so that when the view changes, the view port will change accordingly.
        GLES20.glViewport(0, 0, width, height);
    }

    // Depending on the settings of the OpenGL view, this method will either be called repeatedly
    // and as quickly as it can be or only when we ask it to be called. The default is the
    // former, calling repeatedly and quickly.
    @Override
    public void onDrawFrame(GL10 gl) {
        // What things do we want to clear. We're clearing the color buffer, which is
        // the thing we see. Buffers are bitmaps, essentially pictures.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    }
}
