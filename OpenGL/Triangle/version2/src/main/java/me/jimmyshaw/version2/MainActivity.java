package me.jimmyshaw.version2;

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

    private double mAnimation = 0.0;

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

        String vertexShaderSource = "" +
                "uniform vec2 translate;" +
                "attribute vec3 position;" +
                "" +
                "void main()" +
                "{" +
                "    gl_Position = vec4(position, 1.0) + vec4(translate.x, translate.y, 0.0, 0.0);" +
                "}";

        // The purpose of the fragment shader is similar to the vertex shader in that we want to
        // define a special variable. We're also assigning a vec4 to gl_FragColor but in this case
        // the four values are r, g, b, a.
        String fragmentShaderSource = "" +
                "" +
                "" +
                "void main()" +
                "{" +
                "    gl_FragColor = vec4(0.8, 0.7, 0.6, 1.0);" +
                "}";

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

        // Every time we draw a frame, increment the animation variable by a certain amount.
        mAnimation += 0.01;
        double animationDouble = Math.sin(mAnimation);

        // Remember how we defined the uniform translate variable within the vertex shader GLSL
        // program above? These are the values that will be used with that vertex shader.
        float translateX = (float) animationDouble;
        float translateY = 0.1f;

        // The first parameter, location, is provided to us by the app. Itself takes in the name of
        // the OpenGL program and the name of the vertex shader GLSL variable. What are we going to
        // put into that translate variable? The float values of translateX and translateY.
        GLES20.glUniform2f(GLES20.glGetUniformLocation(mOpenGLProgram, "translate"), translateX, translateY);

        float[] geometry = {
                // We're defining a triangle with its center at 0,0 and has points that extend out
                // by 0.5 in each direction. The four coordinates are x, y, z, w. In homogeneous
                // coordinates, points have a w of 1.0 and vectors have a w of 0. Generally, use 1.0 for w.
                // front
                -0.5f, -0.5f,  0.5f,
                 0.5f, -0.5f,  0.5f,
                 0.5f,  0.5f,  0.5f,
                -0.5f,  0.5f,  0.5f,
                // back
                -0.5f, -0.5f, -0.5f,
                 0.5f, -0.5f, -0.5f,
                 0.5f,  0.5f, -0.5f,
                -0.5f,  0.5f, -0.5f,
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

        int elements = 3;
        // There are now 3 elements per row and the stride between them is 4 bytes times 3.
        GLES20.glVertexAttribPointer(0, elements, GLES20.GL_FLOAT, false, 4 * elements, geometryBuffer);
        // We actually have to turn on the array that we attribute the vertex shader GLSL program
        // position variable.
        GLES20.glEnableVertexAttribArray(0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, geometry.length / elements);
    }
}

