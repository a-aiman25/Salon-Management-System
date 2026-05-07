// AdminPanel.java - Enhanced Purple Theme
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList; // Explicitly added
import java.util.Comparator; // ADDED THIS IMPORT
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class AdminPanel extends JFrame {
    private JTextArea bookingsReportArea;
    private JTextArea upcomingAppointmentsArea;
    private RoundedButton backButton;
    private RoundedButton manageUsersButton;
    private RoundedButton manageServicesButton;
    private RoundedButton refreshButton;
    private JPanel kpiPanel; // Declared as a field

    // Purple theme color palette
    private static final Color PRIMARY_PURPLE = new Color(106, 90, 205);     // Slate Blue
    private static final Color SECONDARY_PURPLE = new Color(147, 112, 219);  // Medium Purple
    private static final Color LIGHT_PURPLE = new Color(230, 230, 250);      // Lavender
    private static final Color DARK_PURPLE = new Color(75, 0, 130);          // Indigo
    private static final Color ACCENT_PURPLE = new Color(138, 43, 226);      // Blue Violet
    private static final Color BACKGROUND_PURPLE = new Color(248, 248, 255); // Ghost White
    private static final Color BORDER_PURPLE = new Color(123, 104, 238);     // Medium Slate Blue
    private static final Color CARD_PURPLE_1 = new Color(153, 102, 255);     // Medium Purple
    private static final Color CARD_PURPLE_2 = new Color(186, 85, 211);      // Medium Orchid
    private static final Color CARD_PURPLE_3 = new Color(147, 112, 219);     // Medium Purple
    private static final Color CARD_PURPLE_4 = new Color(138, 43, 226);      // Blue Violet
    private static final Color CARD_PURPLE_5 = new Color(160, 102, 211);     // Medium Orchid
    private static final Color CARD_PURPLE_6 = new Color(123, 104, 238);     // Medium Slate Blue

    public AdminPanel() {
        setTitle("Belleve Salon - Administration Dashboard");
        
        // Full screen setup
        setSize(1600, 1000);
        setMinimumSize(new Dimension(1400, 900));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(20, 20));
        getContentPane().setBackground(BACKGROUND_PURPLE);

        // Create main content panel with enhanced padding
        JPanel mainPanel = new JPanel(new BorderLayout(25, 25));
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        mainPanel.setBackground(BACKGROUND_PURPLE);

        // Enhanced Header Panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // KPI Panel - Enhanced with better spacing
        kpiPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        kpiPanel.setBackground(BACKGROUND_PURPLE);
        kpiPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        mainPanel.add(kpiPanel, BorderLayout.CENTER);

        // Enhanced Reports Panel
        JPanel reportsPanel = createReportsPanel();
        mainPanel.add(reportsPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        // Enhanced Control Panel
        JPanel controlPanel = createControlPanel();
        add(controlPanel, BorderLayout.SOUTH);

        updateAdminData(); // Initial data load
        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_PURPLE);
        headerPanel.setBorder(new EmptyBorder(0, 0, 25, 0));

        // Main title
        JLabel titleLabel = new JLabel("Administration Dashboard", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(DARK_PURPLE);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Comprehensive Salon Management & Analytics", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subtitleLabel.setForeground(PRIMARY_PURPLE);

        // Icon or logo placeholder
        JLabel iconLabel = new JLabel("👑", JLabel.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));

        JPanel titlePanel = new JPanel(new GridLayout(3, 1, 0, 8));
        titlePanel.setBackground(BACKGROUND_PURPLE);
        titlePanel.add(iconLabel);
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);

        headerPanel.add(titlePanel, BorderLayout.CENTER);
        
        // Enhanced separator
        JSeparator separator = new JSeparator();
        separator.setForeground(BORDER_PURPLE);
        separator.setBackground(BORDER_PURPLE);
        separator.setPreferredSize(new Dimension(0, 3));
        headerPanel.add(separator, BorderLayout.SOUTH);

        return headerPanel;
    }

    private JPanel createReportsPanel() {
        JPanel reportsPanel = new JPanel(new GridLayout(1, 2, 25, 0));
        reportsPanel.setBackground(BACKGROUND_PURPLE);
        reportsPanel.setPreferredSize(new Dimension(0, 350));

        // Enhanced bookings report area
        bookingsReportArea = new JTextArea();
        bookingsReportArea.setEditable(false);
        bookingsReportArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        bookingsReportArea.setBackground(Color.WHITE);
        bookingsReportArea.setForeground(DARK_PURPLE);
        bookingsReportArea.setBorder(new EmptyBorder(15, 15, 15, 15));
        bookingsReportArea.setLineWrap(true);
        bookingsReportArea.setWrapStyleWord(true);

        JScrollPane bookingsScrollPane = new JScrollPane(bookingsReportArea);
        bookingsScrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_PURPLE, 2),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        bookingsScrollPane.getViewport().setBackground(Color.WHITE);

        JPanel bookingsPanel = createTitledPanel("Service Bookings Report", bookingsScrollPane, PRIMARY_PURPLE);
        reportsPanel.add(bookingsPanel);

        // Enhanced upcoming appointments area
        upcomingAppointmentsArea = new JTextArea();
        upcomingAppointmentsArea.setEditable(false);
        upcomingAppointmentsArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        upcomingAppointmentsArea.setBackground(Color.WHITE);
        upcomingAppointmentsArea.setForeground(DARK_PURPLE);
        upcomingAppointmentsArea.setBorder(new EmptyBorder(15, 15, 15, 15));
        upcomingAppointmentsArea.setLineWrap(true);
        upcomingAppointmentsArea.setWrapStyleWord(true);

        JScrollPane upcomingScrollPane = new JScrollPane(upcomingAppointmentsArea);
        upcomingScrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SECONDARY_PURPLE, 2),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        upcomingScrollPane.getViewport().setBackground(Color.WHITE);

        JPanel upcomingPanel = createTitledPanel("Upcoming Appointments (Not Completed)", upcomingScrollPane, SECONDARY_PURPLE);
        reportsPanel.add(upcomingPanel);

        return reportsPanel;
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 20));
        controlPanel.setBackground(BACKGROUND_PURPLE);
        controlPanel.setBorder(new EmptyBorder(20, 30, 30, 30));

        // Enhanced buttons with icons
        manageUsersButton = new RoundedButton("Manage Users");
        styleButton(manageUsersButton, new Color(138, 43, 226)); // Blue Violet
        manageUsersButton.setPreferredSize(new Dimension(200, 50));
        manageUsersButton.addActionListener(e -> new UserManagementDialog(this));
        controlPanel.add(manageUsersButton);

        manageServicesButton = new RoundedButton("Manage Services");
        styleButton(manageServicesButton, new Color(153, 50, 204)); // Dark Orchid
        manageServicesButton.setPreferredSize(new Dimension(200, 50));
        manageServicesButton.addActionListener(e -> new ServiceManagementDialog(this));
        controlPanel.add(manageServicesButton);

        refreshButton = new RoundedButton("Refresh Data");
        styleButton(refreshButton, PRIMARY_PURPLE);
        refreshButton.setPreferredSize(new Dimension(200, 50));
        refreshButton.addActionListener(e -> updateAdminData());
        controlPanel.add(refreshButton);

        backButton = new RoundedButton("Back to Login");
        styleButton(backButton, new Color(108, 117, 125)); // Gray
        backButton.setPreferredSize(new Dimension(200, 50));
        backButton.addActionListener(e -> {
            dispose();
            new RoleSelectionPage();
        });
        controlPanel.add(backButton);

        return controlPanel;
    }

    private void updateAdminData() {
        List<CustomerAppointment> allAppointments = DatabaseManager.getAllAppointments();

        int totalBookings = allAppointments.size();
        double totalRevenue = 0.0;
        int totalServiceDuration = 0;
        HashMap<String, Integer> serviceCounts = new HashMap<>();
        List<Service> uniqueBookedServices = new ArrayList<>();

        for (CustomerAppointment a : allAppointments) {
            totalRevenue += a.getService().getPrice();
            totalServiceDuration += a.getService().getDuration();
            serviceCounts.put(a.getService().getName(), serviceCounts.getOrDefault(a.getService().getName(), 0) + 1);
            if (!uniqueBookedServices.contains(a.getService())) {
                uniqueBookedServices.add(a.getService());
            }
        }

        String mostPopularService = "N/A";
        int maxBookings = 0;
        if (!serviceCounts.isEmpty()) {
            for (Map.Entry<String, Integer> entry : serviceCounts.entrySet()) {
                if (entry.getValue() > maxBookings) {
                    maxBookings = entry.getValue();
                    mostPopularService = entry.getKey();
                }
            }
        }

        double avgServiceDuration = totalBookings == 0 ? 0 : (double) totalServiceDuration / totalBookings;
        long currentQueueSize = allAppointments.stream().filter(a -> !a.isCompleted()).count();

        // Update KPI Cards with enhanced styling
        kpiPanel.removeAll();
        kpiPanel.add(createKpiCard("Total Appointments", String.valueOf(totalBookings), CARD_PURPLE_1));
        kpiPanel.add(createKpiCard("Total Revenue", String.format("$%.2f", totalRevenue), CARD_PURPLE_2));
        kpiPanel.add(createKpiCard("Current Queue Size", String.valueOf(currentQueueSize), CARD_PURPLE_3));
        kpiPanel.add(createKpiCard("Avg. Service Duration", String.format("%.1f mins", avgServiceDuration), CARD_PURPLE_4));
        kpiPanel.add(createKpiCard("Most Popular Service", mostPopularService, CARD_PURPLE_5));
        kpiPanel.add(createKpiCard("Unique Services Booked", String.valueOf(uniqueBookedServices.size()), CARD_PURPLE_6));
        kpiPanel.revalidate();
        kpiPanel.repaint();

        // Update Bookings Report
        StringBuilder bookingsReport = new StringBuilder("BOOKINGS PER SERVICE:\n");
        bookingsReport.append("═".repeat(40)).append("\n");
        if (serviceCounts.isEmpty()) {
            bookingsReport.append("   No services booked yet.\n");
        } else {
            serviceCounts.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .forEach(entry -> bookingsReport.append(String.format("   ▸ %s: %d bookings\n", entry.getKey(), entry.getValue())));
        }
        bookingsReportArea.setText(bookingsReport.toString());

        // Update Upcoming Appointments
        StringBuilder upcomingText = new StringBuilder("UPCOMING APPOINTMENTS:\n");
        upcomingText.append("═".repeat(35)).append("\n");
        List<CustomerAppointment> nonCompletedAppointments = allAppointments.stream()
                .filter(appt -> !appt.isCompleted())
                .sorted(Comparator.comparingInt(CustomerAppointment::getStartTime))
                .collect(Collectors.toList());

        if (nonCompletedAppointments.isEmpty()) {
            upcomingText.append("   No upcoming appointments.\n");
        } else {
            for (CustomerAppointment appt : nonCompletedAppointments) {
                upcomingText.append(String.format("   ▸ %s - %s\n     Time: %s\n\n",
                        appt.getCustomerName(),
                        appt.getService().getName(),
                        formatMinutesToTime(appt.getStartTime())));
            }
        }
        upcomingAppointmentsArea.setText(upcomingText.toString());
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

    private JPanel createKpiCard(String title, String value, Color bgColor) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(bgColor);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 3),
                new EmptyBorder(20, 20, 20, 20)));
        
        // Enhanced shadow effect
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(bgColor.darker(), 2),
                        new EmptyBorder(18, 18, 18, 18))));

        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        card.add(titleLabel, BorderLayout.NORTH);

        JLabel valueLabel = new JLabel(value, JLabel.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(Color.WHITE);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createTitledPanel(String title, JComponent content, Color borderColor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_PURPLE);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(borderColor, 3),
                title,
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 18),
                borderColor
        ));
        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    // Enhanced styling helper methods
    private void styleButton(RoundedButton button, Color bgColor) {
        button.setBackgroundColor(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        
        // Add hover effect simulation through border
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(),
                BorderFactory.createEmptyBorder(10, 23, 10, 23)));
    }

    // Custom message dialog with enhanced styling
    private void showCustomMessage(String title, String message, String type) {
        JLabel messageLabel = new JLabel("<html><div style='font-family: Segoe UI; font-size: 14px; text-align: center;'>" + message + "</div></html>");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
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
}