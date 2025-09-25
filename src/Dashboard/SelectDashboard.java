package Dashboard;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;

public class SelectDashboard extends JFrame {
    public SelectDashboard() {
        setTitle("Advanced Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(38, 50, 56));

        // Create a main panel with GridBagLayout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(20, 20, 20, 20);
        mainPanel.setBackground(new Color(38, 50, 56));

        // Load images in a separate thread with advanced card design
        loadAdvancedImageCard(mainPanel, gbc, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTsGiNCa6fsfp3NM2VJUD8Sxhx8JGhIBDr5Mw&s", "2D Models");
        loadAdvancedImageCard(mainPanel, gbc, "https://p.turbosquid.com/ts-thumb/1n/yaEdIN/au/01/png/1725768881/300x300/sharp_fit_q85/03ddebf44db719e541fa5f4473abf69860949c47/01.jpg", "3D Models");

        add(mainPanel);
    }

    private void loadAdvancedImageCard(JPanel panel, GridBagConstraints gbc, String imageUrl, String labelText) {
        JPanel cardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(255, 255, 255, 50)); // Glassmorphism effect
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(new Color(46, 64, 83, 200)); // Semi-transparent card background
        cardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel imageLabel = new JLabel();
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel textLabel = new JLabel(labelText);
        textLabel.setForeground(Color.WHITE);
        textLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton button = new JButton("View Models") {
            @Override
            protected void paintComponent(Graphics g) {
                if (getModel().isRollover()) {
                    g.setColor(new Color(70, 130, 180)); // Hover color
                } else {
                    g.setColor(new Color(52, 152, 219)); // Normal color
                }
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g);
            }
        };
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setContentAreaFilled(false);
        button.setOpaque(false);

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setCursor(Cursor.getDefaultCursor());
            }
        });

        // Add components to the card panel
        cardPanel.add(imageLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        cardPanel.add(textLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        cardPanel.add(button);

        // Add card panel to the main panel
        panel.add(cardPanel, gbc);

        // Load image in a separate thread
        new Thread(() -> {
            try {
                Image image = ImageIO.read(new URL(imageUrl));
                ImageIcon icon = new ImageIcon(image.getScaledInstance(200, 200, Image.SCALE_SMOOTH));
                SwingUtilities.invokeLater(() -> imageLabel.setIcon(icon));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        // Add action listener to the button
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if ("2D Models".equals(labelText)) {
                    Dashboard_2D dashboard2D = new Dashboard_2D();
                    dashboard2D.setVisible(true);
                } else if ("3D Models".equals(labelText)) {
                    Dashboard_3D dashboard3D = new Dashboard_3D();
                    dashboard3D.setVisible(true);
                }
            }
        });

        // Move to the next column for the next set of components
        gbc.gridx++;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            SelectDashboard dashboard = new SelectDashboard();
            dashboard.setVisible(true);
        });
    }
}
