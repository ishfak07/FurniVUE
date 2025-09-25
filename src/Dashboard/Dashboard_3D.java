package Dashboard;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.formdev.flatlaf.FlatDarkLaf;

public class Dashboard_3D extends JFrame {
    private JLabel dateLabel;
    private JLabel timeLabel;
    private JLabel profileLabel;
    private JButton logoutButton;

    // Array to store product details
    private String[] productNames = {"Wood Table", "Chair", "Bed"};
    private String[] productImagePaths = {
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSbG2m3f8JuRIVNE5Dbp83bdjOUNDIwKnmiAA&s",
            "https://p.turbosquid.com/ts-thumb/0G/JZURg9/k5/z/png/1652440776/1920x1080/fit_q87/390538398d4972dac09c7427be1638310bf40df3/z.jpg",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTE4Tt93BSqmFy9bvZDgHSG090a95t0J-zPDA&s"
    };
    private String[] productPages = {"Table3D", "Chair3D", "Bed3D"};

    public Dashboard_3D() {
        setTitle("Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1324, 720));

        // Initialize FlatDarkLaf
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // Top Panel for Date, Time, Profile, and Logout
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setBackground(new Color(44, 62, 80));
        add(topPanel, BorderLayout.NORTH);


        // Product Card Panel
        JPanel productCardPanel = new JPanel();
        productCardPanel.setLayout(new GridLayout(2, 3, 10, 10)); // 2 rows, 3 columns
        productCardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        productCardPanel.setBackground(new Color(52, 73, 94));
        add(productCardPanel, BorderLayout.CENTER);

        // Add product cards
        for (int i = 0; i < productNames.length; i++) {
            JPanel cardPanel = createProductCard(productNames[i], productImagePaths[i], productPages[i]);
            productCardPanel.add(cardPanel);
        }

        // Footer Panel
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(44, 62, 80));
        JLabel footerLabel = new JLabel("Footer Text");
        footerLabel.setForeground(Color.WHITE);
        footerLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        footerPanel.add(footerLabel);
        add(footerPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private JPanel createProductCard(String productName, String imagePath, String productPage) {
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
        cardPanel.setLayout(new BorderLayout());
        cardPanel.setBackground(new Color(46, 64, 83));
        cardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel productNameLabel = new JLabel(productName);
        productNameLabel.setForeground(Color.WHITE);
        productNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        cardPanel.add(productNameLabel, BorderLayout.NORTH);

        JLabel productImageLabel = new JLabel();
        cardPanel.add(productImageLabel, BorderLayout.CENTER);

        JButton viewButton = new JButton("View") {
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
        viewButton.setForeground(Color.WHITE);
        viewButton.setFocusPainted(false);
        viewButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        viewButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        viewButton.setContentAreaFilled(false);
        viewButton.setOpaque(false);
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openProductPage(productPage);
            }
        });
        cardPanel.add(viewButton, BorderLayout.SOUTH);

        // Add hover effect
        viewButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                viewButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                viewButton.setCursor(Cursor.getDefaultCursor());
            }
        });

        // Load image in a separate thread
        new Thread(() -> {
            try {
                Image image = ImageIO.read(new URL(imagePath));
                ImageIcon icon = new ImageIcon(image.getScaledInstance(200, 150, Image.SCALE_SMOOTH));
                SwingUtilities.invokeLater(() -> productImageLabel.setIcon(icon));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        return cardPanel;
    }

    private void openProductPage(String productPage) {
        // Construct the file path
        try {
            Class<?> clazz = Class.forName("Models_3D." + productPage);
            Method mainMethod = clazz.getMethod("main", String[].class);
            mainMethod.invoke(null, (Object) new String[0]);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error running class: " + e.getMessage());
        }
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Dashboard_3D().setVisible(true);
            }
        });
    }
}
