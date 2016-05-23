package me.jimmyshaw.realtutorialopengles;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.view.MotionEvent;

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
    // A rect is just a container holding 4 boundaries: left, right, top, bottom.
    // It's all we need to keep track of our 2D textured quad.
    public Rect image;

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
        // Initial rect.
        image = new Rect();
        image.left = 10;
        image.right = 100;
        image.top = 200;
        image.bottom = 100;

        // These are the vertices of our view.
        vertices = new float[]
                {
                        10.0f, 200f, 0.0f,
                        10.0f, 100f, 0.0f,
                        100f, 100f, 0.0f,
                        100f, 200f, 0.0f,
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
        int id = mContext.getResources().getIdentifier("drawable/box_texture", null, mContext.getPackageName());

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

    public void translateSprite()
    {
        // In this method we create our vertices array again with the values from the rect called
        // image. In order to render the textured quad at the rect's position, we should recreate
        // the float buffers whenever OpenGL needs and uses those values for the rendering process.
        vertices = new float[]
                {
                        image.left, image.top, 0.0f,
                        image.left, image.bottom, 0.0f,
                        image.right, image.bottom, 0.0f,
                        image.right, image.top, 0.0f

                };

        // The vertex buffer.
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
    }

    public void processTouchEvent(MotionEvent event)
    {
        // This method is being passed a MotionEvent from our GLSurf class. Depending on
        // where the touch event is with respect to the halfs of the width and height of
        // the device screen, the box will move accordingly.

        // Get half of the screen value.
        int screenWidthHalf = (int) (mScreenWidth / 2);
        int screenHeightHalf = (int) (mScreenHeight / 2);

        if (event.getX() < screenWidthHalf)
        {
            image.left -= 10;
            image.right -= 10;
        }
        else
        {
            image.left += 10;
            image.right += 10;
        }

        if (event.getY() > screenHeightHalf)
        {
            image.top -= 10;
            image.bottom -= 10;
        }
        else
        {
            image.top += 10;
            image.bottom += 10;
        }

        // Update the new data.
        translateSprite();
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
//        // Clear screen and depth buffer, we have set the clear color black.
//        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
//
//        // Get handle to vertex shader's vPosition member. To pass our vertex data to our shader, we
//        // need the location of the position variable of our vertex shader.
//        int mPositionHandle = GLES20.glGetAttribLocation(riGraphicTools.sp_SolidColor, "vPosition");
//
//        // Enable generic vertex attribute array.
//        GLES20.glEnableVertexAttribArray(mPositionHandle);
//
//        // Prepare the triangle coordinate data.
//        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, vertexBuffer);
//
//        // Get handle to shape's transformation matrix.
//        int mtxHandle = GLES20.glGetUniformLocation(riGraphicTools.sp_SolidColor, "uMVPMatrix");
//
//        // Apply the projection and view transformation.
//        GLES20.glUniformMatrix4fv(mtxHandle, 1, false, m, 0);
//
//        // Draw the triangle.
//        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);
//
//        // Disable vertex array.
//        GLES20.glDisableVertexAttribArray(mPositionHandle);


        // Clear the screen and depth buffer so there's no accumulation. We have set the clear
        // color as black.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Get handle to vertex shader's vPosition member.
        int mPositionHandle = GLES20.glGetAttribLocation(riGraphicTools.sp_Image, "vPosition");

        // Enable generic vertex attribute array.
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data.
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, vertexBuffer);

        // Get handle to texture coordinates location.
        int mTexCoordLoc = GLES20.glGetAttribLocation(riGraphicTools.sp_Image, "a_texCoord");

        // Enable generic vertex attribute array.
        GLES20.glEnableVertexAttribArray(mTexCoordLoc);

        // Prepare the texture coordinates.
        GLES20.glVertexAttribPointer(mTexCoordLoc, 2, GLES20.GL_FLOAT, false, 0, uvBuffer);

        // Get handle to shape's transformation matrix.
        int mtxHandle = GLES20.glGetUniformLocation(riGraphicTools.sp_Image, "uMVPMatrix");

        // Apply the projection and view transformation.
        GLES20.glUniformMatrix4fv(mtxHandle, 1, false, m, 0);

        // Get handle to textures locations.
        int mSamplerLoc = GLES20.glGetUniformLocation(riGraphicTools.sp_Image, "s_texture");

        // Set the sampler texture unit to 0, where we have saved the texture.
        GLES20.glUniform1i(mSamplerLoc, 0);

        // Draw the triangle.
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array.
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTexCoordLoc);

    }


    // A sprite is a 2D image or animation that is integrated into a larger scene. Our images are
    // exactly that so we'll hold all the information about our image in a class called Sprite.
    // In computer graphics, order is important when translating, scaling and rotating. For example,
    // rotating is always done about an origin. What origin though? In our case, we'll do the
    // transformation each frame from the base position. The order we use is scaling, rotating
    // and translating. The result is an image being scaled, rotated around its own midpoint and then
    // translated to the correct location.
    public class Sprite
    {
        float angle;
        float scale;
        RectF base;
        PointF translation;

        public Sprite()
        {
            // Initialize our initial size around the 0,0 point.
            base = new RectF(-50f, 50f, 50f, -50f);

            // Initial translation.
            translation = new PointF(50f, -50f);

            // We start with out initial size.
            scale = 1f;

            // We start in our initial angle.
            angle = 0f;
        }

        public void scale(float deltas)
        {
            scale += deltas;
        }

        public void rotate(float deltaa)
        {
            angle += deltaa;
        }

        public void translate(float deltax, float deltay)
        {
            // Update our location.
            translation.x += deltax;
            translation.y += deltay;
        }

        public float[] getTransformedVertices()
        {
            // Start with scaling.
            float x1 = base.left * scale;
            float x2 = base.right * scale;
            float y1 = base.bottom * scale;
            float y2 = base.bottom * scale;

            // We detach from our Rect because when rotating, we need separate points. We do so in
            // OpenGL order.
            PointF one = new PointF(x1, y2);
            PointF two = new PointF(x1, y1);
            PointF three = new PointF(x2, y1);
            PointF four = new PointF(x2, y2);

            // We create the sin and cos functions once so we don't have to calculate
            // them each time.
            float sin = (float) Math.sin(angle);
            float cos = (float) Math.cos(angle);

            // Then we rotate each point.
            one.x = x1 * cos - y2 * sin;
            one.y = x1 * sin + y2 * cos;
            two.x = x1 * cos - y1 * sin;
            two.y = x1 * sin + y1 * cos;
            three.x = x2 * cos - y1 * sin;
            three.y = x2 * sin + y1 * cos;
            four.x = x2 * cos - y2 * sin;
            four.y = x2 * sin + y2 * cos;

            // Finally we translate the sprite to its correct position.
            one.x += translation.x;
            one.y += translation.y;
            two.x += translation.x;
            two.y += translation.y;
            three.x += translation.x;
            three.y += translation.y;
            four.x += translation.x;
            four.y += translation.y;

            // We return our float array of vertices.
            return new float[]
                    {
                            one.x, one.y, 0.0f,
                            two.x, two.y, 0.0f,
                            three.x, three.y, 0.0f,
                            four.x, four.y, 0.0f
                    };
        }
    }
}
