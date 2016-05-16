package me.jimmyshaw.openglesintro;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Square
{
    private float vertices[] =
            {
                    // x, y, z
                    -1.0f, 1.0f, 0.0f,     // Top left, we'll call it 0.
                    -1.0f, -1.0f, 0.0f,     // Bottom left, we'll call it 1.
                    1.0f, -1.0f, 0.0f, // Bottom right, we'll call it 2.
                    1.0f, 1.0f, 0.0f, // Top right, we'll call it 3.
            };

    // To make a square, we connect two triangles. Vertex 0 connects to vertex 1, 1 connects
    // to 2, 2 connects to 0, 0 connects to 2, 2 connects to 3.
    private short[] indices = {0, 1, 2, 0, 2, 3};

    private FloatBuffer vertexBuffer;

    private ShortBuffer indexBuffer;

    public Square()
    {
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
        ibb.order(ByteOrder.nativeOrder());
        indexBuffer = ibb.asShortBuffer();
        indexBuffer.put(indices);
        indexBuffer.position(0);
    }

    public void draw(GL10 gl)
    {
        gl.glColor4f(0.5f, 0.5f, 0.5f, 1.0f);

        // Tell OpenGL we're drawing in a counter-clockwise direction.
        gl.glFrontFace(GL10.GL_CCW);

        gl.glEnable(GL10.GL_CULL_FACE);

        // Prevent the back side of our objects from being drawn.
        gl.glCullFace(GL10.GL_BACK);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

        // This is what's actually drawing to our screen.
        gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_SHORT, indexBuffer);

        // Disable some extra features to reduce load on the processor.
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisable(GL10.GL_CULL_FACE);
    }
}
