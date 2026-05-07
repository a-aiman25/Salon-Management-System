// RoleSelectionPage.java - Purple Theme Version
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class RoleSelectionPage extends JFrame implements KeyListener {
    private GraphicsDevice graphicsDevice;
    private Image backgroundImage;
    
    public RoleSelectionPage() {
        // Get the graphics device for fullscreen
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        graphicsDevice = ge.getDefaultScreenDevice();
        
        setTitle("Belleve Salon - Role Selection");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setUndecorated(true); // Remove window decorations for professional look
        
        // Load background image
        loadBackgroundImage();
        
        // Create main panel with purple theme background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (backgroundImage != null) {
                    // Draw background image
                    g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                    
                    // Add purple overlay
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f));
                    GradientPaint overlay = new GradientPaint(
                        0, 0, new Color(75, 0, 130, 150),  // Indigo purple
                        0, getHeight(), new Color(25, 25, 25, 200)  // Dark purple/black
                    );
                    g2d.setPaint(overlay);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                } else {
                    // Purple gradient background
                    GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(75, 0, 130),      // Indigo
                        getWidth(), getHeight(), new Color(25, 25, 25)  // Almost black
                    );
                    g2d.setPaint(gradient);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
                
                // Add subtle pattern with purple tint
                g2d.setColor(new Color(186, 85, 211, 15)); // Medium orchid with low opacity
                for (int i = 0; i < getWidth(); i += 80) {
                    g2d.drawLine(i, 0, i, getHeight());
                }
                for (int i = 0; i < getHeight(); i += 80) {
                    g2d.drawLine(0, i, getWidth(), i);
                }
            }
        };
        mainPanel.setLayout(new BorderLayout());
        
        // Create header panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Create center panel with buttons
        JPanel centerPanel = createCenterPanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Create footer panel
        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Add key listener for ESC key
        addKeyListener(this);
        setFocusable(true);
        
        // Set fullscreen
        setFullscreen();
    }
    
    private void loadBackgroundImage() {
        try {
            ImageIcon imageIcon = new ImageIcon("resurces/salon.jpg");
            if (imageIcon.getImageLoadStatus() == MediaTracker.COMPLETE && imageIcon.getIconWidth() > 0) {
                backgroundImage = imageIcon.getImage();
            }
        } catch (Exception e) {
            backgroundImage = null;
        }
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(60, 0, 40, 0));
        
        // Main title - White text
        JLabel titleLabel = new JLabel("BELLEVE SALON", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 72));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setOpaque(false);
        
        // Subtitle - Light purple
        JLabel subtitleLabel = new JLabel("Management System", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 32));
        subtitleLabel.setForeground(new Color(221, 160, 221)); // Plum color
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        headerPanel.add(titlePanel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Role selection label - White text
        JLabel selectRoleLabel = new JLabel("Please Select Your Role", JLabel.CENTER);
        selectRoleLabel.setFont(new Font("Arial", Font.PLAIN, 28));
        selectRoleLabel.setForeground(Color.WHITE);
        selectRoleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 30, 0);
        centerPanel.add(selectRoleLabel, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 100, 20));
        buttonPanel.setOpaque(false);
        
        // Admin button - Dark purple
        RoundedButton adminButton = new RoundedButton("ADMINISTRATOR");
        adminButton.setBackgroundColor(new Color(130,33, 172)); 
        adminButton.setForeground(Color.WHITE);  
        adminButton.setFont(new Font("Arial", Font.BOLD, 20));
        adminButton.setPreferredSize(new Dimension(280, 70));
        adminButton.setCornerRadius(15);
        adminButton.addActionListener(e -> {
            exitFullscreen();
            new LoginPage("Admin");
        });
        buttonPanel.add(adminButton);
        
        // Receptionist button - Medium purple
        RoundedButton receptionistButton = new RoundedButton("RECEPTIONIST");
        receptionistButton.setBackgroundColor(new Color(130, 33, 172)); 
        receptionistButton.setForeground(Color.WHITE);
        receptionistButton.setFont(new Font("Arial", Font.BOLD, 20));
        receptionistButton.setPreferredSize(new Dimension(280, 70));
        receptionistButton.setCornerRadius(15);
        receptionistButton.addActionListener(e -> {
            exitFullscreen();
            new LoginPage("Receptionist");
        });
        buttonPanel.add(receptionistButton);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        centerPanel.add(buttonPanel, gbc);
        
        return centerPanel;
    }
    
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        
        // Exit instruction - Light gray
        JLabel exitLabel = new JLabel("Press ESC to exit the application", JLabel.CENTER);
        exitLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        exitLabel.setForeground(new Color(200, 200, 200));
        
        footerPanel.add(exitLabel, BorderLayout.CENTER);
        
        return footerPanel;
    }
    
    private void setFullscreen() {
        if (graphicsDevice.isFullScreenSupported()) {
            graphicsDevice.setFullScreenWindow(this);
        } else {
            // Fallback to maximized window
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setVisible(true);
        }
    }
    
    private void exitFullscreen() {
        if (graphicsDevice.getFullScreenWindow() == this) {
            graphicsDevice.setFullScreenWindow(null);
        }
        dispose();
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            exitFullscreen();
            System.exit(0);
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {}
    
    @Override
    public void keyTyped(KeyEvent e) {}
}