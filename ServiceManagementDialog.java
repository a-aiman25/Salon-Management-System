// ServiceManagementDialog.java - Enhanced Purple Theme
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ArrayList;

class ServiceManagementDialog extends JDialog {
    private JList<Service> serviceList;
    private DefaultListModel<Service> listModel;
    private JTextField nameField, durationField, priorityField, priceField;
    private JComboBox<String> departmentComboBox;
    private RoundedButton addButton, updateButton, deleteButton;

    // Enhanced Purple theme color palette
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

    public ServiceManagementDialog(JFrame parent) {
        super(parent, "Belleve Salon - Service Management", true);
        
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

        // Center Panel (Service List) - refined size
        JPanel centerPanel = createServiceListPanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Right Panel (Input Controls) - optimized width
        JPanel rightPanel = createInputPanel();
        mainPanel.add(rightPanel, BorderLayout.EAST);

        add(mainPanel, BorderLayout.CENTER);

        loadServices();
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
        JLabel titleLabel = new JLabel("Service Management", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(DEEP_PURPLE);

        JLabel subtitleLabel = new JLabel("Manage salon services, pricing, and departments", JLabel.CENTER);
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

    private JPanel createServiceListPanel() {
        listModel = new DefaultListModel<>();
        serviceList = new JList<>(listModel);
        serviceList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        serviceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        serviceList.setBackground(Color.WHITE);
        serviceList.setSelectionBackground(HOVER_PURPLE);
        serviceList.setSelectionForeground(DEEP_PURPLE);
        serviceList.setBorder(new EmptyBorder(8, 12, 8, 12));
        serviceList.setFixedCellHeight(32);

        // Enhanced list renderer with hover effects
        serviceList.setCellRenderer(new DefaultListCellRenderer() {
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

        serviceList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && serviceList.getSelectedValue() != null) {
                Service selectedService = serviceList.getSelectedValue();
                nameField.setText(selectedService.getName());
                durationField.setText(String.valueOf(selectedService.getDuration()));
                priorityField.setText(String.valueOf(selectedService.getPriority()));
                priceField.setText(String.format("%.2f", selectedService.getPrice()));
                departmentComboBox.setSelectedItem(selectedService.getDepartment());
                updateButton.setEnabled(true);
                deleteButton.setEnabled(true);
            } else {
                updateButton.setEnabled(false);
                deleteButton.setEnabled(false);
            }
        });

        JScrollPane scrollPane = new JScrollPane(serviceList);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_PURPLE, 2, true),
            BorderFactory.createEmptyBorder(3, 3, 3, 3)
        ));
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setPreferredSize(new Dimension(580, 450));
        
        // Style scrollbar
        styleScrollBar(scrollPane);

        return createTitledPanel("Current Services", scrollPane, PRIMARY_PURPLE);
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
        
        JLabel iconLabel = new JLabel("");
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        
        JLabel formTitle = new JLabel("Service Details");
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

        // Service Name
        gbc.gridx = 0; gbc.gridy++; 
        formPanel.add(createStyledLabel("Service Name:"), gbc);
        gbc.gridx = 1; 
        nameField = new JTextField(15); 
        styleTextField(nameField); 
        formPanel.add(nameField, gbc);

        // Duration
        gbc.gridx = 0; gbc.gridy++; 
        formPanel.add(createStyledLabel("Duration (mins):"), gbc);
        gbc.gridx = 1; 
        durationField = new JTextField(15); 
        styleTextField(durationField); 
        formPanel.add(durationField, gbc);

        // Priority
        gbc.gridx = 0; gbc.gridy++; 
        formPanel.add(createStyledLabel("Priority (1-5):"), gbc);
        gbc.gridx = 1; 
        priorityField = new JTextField(15); 
        styleTextField(priorityField); 
        formPanel.add(priorityField, gbc);

        // Price
        gbc.gridx = 0; gbc.gridy++; 
        formPanel.add(createStyledLabel("Price (PKR):"), gbc);
        gbc.gridx = 1; 
        priceField = new JTextField(15); 
        styleTextField(priceField); 
        formPanel.add(priceField, gbc);

        // Department
        gbc.gridx = 0; gbc.gridy++; 
        formPanel.add(createStyledLabel("Department:"), gbc);
        gbc.gridx = 1;
        departmentComboBox = new JComboBox<>(new String[]{
            "Hair Section", "Makeup", "Nails", "Facial Services", "Body Services"
        });
        styleComboBox(departmentComboBox);
        formPanel.add(departmentComboBox, gbc);

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

        addButton = new RoundedButton("Add Service");
        styleButton(addButton, new Color(106, 90, 205), Color.WHITE);
        addButton.addActionListener(e -> addService());
        addHoverEffect(addButton, new Color(123, 104, 238));
        buttonPanel.add(addButton);

        updateButton = new RoundedButton("Update Service");
        styleButton(updateButton, ACCENT_PURPLE, Color.WHITE);
        updateButton.setEnabled(false);
        updateButton.addActionListener(e -> updateService());
        addHoverEffect(updateButton, new Color(155, 60, 240));
        buttonPanel.add(updateButton);

        deleteButton = new RoundedButton("Delete Service");
        styleButton(deleteButton, new Color(153, 50, 204), Color.WHITE);
        deleteButton.setEnabled(false);
        deleteButton.addActionListener(e -> deleteService());
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

    private void loadServices() {
        listModel.clear();
        List<Service> services = DatabaseManager.getAllServices();
        for (Service service : services) {
            listModel.addElement(service);
        }
        clearFields();
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    private void addService() {
        try {
            String name = nameField.getText().trim();
            int duration = Integer.parseInt(durationField.getText().trim());
            int priority = Integer.parseInt(priorityField.getText().trim());
            double price = Double.parseDouble(priceField.getText().trim());
            String department = (String) departmentComboBox.getSelectedItem();

            if (name.isEmpty() || duration <= 0 || priority <= 0 || price <= 0) {
                showCustomMessage("Input Error", "All fields must be filled and positive numbers.", "warning");
                return;
            }

            if (DatabaseManager.getAllServices().stream().anyMatch(s -> s.getName().equalsIgnoreCase(name))) {
                showCustomMessage("Error", "Service with this name already exists.", "error");
                return;
            }

            Service newService = new Service(name, duration, priority, price, department);
            DatabaseManager.insertService(newService);
            showCustomMessage("Success", "Service '" + name + "' added successfully.", "info");
            loadServices();
        } catch (NumberFormatException ex) {
            showCustomMessage("Input Error", "Please enter valid numbers for Duration, Priority, and Price.", "error");
        }
    }

    private void updateService() {
        Service selectedService = serviceList.getSelectedValue();
        if (selectedService == null) return;

        try {
            String newName = nameField.getText().trim();
            int newDuration = Integer.parseInt(durationField.getText().trim());
            int newPriority = Integer.parseInt(priorityField.getText().trim());
            double newPrice = Double.parseDouble(priceField.getText().trim());
            String newDepartment = (String) departmentComboBox.getSelectedItem();

            if (newName.isEmpty() || newDuration <= 0 || newPriority <= 0 || newPrice <= 0) {
                showCustomMessage("Input Error", "All fields must be filled and positive numbers.", "warning");
                return;
            }

            if (DatabaseManager.getAllServices().stream()
                    .anyMatch(s -> s.getId() != selectedService.getId() && s.getName().equalsIgnoreCase(newName))) {
                showCustomMessage("Error", "Another service with this name already exists.", "error");
                return;
            }

            selectedService.setName(newName);
            selectedService.setDuration(newDuration);
            selectedService.setPriority(newPriority);
            selectedService.setPrice(newPrice);
            selectedService.setDepartment(newDepartment);

            DatabaseManager.updateService(selectedService);
            showCustomMessage("Success", "Service '" + newName + "' updated successfully.", "info");
            loadServices();
        } catch (NumberFormatException ex) {
            showCustomMessage("Input Error", "Please enter valid numbers for Duration, Priority, and Price.", "error");
        }
    }

    private void deleteService() {
        Service selectedService = serviceList.getSelectedValue();
        if (selectedService == null) return;

        int confirm = showCustomConfirm("Confirm Deletion",
                "<html><div style='text-align: center; font-family: Segoe UI;'>" +
                "Are you sure you want to delete service<br><b style='color: #4B0082;'>" + 
                selectedService.getName() + "</b>?<br><br>" +
                "<span style='color: #8B0000;'>This will also remove any appointments<br>associated with this service.</span>" +
                "</div></html>",
                "warning");

        if (confirm == JOptionPane.YES_OPTION) {
            DatabaseManager.deleteService(selectedService.getId());
            showCustomMessage("Success", "Service '" + selectedService.getName() + "' deleted successfully.", "info");
            loadServices();
        }
    }

    private void clearFields() {
        nameField.setText("");
        durationField.setText("");
        priorityField.setText("");
        priceField.setText("");
        departmentComboBox.setSelectedIndex(0);
        serviceList.clearSelection();
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