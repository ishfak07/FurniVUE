package Models_3D;

import javax.swing.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import java.awt.*;
import java.io.IOException;

public class Bed3D extends JFrame implements GLEventListener {
    private GLCapabilities capabilities;
    private GLJPanel gljpanel;
    private GLU glu;
    private GLUT glut;
    private Texture bedTexture;
    private Texture pillowTexture;
    private float bedWidth = 2.5f;
    private float bedLength = 2.0f;
    private float bedHeight = 0.2f;
    private float legHeight = 0.25f;

    public Bed3D() {
        super(" 3D Bed Model");
        capabilities = new GLCapabilities(null);
        gljpanel = new GLJPanel(capabilities);
        gljpanel.addGLEventListener(this);
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());


        JLabel widthLabel = new JLabel("Width:");
        JTextField widthField = new JTextField(5);
        widthField.setText(String.valueOf(bedWidth));
        widthField.addActionListener(e -> bedWidth = Float.parseFloat(widthField.getText()));

        JLabel lengthLabel = new JLabel("Length:");
        JTextField lengthField = new JTextField(5);
        lengthField.setText(String.valueOf(bedLength));
        lengthField.addActionListener(e -> bedLength = Float.parseFloat(lengthField.getText()));

        JLabel heightLabel = new JLabel("Height:");
        JTextField heightField = new JTextField(5);
        heightField.setText(String.valueOf(bedHeight));
        heightField.addActionListener(e -> bedHeight = Float.parseFloat(heightField.getText()));

        controlPanel.add(widthLabel);
        controlPanel.add(widthField);
        controlPanel.add(lengthLabel);
        controlPanel.add(lengthField);
        controlPanel.add(heightLabel);
        controlPanel.add(heightField);

        add(controlPanel, BorderLayout.NORTH);
        add(gljpanel, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Bed3D();
    }


    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        glu = new GLU();
        glut = new GLUT();
        gl.glClearColor(0.8f, 0.8f, 1.0f, 1.0f);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        gl.glEnable(GL2.GL_TEXTURE_2D);
        try {
            bedTexture = TextureIO.newTexture(getClass().getResourceAsStream("/images/bed_texture.jpg"), true, "jpg");
            pillowTexture = TextureIO.newTexture(getClass().getResourceAsStream("/images/pillow_texture.jpeg"), true, "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }


    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        glu.gluLookAt(3, 3, 3, 0, 0, 0, 0, 1, 0); // Set up the camera position

        // Set up lighting
        float[] lightPosition = {3, 3, 3, 1};
        float[] lightColor = {1, 1, 1, 1};
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPosition, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, lightColor, 0);

        // Draw bed
        drawBed(gl);
        drawPillow(gl);
        gl.glFlush();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        double aspect = (double) width / height;
        glu.gluPerspective(45.0, aspect, 1.0, 10.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    private void drawBed(GL2 gl) {
        // Draw bed frame with texture
        bedTexture.enable(gl);
        bedTexture.bind(gl);
        gl.glColor3f(1.0f, 1.0f, 1.0f);

        // Headboard
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, bedHeight / 2, -bedLength / 2); // Translate to headboard position
        gl.glScalef(bedWidth, bedHeight, 0.1f); // Scale the headboard
        glut.glutSolidCube(1.0f); // Draw the headboard
        gl.glPopMatrix();

        // Footboard
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, bedHeight / 2, bedLength / 2); // Translate to footboard position
        gl.glScalef(bedWidth, bedHeight, 0.1f); // Scale the footboard
        glut.glutSolidCube(1.0f); // Draw the footboard
        gl.glPopMatrix();

        // Side rails
        gl.glPushMatrix();
        gl.glTranslatef(-bedWidth / 2, bedHeight / 2, 0.0f); // Translate to left side rail position
        gl.glScalef(0.1f, bedHeight, bedLength); // Scale the left side rail
        glut.glutSolidCube(1.0f); // Draw the left side rail
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(bedWidth / 2, bedHeight / 2, 0.0f); // Translate to right side rail position
        gl.glScalef(0.1f, bedHeight, bedLength); // Scale the right side rail
        glut.glutSolidCube(1.0f); // Draw the right side rail
        gl.glPopMatrix();

        bedTexture.disable(gl);

        // Draw legs
        gl.glColor3f(0.5f, 0.3f, 0.0f); // Brown color for the legs
        float legWidth = 0.1f;
        gl.glPushMatrix();
        gl.glTranslatef(-bedWidth / 2 + legWidth / 2, -bedHeight / 2 + -0.3f / 2, -bedLength / 2 + legWidth / 2); // Front left leg
        gl.glScalef(legWidth, 1f, legWidth);
        glut.glutSolidCube(0.3f);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(bedWidth / 2 - legWidth / 2, -bedHeight / 2 + -0.3f / 2, -bedLength / 2 + legWidth / 2); // Front right leg
        gl.glScalef(legWidth, 0.3f, legWidth);
        glut.glutSolidCube(1.0f);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-bedWidth / 2 + legWidth / 2, -bedHeight / 2 + -0.3f / 2, bedLength / 2 - legWidth / 2); // Back left leg
        gl.glScalef(legWidth, 0.3f, legWidth);
        glut.glutSolidCube(1.0f);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(bedWidth / 2 - legWidth / 2, -bedHeight / 2 + -0.3f / 2, bedLength / 2 - legWidth / 2); // Back right leg
        gl.glScalef(legWidth, 0.3f, legWidth);
        glut.glutSolidCube(1.0f);
        gl.glPopMatrix();

        // Draw mattress
        gl.glColor3f(1.0f, 1.0f, 1.0f); // White color for the mattress
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, bedHeight, 0.0f); // Translate to mattress position
        gl.glScalef(bedWidth - 0.2f, 0.1f, bedLength - 0.2f); // Scale the mattress
        glut.glutSolidCube(1.0f); // Draw the mattress
        gl.glPopMatrix();
    }

    private void drawPillow(GL2 gl) {
        // Draw pillow with texture
        pillowTexture.enable(gl);
        pillowTexture.bind(gl);
        gl.glColor3f(1.0f, 1.0f, 1.0f);

        float pillowWidth = bedWidth * 0.2f;
        float pillowLength = bedLength * 0.4f;
        float pillowHeight = bedHeight * 0.3f;
        float cornerRadius = pillowWidth * 0.1f;

        gl.glPushMatrix();
        gl.glTranslatef(-1.0f, bedHeight + pillowHeight / 2, bedLength / 5); // Adjust the position of the pillow

        // Draw pillow body
        drawPillowShape(gl, pillowWidth, pillowHeight, pillowLength, cornerRadius);

        gl.glPopMatrix();
        pillowTexture.disable(gl);
    }


    private void drawPillowShape(GL2 gl, float width, float height, float depth, float cornerRadius) {
        gl.glBegin(GL2.GL_QUADS);

        // Front face
        gl.glTexCoord2f(0, 0); gl.glVertex3f(0, 0, 0);
        gl.glTexCoord2f(1, 0); gl.glVertex3f(width, 0, 0);
        gl.glTexCoord2f(1, 1); gl.glVertex3f(width, height, 0);
        gl.glTexCoord2f(0, 1); gl.glVertex3f(0, height, 0);

        // Back face
        gl.glTexCoord2f(0, 0); gl.glVertex3f(0, 0, -depth);
        gl.glTexCoord2f(0, 1); gl.glVertex3f(0, height, -depth);
        gl.glTexCoord2f(1, 1); gl.glVertex3f(width, height, -depth);
        gl.glTexCoord2f(1, 0); gl.glVertex3f(width, 0, -depth);

        // Top face
        gl.glTexCoord2f(0, 0); gl.glVertex3f(0, height, 0);
        gl.glTexCoord2f(1, 0); gl.glVertex3f(width, height, 0);
        gl.glTexCoord2f(1, 1); gl.glVertex3f(width, height, -depth);
        gl.glTexCoord2f(0, 1); gl.glVertex3f(0, height, -depth);

        // Bottom face
        gl.glTexCoord2f(0, 0); gl.glVertex3f(0, 0, 0);
        gl.glTexCoord2f(0, 1); gl.glVertex3f(0, 0, -depth);
        gl.glTexCoord2f(1, 1); gl.glVertex3f(width, 0, -depth);
        gl.glTexCoord2f(1, 0); gl.glVertex3f(width, 0, 0);
        gl.glEnd();
    }
}
