package me.jimmyshaw.openglesintro;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.view.MotionEvent;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OpenGLRenderer implements GLSurfaceView.Renderer
{
    private Square square;

    private float angle = 1;

    public boolean onTouchEvent(MotionEvent e)
    {
        return true;
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        gl.glShadeModel(GL10.GL_SMOOTH);

        gl.glClearDepthf(1.0f);

        gl.glEnable(GL10.GL_DEPTH_TEST);

        gl.glDepthFunc(GL10.GL_LEQUAL);

        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);

        square = new Square();
    }

    public void onDrawFrame(GL10 gl)
    {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        gl.glLoadIdentity();

        // x, y, z
        // Anything drawn too close to the viewport won't be rendered so we're push everything back
        // in the negative z axis direction.
        gl.glTranslatef(0, 0, -8);
        // Push the matrix on to the stack, perform some action, pop it off the stack.
        gl.glPushMatrix();
        // Rotate the axis by some value. Rotation along x and y are for 3D objects. Rotation along
        // z is meant for 2D objects.
        gl.glRotatef(angle/4, 0, 0, 1);
        square.draw(gl);
        gl.glPopMatrix();

        // Every push matix must have a respective pop matrix to end the block.
        gl.glPushMatrix();
        gl.glRotatef(-angle, 0, 0, 1);
        gl.glTranslatef(1, 1.5f, 0);
        // Make the square half the size of the original square.
        gl.glScalef(0.25f, 0.25f, 0.25f);
        square.draw(gl);
        gl.glPopMatrix();

        // Every time the square is drawn, we increase the angle.
        angle++;
    }

    public void onSurfaceChanged(GL10 gl, int width, int height)
    {
        gl.glViewport(0, 0, width, height);

        gl.glMatrixMode(GL10.GL_PROJECTION);

        gl.glLoadIdentity();

        GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f, 100.0f);

        gl.glMatrixMode(GL10.GL_MODELVIEW);

        gl.glLoadIdentity();
    }
}
