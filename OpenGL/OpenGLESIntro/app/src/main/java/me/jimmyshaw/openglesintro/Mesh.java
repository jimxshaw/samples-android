package me.jimmyshaw.openglesintro;

import android.graphics.Bitmap;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Mesh
{
    // Translate params.
    public float x = 0;
    public float y = 0;
    public float z = 0;

    // Rotate params.
    public float rx = 0;
    public float ry = 0;
    public float rz = 0;

    // A field being set to -1 means it will be assigned to some other value later.
    private int textureId = -1;

    private Bitmap bitmap;

    private boolean shouldLoadTexture = true;

    private FloatBuffer verticesBuffer = null;
    private ShortBuffer indicesBuffer = null;

    private int numberOfIndices = -1;

    private float[] rgba = new float[]{1.0f, 1.0f, 1.0f, 1.0f};

    private FloatBuffer colorBuffer = null;

    private FloatBuffer textureBuffer;

    public void draw(GL10 gl)
    {
        gl.glFrontFace(GL10.GL_CCW);
        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glCullFace(GL10.GL_BACK);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, verticesBuffer);

        gl.glColor4f(rgba[0], rgba[1], rgba[2], rgba[3]);

        if (colorBuffer != null)
        {
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
            gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
        }

        gl.glTranslatef(x, y, z);
        gl.glRotatef(rx, 1, 0, 0);
        gl.glRotatef(ry, 0, 1, 0);
        gl.glRotatef(rz, 0, 0, 1);

        gl.glDrawElements(GL10.GL_TRIANGLES, numberOfIndices,
                GL10.GL_UNSIGNED_SHORT, indicesBuffer);

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisable(GL10.GL_CULL_FACE);
    }

    protected void setTextureCoordinates(float[] textureCoordinates)
    {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(textureCoordinates.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        textureBuffer = byteBuffer.asFloatBuffer();
        textureBuffer.put(textureCoordinates);
        textureBuffer.position(0);
    }

    protected void setVertices(float[] vertices)
    {
        // A float is 4 bytes, therefore we multiply the number of
        // vertices vt 4.
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        verticesBuffer = vbb.asFloatBuffer();
        verticesBuffer.put(vertices);
        verticesBuffer.position(0);
    }

    protected void setIndices(short[] indices)
    {
        // A short is 2 bytes, therefore we multiply the number of
        // vertices by 2.
        ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
        ibb.order(ByteOrder.nativeOrder());
        indicesBuffer = ibb.asShortBuffer();
        indicesBuffer.put(indices);
        indicesBuffer.position(0);
        numberOfIndices = indices.length;
    }

    protected void setColor(float red, float green, float blue, float alpha)
    {
        rgba[0] = red;
        rgba[1] = green;
        rgba[2] = blue;
        rgba[3] = alpha;
    }

    public void loadBitmap(Bitmap bitmap)
    {
        this.bitmap = bitmap;
        shouldLoadTexture = true;
    }

    private void loadGLTexture(GL10 gl)
    {
        //Generate 1 texture pointer.
        int[] texture = new int[1];
        gl.glGenTextures(1, texture, 0);
        textureId = texture[0];

        // Bind texture pointer to our array.
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);

        // Create nearest filtered texture.
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

        // Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE.
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);

        // Use the Android GLUtils to specify a two-dimensional texture image from our bitmap.
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

    }
}
