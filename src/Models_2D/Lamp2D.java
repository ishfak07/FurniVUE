package Models_2D;

import javax.swing.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class Lamp2D extends JFrame implements GLEventListener {
    private GLJPanel canvas;

    public Lamp2D() {
        super("2D Lamp ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GLCapabilities caps = new GLCapabilities(null);
        canvas = new GLJPanel(caps);
        canvas.addGLEventListener(this);

        getContentPane().add(canvas, BorderLayout.CENTER);
        setSize(800, 600); // Initial size
        setVisible(true);
        FPSAnimator animator = new FPSAnimator(canvas, 60);
        animator.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Lamp2D();
            }
        });
    }

    private Texture loadTexture(String fileName) {
        try {
            InputStream stream = getClass().getResourceAsStream(fileName);
            return TextureIO.newTexture(stream, true, null);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void drawTexturedBase(GL2 gl) {
        Texture texture = loadTexture("../images/metal.jpg");

        gl.glEnable(GL2.GL_TEXTURE_2D);
        texture.bind(gl);

        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2f(-0.5f, -0.5f);
        gl.glTexCoord2f(1, 0);
        gl.glVertex2f(0.5f, -0.5f);
        gl.glTexCoord2f(1, 1);
        gl.glVertex2f(0.5f, 0.5f);
        gl.glTexCoord2f(0, 1);
        gl.glVertex2f(-0.5f, 0.5f);
        gl.glEnd();

        gl.glDisable(GL2.GL_TEXTURE_2D);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        float aspect = (float) width / (float) height;

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        if (width > height) {
            gl.glOrtho(-aspect, aspect, -1, 1, -1, 1);
        } else {
            gl.glOrtho(-1, 1, -1 / aspect, 1 / aspect, -1, 1);
        }

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();

        float scaleFactor = Math.min(width, height) / 800f;
        gl.glScalef(scaleFactor, scaleFactor, 1);

        drawLamp(gl);
        gl.glFlush();
    }

    private void drawLamp(GL2 gl) {
        drawTexturedBase(gl);
        drawStand(gl);
        drawIntricateShade(gl);
        drawDynamicBulb(gl);
    }

    private void drawStand(GL2 gl) {
        gl.glColor3f(0.6f, 0.6f, 0.6f);
        gl.glBegin(GL2.GL_POLYGON);
        gl.glVertex2f(-0.15f, 0);
        gl.glVertex2f(0.15f, 0);
        gl.glVertex2f(0.2f, 0.1f);
        gl.glVertex2f(-0.2f, 0.1f);
        gl.glEnd();
        drawDecorativeElements(gl);
    }

    private void drawDecorativeElements(GL2 gl) {
        gl.glColor3f(0.8f, 0.8f, 0.8f);
        gl.glPushMatrix();
        gl.glTranslatef(-0.1f, 0.1f, 0);
        drawCircle(gl, 0, 0, 0.03f, 50);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.1f, 0.1f, 0);
        drawCircle(gl, 0, 0, 0.03f, 50);
        gl.glPopMatrix();
    }

    private void drawCircle(GL2 gl, float centerX, float centerY, float radius, int segments) {
        gl.glBegin(GL2.GL_TRIANGLE_FAN);
        gl.glVertex2f(centerX, centerY);
        for (int i = 0; i <= segments; i++) {
            double angle = 2.0 * Math.PI * i / segments;
            double x = centerX + radius * Math.cos(angle);
            double y = centerY + radius * Math.sin(angle);
            gl.glVertex2d(x, y);
        }
        gl.glEnd();
    }

    private void drawIntricateShade(GL2 gl) {
        float centerX = 0;
        float centerY = 1.5f;
        float radius = 0.7f;
        int segments = 100;
        float angleIncrement = (float) (2 * Math.PI / segments);
        float angle = 0;

        gl.glColor3f(0.6f, 0.3f, 0.8f);
        gl.glBegin(GL2.GL_TRIANGLE_FAN);
        gl.glVertex2f(centerX, centerY);
        for (int i = 0; i <= segments; i++) {
            float x = centerX + (float) (radius * Math.cos(angle));
            float y = centerY + (float) (radius * Math.sin(angle));

            if (angle < Math.PI / 3 || angle > 2 * Math.PI / 3) {
                gl.glVertex2f(x, y);
                gl.glVertex2f(x + 0.1f, y + 0.3f);
                gl.glVertex2f(x - 0.1f, y + 0.3f);
            } else {
                gl.glVertex2f(x, y);
                gl.glVertex2f(x + 0.2f, y + 0.2f);
                gl.glVertex2f(x, y + 0.4f);
                gl.glVertex2f(x - 0.2f, y + 0.2f);
            }

            angle += angleIncrement;
        }
        gl.glEnd();
    }

    private void drawDynamicBulb(GL2 gl) {
        new DynamicBulb(gl).draw();
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {}

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(0.9f, 0.9f, 0.9f, 1.0f);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        canvas.setSize(width, height);
    }

    public class DynamicBulb {
        private GLU glu;
        private float lightAngle = 0.0f;
        private boolean flicker = false;
        private GL2 gl;

        public DynamicBulb(GL2 gl) {
            this.gl = gl;
            glu = new GLU();
        }

        public void draw() {
            gl.glEnable(GL2.GL_LIGHTING);
            gl.glEnable(GL2.GL_LIGHT0);
            float[] lightPosition = {0.0f, 3.0f, 0.0f, 1.0f};
            gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPosition, 0);

            float[] ambient = {0.0f, 0.0f, 0.0f, 1.0f};
            float[] diffuse = {1.0f, 1.0f, 0.0f, 1.0f};
            float[] specular = {1.0f, 1.0f, 1.0f, 1.0f};
            float[] shininess = {100.0f};
            gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, ambient, 0);
            gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, diffuse, 0);
            gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, specular, 0);
            gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, shininess, 0);

            gl.glShadeModel(GL2.GL_SMOOTH);

            applyDynamicLighting();

            gl.glColor3f(1.0f, 1.0f, 0.0f);
            GLUquadric quadric = glu.gluNewQuadric();
            glu.gluSphere(quadric, 0.3, 20, 20);
            glu.gluDeleteQuadric(quadric);

            gl.glDisable(GL2.GL_LIGHTING);
        }

        private void applyDynamicLighting() {
            if (flicker) {
                gl.glColor3f(1.0f, 0.6f, 0.0f);
            } else {
                gl.glColor3f(1.0f, 1.0f, 0.0f);
            }

            lightAngle += 0.1f;
            float lightX = (float) Math.sin(Math.toRadians(lightAngle)) * 2.0f;
            float lightZ = (float) Math.cos(Math.toRadians(lightAngle)) * 2.0f;
            float[] lightPosition = {lightX, 3.0f, lightZ, 1.0f};
            gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPosition, 0);

            flicker = !flicker;
        }
    }
}
