package me.jimmyshaw.version4;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MainActivity extends AppCompatActivity {

    private GLSurfaceView mGLSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGLSurfaceView = new CustomGLSurfaceView(MainActivity.this);

        setContentView(mGLSurfaceView);
    }

    


    public class CustomGLSurfaceView extends GLSurfaceView {

        private final GLRenderer mGLRenderer;

        public CustomGLSurfaceView(Context context) {
            super(context);
            setEGLContextClientVersion(2);
            mGLRenderer = new GLRenderer();
            setRenderer(mGLRenderer);

        }
    }




    public class GLRenderer implements GLSurfaceView.Renderer {
        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES20.glViewport(0, 0, width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        }
    }
}
