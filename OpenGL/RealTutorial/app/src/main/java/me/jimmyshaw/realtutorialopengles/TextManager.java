package me.jimmyshaw.realtutorialopengles;

import android.opengl.GLES20;

import org.w3c.dom.Text;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Iterator;
import java.util.Vector;

public class TextManager
{
    // This is the step-size of our font.png texture atlas. The scale of the UV texture ranges
    // from 0.0f to 1.0f. Since we have 8 box tiles with the same width, our step-size will be
    // 1.0f / 8 = 0.125f.
    private static final float RI_TEXT_UV_BOX_WIDTH = 0.125f;
    // The tile's width on the texture is 64 pixels but that'll be rendered too big so we
    // use half of that for our size in this app.
    private static final float RI_TEXT_WIDTH = 32.0f;
    // This is the width of a space between letters.
    private static final float RI_TEXT_SPACESIZE = 20.0f;

    private FloatBuffer vertexBuffer;
    private FloatBuffer textureBuffer;
    private FloatBuffer colorBuffer;
    private ShortBuffer drawListBuffer;

    private float[] vecs;
    private float[] uvs;
    private float[] colors;
    private short[] indices;

    private int index_vecs;
    private int index_uvs;
    private int index_colors;
    private int index_indices;

    // Since we render our text from a texture, we should know what texture to use.
    private int texturenr;

    // This is the scale factor for rendering.
    private float uniformscale;

    // These are the exact widths in pixels on the texture for the letters. We store the exact
    // widths of the letters in an array so we can render the text with the correct spaces
    // between letters.
    public static int[] l_size =
            {
                    36, 29, 30, 34, 25, 25, 34, 33,
                    11, 20, 31, 24, 48, 35, 39, 29,
                    42, 31, 27, 31, 34, 35, 46, 35,
                    31, 27, 30, 26, 28, 26, 31, 28,
                    28, 28, 29, 29, 14, 24, 30, 18,
                    26, 14, 14, 14, 25, 28, 31, 0,
                    0, 38, 39, 12, 36, 34, 0, 0,
                    0, 38, 0, 0, 0, 0, 0, 0
            };

    public Vector<TextObject> textCollection;

    public TextManager()
    {
        // Create our container. It creates the actual vector for our collection of TextObjects and
        // we initialize all the variables to some default values.
        textCollection = new Vector<TextObject>();

        // Create the arrays.
        vecs = new float[3 * 10];
        uvs = new float[2 * 10];
        colors = new float[4 * 10];
        indices = new short[10];

        // Init 0 as default.
        texturenr = 0;
    }

    public void addText(TextObject obj)
    {
        // Add text object to our collection. // We add a TextObject by passing the object to our
        // addText method and add it to our vector.
        textCollection.add(obj);
    }

    public void setTextureID(int value)
    {
        texturenr = value;
    }

    public void AddCharRenderInformation(float[] vec, float[] cs, float[] uv, short[] indi)
    {
        // We pass vertices, UV coordinates, colors and indices data and that data is stored
        // in our main data arrays. The purpose is so that we can simply pass that data in one
        // render call when we finally render all the text.

        // We need a base value because the object has indices related to that object and not to
        // this collection. Basically, we need to translate the indices to align with the
        // vertexlocation in our vecs array of vectors.
        short base = (short) (index_vecs / 3);

        // We add the vec, translating the indices to our saved vector.
        for (int i = 0; i < vec.length; i++)
        {
            vecs[index_vecs] = vec[i];
            index_vecs++;
        }

        // We add the uvs.
        for (int i = 0; i < uv.length; i++)
        {
            uvs[index_uvs] = uv[i];
            index_uvs++;
        }

        // We add the colors.
        for (int i = 0; i < cs.length; i++)
        {
            colors[index_colors] = cs[i];
            index_colors++;
        }

        // We handle the indices
        for (int i = 0; i < indi.length; i++)
        {
            indices[index_indices] = (short) (base + indi[i]);
            index_indices++;
        }
    }

    // The prepareDrawInfo and prepareDraw methods both clear all data and populate them with
    // up to date text information.
    public void prepareDrawInfo()
    {
        // Reset the indices.
        index_vecs = 0;
        index_uvs = 0;
        index_colors = 0;
        index_indices = 0;

        // Get the total number of characters by looping through the collection.
        int charCount = 0;
        for (TextObject textObject : textCollection)
        {
            if (textObject != null)
            {
                if (!(textObject.text == null))
                {
                    charCount += textObject.text.length();
                }
            }
        }

        // Create the arrays with the correct size by calculating the total number of characters.
        vecs = null;
        uvs = null;
        colors = null;
        indices = null;

        vecs = new float[charCount * 12];
        uvs = new float[charCount * 8];
        colors = new float[charCount * 16];
        indices = new short[charCount * 6];
    }

    public void prepareDraw()
    {
        // Setup all arrays. When the setup is complete, we iterate through our collection and
        // convert our text objects to triangle information.
        prepareDrawInfo();

        // Using the iterator protects us from problems with concurrency.
        for (Iterator<TextObject> iteratorObject = textCollection.iterator(); iteratorObject.hasNext(); )
        {
            TextObject textObject = iteratorObject.next();
            if (textObject != null)
            {
                if (!(textObject.text == null))
                {
                    convertTextToTriangleInfo(textObject);
                }
            }
        }
    }

    public void draw(float[] m)
    {
        // This is the method to render all the information to the screen. We call this method
        // once every iteration of our game update cycle.

        // Set the correct shader for the grid object.
        GLES20.glUseProgram(riGraphicTools.sp_Text);

        // The vertex buffer.
        ByteBuffer bb = ByteBuffer.allocateDirect(vecs.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vecs);
        vertexBuffer.position(0);

        // The texture buffer.
        ByteBuffer bb2 = ByteBuffer.allocateDirect(uvs.length * 4);
        bb2.order(ByteOrder.nativeOrder());
        textureBuffer = bb2.asFloatBuffer();
        textureBuffer.put(uvs);
        textureBuffer.position(0);

        // The vertex buffer.
        ByteBuffer bb3 = ByteBuffer.allocateDirect(colors.length * 4);
        bb3.order(ByteOrder.nativeOrder());
        colorBuffer = bb3.asFloatBuffer();
        colorBuffer.put(colors);
        colorBuffer.position(0);

        // Initialize byte buffer for the draw list.
        ByteBuffer dlb = ByteBuffer.allocateDirect(indices.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(indices);
        drawListBuffer.position(0);

        // Get handle to the vertex shader's vPosition member.
        int mPositionHandle = GLES20.glGetAttribLocation(riGraphicTools.sp_Text,
                "vPosition");

        // Enable a handle to the triangle vertices.
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the background coordinate data.
        GLES20.glVertexAttribPointer(mPositionHandle, 3,
                GLES20.GL_FLOAT, false,
                0, vertexBuffer);

        int mTexCoordLoc = GLES20.glGetAttribLocation(riGraphicTools.sp_Text,
                "a_texCoord");

        // Prepare the texture coordinates.
        GLES20.glVertexAttribPointer(mTexCoordLoc, 2, GLES20.GL_FLOAT,
                false,
                0, textureBuffer);

        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glEnableVertexAttribArray(mTexCoordLoc);

        int mColorHandle = GLES20.glGetAttribLocation(riGraphicTools.sp_Text,
                "a_Color");

        // Enable a handle to the triangle vertices.
        GLES20.glEnableVertexAttribArray(mColorHandle);

        // Prepare the background coordinate data.
        GLES20.glVertexAttribPointer(mColorHandle, 4,
                GLES20.GL_FLOAT, false,
                0, colorBuffer);

        // Get handle to the shape's transformation matrix.
        int mtrxhandle = GLES20.glGetUniformLocation(riGraphicTools.sp_Text,
                "uMVPMatrix");

        // Apply the projection and view transformation.
        GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, m, 0);

        int mSamplerLoc = GLES20.glGetUniformLocation(riGraphicTools.sp_Text,
                "s_texture");

        // Set the sampler texture unit to our selected id.
        GLES20.glUniform1i(mSamplerLoc, texturenr);

        // Draw the triangle.
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex arrays.
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTexCoordLoc);
        GLES20.glDisableVertexAttribArray(mColorHandle);
    }

    private void convertTextToTriangleInfo(TextObject value)
    {

    }
}