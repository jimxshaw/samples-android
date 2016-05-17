package me.jimmyshaw.version4;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Triangle
{

    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "uniform mat4 uMVPMatrix;" +
                    "void main() {" +
                    "   gl_Position = vPosition * uMVPMatrix;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "   gl_FragColor = vColor;" +
                    "}";

    final int COORDINATES_PER_VERTEX = 3;

    float triangleCoordinates[] = {
            0.0f, 0.66f, 0.0f,
            -0.5f, -0.33f, 0.0f,
            0.5f, -0.33f, 0.0f
    };

    float color[] = {
            0.63f, 0.76f, 0.22f, 1.0f
    };

    private final int mProgram;
    private FloatBuffer vertexBuffer;
    private int mPositionHandle;
    private int mColorHandle;
    private final int vertexCount = triangleCoordinates.length / COORDINATES_PER_VERTEX;
    private final int vertexStride = COORDINATES_PER_VERTEX * 4;

    private int mMVPMatrixHandle;

    public Triangle()
    {
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);

        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(triangleCoordinates.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());

        vertexBuffer = byteBuffer.asFloatBuffer();
        vertexBuffer.put(triangleCoordinates);
        vertexBuffer.position(0);
    }

    public void draw(float[] mvpMatrix)
    {
        GLES20.glUseProgram(mProgram);
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle,   // index
                COORDINATES_PER_VERTEX, // size
                GLES20.GL_FLOAT,    // type
                false,  // normalized
                vertexStride,   // stride
                vertexBuffer);  // pointer
        mColorHandle = GLES20.glGetAttribLocation(mProgram, "vColor");
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

    public int loadShader(int type, String shaderCode)
    {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

}
