package me.jimmyshaw.version4;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MainActivity extends AppCompatActivity
{

    private GLSurfaceView mGLSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mGLSurfaceView = new CustomGLSurfaceView(MainActivity.this);

        setContentView(mGLSurfaceView);
    }


    public class CustomGLSurfaceView extends GLSurfaceView
    {

        private final GLRenderer mGLRenderer;

        public CustomGLSurfaceView(Context context)
        {
            super(context);
            setEGLContextClientVersion(2);
            mGLRenderer = new GLRenderer();
            setRenderer(mGLRenderer);
            //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        }
    }


    public class GLRenderer implements GLSurfaceView.Renderer
    {

        private Triangle mTriangle;

        private final float[] mMVPMatrix = new float[16];
        private final float[] mViewMatrix = new float[16];
        private final float[] mProjectionMatrix = new float[16];

        private final float[] mRotationMatrix = new float[16];

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config)
        {
            GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);

            mTriangle = new Triangle();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height)
        {
            GLES20.glViewport(0, 0, width, height);
            float ratio = (float) width / height;

            // Defines a projection matrix in terms of six clip planes. Sets the view frustum volume.
            Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
        }

        @Override
        public void onDrawFrame(GL10 gl)
        {
            // Defines a viewing transformation in terms of an eye point, a center of view and an up vector.
            Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
            Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

            //mTriangle.draw(mMVPMatrix);

            float[] tempMatrix = new float[16];
            long time = SystemClock.uptimeMillis() % 4000L;
            float angle = 0.5f * ((int) time);
            Matrix.setRotateM(mRotationMatrix, 0, angle, 0, 0, -1.0f);
            Matrix.multiplyMM(tempMatrix, 0, mMVPMatrix, 0, mRotationMatrix, 0);
            mTriangle.draw(tempMatrix);
        }
    }
}
