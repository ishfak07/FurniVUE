package Models_2D;
import javax.swing.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.*;
import java.awt.*;

public class Bed2D extends JFrame implements GLEventListener {
    private GLJPanel canvas;

    public Bed2D() {
        super("2D Sofa with OpenGL");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        GLCapabilities caps = new GLCapabilities(null);
        canvas = new GLJPanel(caps);
        canvas.addGLEventListener(this);

        getContentPane().add(canvas, BorderLayout.CENTER);
        setVisible(true);
        FPSAnimator animator = new FPSAnimator(canvas, 60);
        animator.start();
    }

    public static void main(String[] args) {
        new Bed2D();
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        // Draw the sofa
        drawSofa(gl);
        gl.glFlush();
    }

    private void drawSofa(GL2 gl) {
        // Draw the base of the sofa
        gl.glColor3f(0.6f, 0.4f, 0.2f);
        gl.glBegin(GL2.GL_POLYGON);
        gl.glVertex2f(-0.8f, -0.5f);
        gl.glVertex2f(-0.8f, 0.5f);
        gl.glVertex2f(0.8f, 0.5f);
        gl.glVertex2f(0.8f, -0.5f);
        gl.glEnd();

        // Draw the backrest of the sofa
        gl.glColor3f(0.8f, 0.6f, 0.4f);
        gl.glBegin(GL2.GL_POLYGON);
        gl.glVertex2f(0.8f, -0.2f);
        gl.glVertex2f(1.2f, -0.2f);
        gl.glVertex2f(1.2f, 0.2f);
        gl.glVertex2f(0.8f, 0.2f);
        gl.glEnd();

        // Draw the armrests
        gl.glColor3f(0.6f, 0.4f, 0.2f);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(-0.8f, -0.5f);
        gl.glVertex2f(-0.6f, -0.5f);
        gl.glVertex2f(-0.6f, 0.5f);
        gl.glVertex2f(-0.8f, 0.5f);

        gl.glVertex2f(0.8f, -0.5f);
        gl.glVertex2f(1.0f, -0.5f);
        gl.glVertex2f(1.0f, 0.5f);
        gl.glVertex2f(0.8f, 0.5f);
        gl.glEnd();

        // Draw the cushions
        gl.glColor3f(0.8f, 0.8f, 0.8f);
        // Left cushion
        gl.glBegin(GL2.GL_POLYGON);
        gl.glVertex2f(-0.6f, -0.4f);
        gl.glVertex2f(-0.3f, -0.4f);
        gl.glVertex2f(-0.3f, 0.3f);
        gl.glVertex2f(-0.6f, 0.3f);
        gl.glEnd();
        // Middle cushion
        gl.glBegin(GL2.GL_POLYGON);
        gl.glVertex2f(-0.2f, -0.4f);
        gl.glVertex2f(0.5f, -0.4f);
        gl.glVertex2f(0.5f, 0.3f);
        gl.glVertex2f(-0.2f, 0.3f);
        gl.glEnd();
        // Right cushion
        gl.glBegin(GL2.GL_POLYGON);
        gl.glVertex2f(0.6f, -0.4f);
        gl.glVertex2f(0.9f, -0.4f);
        gl.glVertex2f(0.9f, 0.3f);
        gl.glVertex2f(0.6f, 0.3f);
        gl.glEnd();

        // Draw the legs
        gl.glColor3f(0.4f, 0.4f, 0.4f);
        gl.glBegin(GL2.GL_QUADS);
        // Front left leg
        gl.glVertex2f(-0.7f, -0.7f);
        gl.glVertex2f(-0.6f, -0.7f);
        gl.glVertex2f(-0.6f, -0.5f);
        gl.glVertex2f(-0.7f, -0.5f);
        // Front right leg
        gl.glVertex2f(0.7f, -0.7f);
        gl.glVertex2f(0.8f, -0.7f);
        gl.glVertex2f(0.8f, -0.5f);
        gl.glVertex2f(0.7f, -0.5f);
        // Back left leg
        gl.glVertex2f(-0.7f, 0.4f);
        gl.glVertex2f(-0.6f, 0.4f);
        gl.glVertex2f(-0.6f, 0.6f);
        gl.glVertex2f(-0.7f, 0.6f);
        // Back right leg
        gl.glVertex2f(0.7f, 0.4f);
        gl.glVertex2f(0.8f, 0.4f);
        gl.glVertex2f(0.8f, 0.6f);
        gl.glVertex2f(0.7f, 0.6f);
        gl.glEnd();
    }


    @Override
    public void dispose(GLAutoDrawable drawable) {}

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(0.9f, 0.9f, 0.9f, 1.0f);
        gl.glEnable(GL2.GL_DEPTH_TEST); // Enable depth testing
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(-1.5, 1.5, -1.5 * height / width, 1.5 * height / width, -1.0, 1.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }
}
