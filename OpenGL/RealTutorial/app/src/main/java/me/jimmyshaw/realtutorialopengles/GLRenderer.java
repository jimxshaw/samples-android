package me.jimmyshaw.realtutorialopengles;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLRenderer implements GLSurfaceView.Renderer
{
    // Our matrices.
    private final float[] mtxProjection = new float[16];
    private final float[] mtxView = new float[16];
    private final float[] mtxProjectionAndView = new float[16];

    // Geometric variables.
    public static float vertices[];
    public static short indices[];
    public FloatBuffer vertexBuffer;
    public ShortBuffer drawListBuffer;

    // Our screen resolution.
    float mScreenWidth = 1280;
    float mScreenHeight = 768;

    // Misc.
    Context mContext;
    long mLastTime;
    int mProgram;

    public GLRenderer(Context c)
    {
        mContext = c;
        mLastTime = System.currentTimeMillis() + 100;
    }

    public void onPause()
    {
        // Do stuff to pause the renderer.
    }

    public void onResume()
    {
        // Do stuff to resume the renderer.
        mLastTime = System.currentTimeMillis();
    }

    public void setupTriangle()
    {
        // These are the vertices of our view.
        vertices = new float[]
                {
                        10.0f, 200f, 0.0f,
                        10.0f, 100f, 0.0f,
                        100f, 100f, 0.0f
                };

        indices = new short[]{0, 1, 2};

        // Vertex buffer.
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        // Initialize a byte buffer for the draw list.
        ByteBuffer dlb = ByteBuffer.allocateDirect(indices.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(indices);
        drawListBuffer.position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {
        // Create the triangle.
        setupTriangle();

        // Set the clear color to black.
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1);

        // Create the shaders.
        int vertexShader = riGraphicTools.loadShader(GLES20.GL_VERTEX_SHADER, riGraphicTools.vs_SolidColor);
        int fragmentShader = riGraphicTools.loadShader(GLES20.GL_FRAGMENT_SHADER, riGraphicTools.fs_SolidColor);

        // Create empty OpenGL ES Program.
        riGraphicTools.sp_SolidColor = GLES20.glCreateProgram();
        // Add the vertex shader to the program.
        GLES20.glAttachShader(riGraphicTools.sp_SolidColor, vertexShader);
        // Add the fragment shader to the program.
        GLES20.glAttachShader(riGraphicTools.sp_SolidColor, fragmentShader);
        // Create the program executables.
        GLES20.glLinkProgram(riGraphicTools.sp_SolidColor);
        // Set our shader program.
        GLES20.glUseProgram(riGraphicTools.sp_SolidColor);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height)
    {
        // We need to know the current width and height.
        mScreenWidth = width;
        mScreenHeight = height;

        // Re-do the Viewport, making it fullscreen.
        GLES20.glViewport(0, 0, (int) mScreenWidth, (int) mScreenHeight);

        // Clear our matrices.
        for (int i = 0; i < 16; i++)
        {
            mtxProjection[i] = 0.0f;
            mtxView[i] = 0.0f;
            mtxProjectionAndView[i] = 0.0f;
        }

        // Setup our screen width and height for normal sprite translation.
        Matrix.orthoM(mtxProjection, 0, 0f, mScreenWidth, 0.0f, mScreenHeight, 0, 50);

        // Set the camera position (View matrix).
        Matrix.setLookAtM(mtxView, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation.
        Matrix.multiplyMM(mtxProjectionAndView, 0, mtxProjection, 0, mtxView, 0);


    }

    @Override
    public void onDrawFrame(GL10 gl)
    {
        // Get the current time.
        long now = System.currentTimeMillis();

        if (mLastTime > now)
        {
            return;
        }

        // Get the amount of time the last frame took.
        long elapsed = now - mLastTime;

        // Update our example.

        // Render our example.
        Render(mtxProjectionAndView);

        // Save the current time to see how long it took <img src="http://androidblog.reindustries.com/wp-includes/images/smilies/icon_smile.gif" alt=":)" class="wp-smiley"> .
        mLastTime = now;
    }

    private void Render(float[] m)
    {
        // Clear screen and depth buffer, we have set the clear color black.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Get handle to vertex shader's vPosition member.
        int mPositionHandle = GLES20.glGetAttribLocation(riGraphicTools.sp_SolidColor, "vPosition");

        // Enable generic vertex attribute array.
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data.
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);

        // Get handle to shape's transformation matrix.
        int mtxHandle = GLES20.glGetUniformLocation(riGraphicTools.sp_SolidColor, "uMVPMatrix");

        // Apply the projection and view transformation.
        GLES20.glUniformMatrix4fv(mtxHandle, 1, false, m, 0);

        // Draw the triangle.
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array.
        GLES20.glDisableVertexAttribArray(mPositionHandle);

    }
}
