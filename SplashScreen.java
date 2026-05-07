// SplashScreen.java - Professional Full-Screen Image Only Version
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class SplashScreen extends JWindow implements KeyListener, MouseListener {
    private GraphicsDevice graphicsDevice;
    private Image backgroundImage;
    
    public SplashScreen(int duration) {
        // Get the graphics device for fullscreen
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        graphicsDevice = ge.getDefaultScreenDevice();
        
        // Load the splash image
        loadBackgroundImage();
        
        // Create main panel with image background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (backgroundImage != null) {
                    // Draw the image to fill the entire screen
                    g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    // Fallback elegant gradient if image not found
                    GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(25, 25, 25),
                        getWidth(), getHeight(), new Color(45, 45, 45)
                    );
                    g2d.setPaint(gradient);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                    
                    // Add subtle salon-themed pattern
                    g2d.setColor(new Color(255, 255, 255, 10));
                    for (int i = 0; i < getWidth(); i += 50) {
                        g2d.drawLine(i, 0, i, getHeight());
                    }
                    for (int i = 0; i < getHeight(); i += 50) {
                        g2d.drawLine(0, i, getWidth(), i);
                    }
                }
            }
        };
        
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);
        
        // Add listeners for user interaction
        addKeyListener(this);
        addMouseListener(this);
        setFocusable(true);
        
        // Set fullscreen
        setFullscreen();
        
        // Start timer thread
        startSplashTimer(duration);
    }
    
    private void loadBackgroundImage() {
        try {
            ImageIcon imageIcon = new ImageIcon("D:\\Aiman\\SSUET\\4th Semester\\OS\\OS project\\resurces\\image1.jpeg");
            if (imageIcon.getImageLoadStatus() == MediaTracker.COMPLETE && imageIcon.getIconWidth() > 0) {
                backgroundImage = imageIcon.getImage();
            }
        } catch (Exception e) {
            System.out.println("Could not load splash image: " + e.getMessage());
            backgroundImage = null;
        }
    }
    
    private void setFullscreen() {
        if (graphicsDevice.isFullScreenSupported()) {
            graphicsDevice.setFullScreenWindow(this);
        } else {
            // Fallback to manual fullscreen sizing
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            setBounds(0, 0, screenSize.width, screenSize.height);
            setVisible(true);
        }
        
        // Set cursor to wait cursor
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }
    
    private void startSplashTimer(int duration) {
        new Thread(() -> {
            try {
                Thread.sleep(duration);
                exitSplash();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
    
    private void exitSplash() {
        SwingUtilities.invokeLater(() -> {
            if (graphicsDevice.getFullScreenWindow() == this) {
                graphicsDevice.setFullScreenWindow(null);
            }
            dispose();
            new RoleSelectionPage();
        });
    }
    
    private void forceExit() {
        if (graphicsDevice.getFullScreenWindow() == this) {
            graphicsDevice.setFullScreenWindow(null);
        }
        dispose();
        System.exit(0);
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            forceExit();
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE) {
            exitSplash();
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        exitSplash();
    }
    
    @Override
    public void keyReleased(KeyEvent e) {}
    
    @Override
    public void keyTyped(KeyEvent e) {}
    
    @Override
    public void mousePressed(MouseEvent e) {}
    
    @Override
    public void mouseReleased(MouseEvent e) {}
    
    @Override
    public void mouseEntered(MouseEvent e) {}
    
    @Override
    public void mouseExited(MouseEvent e) {}
}