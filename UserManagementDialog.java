// UserManagementDialog.java - Enhanced Purple Theme (Matching ServiceManagementDialog)
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ArrayList;

class UserManagementDialog extends JDialog {
    private JList<User> userList;
    private DefaultListModel<User> listModel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private RoundedButton addButton, updateButton, deleteButton;

    // Enhanced Purple theme color palette (matching ServiceManagementDialog)
    private static final Color PRIMARY_PURPLE = new Color(123, 104, 238);     // Medium Slate Blue
    private static final Color SECONDARY_PURPLE = new Color(147, 112, 219);  // Medium Purple
    private static final Color LIGHT_PURPLE = new Color(230, 230, 250);      // Lavender
    private static final Color DARK_PURPLE = new Color(75, 0, 130);          // Indigo
    private static final Color ACCENT_PURPLE = new Color(138, 43, 226);      // Blue Violet
    private static final Color BACKGROUND_PURPLE = new Color(248, 248, 255); // Ghost White
    private static final Color CARD_PURPLE = new Color(245, 245, 255);       // Light Card Background
    private static final Color BORDER_PURPLE = new Color(186, 164, 254);     // Soft Purple Border
    private static final Color HOVER_PURPLE = new Color(204, 196, 255);      // Hover Effect
    private static final Color DEEP_PURPLE = new Color(102, 51, 153);        // Deep Purple

    public UserManagementDialog(JFrame parent) {
        super(parent, "Belleve Salon - User Management", true);
        
        // Optimized dialog size for dialog box (not full screen)
        setSize(1100, 750);
        setMinimumSize(new Dimension(950, 650));
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(BACKGROUND_PURPLE);

        // Create main content panel with refined padding
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(BACKGROUND_PURPLE);

        // Header Panel with gradient effect
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Center Panel (User List) - refined size
        JPanel centerPanel = createUserListPanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Right Panel (Input Controls) - optimized width
        JPanel rightPanel = createInputPanel();
        mainPanel.add(rightPanel, BorderLayout.EAST);

        add(mainPanel, BorderLayout.CENTER);

        loadUsers();
        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Subtle gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, BACKGROUND_PURPLE,
                    0, getHeight(), CARD_PURPLE
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setBorder(new EmptyBorder(10, 0, 15, 0));
        headerPanel.setPreferredSize(new Dimension(0, 80));

        // Title with shadow effect
        JLabel titleLabel = new JLabel("User Management", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(DEEP_PURPLE);

        JLabel subtitleLabel = new JLabel("Manage user accounts, roles, and permissions", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        subtitleLabel.setForeground(PRIMARY_PURPLE);

        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 0, 2));
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);

        headerPanel.add(titlePanel, BorderLayout.CENTER);
        
        // Enhanced separator with gradient
        JSeparator separator = new JSeparator() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, BACKGROUND_PURPLE.brighter(),
                    getWidth(), 0, PRIMARY_PURPLE
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, getHeight()/2 - 1, getWidth(), 2);
            }
        };
        separator.setPreferredSize(new Dimension(0, 4));
        headerPanel.add(separator, BorderLayout.SOUTH);

        return headerPanel;
    }

    private JPanel createUserListPanel() {
        listModel = new DefaultListModel<>();
        userList = new JList<>(listModel);
        userList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userList.setBackground(Color.WHITE);
        userList.setSelectionBackground(HOVER_PURPLE);
        userList.setSelectionForeground(DEEP_PURPLE);
        userList.setBorder(new EmptyBorder(8, 12, 8, 12));
        userList.setFixedCellHeight(32);

        // Enhanced list renderer with hover effects
        userList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                
                if (isSelected) {
                    setBackground(HOVER_PURPLE);
                    setForeground(DEEP_PURPLE);
                    setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(PRIMARY_PURPLE, 1),
                        new EmptyBorder(4, 8, 4, 8)
                    ));
                } else {
                    setBackground(index % 2 == 0 ? Color.WHITE : CARD_PURPLE);
                    setForeground(DARK_PURPLE);
                    setBorder(new EmptyBorder(4, 8, 4, 8));
                }
                setFont(new Font("Segoe UI", Font.PLAIN, 13));
                return this;
            }
        });

        userList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && userList.getSelectedValue() != null) {
                User selectedUser = userList.getSelectedValue();
                usernameField.setText(selectedUser.getUsername());
                passwordField.setText(""); // Don't show password for security
                roleComboBox.setSelectedItem(selectedUser.getRole());
                updateButton.setEnabled(true);
                deleteButton.setEnabled(true);
            } else {
                updateButton.setEnabled(false);
                deleteButton.setEnabled(false);
            }
        });

        JScrollPane scrollPane = new JScrollPane(userList);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_PURPLE, 2, true),
            BorderFactory.createEmptyBorder(3, 3, 3, 3)
        ));
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setPreferredSize(new Dimension(580, 450));
        
        // Style scrollbar
        styleScrollBar(scrollPane);

        return createTitledPanel("Current Users", scrollPane, PRIMARY_PURPLE);
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Card-like background with subtle shadow
                g2d.setColor(new Color(0, 0, 0, 10));
                g2d.fillRoundRect(3, 3, getWidth()-3, getHeight()-3, 12, 12);
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth()-3, getHeight()-3, 12, 12);
            }
        };
        inputPanel.setPreferredSize(new Dimension(350, 0));
        inputPanel.setOpaque(false);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_PURPLE, 1, true),
            new EmptyBorder(20, 20, 20, 20)
        ));

        // Form Panel with enhanced layout
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Enhanced form title with icon
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setOpaque(false);
        
        JLabel iconLabel = new JLabel("👤");
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        
        JLabel formTitle = new JLabel("User Details");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitle.setForeground(DEEP_PURPLE);
        
        titlePanel.add(iconLabel);
        titlePanel.add(formTitle);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(titlePanel, gbc);

        // Add subtle separator
        JSeparator titleSeparator = new JSeparator();
        titleSeparator.setForeground(BORDER_PURPLE);
        gbc.gridy++; gbc.insets = new Insets(5, 20, 15, 20);
        formPanel.add(titleSeparator, gbc);

        gbc.gridwidth = 1;
        gbc.insets = new Insets(6, 6, 6, 6);

        // Username
        gbc.gridx = 0; gbc.gridy++; 
        formPanel.add(createStyledLabel("Username:"), gbc);
        gbc.gridx = 1; 
        usernameField = new JTextField(15); 
        styleTextField(usernameField); 
        formPanel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy++; 
        formPanel.add(createStyledLabel("Password:"), gbc);
        gbc.gridx = 1; 
        passwordField = new JPasswordField(15); 
        styleTextField(passwordField); 
        formPanel.add(passwordField, gbc);

        // Role
        gbc.gridx = 0; gbc.gridy++; 
        formPanel.add(createStyledLabel("Role:"), gbc);
        gbc.gridx = 1;
        roleComboBox = new JComboBox<>(new String[]{"Receptionist", "Admin"});
        styleComboBox(roleComboBox);
        formPanel.add(roleComboBox, gbc);

        // Button Panel with enhanced spacing
        JPanel buttonPanel = createButtonPanel();
        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 6, 6, 6);
        formPanel.add(buttonPanel, gbc);

        inputPanel.add(formPanel, BorderLayout.CENTER);
        return inputPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 0, 8));
        buttonPanel.setOpaque(false);

        addButton = new RoundedButton("Add User");
        styleButton(addButton, new Color(106, 90, 205), Color.WHITE);
        addButton.addActionListener(e -> addUser());
        addHoverEffect(addButton, new Color(123, 104, 238));
        buttonPanel.add(addButton);

        updateButton = new RoundedButton("Update User");
        styleButton(updateButton, ACCENT_PURPLE, Color.WHITE);
        updateButton.setEnabled(false);
        updateButton.addActionListener(e -> updateUser());
        addHoverEffect(updateButton, new Color(155, 60, 240));
        buttonPanel.add(updateButton);

        deleteButton = new RoundedButton("Delete User");
        styleButton(deleteButton, new Color(153, 50, 204), Color.WHITE);
        deleteButton.setEnabled(false);
        deleteButton.addActionListener(e -> deleteUser());
        addHoverEffect(deleteButton, new Color(170, 67, 220));
        buttonPanel.add(deleteButton);

        return buttonPanel;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(DEEP_PURPLE);
        return label;
    }

    private void loadUsers() {
        listModel.clear();
        List<User> users = DatabaseManager.getAllUsers();
        for (User user : users) {
            listModel.addElement(user);
        }
        clearFields();
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    private void addUser() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String role = (String) roleComboBox.getSelectedItem();

        if (username.isEmpty() || password.isEmpty()) {
            showCustomMessage("Input Error", "Username and Password cannot be empty.", "warning");
            return;
        }

        if (DatabaseManager.userExists(username, role)) {
            showCustomMessage("Error", "User with this username and role already exists.", "error");
            return;
        }

        DatabaseManager.insertUser(username, password, role);
        showCustomMessage("Success", "User '" + username + "' added successfully.", "info");
        loadUsers();
    }

    private void updateUser() {
        User selectedUser = userList.getSelectedValue();
        if (selectedUser == null) return;

        String newUsername = usernameField.getText().trim();
        String newPassword = new String(passwordField.getPassword());
        String newRole = (String) roleComboBox.getSelectedItem();

        if (newUsername.isEmpty()) {
            showCustomMessage("Input Error", "Username cannot be empty.", "warning");
            return;
        }

        // Check if username/role combination already exists for *another* user
        if (!newUsername.equals(selectedUser.getUsername()) || !newRole.equals(selectedUser.getRole())) {
            if (DatabaseManager.userExists(newUsername, newRole)) {
                showCustomMessage("Error", "Another user with this username and role already exists.", "error");
                return;
            }
        }

        selectedUser.setUsername(newUsername);
        selectedUser.setRole(newRole);
        if (!newPassword.isEmpty()) {
            selectedUser.setPassword(newPassword);
        } else {
            showCustomMessage("Note", "Password was not updated. Enter new password to change.", "info");
        }

        DatabaseManager.updateUser(selectedUser);
        showCustomMessage("Success", "User '" + newUsername + "' updated successfully.", "info");
        loadUsers();
    }

    private void deleteUser() {
        User selectedUser = userList.getSelectedValue();
        if (selectedUser == null) return;

        int confirm = showCustomConfirm("Confirm Deletion",
                "<html><div style='text-align: center; font-family: Segoe UI;'>" +
                "Are you sure you want to delete user<br><b style='color: #4B0082;'>" + 
                selectedUser.getUsername() + " (" + selectedUser.getRole() + ")</b>?<br><br>" +
                "<span style='color: #8B0000;'>This action cannot be undone.</span>" +
                "</div></html>",
                "warning");

        if (confirm == JOptionPane.YES_OPTION) {
            DatabaseManager.deleteUser(selectedUser.getId());
            showCustomMessage("Success", "User '" + selectedUser.getUsername() + "' deleted successfully.", "info");
            loadUsers();
        }
    }

    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        roleComboBox.setSelectedIndex(0);
        userList.clearSelection();
    }

    // Enhanced Styling Helper Methods
    private void styleButton(RoundedButton button, Color bgColor, Color textColor) {
        button.setBackgroundColor(bgColor);
        button.setForeground(textColor);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(180, 38));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
    }

    private void addHoverEffect(RoundedButton button, Color hoverColor) {
        Color originalColor = button.getBackgroundColor();
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackgroundColor(hoverColor);
                    button.repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackgroundColor(originalColor);
                    button.repaint();
                }
            }
        });
    }

    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_PURPLE, 1, true),
                new EmptyBorder(6, 10, 6, 10)));
        textField.setBackground(Color.WHITE);
        textField.setForeground(DARK_PURPLE);
        textField.setPreferredSize(new Dimension(160, 30));
        
        // Add focus effect
        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PRIMARY_PURPLE, 2, true),
                    new EmptyBorder(5, 10, 5, 10)));
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_PURPLE, 1, true),
                    new EmptyBorder(6, 10, 6, 10)));
            }
        });
    }

    private void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_PURPLE, 1, true),
                new EmptyBorder(6, 10, 6, 10)));
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(DARK_PURPLE);
        comboBox.setPreferredSize(new Dimension(160, 30));
        
        // Enhanced dropdown renderer
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (isSelected) {
                    setBackground(HOVER_PURPLE);
                    setForeground(DEEP_PURPLE);
                } else {
                    setBackground(Color.WHITE);
                    setForeground(DARK_PURPLE);
                }
                setBorder(new EmptyBorder(4, 8, 4, 8));
                setFont(new Font("Segoe UI", Font.PLAIN, 12));
                return this;
            }
        });
    }

    private void styleScrollBar(JScrollPane scrollPane) {
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = BORDER_PURPLE;
                this.trackColor = CARD_PURPLE;
            }
            
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createInvisibleButton();
            }
            
            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createInvisibleButton();
            }
            
            private JButton createInvisibleButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }
        });
        
        verticalScrollBar.setPreferredSize(new Dimension(12, 0));
    }

    private JPanel createTitledPanel(String title, JComponent content, Color borderColor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_PURPLE);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(borderColor, 2, true),
                title,
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16),
                borderColor
        ));
        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    private void showCustomMessage(String title, String message, String type) {
        JLabel messageLabel = new JLabel("<html><div style='font-family: Segoe UI; font-size: 13px; text-align: center;'>" + message + "</div></html>");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        messageLabel.setForeground(DARK_PURPLE);

        int messageType = JOptionPane.PLAIN_MESSAGE;
        switch (type.toLowerCase()) {
            case "info":
                messageType = JOptionPane.INFORMATION_MESSAGE;
                break;
            case "warning":
                messageType = JOptionPane.WARNING_MESSAGE;
                break;
            case "error":
                messageType = JOptionPane.ERROR_MESSAGE;
                break;
        }
        JOptionPane.showMessageDialog(this, messageLabel, title, messageType);
    }

    private int showCustomConfirm(String title, String message, String type) {
        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        messageLabel.setForeground(DARK_PURPLE);

        int messageType = JOptionPane.QUESTION_MESSAGE;
        switch (type.toLowerCase()) {
            case "warning":
                messageType = JOptionPane.WARNING_MESSAGE;
                break;
            case "error":
                messageType = JOptionPane.ERROR_MESSAGE;
                break;
        }
        return JOptionPane.showConfirmDialog(this, messageLabel, title, JOptionPane.YES_NO_OPTION, messageType);
    }
}