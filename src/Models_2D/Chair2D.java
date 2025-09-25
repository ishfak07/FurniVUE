package Models_2D;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Chair2D extends GLJPanel implements GLEventListener {

    public Chair2D() {
        this.addGLEventListener(this);
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

        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        Chair(gl);
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {

    }

    public static void createUIandShow() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("2D Chair Design");
            frame.getContentPane().add(new Chair2D());
            frame.setSize(640, 480);
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {

                }
            });
            frame.setVisible(true);
            FPSAnimator animator = new FPSAnimator(new Chair2D(), 60);
            animator.start();
        });
    }

    public static void main(String[] args) {
        createUIandShow();
    }

    public void Chair(GL2 gl) {
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        // Draw a more advanced chair

        // Define colors
        float chairColorR = 0.3f;
        float chairColorG = 0.6f;
        float chairColorB = 0.8f;

        // Define dimensions
        float seatWidth = 0.4f;
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

        // Backrest
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(-seatWidth / 2, seatDepth / 2);
        gl.glVertex2f(seatWidth / 2, seatDepth / 2);
        gl.glVertex2f(seatWidth / 2, seatDepth / 2 + backrestHeight);
        gl.glVertex2f(-seatWidth / 2, seatDepth / 2 + backrestHeight);
        gl.glEnd();

        // Legs
        // Front left leg
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(-seatWidth / 2, -seatDepth / 2);
        gl.glVertex2f(-seatWidth / 2 + legWidth, -seatDepth / 2);
        gl.glVertex2f(-seatWidth / 2 + legWidth, -seatDepth / 2 - legHeight);
        gl.glVertex2f(-seatWidth / 2, -seatDepth / 2 - legHeight);
        gl.glEnd();

        // Front right leg
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(seatWidth / 2, -seatDepth / 2);
        gl.glVertex2f(seatWidth / 2 - legWidth, -seatDepth / 2);
        gl.glVertex2f(seatWidth / 2 - legWidth, -seatDepth / 2 - legHeight);
        gl.glVertex2f(seatWidth / 2, -seatDepth / 2 - legHeight);
        gl.glEnd();

        // Back left leg
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(-seatWidth / 2, seatDepth / 2);
        gl.glVertex2f(-seatWidth / 2 + legWidth, seatDepth / 2);
        gl.glVertex2f(-seatWidth / 2 + legWidth, seatDepth / 2 - legHeight);
        gl.glVertex2f(-seatWidth / 2, seatDepth / 2 - legHeight);
        gl.glEnd();

        // Back right leg
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(seatWidth / 2, seatDepth / 2);
        gl.glVertex2f(seatWidth / 2 - legWidth, seatDepth / 2);
        gl.glVertex2f(seatWidth / 2 - legWidth, seatDepth / 2 - legHeight);
        gl.glVertex2f(seatWidth / 2, seatDepth / 2 - legHeight);
        gl.glEnd();

        // Armrests
        // Left armrest
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
        gl.glColor3f(1.0f, 0.8f, 0.6f); // Beige color for cushion
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(-seatWidth / 2, -cushionThickness / 2);
        gl.glVertex2f(seatWidth / 2, -cushionThickness / 2);
        gl.glVertex2f(seatWidth / 2, cushionThickness / 2);
        gl.glVertex2f(-seatWidth / 2, cushionThickness / 2);
        gl.glEnd();
    }


}