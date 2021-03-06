//package me.jimmyshaw.realtutorialopengles;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.PointF;
//import android.graphics.Rect;
//import android.graphics.RectF;
//import android.opengl.GLES20;
//import android.opengl.GLSurfaceView;
//import android.opengl.GLUtils;
//import android.opengl.Matrix;
//import android.view.MotionEvent;
//
//import java.nio.ByteBuffer;
//import java.nio.ByteOrder;
//import java.nio.FloatBuffer;
//import java.nio.IntBuffer;
//import java.nio.ShortBuffer;
//import java.util.Random;
//
//import javax.microedition.khronos.egl.EGLConfig;
//import javax.microedition.khronos.opengles.GL10;
//
//public class GLRenderer implements GLSurfaceView.Renderer
//{
//    // Our matrices.
//    private final float[] mtxProjection = new float[16];
//    private final float[] mtxView = new float[16];
//    private final float[] mtxProjectionAndView = new float[16];
//
//    // Geometric variables.
//    public static float vertices[];
//    public static short indices[];
//    public static float uvs[];
//    public FloatBuffer vertexBuffer;
//    public ShortBuffer drawListBuffer;
//    public FloatBuffer uvBuffer;
//    // A rect is just a container holding 4 boundaries: left, right, top, bottom.
//    // It's all we need to keep track of our 2D textured quad.
//    //public Rect image;
//
//    // A Rect isn't needed when we have a Sprite.
//    public Sprite sprite;
//
//    // Our screen resolution.
//    float mScreenWidth = 1920;
//    float mScreenHeight = 1080;
//
//    // Misc.
//    Context mContext;
//    long mLastTime;
//    int mProgram;
//    private static final int COORDS_PER_VERTEX = 3;
//
//    public TextManager mTextManager;
//
//    // Scaling factor for various screens.
//    float ssu = 1.0f; // universal screen scaling.
//    float ssx = 1.0f; // screen scaling x axis.
//    float ssy = 1.0f; // screen scaling y axis.
//    float swp = 320.0f; // scaling width pixel.
//    float shp = 480.0f; // scaling height pixel.
//
//    public GLRenderer(Context c)
//    {
//        mContext = c;
//        mLastTime = System.currentTimeMillis() + 100;
//        sprite = new Sprite();
//    }
//
//    public void onPause()
//    {
//        // Do stuff to pause the renderer.
//    }
//
//    public void onResume()
//    {
//        // Do stuff to resume the renderer.
//        mLastTime = System.currentTimeMillis();
//    }
//
//    public void setupScaling()
//    {
//        // The screen resolution.
//        swp = mContext.getResources().getDisplayMetrics().widthPixels;
//        shp = mContext.getResources().getDisplayMetrics().heightPixels;
//
//        // Orientation is assumed to be portrait.
//        ssx = swp / 320.0f;
//        ssy = shp / 480.0f;
//
//        // Get our uniform scaler.
//        if (ssx > ssy)
//        {
//            ssu = ssy;
//        }
//        else
//        {
//            ssu = ssx;
//        }
//    }
//
//    public void setupTriangle()
//    {
//        // We will need a randomizer.
//        Random rng = new Random();
//
//        // Our collection of vertices. We create the vertices for the 30 textured quads.
//        // That's 30 textured quads x 4 vertices x 3 components (x, y, z) of floats to be able
//        // to store that information.
//        vertices = new float[30 * 4 * 3];
//
//        // Create the vertex data. We want to randomize the locations of the textured quads so we get
//        // a random offset. With the ssu value we can ensure a squared texture at the
//        // specified locations.
//        for (int i = 0; i < 30; i++)
//        {
//            int offset_x = rng.nextInt((int) swp);
//            int offset_y = rng.nextInt((int) shp);
//
//            // Create the 2D parts of our 3D vertices, others are default to 0.0f.
//            vertices[(i * 12) + 0] = offset_x;
//            vertices[(i * 12) + 1] = offset_y + (30.0f * ssu);
//            vertices[(i * 12) + 2] = 0f;
//            vertices[(i * 12) + 3] = offset_x;
//            vertices[(i * 12) + 4] = offset_y;
//            vertices[(i * 12) + 5] = 0f;
//            vertices[(i * 12) + 6] = offset_x + (30.0f * ssu);
//            vertices[(i * 12) + 7] = offset_y;
//            vertices[(i * 12) + 8] = 0f;
//            vertices[(i * 12) + 9] = offset_x + (30.0f * ssu);
//            vertices[(i * 12) + 10] = offset_y + (30.0f * ssu);
//            vertices[(i * 12) + 11] = 0f;
//        }
//
//        // Indices for all textured quads. The indices tell us what vertices to use to build
//        // up a triangle. Just a set of {0,1,2,0,2,3} doesn't work anymore because we have more
//        // vertices now. So we loop through all the quads and create the correct indices. We
//        // store the last index so that we can use it as the base of our next iteration. This way
//        // we have a correct list of indices for all vertices.
//        indices = new short[30 * 6];
//        int last = 0;
//        for (int i = 0; i < 30; i++)
//        {
//            // Set the new indices for the new quad.
//            indices[(i * 6) + 0] = (short) (last + 0);
//            indices[(i * 6) + 1] = (short) (last + 1);
//            indices[(i * 6) + 2] = (short) (last + 2);
//            indices[(i * 6) + 3] = (short) (last + 0);
//            indices[(i * 6) + 4] = (short) (last + 2);
//            indices[(i * 6) + 5] = (short) (last + 3);
//
//            // Our indices are connected to the vertices so we need to keep them in the correct order.
//            // a normal quad = 0,1,2,0,2,3 so the next quad would be 4,5,6,4,6,7.
//            last = last + 4;
//        }
//
//        // Vertex buffer.
//        // Number of coordinate values * 4 bytes per float type.
//        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
//        // Use the device hardware's native byte order.
//        bb.order(ByteOrder.nativeOrder());
//        // Create a floating point buffer from the ByteBuffer.
//        vertexBuffer = bb.asFloatBuffer();
//        // Add the coordinates to the FloatBuffer.
//        vertexBuffer.put(vertices);
//        // Set the buffer to read the first coordinate.
//        vertexBuffer.position(0);
//
//        // Initialize a byte buffer for the draw list.
//        // Number of coordinate values * 2 bytes per short type.
//        ByteBuffer dlb = ByteBuffer.allocateDirect(indices.length * 2);
//        dlb.order(ByteOrder.nativeOrder());
//        drawListBuffer = dlb.asShortBuffer();
//        drawListBuffer.put(indices);
//        drawListBuffer.position(0);
//    }
//
//    public void setupImage()
//    {
//        // We will use a randomizer for randomizing the textures from the texture atlas.
//        Random rng = new Random();
//
//        // 30 image objects x 4 vertices x (u & v). We need to create a new float array for
//        // which we need memory space equal to the amount of 30 quads, 4 vertices per quad and per
//        // vertex we need a U and V float value.
//        uvs = new float[30 * 4 * 2];
//
//        // We'll make 30 randomly textured objects.
//        for (int i = 0; i < 30; i++)
//        {
//            int random_u_offset = rng.nextInt(2);
//            int random_v_offset = rng.nextInt(2);
//
//            // Adding the UVs using the offsets. The texture atlas has 4 textures on it. With the
//            // random offset we choose a random texture from the atlas.
//            uvs[(i * 8) + 0] = random_u_offset * 0.5f;
//            uvs[(i * 8) + 1] = random_v_offset * 0.5f;
//            uvs[(i * 8) + 2] = random_u_offset * 0.5f;
//            uvs[(i * 8) + 3] = (random_v_offset + 1) * 0.5f;
//            uvs[(i * 8) + 4] = (random_u_offset + 1) * 0.5f;
//            uvs[(i * 8) + 5] = (random_v_offset + 1) * 0.5f;
//            uvs[(i * 8) + 6] = (random_u_offset + 1) * 0.5f;
//            uvs[(i * 8) + 7] = random_v_offset * 0.5f;
//        }
//
//        // The texture buffer.
//        ByteBuffer bb = ByteBuffer.allocateDirect(uvs.length * 4);
//        bb.order(ByteOrder.nativeOrder());
//        uvBuffer = bb.asFloatBuffer();
//        uvBuffer.put(uvs);
//        uvBuffer.position(0);
//
//        // Generate textures by creating a texture object. If more are needed, alter these numbers.
//        int[] textureNames = new int[2];
//        GLES20.glGenTextures(2, textureNames, 0);
//
//        // Retrieve our image from resources.
//        int id = mContext.getResources().getIdentifier("drawable/texture_atlas", null, mContext.getPackageName());
//
//        // Temporarily create a bitmap.
//        Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), id);
//
//        // Bind texture to textureName.
//        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
//        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureNames[0]);
//
//        // Set filtering. The texture resolution is often not the same as our resulting screen
//        // resolution so we need to set a filter to let OpenGL know what to do when it needs to
//        // resize the texture. Popular filters are GL_NEAREST, which takes the closest pixels. It's
//        // fast but has visual downsides or GL_LINEAR, which calculates the pixel with taking its
//        // neighbors into account. It's slower but better looking visually.
//        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
//        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
//
//        // Set wrapping mode. For OpenGL's texture coordinate system, s is x and t is y. Popular
//        // wrapping modes are GL_REPEAT, which works great with POT (powers of two) textures or
//        // GL_CLAMP_TO_EDGES, which works great with NPOT (non-powers of two) textures. Since our
//        // texture is 1, which is a NPOT, we use GL_CLAMP_TO_EDGES. We can use GL_REPEAT but it
//        // is not guaranteed to work.
//        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
//        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
//
//        // Load the bitmap into the bound texture.
//        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);
//
//        // We are done using the bitmap so we should recycle it.
//        bmp.recycle();
//
//        // This is for the text texture.
//        id = mContext.getResources().getIdentifier("drawable/font", null, mContext.getPackageName());
//        bmp = BitmapFactory.decodeResource(mContext.getResources(), id);
//        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + 1);
//        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureNames[1]);
//        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
//        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
//        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);
//        bmp.recycle();
//
//    }
//
//    public void setupText()
//    {
//        // Create our text manager.
//        mTextManager = new TextManager();
//
//        // Tell our text manager to use index 1 of the textures that have been loaded.
//        mTextManager.setTextureID(1);
//
//        // Pass the uniform scale.
//        mTextManager.setUniformScale(ssu);
//
//        // Create our new text object.
//        TextObject textObject = new TextObject("Hello OpenGL", 10f, 10f);
//
//        // Add the text to our manager.
//        mTextManager.addText(textObject);
//
//        // Prepare the text for rendering.
//        mTextManager.prepareDraw();
//    }
//
//    public void updateSprite()
//    {
//        // Get the new transformed vertices.
//        vertices = sprite.getTransformedVertices();
//
//        // The vertex buffer.
//        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
//        bb.order(ByteOrder.nativeOrder());
//        vertexBuffer = bb.asFloatBuffer();
//        vertexBuffer.put(vertices);
//        vertexBuffer.position(0);
//    }
//
//    public void processTouchEvent(MotionEvent event)
//    {
//        // This method is being passed a MotionEvent from our GLSurf class.
//        // Our screen is divided in to a 2x3 grid of touch areas. Each area has its
//        // own functionality: rotating, scaling and translating the sprite in both directions.
//
//        // Get half of the screen value.
//        int screenHalf = (int) (mScreenWidth / 2);
//        int screenHeightPart = (int) (mScreenHeight / 3);
//
//        if (event.getX() < screenHalf)
//        {
//            // Left screen touch
//            if (event.getY() < screenHeightPart)
//            {
//                sprite.scale(-0.01f);
//            }
//            else if (event.getY() < (screenHeightPart * 2))
//            {
//                sprite.translate(-10f * ssu, -10f * ssu);
//            }
//            else
//            {
//                sprite.rotate(0.01f);
//            }
//        }
//        else
//        {
//            // Right screen touch
//            if (event.getY() < screenHeightPart)
//            {
//                sprite.scale(0.01f);
//            }
//            else if (event.getY() < (screenHeightPart * 2))
//            {
//                sprite.translate(10f * ssu, 10f * ssu);
//            }
//            else
//            {
//                sprite.rotate(-0.01f);
//            }
//        }
//
//    }
//
//    @Override
//    public void onSurfaceCreated(GL10 gl, EGLConfig config)
//    {
//        // Setup our scaling system.
//        setupScaling();
//
//        // Create the triangle.
//        setupTriangle();
//
//        // Create the image information.
//        setupImage();
//
//        // Create the text.
//        setupText();
//
//        // Set the clear color to black. Every time OpenGL clears our screen for a new drawsession,
//        // it clears our screen to that specified color.
//        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1);
//
//        // OpenGL by default doesn't blend anything. It copies an image over the surface. To change
//        // this, we enable blending and then tell OpenGL how it should blend. The constants we
//        // specify are the scaling factors used to multiple the source color and the destination color.
//        // GL_ONE specifies we take the incoming fragment color and multiply it by (1,1,1,1) which
//        // means take the pixel as is. The second parameter tells OpenGL that we do not want the
//        // alpha of the source taken into account for the destination pixel which is factor
//        // (1-AlphaSource,1-AlphaSource,1-AlphaSource,1-AlphaSource). Both resulting colors are
//        // then added up together and is then written back into the pixelbuffer.
//        GLES20.glEnable(GLES20.GL_BLEND);
//        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
//
//        // Create the shaders, solid color.
//        int vertexShader = riGraphicTools.loadShader(GLES20.GL_VERTEX_SHADER, riGraphicTools.vs_SolidColor);
//        int fragmentShader = riGraphicTools.loadShader(GLES20.GL_FRAGMENT_SHADER, riGraphicTools.fs_SolidColor);
//
//        // Create empty OpenGL ES Program.
//        riGraphicTools.sp_SolidColor = GLES20.glCreateProgram();
//        // Add the vertex shader to the program.
//        GLES20.glAttachShader(riGraphicTools.sp_SolidColor, vertexShader);
//        // Add the fragment shader to the program.
//        GLES20.glAttachShader(riGraphicTools.sp_SolidColor, fragmentShader);
//        // Create the program executables.
//        GLES20.glLinkProgram(riGraphicTools.sp_SolidColor);
//        // Set our shader program.
//        GLES20.glUseProgram(riGraphicTools.sp_SolidColor);
//
//        // Create the shaders, images.
//        vertexShader = riGraphicTools.loadShader(GLES20.GL_VERTEX_SHADER, riGraphicTools.vs_Image);
//        fragmentShader = riGraphicTools.loadShader(GLES20.GL_FRAGMENT_SHADER, riGraphicTools.fs_Image);
//        riGraphicTools.sp_Image = GLES20.glCreateProgram();
//        GLES20.glAttachShader(riGraphicTools.sp_Image, vertexShader);
//        GLES20.glAttachShader(riGraphicTools.sp_Image, fragmentShader);
//        GLES20.glLinkProgram(riGraphicTools.sp_Image);
//        GLES20.glUseProgram(riGraphicTools.sp_Image);
//
//    }
//
//    @Override
//    public void onSurfaceChanged(GL10 gl, int width, int height)
//    {
//        // We have to take into account what happens when our surface changes. For example, when
//        // we rotate our phone and our surface rotates with it. This method is used for
//        // those occasions.
//
//        // We need to know the current width and height.
//        mScreenWidth = width;
//        mScreenHeight = height;
//
//        // Re-do the Viewport, making it fullscreen. We have to re-do it every time the surface changes.
//        // Our viewport changes therefore all our matrices are no longer valid. Hence we have to
//        // clear them and fill them again.
//        GLES20.glViewport(0, 0, (int) mScreenWidth, (int) mScreenHeight);
//
//        // Clear our matrices.
//        for (int i = 0; i < 16; i++)
//        {
//            mtxProjection[i] = 0.0f;
//            mtxView[i] = 0.0f;
//            mtxProjectionAndView[i] = 0.0f;
//        }
//
//        // Setup our screen width and height for normal sprite translation. The orthoM method is used
//        // so that we wouldn't have any need for a perspective view of our scene, as we have no depth.
//        Matrix.orthoM(mtxProjection, 0, 0f, mScreenWidth, 0.0f, mScreenHeight, 0, 50);
//
//        // Set the camera position (View matrix).
//        Matrix.setLookAtM(mtxView, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
//
//        // Calculate the projection and view transformation.
//        Matrix.multiplyMM(mtxProjectionAndView, 0, mtxProjection, 0, mtxView, 0);
//
//
//    }
//
//    @Override
//    public void onDrawFrame(GL10 gl)
//    {
//        // This method is called over and over again.
//
//        // Get the current time. We need to calculate how long the previous frame took.
//        // This value can also be used for animations and the sort.
//        long now = System.currentTimeMillis();
//
//        if (mLastTime > now)
//        {
//            return;
//        }
//
//        // Get the amount of time the last frame took.
//        long elapsed = now - mLastTime;
//
//        // Update our example.
//        //updateSprite();
//
//        // Render our example.
//        render(mtxProjectionAndView);
//
//        // Render the text.
//        if (mTextManager != null)
//        {
//            mTextManager.draw(mtxProjectionAndView);
//        }
//
//        // Save the current time to see how long it took <img src="http://androidblog.reindustries.com/wp-includes/images/smilies/icon_smile.gif" alt=":)" class="wp-smiley"> .
//        mLastTime = now;
//    }
//
//    private void render(float[] m)
//    {
//        // Set our shader program to the image shader. This is because the TextManager class sets
//        // the current shader program to the text shader program. It needs to be set back to the
//        // image shader program as the first thing after Render is called.
//        GLES20.glUseProgram(riGraphicTools.sp_Image);
//
//        // Clear the screen and depth buffer so there's no accumulation. We have set the clear
//        // color as black.
//        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
//
//        // Get handle to vertex shader's vPosition member.
//        int mPositionHandle = GLES20.glGetAttribLocation(riGraphicTools.sp_Image, "vPosition");
//
//        // Enable generic vertex attribute array.
//        GLES20.glEnableVertexAttribArray(mPositionHandle);
//
//        // Prepare the triangle coordinate data.
//        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, vertexBuffer);
//
//        // Get handle to texture coordinates location.
//        int mTexCoordLoc = GLES20.glGetAttribLocation(riGraphicTools.sp_Image, "a_texCoord");
//
//        // Enable generic vertex attribute array.
//        GLES20.glEnableVertexAttribArray(mTexCoordLoc);
//
//        // Prepare the texture coordinates.
//        GLES20.glVertexAttribPointer(mTexCoordLoc, 2, GLES20.GL_FLOAT, false, 0, uvBuffer);
//
//        // Get handle to shape's transformation matrix.
//        int mtxHandle = GLES20.glGetUniformLocation(riGraphicTools.sp_Image, "uMVPMatrix");
//
//        // Apply the projection and view transformation.
//        GLES20.glUniformMatrix4fv(mtxHandle, 1, false, m, 0);
//
//        // Get handle to textures locations.
//        int mSamplerLoc = GLES20.glGetUniformLocation(riGraphicTools.sp_Image, "s_texture");
//
//        // Set the sampler texture unit to 0, where we have saved the texture.
//        GLES20.glUniform1i(mSamplerLoc, 0);
//
//        // Draw the triangle.
//        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);
//
//        // Disable vertex array.
//        GLES20.glDisableVertexAttribArray(mPositionHandle);
//        GLES20.glDisableVertexAttribArray(mTexCoordLoc);
//
//    }
//
//
//    // A sprite is a 2D image or animation that is integrated into a larger scene. Our images are
//    // exactly that so we'll hold all the information about our image in a class called Sprite.
//    // In computer graphics, order is important when translating, scaling and rotating. For example,
//    // rotating is always done about an origin. What origin though? In our case, we'll do the
//    // transformation each frame from the base position. The order we use is scaling, rotating
//    // and translating. The result is an image being scaled, rotated around its own midpoint and then
//    // translated to the correct location.
//    public class Sprite
//    {
//        // Our sprite always has a base. This is the original position that we place
//        // around our origin.
//        RectF base;
//        // From our base, we need values to scale, rotate (angle) and translate.
//        float scale;
//        float angle;
//        PointF translation;
//
//        public Sprite()
//        {
//            // Our origin, initialize our initial size around the 0,0 point. We multiple the base
//            // coordinates withour global scale value. This way we keep local aspect ratio but make
//            // the rectangle look the same size relative to the screen size on all devices.
//            base = new RectF(-50f * ssu, 50f * ssu, 50f * ssu, -50f * ssu);
//
//            // Initial translation.
//            // Because our image is 100 pixels by pixels in size and we have placed it
//            // around the origin, we start our translation 50 pixels in both directions so that
//            // our image is fully on our screen on the left bottom.
//            translation = new PointF(50f * ssu, 50f * ssu);
//
//            // We start with out initial size. A scale factor of 1 means that it's at normal size.
//            scale = 1f;
//
//            // We start in our initial rotation angle at zero.
//            angle = 0f;
//        }
//
//        public void scale(float deltas)
//        {
//            scale += deltas;
//        }
//
//        public void rotate(float deltaa)
//        {
//            angle += deltaa;
//        }
//
//        public void translate(float deltax, float deltay)
//        {
//            // Update our location.
//            translation.x += deltax;
//            translation.y += deltay;
//        }
//
//        public float[] getTransformedVertices()
//        {
//            // Start with scaling. We pass in a delta scale value to the scale method. The actual
//            // scaling is done in this method. Because we have our base around the origin (0,0),
//            // we can simply multiple the vertex locations with the scaling factor to get a scaled sprite.
//            float x1 = base.left * scale;
//            float x2 = base.right * scale;
//            float y1 = base.bottom * scale;
//            float y2 = base.top * scale;
//
//            // We detach from our Rect because when rotating, we need separate points. We do so in
//            // OpenGL order.
//            // We rotate the sprite after scaling it. Rotating a point P around the origin O can be
//            // done using the following formula (point R is the resulting point and α is the angle
//            // of rotation).
//            // Rx = cos(α) * (Px - Ox) - sin(α) * (Py - Oy) + Ox
//            // Ry = sin(α) * (Px - Ox) + cos(α) * (Py - Oy) + Oy
//            // Since our point O is at (0,0), we can simplify the formula by filling in zeros.
//            // Rx = cos(α) * Px - sin(α) * Py
//            // Ry = sin(α) * Px + cos(α) * Py
//            // Before rotation can occur we have to create all four points from our Rect. We apply
//            // the formula on all the points.
//            PointF one = new PointF(x1, y2);
//            PointF two = new PointF(x1, y1);
//            PointF three = new PointF(x2, y1);
//            PointF four = new PointF(x2, y2);
//
//            // We create the sin and cos functions once so we don't have to calculate
//            // them each time.
//            float sin = (float) Math.sin(angle);
//            float cos = (float) Math.cos(angle);
//
//            // Then we rotate each point. We now have rotated the vertices around the sprite
//            // origin (0,0).
//            one.x = x1 * cos - y2 * sin;
//            one.y = x1 * sin + y2 * cos;
//            two.x = x1 * cos - y1 * sin;
//            two.y = x1 * sin + y1 * cos;
//            three.x = x2 * cos - y1 * sin;
//            three.y = x2 * sin + y1 * cos;
//            four.x = x2 * cos - y2 * sin;
//            four.y = x2 * sin + y2 * cos;
//
//            // Finally we translate the sprite to its correct position by adding the x and y
//            // translation offset to our scale and rotated points.
//            one.x += translation.x;
//            one.y += translation.y;
//            two.x += translation.x;
//            two.y += translation.y;
//            three.x += translation.x;
//            three.y += translation.y;
//            four.x += translation.x;
//            four.y += translation.y;
//
//            // We return a float array of vertices with all the transformed vertices that we
//            // can use in our render process.
//            return new float[]
//                    {
//                            one.x, one.y, 0.0f,
//                            two.x, two.y, 0.0f,
//                            three.x, three.y, 0.0f,
//                            four.x, four.y, 0.0f
//                    };
//        }
//    }
//}

package me.jimmyshaw.realtutorialopengles;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.RectF;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.view.MotionEvent;

import me.jimmyshaw.realtutorialopengles.TextManager;
import me.jimmyshaw.realtutorialopengles.TextObject;
import me.jimmyshaw.realtutorialopengles.riGraphicTools;

public class GLRenderer implements Renderer {

    // Our matrices
    private final float[] mtrxProjection = new float[16];
    private final float[] mtrxView = new float[16];
    private final float[] mtrxProjectionAndView = new float[16];

    // Geometric variables
    public static float vertices[];
    public static short indices[];
    public static float uvs[];
    public FloatBuffer vertexBuffer;
    public ShortBuffer drawListBuffer;
    public FloatBuffer uvBuffer;
    public Sprite sprite;
    public TextManager tm;
    // Our screenresolution
    float	mScreenWidth = 1280;
    float	mScreenHeight = 768;
    float 	ssu = 1.0f;
    float 	ssx = 1.0f;
    float 	ssy = 1.0f;
    float 	swp = 320.0f;
    float 	shp = 480.0f;

    // Misc
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
		/* Do stuff to pause the renderer */
    }

    public void onResume()
    {
		/* Do stuff to resume the renderer */
        mLastTime = System.currentTimeMillis();
    }

    @Override
    public void onDrawFrame(GL10 unused) {

        // Get the current time
        long now = System.currentTimeMillis();

        // We should make sure we are valid and sane
        if (mLastTime > now) return;

        // Get the amount of time the last frame took.
        long elapsed = now - mLastTime;

        // Update our example
        //UpdateSprite();

        // Render our example
        Render(mtrxProjectionAndView);

        // Render the text
        if(tm!=null)
            tm.Draw(mtrxProjectionAndView);

        // Save the current time to see how long it took :).
        mLastTime = now;

    }

    private void Render(float[] m) {
        // Set our shaderprogram to image shader
        GLES20.glUseProgram(riGraphicTools.sp_Image);

        // clear Screen and Depth Buffer, we have set the clear color as black.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        // get handle to vertex shader's vPosition member and add vertices
        int mPositionHandle = GLES20.glGetAttribLocation(riGraphicTools.sp_Image, "vPosition");
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Get handle to texture coordinates location and load the texture uvs
        int mTexCoordLoc = GLES20.glGetAttribLocation(riGraphicTools.sp_Image, "a_texCoord" );
        GLES20.glVertexAttribPointer ( mTexCoordLoc, 2, GLES20.GL_FLOAT, false, 0, uvBuffer);
        GLES20.glEnableVertexAttribArray ( mTexCoordLoc );

        // Get handle to shape's transformation matrix and add our matrix
        int mtrxhandle = GLES20.glGetUniformLocation(riGraphicTools.sp_Image, "uMVPMatrix");
        GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, m, 0);

        // Get handle to textures locations
        int mSamplerLoc = GLES20.glGetUniformLocation (riGraphicTools.sp_Image, "s_texture" );

        // Set the sampler texture unit to 0, where we have saved the texture.
        GLES20.glUniform1i ( mSamplerLoc, 0);

        // Draw the triangle
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTexCoordLoc);

    }


    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        // We need to know the current width and height.
        mScreenWidth = width;
        mScreenHeight = height;

        // Redo the Viewport, making it fullscreen.
        GLES20.glViewport(0, 0, (int)mScreenWidth, (int)mScreenHeight);

        // Clear our matrices
        for(int i=0;i<16;i++)
        {
            mtrxProjection[i] = 0.0f;
            mtrxView[i] = 0.0f;
            mtrxProjectionAndView[i] = 0.0f;
        }

        // Setup our screen width and height for normal sprite translation.
        Matrix.orthoM(mtrxProjection, 0, 0f, mScreenWidth, 0.0f, mScreenHeight, 0, 50);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mtrxView, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mtrxProjectionAndView, 0, mtrxProjection, 0, mtrxView, 0);

        // Setup our scaling system
        SetupScaling();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        // Setup our scaling system
        SetupScaling();
        // Create the triangles
        SetupTriangle();
        // Create the image information
        SetupImage();
        // Create our texts
        SetupText();

        // Set the clear color to black
        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 1);

        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        // Create the shaders, images
        int vertexShader = riGraphicTools.loadShader(GLES20.GL_VERTEX_SHADER, riGraphicTools.vs_Image);
        int fragmentShader = riGraphicTools.loadShader(GLES20.GL_FRAGMENT_SHADER, riGraphicTools.fs_Image);

        riGraphicTools.sp_Image = GLES20.glCreateProgram();             // create empty OpenGL ES Program
        GLES20.glAttachShader(riGraphicTools.sp_Image, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(riGraphicTools.sp_Image, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(riGraphicTools.sp_Image);                  // creates OpenGL ES program executables

        // Text shader
        int vshadert = riGraphicTools.loadShader(GLES20.GL_VERTEX_SHADER, riGraphicTools.vs_Text);
        int fshadert = riGraphicTools.loadShader(GLES20.GL_FRAGMENT_SHADER, riGraphicTools.fs_Text);

        riGraphicTools.sp_Text = GLES20.glCreateProgram();
        GLES20.glAttachShader(riGraphicTools.sp_Text, vshadert);
        GLES20.glAttachShader(riGraphicTools.sp_Text, fshadert); 		// add the fragment shader to program
        GLES20.glLinkProgram(riGraphicTools.sp_Text);                  // creates OpenGL ES program executables

        // Set our shader programm
        GLES20.glUseProgram(riGraphicTools.sp_Image);
    }

    public void SetupText()
    {
        // Create our text manager
        tm = new TextManager();

        // Tell our text manager to use index 1 of textures loaded
        tm.setTextureID(1);

        // Pass the uniform scale
        tm.setUniformscale(ssu);

        // Create our new textobject
        TextObject txt = new TextObject("hello world", 10f, 10f);

        // Add it to our manager
        tm.addText(txt);

        // Prepare the text for rendering
        tm.PrepareDraw();
    }

    public void SetupScaling()
    {
        // The screen resolutions
        swp = (int) (mContext.getResources().getDisplayMetrics().widthPixels);
        shp = (int) (mContext.getResources().getDisplayMetrics().heightPixels);

        // Orientation is assumed portrait
        ssx = swp / 320.0f;
        ssy = shp / 480.0f;

        // Get our uniform scaler
        if(ssx > ssy)
            ssu = ssy;
        else
            ssu = ssx;
    }

    public void processTouchEvent(MotionEvent event)
    {
        // Get the half of screen value
        int screenhalf = (int) (mScreenWidth / 2);
        int screenheightpart = (int) (mScreenHeight / 3);
        if(event.getX()<screenhalf)
        {
            // Left screen touch
            if(event.getY() < screenheightpart)
                sprite.scale(-0.01f);
            else if(event.getY() < (screenheightpart*2))
                sprite.translate(-10f*ssu, -10f*ssu);
            else
                sprite.rotate(0.01f);
        }
        else
        {
            // Right screen touch
            if(event.getY() < screenheightpart)
                sprite.scale(0.01f);
            else if(event.getY() < (screenheightpart*2))
                sprite.translate(10f*ssu, 10f*ssu);
            else
                sprite.rotate(-0.01f);
        }
    }

    public void UpdateSprite()
    {
        // Get new transformed vertices
        //vertices = sprite.getTransformedVertices();

        // The vertex buffer.
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
    }


    public void SetupImage()
    {
        // We will use a randomizer for randomizing the textures from texture atlas.
        // This is strictly optional as it only effects the output of our app,
        // Not the actual knowledge.
        Random rnd = new Random();

        // 30 imageobjects times 4 vertices times (u and v)
        uvs = new float[30*4*2];

        // We will make 30 randomly textures objects
        for(int i=0; i<30; i++)
        {
            int random_u_offset = rnd.nextInt(2);
            int random_v_offset = rnd.nextInt(2);

            // Adding the UV's using the offsets
            uvs[(i*8) + 0] = random_u_offset * 0.5f;
            uvs[(i*8) + 1] = random_v_offset * 0.5f;
            uvs[(i*8) + 2] = random_u_offset * 0.5f;
            uvs[(i*8) + 3] = (random_v_offset+1) * 0.5f;
            uvs[(i*8) + 4] = (random_u_offset+1) * 0.5f;
            uvs[(i*8) + 5] = (random_v_offset+1) * 0.5f;
            uvs[(i*8) + 6] = (random_u_offset+1) * 0.5f;
            uvs[(i*8) + 7] = random_v_offset * 0.5f;
        }

        // The texture buffer
        ByteBuffer bb = ByteBuffer.allocateDirect(uvs.length * 4);
        bb.order(ByteOrder.nativeOrder());
        uvBuffer = bb.asFloatBuffer();
        uvBuffer.put(uvs);
        uvBuffer.position(0);

        // Generate Textures, if more needed, alter these numbers.
        int[] texturenames = new int[2];
        GLES20.glGenTextures(2, texturenames, 0);

        // Retrieve our image from resources.
        int id = mContext.getResources().getIdentifier("drawable/texture_atlas", null, mContext.getPackageName());

        // Temporary create a bitmap
        Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), id);

        // Bind texture to texturename
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturenames[0]);

        // Set filtering
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);

        // We are done using the bitmap so we should recycle it.
        bmp.recycle();

        // Again for the text texture
        id = mContext.getResources().getIdentifier("drawable/font", null, mContext.getPackageName());
        bmp = BitmapFactory.decodeResource(mContext.getResources(), id);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + 1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturenames[1]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);
        bmp.recycle();
    }

    public void SetupTriangle()
    {
        // We will need a randomizer
        Random rnd = new Random();

        // Our collection of vertices
        vertices = new float[30*4*3];

        // Create the vertex data
        for(int i=0;i<30;i++)
        {
            int offset_x = rnd.nextInt((int)swp);
            int offset_y = rnd.nextInt((int)shp);

            // Create the 2D parts of our 3D vertices, others are default 0.0f
            vertices[(i*12) + 0] = offset_x;
            vertices[(i*12) + 1] = offset_y + (30.0f*ssu);
            vertices[(i*12) + 2] = 0f;
            vertices[(i*12) + 3] = offset_x;
            vertices[(i*12) + 4] = offset_y;
            vertices[(i*12) + 5] = 0f;
            vertices[(i*12) + 6] = offset_x + (30.0f*ssu);
            vertices[(i*12) + 7] = offset_y;
            vertices[(i*12) + 8] = 0f;
            vertices[(i*12) + 9] = offset_x + (30.0f*ssu);
            vertices[(i*12) + 10] = offset_y + (30.0f*ssu);
            vertices[(i*12) + 11] = 0f;
        }

        // The indices for all textured quads
        indices = new short[30*6];
        int last = 0;
        for(int i=0;i<30;i++)
        {
            // We need to set the new indices for the new quad
            indices[(i*6) + 0] = (short) (last + 0);
            indices[(i*6) + 1] = (short) (last + 1);
            indices[(i*6) + 2] = (short) (last + 2);
            indices[(i*6) + 3] = (short) (last + 0);
            indices[(i*6) + 4] = (short) (last + 2);
            indices[(i*6) + 5] = (short) (last + 3);

            // Our indices are connected to the vertices so we need to keep them
            // in the correct order.
            // normal quad = 0,1,2,0,2,3 so the next one will be 4,5,6,4,6,7
            last = last + 4;
        }

        // The vertex buffer.
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(indices.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(indices);
        drawListBuffer.position(0);
    }

    class Sprite
    {
        float angle;
        float scale;
        RectF base;
        PointF translation;

        public Sprite()
        {
            // Initialise our intital size around the 0,0 point
            base = new RectF(-50f*ssu,50f*ssu, 50f*ssu, -50f*ssu);

            // Initial translation
            translation = new PointF(50f*ssu,50f*ssu);

            // We start with our inital size
            scale = 1f;

            // We start in our inital angle
            angle = 0f;
        }


        public void translate(float deltax, float deltay)
        {
            // Update our location.
            translation.x += deltax;
            translation.y += deltay;
        }

        public void scale(float deltas)
        {
            scale += deltas;
        }

        public void rotate(float deltaa)
        {
            angle += deltaa;
        }

        public float[] getTransformedVertices()
        {
            // Start with scaling
            float x1 = base.left * scale;
            float x2 = base.right * scale;
            float y1 = base.bottom * scale;
            float y2 = base.top * scale;

            // We now detach from our Rect because when rotating,
            // we need the seperate points, so we do so in opengl order
            PointF one = new PointF(x1, y2);
            PointF two = new PointF(x1, y1);
            PointF three = new PointF(x2, y1);
            PointF four = new PointF(x2, y2);

            // We create the sin and cos function once,
            // so we do not have calculate them each time.
            float s = (float) Math.sin(angle);
            float c = (float) Math.cos(angle);

            // Then we rotate each point
            one.x = x1 * c - y2 * s;
            one.y = x1 * s + y2 * c;
            two.x = x1 * c - y1 * s;
            two.y = x1 * s + y1 * c;
            three.x = x2 * c - y1 * s;
            three.y = x2 * s + y1 * c;
            four.x = x2 * c - y2 * s;
            four.y = x2 * s + y2 * c;

            // Finally we translate the sprite to its correct position.
            one.x += translation.x;
            one.y += translation.y;
            two.x += translation.x;
            two.y += translation.y;
            three.x += translation.x;
            three.y += translation.y;
            four.x += translation.x;
            four.y += translation.y;

            // We now return our float array of vertices.
            return new float[]
                    {
                            one.x, one.y, 0.0f,
                            two.x, two.y, 0.0f,
                            three.x, three.y, 0.0f,
                            four.x, four.y, 0.0f,
                    };
        }
    }
}

