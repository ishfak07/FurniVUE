package Models_3D;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
public class Table3D extends JFrame implements GLEventListener, ActionListener {

    private GLU glu;
    private GLUT glut;

    private int prevMouseX;
    private int prevMouseY;
    private float rotateX = 0;
    private float rotateY = 0;

    private JButton uploadButton1;
    private JButton uploadButton2;

    private Texture woodTexture;
    private Texture legTexture;
    private Texture backgroundTexture;

    private GLJPanel gljPanel;
    private JSlider seekBar;



    public Table3D() {
        setTitle("Office Table 3D");
        setSize(1020, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        GLProfile glProfile = GLProfile.getDefault();
        GLCapabilities glCapabilities = new GLCapabilities(glProfile);
        gljPanel = new GLJPanel(glCapabilities);
        gljPanel.addGLEventListener(this);

        gljPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                int deltaX = x - prevMouseX;
                int deltaY = y - prevMouseY;
                rotateX += (float) deltaY;
                rotateY += (float) deltaX;
                prevMouseX = x;
                prevMouseY = y;
                gljPanel.repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                prevMouseX = e.getX();
                prevMouseY = e.getY();
            }


        });

        uploadButton1 = new JButton("Texture 1");
        uploadButton2 = new JButton("Texture 2");

        uploadButton1.addActionListener(this);
        uploadButton2.addActionListener(this);

        JPanel buttonPanel = new JPanel();


        add(buttonPanel, BorderLayout.SOUTH);
        add(gljPanel);

        glu = new GLU();

        seekBar = new JSlider(JSlider.HORIZONTAL, -180, 180, 0); // Range from -180 to 180 with initial value 0
        seekBar.setMajorTickSpacing(85); // Set major tick spacing
        seekBar.setMinorTickSpacing(35); // Set minor tick spacing
        seekBar.setPaintTicks(true); // Display tick marks
        seekBar.setPaintLabels(true); // Display labels
        seekBar.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                if (!source.getValueIsAdjusting()) {
                    // Update rotation based on seek bar value
                    rotateY = (float) source.getValue();
                    gljPanel.repaint();
                }
            }
        });

        JPanel sliderPanel = new JPanel();
        sliderPanel.add(new JLabel("Rotation:"));
        sliderPanel.add(seekBar);
        add(sliderPanel, BorderLayout.NORTH);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Table3D().setVisible(true);
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == uploadButton1) {
            SwingUtilities.invokeLater(() -> {
                try {
                    woodTexture = TextureIO.newTexture(new File(Table3D.class.getResource("images/wood.jpg").toURI()), true);
                    gljPanel.repaint(); // Repaint the panel to apply texture changes
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        } else if (e.getSource() == uploadButton2) {
            SwingUtilities.invokeLater(() -> {
                try {
                    legTexture = TextureIO.newTexture(new File(Table3D.class.getResource("images/metal.jpg").toURI()), true);
                    gljPanel.repaint(); // Repaint the panel to apply texture changes
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        }
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        glut = new GLUT();
        try {
            backgroundTexture = TextureIO.newTexture(new File(Table3D.class.getResource("../images/bg.png").toURI()), true);
            woodTexture = TextureIO.newTexture(new File(Table3D.class.getResource("../images/wood.jpg").toURI()), true);
            legTexture = TextureIO.newTexture(new File(Table3D.class.getResource("../images/metal.jpg").toURI()), true);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        gl.glTranslatef(rotateY / 180.0f, 0, 0);
        gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f); // Set background to white
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        float[] lightAmbient = {0.1f, 0.2f, 0.2f, 1.0f};
        float[] lightDiffuse = {0.7f, 0.4f, 0.7f, 1.0f};
        float[] lightPosition = {5, 5, 5, 1.0f};
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, lightAmbient, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, lightDiffuse, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPosition, 0);
        gl.glEnable(GL2.GL_TEXTURE_2D);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        double aspect = getWidth() / (double) getHeight();
        glu.gluPerspective(25, aspect, 1, 100);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        glu.gluLookAt(5, 5, 5, 0, 0, 0, 0, 1, 0);

        gl.glPushMatrix();
        gl.glLoadIdentity();

        // Draw background without rotation
        gl.glDisable(GL2.GL_LIGHTING); // Disable lighting for background
        backgroundTexture.bind(gl);
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-5.0f, -5.0f, -10.0f);  // Bottom left
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(5.0f, -5.0f, -10.0f);  // Bottom right
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(5.0f, 5.0f, -10.0f);  // Top right
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-5.0f, 5.0f, -10.0f);  // Top left
        gl.glEnd();
        gl.glEnable(GL2.GL_LIGHTING); // Re-enable lighting for the rest of the scene

        gl.glPopMatrix();

        gl.glRotatef(rotateX, 1, 0, 0);
        gl.glRotatef(rotateY, 0, 1, 0);

        if (woodTexture != null) {
            woodTexture.bind(gl);
        }

        float legWidth = 0.1f;
        float legHeight = 0.8f; // Adjust according to your preference
        float legDepth = 0.1f;

// Front face
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, 0.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, 0.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 0.1f, 1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 0.1f, 1.0f);
        gl.glEnd();

// Back face
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(1.0f, 0.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(-1.0f, 0.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(-1.0f, 0.1f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(1.0f, 0.1f, -1.0f);
        gl.glEnd();

// Top face
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, 0.1f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, 0.1f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 0.1f, 1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 0.1f, 1.0f);
        gl.glEnd();

// Right face
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(1.0f, 0.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, 0.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 0.1f, 1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(1.0f, 0.1f, -1.0f);
        gl.glEnd();

// Left face
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, 0.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(-1.0f, 0.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(-1.0f, 0.1f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 0.1f, 1.0f);
        gl.glEnd();

        gl.glPushMatrix();
        gl.glTranslatef(-0.9f, 0.0f, -0.9f);
        gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
        drawChairLegs(gl, 0.05f, 0.8f, 20);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.9f, 0.0f, -0.9f);
        gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
        drawChairLegs(gl, 0.05f, 0.8f, 20);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.9f, 0.0f, 0.9f);
        gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
        drawChairLegs(gl, 0.05f, 0.8f, 20);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-0.9f, 0.0f, 0.9f);
        gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
        drawChairLegs(gl, 0.05f, 0.8f, 20);
        gl.glPopMatrix();

    }
    void drawChairLegs(GL2 gl, float radius, float height, int slices) {
        GLU glu = new GLU();
        GLUquadric quadric = glu.gluNewQuadric();
        glu.gluQuadricTexture(quadric, true);
        glu.gluCylinder(quadric, radius, radius, height, slices, slices);
        glu.gluDeleteQuadric(quadric);
    }


    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glViewport(0, 0, width, height);
    }
}
