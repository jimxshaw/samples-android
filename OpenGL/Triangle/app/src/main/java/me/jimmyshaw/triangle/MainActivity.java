package me.jimmyshaw.triangle;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MainActivity extends AppCompatActivity implements GLSurfaceView.Renderer {

    private int mOpenGLProgram = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // In order to use OpenGL, it must be added to the Android Manifest. Don't forget.

        // OpenGL sequence:
        // Data read from OBJ files -> OpenGL ES primitive processing -> Vertex shader ->
        //                              OpenGL ES rasterizer -> Fragment shader ->
        //                               OpenGL ES fragment processing -> Frame buffer

        // Instantiate the view.
        GLSurfaceView glView = new GLSurfaceView(this);

        // Provide the view with some information to configure the OpenGL state.
        // We'll use OpenGL version 2.
        glView.setEGLContextClientVersion(2);
        // What quality of the output image, in bits, do we want. We're using some typical
        // values used by convention.
        glView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        // What is the source of the thing that's doing the rendering. We add a renderer
        // interface that contains three methods. For convenience, our activity will
        // implement this interface and act as the renderer for our view.
        glView.setRenderer(this);


        setContentView(glView);
    }

    // This is where OpenGL starts up essentially.
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        // Here we are defining a couple of shaders.
        // OpenGL is asking us to write two programs that OpenGL itself will compile for us during
        // runtime. Doing this at runtime is possible because OpenGL contains a compiler and a linker.
        // It will send these programs to the video hardware to be executed by the video hardware (GPU).
        // The two shader programs, one for vertex and the other for fragment are actually just
        // string data that gets put into OpenGL.

        // The programming language for shaders, looks similar to C, is called GLSL. GLSL defines
        // most things C has but with a few more extensions.
        // The purpose of this vertex program is to set the gl_position variable. Essentially it states
        // no matter what data comes in, make the output vertex at the specified location. The vec4
        // parameters are x, y, z, w. In homogeneous coordinates, points have a w of 1.0 and vectors
        // have a w of 0. Generally, use 1.0 for w.
        // We declare a variable called position and give it a qualifier called attribute. Eventually
        // position will contain each row within our geometry float array defined in onDrawFrame. We
        // take in the input data and directly output that data.
        String vertexShaderSource = "" +
                "" +
                "attribute vec4 position;" +
                "" +
                "void main()" +
                "{" +
                "    gl_position = position;" +
                "}";

        String fragmentShaderSource = "";

        // First, create a shader object. OpenGL identifies a shader by its unique int value.
        int vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        // We add the source of our string to it.
        GLES20.glShaderSource(vertexShader, vertexShaderSource);
        // We have to compile the shader.
        GLES20.glCompileShader(vertexShader);
        // Find the result of compiling the shader.
        String vertexShaderCompileLog = GLES20.glGetShaderInfoLog(vertexShader);

        int fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(fragmentShader, fragmentShaderSource);
        GLES20.glCompileShader(fragmentShader);
        String fragmentShaderCompileLog = GLES20.glGetShaderInfoLog(fragmentShader);

        // Next, we link the two shaders into what's known as an OpenGL program. An OpenGL program
        // is best defined as a class member variable as it will be used everywhere within the class.
        mOpenGLProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mOpenGLProgram, vertexShader);
        GLES20.glAttachShader(mOpenGLProgram, fragmentShader);
        // After we attach the shaders but before we link them with our OpenGL program, we have to bind the
        // attribute location that we defined in the above vertexShaderSource GLSL program, which
        // in this case is a variable called position. Note that the value passed in to the second
        // parameter, index, must match the value passed into the first parameter of the
        // glVertexAttribPointer method in onDrawFrame. 
        GLES20.glBindAttribLocation(mOpenGLProgram, 0, "position");
        GLES20.glLinkProgram(mOpenGLProgram);
        String openGLProgramLinkLog = GLES20.glGetProgramInfoLog(mOpenGLProgram);

        // Assuming all goes well, we tell OpenGL to use the program to do the drawing.
        GLES20.glUseProgram(mOpenGLProgram);

        // Set the background color.
        GLES20.glClearColor(0.2f, 0.6f, 0.6f, 1.0f);
    }

    // If our OpenGL view ever gets resized, this method gets called.
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // The view port is the size of the thing we're drawing into. Where in the glView are
        // we rendering to. We could set the thing larger than the view if we wanted by
        // inputting the necessary values. Generally, we tie the view port to the GL surface
        // view so that when the view changes, the view port will change accordingly.
        GLES20.glViewport(0, 0, width, height);
    }

    // Depending on the settings of the OpenGL view, this method will either be called repeatedly
    // and as quickly as it can be or only when we ask it to be called. The default is the
    // former, calling repeatedly and quickly.
    @Override
    public void onDrawFrame(GL10 gl) {
        // What things do we want to clear. We're clearing the color buffer, which is
        // the thing we see. Buffers are bitmaps, essentially pictures.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        float[] geometry = {
                // We're defining a triangle with its center at 0,0 and has points that extend out
                // by 0.5 in each direction. The four coordinates are x, y, z, w. In homogeneous
                // coordinates, points have a w of 1.0 and vectors have a w of 0. Generally, use 1.0 for w.
                -0.5f, -0.5f, 0.0f, 1.0f,
                 0.5f, -0.5f, 0.0f, 1.0f,
                 0.0f,  0.5f, 0.0f, 1.0f
        };

        // We have to pass in the exact bytes needed to this buffer. Allocation is calculated with
        // the length of the array multiplied by the size of its elements, which is 4 bytes per
        // floating point value. Even the exact position of the bytes has to be specified
        // by using the order method. Finally, the buffer has to be converted into the type we want.
        ByteBuffer geometryByteBuffer = ByteBuffer.allocateDirect(geometry.length * 4);
        geometryByteBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer geometryBuffer = geometryByteBuffer.asFloatBuffer();
        // The buffer gets filled with our data and needs to rewind back to the start so that
        // OpenGL can read the data from the beginning. Another way to do this would be to set the
        // buffer's position to zero.
        geometryBuffer.put(geometry);
        geometryBuffer.rewind();

        // We hand the data to OpenGL by using glVertexAttribPointer. The size parameter means the
        // number of points in the above array. It's 4 because we've 4 points (columns) in each point of
        // the triangle. Normalize asks if we want our numbers to go between 0 and 1, which generally
        // we don't. Stride is the distance between each set of data, which is 4 * 4. That's 4
        // floating point numbers of 4 bytes each. Buffer is the information itself that we're
        // giving to OpenGl. We told it what the information is so now we have to pass that in.
        GLES20.glVertexAttribPointer(0, 4, GLES20.GL_FLOAT, false, 4 * 4, geometryBuffer);

        // All info has been passed to OpenGL. We now can ask it to use the information.
        // It needs to know to how organize the data with the mode parameter. The second parameter
        // titled first means the starting position to begin drawing. Finally, it needs to know
        // the number of items (rows) it can draw with count. A way to test if the count value is correct
        // is that items (rows) * points (columns) should equal the total number of elements in the array.
        // So with our geometry array above, 3 * 4 = 12 total array elements.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
    }
}
