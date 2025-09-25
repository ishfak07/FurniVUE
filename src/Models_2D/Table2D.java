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

public class Table2D extends GLJPanel implements GLEventListener {

    public Table2D() {
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
        drawTable(gl);
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {

    }

    public static void createUIandShow() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("2D Table Design");
            frame.getContentPane().add(new Table2D());
            frame.setSize(640, 480);
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {

                }
            });
            frame.setVisible(true);
            FPSAnimator animator = new FPSAnimator(new Table2D(), 60);
            animator.start();
        });
    }

    public static void main(String[] args) {
        createUIandShow();
    }

    public void drawTable(GL2 gl) {
        // Define colors
        float tableColorR = 0.6f;
        float tableColorG = 0.3f;
        float tableColorB = 0.1f;

        // Define dimensions
        float tableWidth = 0.8f;
        float tableHeight = 0.08f;
        float legWidth = 0.05f;
        float legHeight = 0.3f;

        // Set table color
        gl.glColor3f(tableColorR, tableColorG, tableColorB);

        // Table top
        // Draw thicker top with rounded corners
        float cornerRadius = 0.05f;
        gl.glBegin(GL2.GL_QUADS);
        // Top side
        gl.glVertex2f(-tableWidth / 2 + cornerRadius, -tableHeight / 2);
        gl.glVertex2f(tableWidth / 2 - cornerRadius, -tableHeight / 2);
        gl.glVertex2f(tableWidth / 2, tableHeight / 2);
        gl.glVertex2f(-tableWidth / 2, tableHeight / 2);
        // Bottom side
        gl.glVertex2f(-tableWidth / 2, -tableHeight / 2);
        gl.glVertex2f(tableWidth / 2, -tableHeight / 2);
        gl.glVertex2f(tableWidth / 2, -tableHeight / 2 - cornerRadius);
        gl.glVertex2f(-tableWidth / 2, -tableHeight / 2 - cornerRadius);
        // Left side
        gl.glVertex2f(-tableWidth / 2, -tableHeight / 2);
        gl.glVertex2f(-tableWidth / 2, tableHeight / 2);
        gl.glVertex2f(-tableWidth / 2 + cornerRadius, tableHeight / 2);
        gl.glVertex2f(-tableWidth / 2 + cornerRadius, -tableHeight / 2);
        // Right side
        gl.glVertex2f(tableWidth / 2, -tableHeight / 2);
        gl.glVertex2f(tableWidth / 2, tableHeight / 2);
        gl.glVertex2f(tableWidth / 2 - cornerRadius, tableHeight / 2);
        gl.glVertex2f(tableWidth / 2 - cornerRadius, -tableHeight / 2);
        gl.glEnd();

        // Draw decorative pattern on the table top
        drawDecorativePattern(gl, -tableWidth / 2 + 0.1f, -tableHeight / 2 + 0.02f, tableWidth - 0.2f, tableHeight - 0.04f);

        // Table legs
        // Front left leg
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(-tableWidth / 2, -tableHeight / 2);
        gl.glVertex2f(-tableWidth / 2 + legWidth, -tableHeight / 2);
        gl.glVertex2f(-tableWidth / 2 + legWidth, -tableHeight / 2 - legHeight);
        gl.glVertex2f(-tableWidth / 2, -tableHeight / 2 - legHeight);
        gl.glEnd();

        // Front right leg
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(tableWidth / 2, -tableHeight / 2);
        gl.glVertex2f(tableWidth / 2 - legWidth, -tableHeight / 2);
        gl.glVertex2f(tableWidth / 2 - legWidth, -tableHeight / 2 - legHeight);
        gl.glVertex2f(tableWidth / 2, -tableHeight / 2 - legHeight);
        gl.glEnd();

        // Back left leg
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(-tableWidth / 2, tableHeight / 2);
        gl.glVertex2f(-tableWidth / 2 + legWidth, tableHeight / 2);
        gl.glVertex2f(-tableWidth / 2 + legWidth, tableHeight / 2 - legHeight);
        gl.glVertex2f(-tableWidth / 2, tableHeight / 2 - legHeight);
        gl.glEnd();

        // Back right leg
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(tableWidth / 2, tableHeight / 2);
        gl.glVertex2f(tableWidth / 2 - legWidth, tableHeight / 2);
        gl.glVertex2f(tableWidth / 2 - legWidth, tableHeight / 2 - legHeight);
        gl.glVertex2f(tableWidth / 2, tableHeight / 2 - legHeight);
        gl.glEnd();
    }

    private void drawDecorativePattern(GL2 gl, float x, float y, float width, float height) {
        // Draw a simple grid pattern for demonstration
        int numRows = 5;
        int numCols = 5;
        float cellWidth = width / numCols;
        float cellHeight = height / numRows;

        // Set color for pattern
        gl.glColor3f(0.8f, 0.8f, 0.8f);

        // Draw vertical lines
        for (int i = 0; i < numCols - 1; i++) {
            float xPos = x + (i + 1) * cellWidth;
            gl.glBegin(GL2.GL_LINES);
            gl.glVertex2f(xPos, y);
            gl.glVertex2f(xPos, y + height);
            gl.glEnd();
        }

        // Draw horizontal lines
        for (int i = 0; i < numRows - 1; i++) {
            float yPos = y + (i + 1) * cellHeight;
            gl.glBegin(GL2.GL_LINES);
            gl.glVertex2f(x, yPos);
            gl.glVertex2f(x + width, yPos);
            gl.glEnd();
        }
    }
}
