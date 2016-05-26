package me.jimmyshaw.realtutorialopengles;

import android.opengl.GLES20;

public class riGraphicTools
{
    // Program variables.
    public static int sp_SolidColor;
    public static int sp_Image;
    public static int sp_Text;

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
    // This second vertex shader has a texture location, a_texCoord, as input and gives it to the
    // second fragment shader.
    public static final String vs_Image = "" +
            "uniform mat4 uMVPMatrix;" +
            "attribute vec4 vPosition;" +
            "attribute vec2 a_texCoord;" +
            "varying vec2 v_texCoord;" +
            "void main() {" +
            "  gl_Position = uMVPMatrix * vPosition;" +
            "  v_texCoord = a_texCoord;" +
            "}";

    // The fragment gets the texture unit id and with the help of the texture2D function, it gets
    // the color from the texture location passed to the shader.
    public static final String fs_Image = "" +
            "precision mediump float;" +
            "varying vec2 v_texCoord;" +
            "uniform sampler2D s_texture;" +
            "void main() {" +
            "  gl_FragColor = texture2D( s_texture, v_texCoord );" +
            "}";

    // SHADER Text
    // This shader is for rendering 2D text textures straight from a texture color and alpha blended.
    public static final String vs_Text =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "attribute vec4 a_Color;" +
                    "attribute vec2 a_texCoord;" +
                    "varying vec4 v_Color;" +
                    "varying vec2 v_texCoord;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "  v_texCoord = a_texCoord;" +
                    "  v_Color = a_Color;" +
                    "}";

    public static final String fs_Text =
            "precision mediump float;" +
                    "varying vec4 v_Color;" +
                    "varying vec2 v_texCoord;" +
                    "uniform sampler2D s_texture;" +
                    "void main() {" +
                    "  gl_FragColor = texture2D( s_texture, v_texCoord ) * v_Color;" +
                    "  gl_FragColor.rgb *= v_Color.a;" +
                    "}";

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
