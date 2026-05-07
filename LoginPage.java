// LoginPage.java - Purple Theme Version
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class LoginPage extends JFrame implements ActionListener, KeyListener {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private RoundedButton loginButton;
    private RoundedButton backButton;
    private String userRole;
    private GraphicsDevice graphicsDevice;

    public LoginPage(String role) {
        this.userRole = role;
        
        // Get graphics device for fullscreen
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        graphicsDevice = ge.getDefaultScreenDevice();
        
        setTitle("Belleve Salon - " + role + " Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setUndecorated(true); // Remove window decorations
        
        // Create main panel with purple theme background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Purple gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(75, 0, 130),         // Indigo
                    getWidth(), getHeight(), new Color(25, 25, 25)  // Almost black
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Add subtle purple pattern
                g2d.setColor(new Color(147, 112, 219, 10)); // Medium slate blue with low opacity
                for (int i = 0; i < getWidth(); i += 80) {
                    g2d.drawLine(i, 0, i, getHeight());
                }
                for (int i = 0; i < getHeight(); i += 80) {
                    g2d.drawLine(0, i, getWidth(), i);
                }
            }
        };
        mainPanel.setLayout(new BorderLayout());
        
        // Create and add components
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        mainPanel.add(createLoginPanel(), BorderLayout.CENTER);
        mainPanel.add(createFooterPanel(), BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Add key listener
        addKeyListener(this);
        setFocusable(true);
        
        // Set fullscreen
        setFullscreen();
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 30, 0));
        
        // Main title - White
        JLabel titleLabel = new JLabel("BELLEVE SALON", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        
        // Role subtitle - Purple accent color based on role
        String roleColor = userRole.equals("Admin") ? "#9370DB" : "#BA55D3"; // Medium slate blue : Medium orchid
        JLabel roleLabel = new JLabel("<html><div style='text-align: center; color: " + roleColor + ";'>" + 
                                     userRole.toUpperCase() + " LOGIN</div></html>", JLabel.CENTER);
        roleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        roleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.add(roleLabel, BorderLayout.SOUTH);
        
        headerPanel.add(titlePanel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    private JPanel createLoginPanel() {
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setOpaque(false);
        
        // Create login form panel with glassmorphism effect
        JPanel formPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Glassmorphism background with purple tint
                g2d.setColor(new Color(75, 0, 130, 30)); // Semi-transparent indigo
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                // Border with purple accent
                g2d.setColor(new Color(147, 112, 219, 60)); // Medium slate blue border
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
            }
        };
        formPanel.setOpaque(false);
        formPanel.setLayout(new GridBagLayout());
        formPanel.setPreferredSize(new Dimension(600, 500));
        formPanel.setBorder(BorderFactory.createEmptyBorder(60, 80, 60, 80));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 0, 20, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Username label - White
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        usernameLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(usernameLabel, gbc);
        
        // Username field
        usernameField = createStyledTextField();
        gbc.gridy = 1;
        gbc.insets = new Insets(8, 0, 25, 0);
        formPanel.add(usernameField, gbc);
        
        // Password label - White
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 20));
        passwordLabel.setForeground(Color.WHITE);
        gbc.gridy = 2;
        gbc.insets = new Insets(20, 0, 20, 0);
        formPanel.add(passwordLabel, gbc);
        
        // Password field
        passwordField = createStyledPasswordField();
        gbc.gridy = 3;
        gbc.insets = new Insets(8, 0, 35, 0);
        formPanel.add(passwordField, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        buttonPanel.setOpaque(false);
        
        // Back button - Gray/Black
        backButton = new RoundedButton("BACK");
        backButton.setBackgroundColor(new Color(64, 64, 64)); // Dark gray
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setPreferredSize(new Dimension(120, 55));
        backButton.setCornerRadius(10);
        backButton.addActionListener(e -> {
            dispose();
            new RoleSelectionPage();
        });
        buttonPanel.add(backButton);
        
        // Login button - Purple based on role
        loginButton = new RoundedButton("LOGIN");
        Color buttonColor = userRole.equals("Admin") ? 
            new Color(75, 0, 130) :      // Indigo for Admin
            new Color(138, 43, 226);     // Blue violet for Receptionist
        loginButton.setBackgroundColor(buttonColor);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setPreferredSize(new Dimension(120, 55));
        loginButton.setCornerRadius(10);
        loginButton.addActionListener(this);
        buttonPanel.add(loginButton);
        
        gbc.gridy = 4;
        gbc.insets = new Insets(25, 0, 0, 0);
        formPanel.add(buttonPanel, gbc);
        
        loginPanel.add(formPanel);
        
        return loginPanel;
    }
    
    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20);
        field.setFont(new Font("Arial", Font.PLAIN, 18));
        field.setPreferredSize(new Dimension(400, 50));
        field.setBackground(new Color(255, 255, 255, 220)); // More opaque white
        field.setForeground(Color.BLACK);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(147, 112, 219), 1), // Purple border
            BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));
        field.addKeyListener(this);
        return field;
    }
    
    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField(20);
        field.setFont(new Font("Arial", Font.PLAIN, 18));
        field.setPreferredSize(new Dimension(400, 50));
        field.setBackground(new Color(255, 255, 255, 220)); // More opaque white
        field.setForeground(Color.BLACK);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(147, 112, 219), 1), // Purple border
            BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));
        field.addKeyListener(this);
        return field;
    }
    
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        
        JLabel instructionLabel = new JLabel("Press ESC to exit | Enter to login", JLabel.CENTER);
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        instructionLabel.setForeground(new Color(100, 100, 100));
        
        footerPanel.add(instructionLabel, BorderLayout.CENTER);
        
        return footerPanel;
    }
    
    private void setFullscreen() {
        if (graphicsDevice.isFullScreenSupported()) {
            graphicsDevice.setFullScreenWindow(this);
        } else {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setVisible(true);
        }
        
        // Focus on username field
        SwingUtilities.invokeLater(() -> usernameField.requestFocus());
    }
    
    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            showErrorMessage("Please enter both username and password.");
            return;
        }
        
        // Show loading cursor
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        // Simulate authentication (replace with actual database call)
        SwingUtilities.invokeLater(() -> {
            User user = DatabaseManager.authenticateUser(username, password, userRole);
            
            setCursor(Cursor.getDefaultCursor());
            
            if (user != null) {
                dispose();
                if (user.getRole().equals("Admin")) {
                    new AdminPanel();
                } else {
                    new ReceptionistDashboard();
                }
            } else {
                showErrorMessage("Invalid credentials. Please try again.");
                passwordField.setText("");
                usernameField.requestFocus();
            }
        });
    }
    
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(
            this, 
            message, 
            "Authentication Error", 
            JOptionPane.ERROR_MESSAGE
        );
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            performLogin();
        }
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            dispose();
            System.exit(0);
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            performLogin();
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {}
    
    @Override
    public void keyTyped(KeyEvent e) {}
}