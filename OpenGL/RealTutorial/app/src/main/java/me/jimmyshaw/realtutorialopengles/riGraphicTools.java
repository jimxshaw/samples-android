package me.jimmyshaw.realtutorialopengles;

import android.opengl.GLES20;

public class riGraphicTools
{
    // Program variables.
    public static int sp_SolidColor;
    public static int sp_Image;

    // SHADER Solid Color
    // Vertex shaders control what is done to vertices and how they should be rendered.
    // They do nothing more than translate our modelspace coordinates to screen coordinates.
    public static final String vs_SolidColor = "" +
            "uniform mat4 uMVPMatrix;" +
            "attribute vec4 vPosition;" +
            "void main() {" +
            "   gl_Position = uMVPMatrix * vPosition;" +
            "}";

    // Fragment shaders control what is done to the fragments created by the rasterization
    // process, which in our case are the pixels of our triangle. Rasterization creates
    // a pixelbuffer holding the triangle translated to screen space. The fragment shader then
    // applies a color to each fragment. The resulting triangle will be that color.
    public static final String fs_SolidColor = "" +
            "precision mediump float;" +
            "void main() {" +
            "   gl_FragColor = vec4(0.5, 0, 0, 1);" +
            "}";

    // SHADER Image
    public static final String vs_Image = "";
    public static final String fs_Image = "";

    public static int loadShader(int type, String shaderCode)
    {
        // Create a vertex shader type (GLES20.GL_VERTEX_SHADER).
        // Create a fragment shader type (GLES20.GL_FRAGMENT_SHADER).
        int shader = GLES20.glCreateShader(type);

        // Add the source code to the shader and compile it.
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }
}
