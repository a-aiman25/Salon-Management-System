// BelleveSalonApp.java
import javax.swing.*;

public class BelleveSalonApp {
    public static void main(String[] args) {
        // Initialize the database and create tables if they don't exist
        DatabaseManager.initializeDatabase();

        try {
            // Set Nimbus Look and Feel for a modern look
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback to default look and feel if Nimbus is not available
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // Run the Swing UI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            // Start with the splash screen for 5 seconds
            new SplashScreen(5000); // 5000 milliseconds = 5 seconds
        });
    }
}