// ReceptionistDashboard.java - Enhanced Professional Purple Theme
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;

class ReceptionistDashboard extends JFrame {
    // Color Theme - Professional Purple Palette
    private static final Color PRIMARY_PURPLE = new Color(79, 70, 229);        // Deep Indigo Purple
    private static final Color SECONDARY_PURPLE = new Color(109, 40, 217);     // Rich Purple
    private static final Color LIGHT_PURPLE = new Color(196, 181, 253);        // Light Lavender (kept as is - already good)
    private static final Color BACKGROUND_PURPLE = new Color(130,33, 172);   // Very Light Purple (kept as is - already good)
    private static final Color ACCENT_PURPLE = new Color(147, 51, 234);        // Vibrant Purple
    private static final Color TEXT_DARK = new Color(55, 65, 81);              // Dark Gray (kept as is - good for readability)
    private static final Color SUCCESS_COLOR = new Color(16, 185, 129);        // Emerald (kept as is - standard success color)
   private static final Color WARNING_COLOR = new Color(245, 158, 11);        // Amber (kept as is - standard warning color)
   private static final Color DANGER_COLOR = new Color(239, 68, 68);          // Red (kept as is - standard danger color)
   private static final Color INFO_COLOR = new Color(99, 102, 241);           // Purple-Blue for better theme consistency

    // UI Components
    private JList<CustomerAppointment> appointmentJList;
    private DefaultListModel<CustomerAppointment> listModel;
    private JComboBox<Service> serviceList;
    private JTextField customerNameField;
    private JLabel nextServiceDueLabel;
    private RoundedButton bookButton, completeButton, deleteButton, editButton, backButton;
    private JLabel currentTimeLabel;
    private JLabel selectedAlgoLabel;

    // Data
    private List<Service> services;
    private List<CustomerAppointment> currentAppointments;
    private Timer autoUpdateTimer;

    public ReceptionistDashboard() {
        setTitle("Belleve Salon - Professional Management System");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen
        setLayout(new BorderLayout(0, 0));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(LIGHT_PURPLE);
        
        

        services = DatabaseManager.getAllServices();
        currentAppointments = new ArrayList<>();

        createHeaderPanel();
        createMainContentPanel();
        createFooterPanel();
        
        // Initialize timers
        initializeTimers();
        
        // Initial updates
        updateQueueDisplay();
        updateTimeLabels();
        updateCurrentTimeDisplay();
        updateSelectedAlgorithmDisplay();
        
        setVisible(true);
    }

    private void createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_PURPLE);
        headerPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        headerPanel.setPreferredSize(new Dimension(0, 100));

        // Title with gradient effect simulation
        JLabel headerLabel = new JLabel("Belleve Salon Management System", JLabel.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel, BorderLayout.CENTER);

        // Current time display
        currentTimeLabel = new JLabel("Current Time: --:-- --", JLabel.RIGHT);
        currentTimeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        currentTimeLabel.setForeground(LIGHT_PURPLE);
        headerPanel.add(currentTimeLabel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);
    }

    private void createMainContentPanel() {
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(LIGHT_PURPLE);
        contentPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        GridBagConstraints mainGBC = new GridBagConstraints();
        mainGBC.insets = new Insets(15, 15, 15, 15);
        mainGBC.fill = GridBagConstraints.BOTH;

        // Left Panel - Booking Form
        JPanel bookingFormPanel = createBookingFormPanel();
        JPanel styledBookingPanel = createStyledPanel("New Appointment Booking", bookingFormPanel, SECONDARY_PURPLE);
        mainGBC.gridx = 0; mainGBC.gridy = 0; mainGBC.weightx = 0.35; mainGBC.weighty = 1.0;
        contentPanel.add(styledBookingPanel, mainGBC);

        // Right Panel - Queue Management
        JPanel queuePanel = createQueuePanel();
        mainGBC.gridx = 1; mainGBC.gridy = 0; mainGBC.weightx = 0.65;
        contentPanel.add(queuePanel, mainGBC);

        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createBookingFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(30, 25, 30, 25));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 5, 12, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Customer Name
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel nameLabel = new JLabel("Customer Name:");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLabel.setForeground(TEXT_DARK);
        formPanel.add(nameLabel, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        customerNameField = new JTextField(20);
        styleTextField(customerNameField);
        formPanel.add(customerNameField, gbc);

        // Service Selection
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
        JLabel serviceLabel = new JLabel("Select Service:");
        serviceLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        serviceLabel.setForeground(TEXT_DARK);
        formPanel.add(serviceLabel, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        serviceList = new JComboBox<>(services.toArray(new Service[0]));
        styleComboBox(serviceList);
        serviceList.addActionListener(e -> updateSelectedAlgorithmDisplay());
        formPanel.add(serviceList, gbc);

        // Algorithm Display
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0;
        JLabel algoLabel = new JLabel("Scheduling Algorithm:");
        algoLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        algoLabel.setForeground(TEXT_DARK);
        formPanel.add(algoLabel, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        selectedAlgoLabel = new JLabel("N/A");
        selectedAlgoLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        selectedAlgoLabel.setForeground(SECONDARY_PURPLE);
        formPanel.add(selectedAlgoLabel, gbc);

        // Book Button
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 5, 10, 5);
        bookButton = new RoundedButton("Book Appointment");
        styleButton(bookButton, BACKGROUND_PURPLE);
        bookButton.setPreferredSize(new Dimension(200, 50));
        bookButton.addActionListener(e -> bookAppointment());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(bookButton);
        formPanel.add(buttonPanel, gbc);

        return formPanel;
    }

    private JPanel createQueuePanel() {
        JPanel queuePanel = new JPanel(new BorderLayout(15, 15));
        queuePanel.setBackground(BACKGROUND_PURPLE);

        // Queue Display
        listModel = new DefaultListModel<>();
        appointmentJList = new JList<>(listModel);
        appointmentJList.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        appointmentJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        appointmentJList.setBackground(Color.WHITE);
        appointmentJList.setFixedCellHeight(45);
        appointmentJList.setBorder(new EmptyBorder(10, 15, 10, 15));
        
        appointmentJList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, 
                    boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setFont(new Font("Segoe UI", Font.PLAIN, 15));
                
                if (isSelected) {
                    label.setBackground(LIGHT_PURPLE);
                    label.setForeground(TEXT_DARK);
                } else {
                    label.setBackground(index % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                    label.setForeground(TEXT_DARK);
                }
                
                CustomerAppointment appt = (CustomerAppointment) value;
                if (appt.isCompleted()) {
                    label.setForeground(Color.GRAY);
                    label.setText("<html><strike>" + appt.getCustomerName() + " - " + 
                                appt.getService().getName() + " (Start: " + 
                                formatMinutesToTime(appt.getStartTime()) + ")</strike></html>");
                } else {
                    label.setText(appt.getCustomerName() + " - " + appt.getService().getName() + 
                                " (Start: " + formatMinutesToTime(appt.getStartTime()) + ")");
                }
                
                label.setBorder(new EmptyBorder(8, 15, 8, 15));
                return label;
            }
        });

        JScrollPane queueScrollPane = new JScrollPane(appointmentJList);
        queueScrollPane.setBorder(BorderFactory.createLineBorder(SECONDARY_PURPLE, 2));
        queueScrollPane.getViewport().setBackground(Color.WHITE);
        
        JPanel queueDisplayPanel = createStyledPanel("Current Appointments Queue", queueScrollPane, SECONDARY_PURPLE);
        queuePanel.add(queueDisplayPanel, BorderLayout.CENTER);

        // Action Buttons
        JPanel actionPanel = createActionButtonsPanel();
        queuePanel.add(actionPanel, BorderLayout.SOUTH);

        return queuePanel;
    }

    private JPanel createActionButtonsPanel() {
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        actionPanel.setBackground(LIGHT_PURPLE);

        completeButton = new RoundedButton("Mark Complete");
        styleButton(completeButton, BACKGROUND_PURPLE);
        completeButton.setPreferredSize(new Dimension(160, 45));
        completeButton.addActionListener(e -> markAppointmentComplete());
        actionPanel.add(completeButton);

        editButton = new RoundedButton("Edit Appointment");
        styleButton(editButton, BACKGROUND_PURPLE);
        editButton.setPreferredSize(new Dimension(160, 45));
        editButton.addActionListener(e -> editAppointment());
        actionPanel.add(editButton);

        deleteButton = new RoundedButton("Delete Appointment");
        styleButton(deleteButton, BACKGROUND_PURPLE);
        deleteButton.setPreferredSize(new Dimension(160, 45));
        deleteButton.addActionListener(e -> deleteAppointment());
        actionPanel.add(deleteButton);

        return actionPanel;
    }

    private void createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(196, 181, 253));
        footerPanel.setBorder(new EmptyBorder(15, 30, 15, 30));
        footerPanel.setPreferredSize(new Dimension(0, 80));

        nextServiceDueLabel = new JLabel("Next Service Due: N/A");
        nextServiceDueLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nextServiceDueLabel.setForeground(TEXT_DARK);
        footerPanel.add(nextServiceDueLabel, BorderLayout.WEST);

        backButton = new RoundedButton("Back to Login");
        styleButton(backButton, new Color(130,33, 172));
        backButton.setPreferredSize(new Dimension(180, 45));
        backButton.addActionListener(e -> {
            dispose();
            autoUpdateTimer.stop();
            new RoleSelectionPage();
        });
        
        JPanel footerButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerButtonPanel.setBackground(new Color(196, 181, 253));
        footerButtonPanel.add(backButton);
        footerPanel.add(footerButtonPanel, BorderLayout.EAST);

        add(footerPanel, BorderLayout.SOUTH);
    }

    private void initializeTimers() {
        Timer timer = new Timer(60 * 1000, e -> updateCurrentTimeDisplay());
        timer.setInitialDelay(0);
        timer.start();

        autoUpdateTimer = new Timer(60 * 1000, e -> checkAndAutoRemoveCompletedAppointments());
        autoUpdateTimer.setInitialDelay(5000);
        autoUpdateTimer.start();
    }

    // [Rest of the methods remain the same - updateCurrentTimeDisplay, updateSelectedAlgorithmDisplay, 
    // updateTimeLabels, bookAppointment, formatMinutesToTime, checkAndAutoRemoveCompletedAppointments,
    // updateQueueDisplay, markAppointmentComplete, deleteAppointment, editAppointment remain unchanged]

    private void updateCurrentTimeDisplay() {
        currentTimeLabel.setText("Current Time: " + formatMinutesToTime(DatabaseManager.getMinutesFromCurrentTime()));
    }

    private void updateSelectedAlgorithmDisplay() {
        Service selectedService = (Service) serviceList.getSelectedItem();
        if (selectedService != null) {
            String department = selectedService.getDepartment();
            String algorithm = "N/A";
            switch (department) {
                case "Hair Section":
                    algorithm = "SJF (Shortest Job First)";
                    break;
                case "Makeup":
                    algorithm = "Priority Scheduling";
                    break;
                case "Nails":
                    algorithm = "FCFS (First-Come, First-Served)";
                    break;
                case "Facial Services":
                    algorithm = "FCFS (First-Come, First-Served)";
                    break;
                case "Body Services":
                    algorithm = "Priority Scheduling";
                    break;
                default:
                    algorithm = "FCFS (Default)";
                    break;
            }
            selectedAlgoLabel.setText(algorithm);
        } else {
            selectedAlgoLabel.setText("N/A");
        }
        updateQueueDisplay();
    }

    private void updateTimeLabels() {
        CustomerAppointment nextAppt = currentAppointments.stream()
                .filter(appt -> !appt.isCompleted())
                .min(Comparator.comparingInt(CustomerAppointment::getStartTime))
                .orElse(null);

        if (nextAppt != null) {
            nextServiceDueLabel.setText(String.format("Next Service Due: %s (%s) at %s",
                    nextAppt.getCustomerName(),
                    nextAppt.getService().getName(),
                    formatMinutesToTime(nextAppt.getStartTime())));
        } else {
            nextServiceDueLabel.setText("Next Service Due: None");
        }
    }

    private void bookAppointment() {
        String name = customerNameField.getText().trim();
        Service selectedService = (Service) serviceList.getSelectedItem();

        if (name.isEmpty() || selectedService == null) {
            showCustomMessage("Input Error", "Please enter customer name and select a service.", "warning");
            return;
        }

        String department = selectedService.getDepartment();
        String schedulingAlgorithm = "FCFS (First-Come, First-Served)";

        switch (department) {
            case "Hair Section":
                schedulingAlgorithm = "SJF (Shortest Job First)";
                break;
            case "Makeup":
                schedulingAlgorithm = "Priority Scheduling";
                break;
            case "Nails":
                schedulingAlgorithm = "FCFS (First-Come, First-Served)";
                break;
            case "Facial Services":
                schedulingAlgorithm = "FCFS (First-Come, First-Served)";
                break;
            case "Body Services":
                schedulingAlgorithm = "Priority Scheduling";
                break;
        }

        int startTime = DatabaseManager.findAvailableTimeSlot(selectedService, schedulingAlgorithm);

        if (startTime == -1) {
            showCustomMessage("Booking Failed", "No available time slot found for the selected service with the chosen algorithm today. Try later.", "error");
            return;
        }

        int endTime = startTime + selectedService.getDuration();
        long bookingTimestamp = System.currentTimeMillis();

        CustomerAppointment appointment = new CustomerAppointment(name, selectedService, startTime, endTime, bookingTimestamp);
        DatabaseManager.insertAppointment(appointment);

        updateQueueDisplay();
        updateTimeLabels();

        new ReceiptDialog(this, name, selectedService, startTime, endTime, bookingTimestamp);
        customerNameField.setText("");
    }

    private String formatMinutesToTime(int totalMinutes) {
        int hour = totalMinutes / 60;
        int minute = totalMinutes % 60;
        String ampm = "AM";
        if (hour >= 12) {
            ampm = "PM";
            if (hour > 12) hour -= 12;
        }
        if (hour == 0) hour = 12;
        return String.format("%02d:%02d %s", hour, minute, ampm);
    }

    private void checkAndAutoRemoveCompletedAppointments() {
        long currentMinutesFromMidnight = DatabaseManager.getMinutesFromCurrentTime();
        List<CustomerAppointment> allAppointments = DatabaseManager.getAllAppointments();

        for (CustomerAppointment appt : allAppointments) {
            if (!appt.isCompleted() && appt.getEndTime() <= currentMinutesFromMidnight) {
                appt.setCompleted(true);
                DatabaseManager.updateAppointment(appt);
                System.out.println("DEBUG: Automatically marked appointment for " + appt.getCustomerName() + " as completed.");
            }
        }
        updateQueueDisplay();
    }

    private void updateQueueDisplay() {
        listModel.clear();
        currentAppointments = DatabaseManager.getAllAppointments().stream()
                .filter(appt -> !appt.isCompleted())
                .collect(Collectors.toList());

        System.out.println("DEBUG: Number of non-completed appointments retrieved: " + currentAppointments.size());

        Service currentSelectedService = (Service) serviceList.getSelectedItem();
        String displayAlgorithm = "FCFS (First-Come, First-Served)";

        if (currentSelectedService != null) {
            String department = currentSelectedService.getDepartment();
            switch (department) {
                case "Hair Section":
                    displayAlgorithm = "SJF (Shortest Job First)";
                    break;
                case "Makeup":
                    displayAlgorithm = "Priority Scheduling";
                    break;
                case "Nails":
                    displayAlgorithm = "FCFS (First-Come, First-Served)";
                    break;
                case "Facial Services":
                    displayAlgorithm = "FCFS (First-Come, First-Served)";
                    break;
                case "Body Services":
                    displayAlgorithm = "Priority Scheduling";
                    break;
            }
        }

        switch (displayAlgorithm) {
            case "FCFS (First-Come, First-Served)":
                currentAppointments.sort(Comparator.comparingLong(CustomerAppointment::getBookingTimestamp));
                break;
            case "SJF (Shortest Job First)":
                currentAppointments.sort(Comparator.comparingInt(a -> a.getService().getDuration()));
                break;
            case "Priority Scheduling":
                currentAppointments.sort(Comparator.comparingInt(a -> a.getService().getPriority()));
                break;
        }

        if (currentAppointments.isEmpty()) {
            listModel.addElement(new CustomerAppointment("No appointments in queue.", new Service("N/A", 0, 0, 0, "N/A"), 0, 0, 0L));
            appointmentJList.setEnabled(false);
            completeButton.setEnabled(false);
            deleteButton.setEnabled(false);
            editButton.setEnabled(false);
        } else {
            for (CustomerAppointment appt : currentAppointments) {
                listModel.addElement(appt);
            }
            appointmentJList.setEnabled(true);
            completeButton.setEnabled(true);
            deleteButton.setEnabled(true);
            editButton.setEnabled(true);
        }
        updateTimeLabels();
        appointmentJList.revalidate();
        appointmentJList.repaint();
    }

    private void markAppointmentComplete() {
        int selectedIndex = appointmentJList.getSelectedIndex();
        if (selectedIndex == -1 || (listModel.getSize() == 1 && listModel.getElementAt(0).getCustomerName().equals("No appointments in queue."))) {
            showCustomMessage("No Selection", "Please select an appointment to mark as complete.", "warning");
            return;
        }
        CustomerAppointment selectedAppt = listModel.getElementAt(selectedIndex);

        int confirm = showCustomConfirm("Confirm Completion",
                "<html>Are you sure you want to mark <b>" +
                        selectedAppt.getCustomerName() + "'s " + selectedAppt.getService().getName() +
                        "</b> as complete?</html>", "question");

        if (confirm == JOptionPane.YES_OPTION) {
            selectedAppt.setCompleted(true);
            DatabaseManager.updateAppointment(selectedAppt);
            updateQueueDisplay();
            showCustomMessage("Success", "Appointment marked as complete!", "info");
        }
    }

    private void deleteAppointment() {
        int selectedIndex = appointmentJList.getSelectedIndex();
        if (selectedIndex == -1 || (listModel.getSize() == 1 && listModel.getElementAt(0).getCustomerName().equals("No appointments in queue."))) {
            showCustomMessage("No Selection", "Please select an appointment to delete.", "warning");
            return;
        }
        CustomerAppointment selectedAppt = listModel.getElementAt(selectedIndex);

        int confirm = showCustomConfirm("Confirm Deletion",
                "<html>Are you sure you want to <b>delete</b> " +
                        selectedAppt.getCustomerName() + "'s " + selectedAppt.getService().getName() + " appointment? This cannot be undone.</html>",
                "warning");

        if (confirm == JOptionPane.YES_OPTION) {
            DatabaseManager.deleteAppointment(selectedAppt.getId());
            updateQueueDisplay();
            showCustomMessage("Success", "Appointment deleted.", "info");
        }
    }

    private void editAppointment() {
        int selectedIndex = appointmentJList.getSelectedIndex();
        if (selectedIndex == -1 || (listModel.getSize() == 1 && listModel.getElementAt(0).getCustomerName().equals("No appointments in queue."))) {
            showCustomMessage("No Selection", "Please select an appointment to edit.", "warning");
            return;
        }
        CustomerAppointment apptToEdit = listModel.getElementAt(selectedIndex);

        JTextField newNameField = new JTextField(apptToEdit.getCustomerName());
        JComboBox<Service> newServiceList = new JComboBox<>(services.toArray(new Service[0]));
        newServiceList.setSelectedItem(apptToEdit.getService());

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("New Customer Name:"));
        panel.add(newNameField);
        panel.add(new JLabel("New Service:"));
        panel.add(newServiceList);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Appointment",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String newName = newNameField.getText().trim();
            Service newService = (Service) newServiceList.getSelectedItem();

            if (newName.isEmpty()) {
                showCustomMessage("Input Error", "Customer name cannot be empty.", "error");
                return;
            }

            String department = newService.getDepartment();
            String schedulingAlgorithm = "FCFS (First-Come, First-Served)";

            switch (department) {
                case "Hair Section":
                    schedulingAlgorithm = "SJF (Shortest Job First)";
                    break;
                case "Makeup":
                    schedulingAlgorithm = "Priority Scheduling";
                    break;
                case "Nails":
                    schedulingAlgorithm = "FCFS (First-Come, First-Served)";
                    break;
                case "Facial Services":
                    schedulingAlgorithm = "FCFS (First-Come, First-Served)";
                    break;
                case "Body Services":
                    schedulingAlgorithm = "Priority Scheduling";
                    break;
            }

            apptToEdit.setCustomerName(newName);
            apptToEdit.setService(newService);

            int newStartTime = DatabaseManager.findAvailableTimeSlot(newService, schedulingAlgorithm);
            if (newStartTime == -1) {
                showCustomMessage("Rescheduling Failed", "Could not find a new slot for the updated service. Please try again later.", "error");
                return;
            }
            apptToEdit.setStartTime(newStartTime);
            apptToEdit.setEndTime(newStartTime + newService.getDuration());

            DatabaseManager.updateAppointment(apptToEdit);
            updateQueueDisplay();
            showCustomMessage("Update Success", "Appointment updated successfully!", "info");
        }
    }

    // Styling Helper Methods
    private void styleButton(RoundedButton button, Color bgColor) {
        button.setBackgroundColor(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setFocusPainted(false);
    }

    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SECONDARY_PURPLE, 2),
                new EmptyBorder(12, 15, 12, 15)));
        textField.setBackground(Color.WHITE);
        textField.setPreferredSize(new Dimension(0, 45));
    }

    private void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        comboBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SECONDARY_PURPLE, 2),
                new EmptyBorder(8, 12, 8, 12)));
        comboBox.setBackground(Color.WHITE);
        comboBox.setPreferredSize(new Dimension(0, 45));
    }

    private JPanel createStyledPanel(String title, JComponent content, Color borderColor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(borderColor, 3),
                title,
                javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 20),
                borderColor
        ));
        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    private void showCustomMessage(String title, String message, String type) {
        JLabel messageLabel = new JLabel("<html><div style='width: 300px; padding: 10px;'>" + message + "</div></html>");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        messageLabel.setForeground(TEXT_DARK);

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
        JLabel messageLabel = new JLabel("<html><div style='width: 350px; padding: 10px;'>" + message + "</div></html>");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        messageLabel.setForeground(TEXT_DARK);

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