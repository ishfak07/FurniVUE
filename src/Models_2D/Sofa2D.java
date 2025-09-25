package Models_2D;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Sofa2D extends GLJPanel implements GLEventListener {

    private BufferedImage backgroundImage; // Added field for background image

    public Sofa2D() {
        this.addGLEventListener(this);

        // Load background image
        try {
            backgroundImage = ImageIO.read(new File("background.jpg")); // Change "background.jpg" to your image file path
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        // Set up any initialization here such as the background color
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glClearColor(1, 1, 1, 1); // White background
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();

        // Draw background image
        drawBackground(gl);

        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        Sofa(gl);
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {

    }

    public static void createUIandShow() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("2D Chair Design");
            frame.getContentPane().add(new Sofa2D());
            frame.setSize(640, 480);
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {

                }
            });
            frame.setVisible(true);
            FPSAnimator animator = new FPSAnimator(new Sofa2D(), 60);
            animator.start();
        });
    }

    public static void main(String[] args) {
        createUIandShow();
    }


    public void Sofa(GL2 gl) {
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        // Define colors
        float chairColorR = 0.0f;  // Blue color
        float chairColorG = 0.0f;
        float chairColorB = 1.0f;
        float cushionColorR = 1.0f;  // Light red color
        float cushionColorG = 0.5f;
        float cushionColorB = 0.5f;

        // Define dimensions
        float seatWidth = 1.0f;
        float seatDepth = 0.3f;
        float backrestHeight = 0.3f;
        float legWidth = 0.02f;
        float legHeight = 0.1f;
        float armrestWidth = 0.05f;
        float armrestHeight = 0.1f;
        float cushionThickness = 0.05f;

        // Set chair color
        gl.glColor3f(chairColorR, chairColorG, chairColorB);

        // Seat
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(-seatWidth / 2, -seatDepth / 2);
        gl.glVertex2f(seatWidth / 2, -seatDepth / 2);
        gl.glVertex2f(seatWidth / 2, seatDepth / 2);
        gl.glVertex2f(-seatWidth / 2, seatDepth / 2);
        gl.glEnd();

        // Backrest (slightly angled)
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(-seatWidth / 2, seatDepth / 2);
        gl.glVertex2f(seatWidth / 2, seatDepth / 2);
        gl.glVertex2f(seatWidth / 2 - 0.1f, seatDepth / 2 + backrestHeight); // Angled top
        gl.glVertex2f(-seatWidth / 2 + 0.1f, seatDepth / 2 + backrestHeight);
        gl.glEnd();

        // Legs (including middle legs)
        gl.glBegin(GL2.GL_QUADS);
        // Front left leg
        gl.glVertex2f(-seatWidth / 2, -seatDepth / 2);
        gl.glVertex2f(-seatWidth / 2 + legWidth, -seatDepth / 2);
        gl.glVertex2f(-seatWidth / 2 + legWidth, -seatDepth / 2 - legHeight);
        gl.glVertex2f(-seatWidth / 2, -seatDepth / 2 - legHeight);
        gl.glEnd();

        gl.glBegin(GL2.GL_QUADS);
        // Front right leg
        gl.glVertex2f(seatWidth / 2, -seatDepth / 2);
        gl.glVertex2f(seatWidth / 2 - legWidth, -seatDepth / 2);
        gl.glVertex2f(seatWidth / 2 - legWidth, -seatDepth / 2 - legHeight);
        gl.glVertex2f(seatWidth / 2, -seatDepth / 2 - legHeight);
        gl.glEnd();

        gl.glBegin(GL2.GL_QUADS);
        // Middle left leg
        gl.glVertex2f(-seatWidth / 3, -seatDepth / 2);
        gl.glVertex2f(-seatWidth / 3 + legWidth, -seatDepth / 2);
        gl.glVertex2f(-seatWidth / 3 + legWidth, -seatDepth / 2 - legHeight);
        gl.glVertex2f(-seatWidth / 3, -seatDepth / 2 - legHeight);
        gl.glEnd();

        gl.glBegin(GL2.GL_QUADS);
        // Middle right leg
        gl.glVertex2f(seatWidth / 3, -seatDepth / 2);
        gl.glVertex2f(seatWidth / 3 - legWidth, -seatDepth / 2);
        gl.glVertex2f(seatWidth / 3 - legWidth, -seatDepth / 2 - legHeight);
        gl.glVertex2f(seatWidth / 3, -seatDepth / 2 - legHeight);
        gl.glEnd();

        gl.glBegin(GL2.GL_QUADS);
        // Back left leg
        gl.glVertex2f(-seatWidth / 2, seatDepth / 2);

        gl.glVertex2f(-seatWidth / 2 + legWidth, seatDepth / 2);
        gl.glVertex2f(-seatWidth / 2 + legWidth, seatDepth / 2 - legHeight);
        gl.glVertex2f(-seatWidth / 2, seatDepth / 2 - legHeight);
        gl.glEnd();

        gl.glBegin(GL2.GL_QUADS);
        // Back right leg
        gl.glVertex2f(seatWidth / 2, seatDepth / 2);
        gl.glVertex2f(seatWidth / 2 - legWidth, seatDepth / 2);
        gl.glVertex2f(seatWidth / 2 - legWidth, seatDepth / 2 - legHeight);
        gl.glVertex2f(seatWidth / 2, seatDepth / 2 - legHeight);
        gl.glEnd();

        // Armrests
        // Left armrest
        gl.glColor3f(chairColorR, chairColorG, chairColorB); // Back to chair color for armrests
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(-seatWidth / 2 - armrestWidth, -seatDepth / 4);
        gl.glVertex2f(-seatWidth / 2, -seatDepth / 4);
        gl.glVertex2f(-seatWidth / 2, seatDepth / 4);
        gl.glVertex2f(-seatWidth / 2 - armrestWidth, seatDepth / 4);
        gl.glEnd();

        // Right armrest
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(seatWidth / 2 + armrestWidth, -seatDepth / 4);
        gl.glVertex2f(seatWidth / 2, -seatDepth / 4);
        gl.glVertex2f(seatWidth / 2, seatDepth / 4);
        gl.glVertex2f(seatWidth / 2 + armrestWidth, seatDepth / 4);
        gl.glEnd();

        // Cushion
        gl.glColor3f(cushionColorR, cushionColorG, cushionColorB); // Set cushion color
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(-seatWidth / 2 + cushionThickness, -cushionThickness / 2);
        gl.glVertex2f(seatWidth / 2 - cushionThickness, -cushionThickness / 2);
        gl.glVertex2f(seatWidth / 2 - cushionThickness, cushionThickness / 2);
        gl.glVertex2f(-seatWidth / 2 + cushionThickness, cushionThickness / 2);
        gl.glEnd();
    }

    private void drawBackground(GL2 gl) {
        if (backgroundImage != null) {
            gl.glMatrixMode(GL2.GL_PROJECTION);
            gl.glLoadIdentity();
            gl.glOrtho(0, getWidth(), getHeight(), 0, -1, 1);
            gl.glMatrixMode(GL2.GL_MODELVIEW);
            gl.glLoadIdentity();

            gl.glEnable(GL.GL_TEXTURE_2D);
            gl.glBindTexture(GL.GL_TEXTURE_2D, 1);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
            gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB, backgroundImage.getWidth(), backgroundImage.getHeight(), 0, GL.GL_BGR, GL.GL_UNSIGNED_BYTE, java.nio.ByteBuffer.wrap(getPixels(backgroundImage)));

            gl.glBegin(GL2.GL_QUADS);
            gl.glTexCoord2f(0.0f, 1.0f);
            gl.glVertex2f(0, 0);
            gl.glTexCoord2f(1.0f, 1.0f);
            gl.glVertex2f(getWidth(), 0);
            gl.glTexCoord2f(1.0f, 0.0f);
            gl.glVertex2f(getWidth(), getHeight());
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex2f(0, getHeight());
            gl.glEnd();
            gl.glFlush();

            gl.glDisable(GL.GL_TEXTURE_2D);
        }
    }

    private byte[] getPixels(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        byte[] pixels = new byte[width * height * 3];
        int[] rawPixels = image.getRGB(0, 0, width, height, null, 0, width);
        for (int i = 0; i < rawPixels.length; i++) {
            int pixel = rawPixels[i];
            pixels[i * 3] = (byte) ((pixel >> 16) & 0xFF);
            pixels[i * 3 + 1] = (byte) ((pixel >> 8) & 0xFF);
            pixels[i * 3 + 2] = (byte) (pixel & 0xFF);
        }
        return pixels;
    }



}
