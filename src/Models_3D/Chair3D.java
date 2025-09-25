package Models_3D;

import javax.swing.*;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.FPSAnimator;

import java.awt.*;
import java.awt.event.*;

public class Chair3D extends JFrame implements GLEventListener, ActionListener, MouseMotionListener, MouseWheelListener {
    private static final long serialVersionUID = 1L;

    private GLCapabilities capabilities;
    private GLJPanel gljpanel;
    private GLU glu;
    private GLUT glut;
    private FPSAnimator animator;

    // UI Components
    private JButton chooseChairColorButton;
    private JButton chooseWallColorButton;
    private JButton chooseFloorColorButton;
    private JButton toggleLightingButton;
    private JButton resetViewButton;
    private JComboBox<String> lightingPresetCombo;
    private JButton moveForwardButton;
    private JButton moveBackwardButton;
    private JButton moveLeftButton;
    private JButton moveRightButton;
    private JButton rotateLeftButton;
    private JButton rotateRightButton;

    // Material properties
    private float[] chairColor = {0.8f, 0.5f, 0.3f, 1.0f}; // Default chair color: wooden brown
    private float[] wallColor = {0.9f, 0.9f, 1.0f, 1.0f};  // Default wall color: off-white
    private float[] floorColor = {0.6f, 0.6f, 0.6f, 1.0f}; // Default floor color: gray

    // Camera and view parameters
    private float cameraDistance = 10.0f;
    private float rotationX = 30.0f;
    private float rotationY = 45.0f;
    private int lastMouseX, lastMouseY;
    private boolean mousePressed = false;

    // Lighting control
    private boolean lightingEnabled = true;
    private float[] lightPosition = {5.0f, 8.0f, 5.0f, 1.0f};
    private float[] lightAmbient = {0.2f, 0.2f, 0.2f, 1.0f};
    private float[] lightDiffuse = {0.8f, 0.8f, 0.8f, 1.0f};
    private float[] lightSpecular = {1.0f, 1.0f, 1.0f, 1.0f};

    // Material properties
    private float[] matAmbient = {0.2f, 0.2f, 0.2f, 1.0f};
    private float[] matDiffuse = {0.8f, 0.8f, 0.8f, 1.0f};
    private float[] matSpecular = {1.0f, 1.0f, 1.0f, 1.0f};
    private float matShininess = 50.0f;

    // Room dimensions
    private float roomWidth = 8.0f;
    private float roomHeight = 5.0f;
    private float roomDepth = 8.0f;

    // Chair position and rotation
    private float chairX = 0.0f;
    private float chairY = 0.0f;
    private float chairZ = 0.0f;
    private float chairRotation = 0.0f;

    public Chair3D() {
        super("Modern 3D Room with Chair");

        // Initialize OpenGL capabilities
        capabilities = new GLCapabilities(null);
        capabilities.setSampleBuffers(true);
        capabilities.setNumSamples(8); // Better anti-aliasing

        // Create OpenGL panel
        gljpanel = new GLJPanel(capabilities);
        gljpanel.addGLEventListener(this);
        gljpanel.addMouseMotionListener(this);
        gljpanel.addMouseWheelListener(this);

        // Create control panel with modern look
        JPanel controlPanel = new JPanel(new GridBagLayout());
        controlPanel.setBackground(new Color(240, 240, 240));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridwidth = 1;

        // Modern button styling
        chooseChairColorButton = createStyledButton("Chair Color");
        chooseWallColorButton = createStyledButton("Wall Color");
        chooseFloorColorButton = createStyledButton("Floor Color");
        toggleLightingButton = createStyledButton("Toggle Lighting");
        resetViewButton = createStyledButton("Reset View");
        moveForwardButton = createStyledButton("Move Forward");
        moveBackwardButton = createStyledButton("Move Backward");
        moveLeftButton = createStyledButton("Move Left");
        moveRightButton = createStyledButton("Move Right");
        rotateLeftButton = createStyledButton("Rotate Left");
        rotateRightButton = createStyledButton("Rotate Right");

        // Lighting presets combo box
        String[] lightingPresets = {"Default", "Warm", "Cool", "Bright", "Dim"};
        lightingPresetCombo = new JComboBox<>(lightingPresets);
        lightingPresetCombo.setBackground(Color.WHITE);
        lightingPresetCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lightingPresetCombo.addActionListener(this);

        // Add components to control panel
        gbc.gridy = 0;
        controlPanel.add(chooseChairColorButton, gbc);
        gbc.gridy = 1;
        controlPanel.add(chooseWallColorButton, gbc);
        gbc.gridy = 2;
        controlPanel.add(chooseFloorColorButton, gbc);
        gbc.gridy = 3;
        controlPanel.add(toggleLightingButton, gbc);
        gbc.gridy = 4;
        controlPanel.add(resetViewButton, gbc);
        gbc.gridy = 5;
        controlPanel.add(moveForwardButton, gbc);
        gbc.gridy = 6;
        controlPanel.add(moveBackwardButton, gbc);
        gbc.gridy = 7;
        controlPanel.add(moveLeftButton, gbc);
        gbc.gridy = 8;
        controlPanel.add(moveRightButton, gbc);
        gbc.gridy = 9;
        controlPanel.add(rotateLeftButton, gbc);
        gbc.gridy = 10;
        controlPanel.add(rotateRightButton, gbc);
        gbc.gridy = 11;
        controlPanel.add(new JLabel("Lighting Preset:"), gbc);
        gbc.gridy = 12;
        controlPanel.add(lightingPresetCombo, gbc);

        // Add panels to frame
        add(controlPanel, BorderLayout.WEST);
        add(gljpanel, BorderLayout.CENTER);

        // Add mouse listeners for rotation
        gljpanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                lastMouseX = e.getX();
                lastMouseY = e.getY();
                mousePressed = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mousePressed = false;
            }
        });

        // Configure frame with modern look
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(240, 240, 240));

        // Create animator for smooth rendering
        animator = new FPSAnimator(gljpanel, 60);
        animator.start();

        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.addActionListener(this);
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new Chair3D();
        });
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        glu = new GLU();
        glut = new GLUT();

        // Enable depth testing
        gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f); // Darker background
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glDepthFunc(GL2.GL_LEQUAL);

        // Enable lighting with proper settings
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);

        // Enable color tracking for materials
        gl.glEnable(GL2.GL_COLOR_MATERIAL);
        gl.glColorMaterial(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE);

        // Enable smooth shading
        gl.glShadeModel(GL2.GL_SMOOTH);

        // Enable anti-aliasing
        gl.glEnable(GL2.GL_MULTISAMPLE);
        gl.glEnable(GL2.GL_NORMALIZE); // Important for proper lighting with scaled objects

        // Setup light 0 with better parameters
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPosition, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, lightAmbient, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, lightDiffuse, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, lightSpecular, 0);

        // Set global ambient light (not from any particular source)
        float[] globalAmbient = {0.2f, 0.2f, 0.2f, 1.0f};
        gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, globalAmbient, 0);

        // Enable two-sided lighting
        gl.glLightModeli(GL2.GL_LIGHT_MODEL_TWO_SIDE, GL2.GL_TRUE);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        if (animator != null) {
            animator.stop();
        }
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        // Set camera position based on spherical coordinates
        float cameraPosX = cameraDistance * (float)Math.sin(Math.toRadians(rotationY)) * (float)Math.cos(Math.toRadians(rotationX));
        float cameraPosY = cameraDistance * (float)Math.sin(Math.toRadians(rotationX));
        float cameraPosZ = cameraDistance * (float)Math.cos(Math.toRadians(rotationY)) * (float)Math.cos(Math.toRadians(rotationX));

        glu.gluLookAt(
                cameraPosX, cameraPosY, cameraPosZ,  // Camera position
                0.0, 0.0, 0.0,                      // Look at point
                0.0, 1.0, 0.0                       // Up vector
        );

        // Toggle lighting if requested
        if (lightingEnabled) {
            gl.glEnable(GL2.GL_LIGHTING);
            gl.glEnable(GL2.GL_LIGHT0);
        } else {
            gl.glDisable(GL2.GL_LIGHTING);
        }

        // Draw coordinate axes for reference (optional)
        drawAxes(gl);

        // Draw the scene
        drawRoom(gl);
        drawChair(gl);

        // Draw light source position as a small sphere
        if (lightingEnabled) {
            gl.glDisable(GL2.GL_LIGHTING);
            gl.glColor3f(1.0f, 1.0f, 0.0f); // Yellow
            gl.glPushMatrix();
            gl.glTranslatef(lightPosition[0], lightPosition[1], lightPosition[2]);
            glut.glutSolidSphere(0.1, 16, 16);
            gl.glPopMatrix();
            gl.glEnable(GL2.GL_LIGHTING);
        }

        gl.glFlush();
    }

    private void drawAxes(GL2 gl) {
        // Draw coordinate axes (X-red, Y-green, Z-blue)
        gl.glDisable(GL2.GL_LIGHTING);
        gl.glBegin(GL2.GL_LINES);

        // X axis (red)
        gl.glColor3f(1.0f, 0.0f, 0.0f);
        gl.glVertex3f(0.0f, 0.0f, 0.0f);
        gl.glVertex3f(2.0f, 0.0f, 0.0f);

        // Y axis (green)
        gl.glColor3f(0.0f, 1.0f, 0.0f);
        gl.glVertex3f(0.0f, 0.0f, 0.0f);
        gl.glVertex3f(0.0f, 2.0f, 0.0f);

        // Z axis (blue)
        gl.glColor3f(0.0f, 0.0f, 1.0f);
        gl.glVertex3f(0.0f, 0.0f, 0.0f);
        gl.glVertex3f(0.0f, 0.0f, 2.0f);

        gl.glEnd();
        gl.glEnable(GL2.GL_LIGHTING);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        float aspect = (float) width / (float) height;
        glu.gluPerspective(45.0, aspect, 0.1, 100.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    private void drawRoom(GL2 gl) {
        // Draw floor
        gl.glPushMatrix();
        setMaterial(gl, floorColor, 0.3f);
        gl.glTranslatef(0.0f, -roomHeight/2.0f, 0.0f);
        gl.glScalef(roomWidth, 0.1f, roomDepth);
        gl.glColor3fv(floorColor, 0);
        drawCube(gl);
        gl.glPopMatrix();

        // Draw back wall
        gl.glPushMatrix();
        setMaterial(gl, wallColor, 0.2f);
        gl.glTranslatef(0.0f, 0.0f, -roomDepth/2.0f);
        gl.glScalef(roomWidth, roomHeight, 0.1f);
        gl.glColor3fv(wallColor, 0);
        drawCube(gl);
        gl.glPopMatrix();

        // Draw left wall
        gl.glPushMatrix();
        setMaterial(gl, wallColor, 0.2f);
        gl.glTranslatef(-roomWidth/2.0f, 0.0f, 0.0f);
        gl.glScalef(0.1f, roomHeight, roomDepth);
        gl.glColor3fv(wallColor, 0);
        drawCube(gl);
        gl.glPopMatrix();

        // Draw right wall
        gl.glPushMatrix();
        setMaterial(gl, wallColor, 0.2f);
        gl.glTranslatef(roomWidth/2.0f, 0.0f, 0.0f);
        gl.glScalef(0.1f, roomHeight, roomDepth);
        gl.glColor3fv(wallColor, 0);
        drawCube(gl);
        gl.glPopMatrix();
    }

    private void drawCube(GL2 gl) {
        // Draw a unit cube centered at origin with proper normals
        gl.glBegin(GL2.GL_QUADS);

        // Front face
        gl.glNormal3f(0.0f, 0.0f, 1.0f);
        gl.glVertex3f(-0.5f, -0.5f, 0.5f);
        gl.glVertex3f(0.5f, -0.5f, 0.5f);
        gl.glVertex3f(0.5f, 0.5f, 0.5f);
        gl.glVertex3f(-0.5f, 0.5f, 0.5f);

        // Back face
        gl.glNormal3f(0.0f, 0.0f, -1.0f);
        gl.glVertex3f(-0.5f, -0.5f, -0.5f);
        gl.glVertex3f(-0.5f, 0.5f, -0.5f);
        gl.glVertex3f(0.5f, 0.5f, -0.5f);
        gl.glVertex3f(0.5f, -0.5f, -0.5f);

        // Top face
        gl.glNormal3f(0.0f, 1.0f, 0.0f);
        gl.glVertex3f(-0.5f, 0.5f, -0.5f);
        gl.glVertex3f(-0.5f, 0.5f, 0.5f);
        gl.glVertex3f(0.5f, 0.5f, 0.5f);
        gl.glVertex3f(0.5f, 0.5f, -0.5f);

        // Bottom face
        gl.glNormal3f(0.0f, -1.0f, 0.0f);
        gl.glVertex3f(-0.5f, -0.5f, -0.5f);
        gl.glVertex3f(0.5f, -0.5f, -0.5f);
        gl.glVertex3f(0.5f, -0.5f, 0.5f);
        gl.glVertex3f(-0.5f, -0.5f, 0.5f);

        // Right face
        gl.glNormal3f(1.0f, 0.0f, 0.0f);
        gl.glVertex3f(0.5f, -0.5f, -0.5f);
        gl.glVertex3f(0.5f, 0.5f, -0.5f);
        gl.glVertex3f(0.5f, 0.5f, 0.5f);
        gl.glVertex3f(0.5f, -0.5f, 0.5f);

        // Left face
        gl.glNormal3f(-1.0f, 0.0f, 0.0f);
        gl.glVertex3f(-0.5f, -0.5f, -0.5f);
        gl.glVertex3f(-0.5f, -0.5f, 0.5f);
        gl.glVertex3f(-0.5f, 0.5f, 0.5f);
        gl.glVertex3f(-0.5f, 0.5f, -0.5f);

        gl.glEnd();
    }

    private void drawChair(GL2 gl) {
        // Draw chair positioned on the floor in the room
        gl.glPushMatrix();
        gl.glTranslatef(chairX, -roomHeight/2.0f + 0.8f, chairZ); // Position chair on the floor
        gl.glRotatef(chairRotation, 0.0f, 1.0f, 0.0f); // Rotate chair

        // Draw seat
        setMaterial(gl, chairColor, 0.8f);
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 0.0f, 0.0f);
        gl.glScalef(1.5f, 0.1f, 1.5f);
        gl.glColor3fv(chairColor, 0);
        drawCube(gl);
        gl.glPopMatrix();

        // Draw backrest
        drawBackrest(gl);

        // Draw legs
        drawLegs(gl);

        gl.glPopMatrix();
    }

    private void drawBackrest(GL2 gl) {
        setMaterial(gl, chairColor, 0.8f);

        // Draw backrest
        float backrestWidth = 1.5f;
        float backrestHeight = 1.5f;
        float backrestDepth = 0.1f;

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 0.8f, -0.75f);
        gl.glScalef(backrestWidth, backrestHeight, backrestDepth);
        gl.glColor3fv(chairColor, 0);
        drawCube(gl);
        gl.glPopMatrix();

        // Draw decorative bars in the backrest
        float barWidth = 0.05f;
        float barHeight = 1.2f;
        float barSpacing = 0.2f;
        int numBars = 6;

        gl.glPushMatrix();
        gl.glTranslatef(-0.6f, 0.8f, -0.75f);
        for (int i = 0; i < numBars; i++) {
            gl.glPushMatrix();
            gl.glTranslatef(i * barSpacing, 0.0f, 0.0f);
            gl.glScalef(barWidth, barHeight, backrestDepth + 0.05f);
            drawCube(gl);
            gl.glPopMatrix();
        }
        gl.glPopMatrix();
    }

    private void drawLegs(GL2 gl) {
        setMaterial(gl, chairColor, 0.5f);

        // Draw legs
        float legHeight = 0.7f;
        float legWidth = 0.1f;

        // Front left leg
        gl.glPushMatrix();
        gl.glTranslatef(-0.6f, -0.4f, 0.6f);
        gl.glScalef(legWidth, legHeight, legWidth);
        gl.glColor3fv(chairColor, 0);
        drawCube(gl);
        gl.glPopMatrix();

        // Front right leg
        gl.glPushMatrix();
        gl.glTranslatef(0.6f, -0.4f, 0.6f);
        gl.glScalef(legWidth, legHeight, legWidth);
        gl.glColor3fv(chairColor, 0);
        drawCube(gl);
        gl.glPopMatrix();

        // Back left leg
        gl.glPushMatrix();
        gl.glTranslatef(-0.6f, -0.4f, -0.6f);
        gl.glScalef(legWidth, legHeight, legWidth);
        gl.glColor3fv(chairColor, 0);
        drawCube(gl);
        gl.glPopMatrix();

        // Back right leg
        gl.glPushMatrix();
        gl.glTranslatef(0.6f, -0.4f, -0.6f);
        gl.glScalef(legWidth, legHeight, legWidth);
        gl.glColor3fv(chairColor, 0);
        drawCube(gl);
        gl.glPopMatrix();
    }

    private void setMaterial(GL2 gl, float[] color, float shininess) {
        // Set material properties with proper scaling
        float[] ambient = {color[0] * 0.2f, color[1] * 0.2f, color[2] * 0.2f, color[3]};
        float[] diffuse = {color[0] * 0.8f, color[1] * 0.8f, color[2] * 0.8f, color[3]};
        float[] specular = {0.5f, 0.5f, 0.5f, 1.0f}; // Less shiny specular

        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, ambient, 0);
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, diffuse, 0);
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, specular, 0);
        gl.glMaterialf(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, shininess * 128.0f);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == chooseChairColorButton) {
            Color color = JColorChooser.showDialog(this, "Choose Chair Color", new Color(
                    (int)(chairColor[0] * 255),
                    (int)(chairColor[1] * 255),
                    (int)(chairColor[2] * 255)));
            if (color != null) {
                chairColor[0] = color.getRed() / 255.0f;
                chairColor[1] = color.getGreen() / 255.0f;
                chairColor[2] = color.getBlue() / 255.0f;
                gljpanel.repaint();
            }
        } else if (e.getSource() == chooseWallColorButton) {
            Color color = JColorChooser.showDialog(this, "Choose Wall Color", new Color(
                    (int)(wallColor[0] * 255),
                    (int)(wallColor[1] * 255),
                    (int)(wallColor[2] * 255)));
            if (color != null) {
                wallColor[0] = color.getRed() / 255.0f;
                wallColor[1] = color.getGreen() / 255.0f;
                wallColor[2] = color.getBlue() / 255.0f;
                gljpanel.repaint();
            }
        } else if (e.getSource() == chooseFloorColorButton) {
            Color color = JColorChooser.showDialog(this, "Choose Floor Color", new Color(
                    (int)(floorColor[0] * 255),
                    (int)(floorColor[1] * 255),
                    (int)(floorColor[2] * 255)));
            if (color != null) {
                floorColor[0] = color.getRed() / 255.0f;
                floorColor[1] = color.getGreen() / 255.0f;
                floorColor[2] = color.getBlue() / 255.0f;
                gljpanel.repaint();
            }
        } else if (e.getSource() == toggleLightingButton) {
            lightingEnabled = !lightingEnabled;
            gljpanel.repaint();
        } else if (e.getSource() == resetViewButton) {
            rotationX = 30.0f;
            rotationY = 45.0f;
            cameraDistance = 10.0f;
            gljpanel.repaint();
        } else if (e.getSource() == lightingPresetCombo) {
            String preset = (String)lightingPresetCombo.getSelectedItem();
            switch(preset) {
                case "Warm":
                    lightAmbient = new float[]{0.3f, 0.2f, 0.1f, 1.0f};
                    lightDiffuse = new float[]{0.8f, 0.6f, 0.4f, 1.0f};
                    lightSpecular = new float[]{1.0f, 0.9f, 0.8f, 1.0f};
                    break;
                case "Cool":
                    lightAmbient = new float[]{0.1f, 0.1f, 0.3f, 1.0f};
                    lightDiffuse = new float[]{0.4f, 0.4f, 0.8f, 1.0f};
                    lightSpecular = new float[]{0.8f, 0.8f, 1.0f, 1.0f};
                    break;
                case "Bright":
                    lightAmbient = new float[]{0.4f, 0.4f, 0.4f, 1.0f};
                    lightDiffuse = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
                    lightSpecular = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
                    break;
                case "Dim":
                    lightAmbient = new float[]{0.1f, 0.1f, 0.1f, 1.0f};
                    lightDiffuse = new float[]{0.4f, 0.4f, 0.4f, 1.0f};
                    lightSpecular = new float[]{0.5f, 0.5f, 0.5f, 1.0f};
                    break;
                default: // "Default"
                    lightAmbient = new float[]{0.2f, 0.2f, 0.2f, 1.0f};
                    lightDiffuse = new float[]{0.8f, 0.8f, 0.8f, 1.0f};
                    lightSpecular = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
            }

            GL2 gl = gljpanel.getGL().getGL2();
            gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, lightAmbient, 0);
            gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, lightDiffuse, 0);
            gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, lightSpecular, 0);
            gljpanel.repaint();
        } else if (e.getSource() == moveForwardButton) {
            chairZ += 0.2f;
            gljpanel.repaint();
        } else if (e.getSource() == moveBackwardButton) {
            chairZ -= 0.2f;
            gljpanel.repaint();
        } else if (e.getSource() == moveLeftButton) {
            chairX -= 0.2f;
            gljpanel.repaint();
        } else if (e.getSource() == moveRightButton) {
            chairX += 0.2f;
            gljpanel.repaint();
        } else if (e.getSource() == rotateLeftButton) {
            chairRotation += 5.0f;
            gljpanel.repaint();
        } else if (e.getSource() == rotateRightButton) {
            chairRotation -= 5.0f;
            gljpanel.repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (mousePressed) {
            int deltaX = e.getX() - lastMouseX;
            int deltaY = e.getY() - lastMouseY;

            rotationY += deltaX * 0.5f;
            rotationX += deltaY * 0.5f;

            // Limit rotation on X axis to avoid flipping
            if (rotationX > 89.0f) rotationX = 89.0f;
            if (rotationX < -89.0f) rotationX = -89.0f;

            lastMouseX = e.getX();
            lastMouseY = e.getY();

            gljpanel.repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // Not used
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        cameraDistance += e.getWheelRotation() * 0.5f;

        if (cameraDistance < 3.0f) cameraDistance = 3.0f;
        if (cameraDistance > 20.0f) cameraDistance = 20.0f;

        gljpanel.repaint();
    }
}
