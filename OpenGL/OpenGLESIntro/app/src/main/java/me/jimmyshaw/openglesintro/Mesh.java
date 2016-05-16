package me.jimmyshaw.openglesintro;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

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

    private FloatBuffer verticesBuffer = null;
    private ShortBuffer indicesBuffer = null;

    private int numberOfIndices = -1;

    private float[] rgba = new float[]{1.0f, 1.0f, 1.0f, 1.0f};

    private FloatBuffer colorBuffer = null;

    // Method to set all of our vertices.
    protected void setVertices(float[] vertices)
    {
        // A float is 4 bytes, therefore we multiply the number of vertices by 4.
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        verticesBuffer = vbb.asFloatBuffer();
        verticesBuffer.put(vertices);
        verticesBuffer.position(0);
    }

    protected void setIndices(short[] indices)
    {
        // A short is 2 bytes, therefore we multiply the number of indices by 2.
        ByteBuffer ibb = ByteBuffer.allocate(indices.length * 2);
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
}
