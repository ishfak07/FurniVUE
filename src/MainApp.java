import Dashboard.SelectDashboard;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;

public class MainApp {
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
