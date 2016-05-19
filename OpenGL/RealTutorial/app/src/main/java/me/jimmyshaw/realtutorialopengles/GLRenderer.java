package me.jimmyshaw.realtutorialopengles;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
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
    public static float uvs[];
    public FloatBuffer vertexBuffer;
    public ShortBuffer drawListBuffer;
    public FloatBuffer uvBuffer;

    // Our screen resolution.
    float mScreenWidth = 1920;
    float mScreenHeight = 1080;

    // Misc.
    Context mContext;
    long mLastTime;
    int mProgram;
    private static final int COORDS_PER_VERTEX = 3;

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
                        100f, 100f, 0.0f,
                        100f, 200f, 0.0f
                };

        // The order of vertex rendering.
        indices = new short[]{0, 1, 2, 0, 2, 3};

        // Vertex buffer.
        // Number of coordinate values * 4 bytes per float type.
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
        // Use the device hardware's native byte order.
        bb.order(ByteOrder.nativeOrder());
        // Create a floating point buffer from the ByteBuffer.
        vertexBuffer = bb.asFloatBuffer();
        // Add the coordinates to the FloatBuffer.
        vertexBuffer.put(vertices);
        // Set the buffer to read the first coordinate.
        vertexBuffer.position(0);

        // Initialize a byte buffer for the draw list.
        // Number of coordinate values * 2 bytes per short type.
        ByteBuffer dlb = ByteBuffer.allocateDirect(indices.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(indices);
        drawListBuffer.position(0);
    }

    public void setupImage()
    {
        // Create our UV coordinates.
        uvs = new float[]
                {
                        0.0f, 0.0f,
                        0.0f, 1.0f,
                        1.0f, 1.0f,
                        1.0f, 0.0f
                };

        // The texture buffer.
        ByteBuffer bb = ByteBuffer.allocateDirect(uvs.length * 4);
        bb.order(ByteOrder.nativeOrder());
        uvBuffer = bb.asFloatBuffer();
        uvBuffer.put(uvs);
        uvBuffer.position(0);

        // Generate textures by creating a texture object. If more are needed, alter these numbers.
        int[] textureNames = new int[1];
        GLES20.glGenTextures(1, textureNames, 0);

        // Retrieve our image from resources.
        int id = mContext.getResources().getIdentifier("drawable/ic_launcher", null, mContext.getPackageName());

        // Temporarily create a bitmap.
        Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), id);

        // Bind texture to textureName.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureNames[0]);

        // Set filtering. The texture resolution is often not the same as our resulting screen
        // resolution so we need to set a filter to let OpenGL know what to do when it needs to
        // resize the texture. Popular filters are GL_NEAREST, which takes the closest pixels. It's
        // fast but has visual downsides or GL_LINEAR, which calculates the pixel with taking its
        // neighbors into account. It's slower but better looking visually.
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        // Set wrapping mode. For OpenGL's texture coordinate system, s is x and t is y. Popular
        // wrapping modes are GL_REPEAT, which works great with POT (powers of two) textures or
        // GL_CLAMP_TO_EDGES, which works great with NPOT (non-powers of two) textures. Since our
        // texture is 1, which is a NPOT, we use GL_CLAMP_TO_EDGES. We can use GL_REPEAT but it
        // is not guaranteed to work.
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);

        // We are done using the bitmap so we should recycle it.
        bmp.recycle();

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {
        // Create the triangle.
        setupTriangle();

        // Create the image information.
        setupImage();

        // Set the clear color to black. Every time OpenGL clears our screen for a new drawsession,
        // it clears our screen to that specified color.
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1);

        // Create the shaders, solid color.
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

        // Create the shaders, images.
        vertexShader = riGraphicTools.loadShader(GLES20.GL_VERTEX_SHADER, riGraphicTools.vs_Image);
        fragmentShader = riGraphicTools.loadShader(GLES20.GL_FRAGMENT_SHADER, riGraphicTools.fs_Image);
        riGraphicTools.sp_Image = GLES20.glCreateProgram();
        GLES20.glAttachShader(riGraphicTools.sp_Image, vertexShader);
        GLES20.glAttachShader(riGraphicTools.sp_Image, fragmentShader);
        GLES20.glLinkProgram(riGraphicTools.sp_Image);
        GLES20.glUseProgram(riGraphicTools.sp_Image);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height)
    {
        // We have to take into account what happens when our surface changes. For example, when
        // we rotate our phone and our surface rotates with it. This method is used for
        // those occasions.

        // We need to know the current width and height.
        mScreenWidth = width;
        mScreenHeight = height;

        // Re-do the Viewport, making it fullscreen. We have to re-do it every time the surface changes.
        // Our viewport changes therefore all our matrices are no longer valid. Hence we have to
        // clear them and fill them again.
        GLES20.glViewport(0, 0, (int) mScreenWidth, (int) mScreenHeight);

        // Clear our matrices.
        for (int i = 0; i < 16; i++)
        {
            mtxProjection[i] = 0.0f;
            mtxView[i] = 0.0f;
            mtxProjectionAndView[i] = 0.0f;
        }

        // Setup our screen width and height for normal sprite translation. The orthoM method is used
        // so that we wouldn't have any need for a perspective view of our scene, as we have no depth.
        Matrix.orthoM(mtxProjection, 0, 0f, mScreenWidth, 0.0f, mScreenHeight, 0, 50);

        // Set the camera position (View matrix).
        Matrix.setLookAtM(mtxView, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation.
        Matrix.multiplyMM(mtxProjectionAndView, 0, mtxProjection, 0, mtxView, 0);


    }

    @Override
    public void onDrawFrame(GL10 gl)
    {
        // This method is called over and over again.

        // Get the current time. We need to calculate how long the previous frame took.
        // This value can also be used for animations and the sort.
        long now = System.currentTimeMillis();

        if (mLastTime > now)
        {
            return;
        }

        // Get the amount of time the last frame took.
        long elapsed = now - mLastTime;

        // Update our example. This app doesn't have any game logic but if it did then update the
        // logic before calling render.

        // Render our example.
        Render(mtxProjectionAndView);

        // Save the current time to see how long it took <img src="http://androidblog.reindustries.com/wp-includes/images/smilies/icon_smile.gif" alt=":)" class="wp-smiley"> .
        mLastTime = now;
    }

    private void Render(float[] m)
    {
        // Clear screen and depth buffer, we have set the clear color black.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Get handle to vertex shader's vPosition member. To pass our vertex data to our shader, we
        // need the location of the position variable of our vertex shader.
        int mPositionHandle = GLES20.glGetAttribLocation(riGraphicTools.sp_SolidColor, "vPosition");

        // Enable generic vertex attribute array.
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data.
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, vertexBuffer);

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
