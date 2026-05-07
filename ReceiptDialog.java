// ReceiptDialog.java
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

class ReceiptDialog extends JDialog {

    public ReceiptDialog(JFrame parent, String customerName, Service service, int bookedTime, int estimatedCompletionTime, long bookingTimestamp) {
        super(parent, "Appointment Receipt", true); // Modal dialog
        setLayout(new BorderLayout());
        setBackground(new Color(248, 245, 255));
        setResizable(false);

        // Main container with purple gradient background
        JPanel mainContainer = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Purple gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(250, 245, 255),
                    0, getHeight(), new Color(240, 230, 250)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        // Receipt panel with enhanced purple styling
        JPanel receiptPanel = new JPanel();
        receiptPanel.setLayout(new BoxLayout(receiptPanel, BoxLayout.Y_AXIS));
        receiptPanel.setOpaque(true);
        receiptPanel.setBackground(Color.WHITE);

        // Add elegant purple border with shadow effect
        receiptPanel.setBorder(new CompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            new CompoundBorder(
                new LineBorder(new Color(147, 112, 219), 2, true), // Medium slate blue border
                new EmptyBorder(30, 40, 30, 40)
            )
        ));

        // Header section with elegant purple styling
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);
        
        // Elegant salon icon
        JLabel logoLabel = new JLabel("💎", JLabel.CENTER);
        logoLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(logoLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        // Main title with purple gradient effect
        JLabel titleLabel = new JLabel("Belleve Salon", JLabel.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create gradient paint for text
                GradientPaint textGradient = new GradientPaint(
                    0, 0, new Color(138, 43, 226), // Blue violet
                    0, getHeight(), new Color(75, 0, 130) // Indigo
                );
                g2d.setPaint(textGradient);
                g2d.setFont(getFont());
                
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(getText(), x, y);
            }
        };
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(titleLabel);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Appointment Confirmation", JLabel.CENTER);
        subtitleLabel.setFont(new Font("SansSerif", Font.ITALIC, 16));
        subtitleLabel.setForeground(new Color(123, 104, 238)); // Medium slate blue
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(subtitleLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        receiptPanel.add(headerPanel);

        // Elegant purple separator
        JSeparator separator1 = new JSeparator(SwingConstants.HORIZONTAL);
        separator1.setForeground(new Color(186, 85, 211)); // Medium orchid
        separator1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        receiptPanel.add(separator1);
        receiptPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Appointment Details Section
        JLabel detailsHeader = new JLabel("Appointment Details");
        detailsHeader.setFont(new Font("SansSerif", Font.BOLD, 16));
        detailsHeader.setForeground(new Color(75, 0, 130)); // Indigo
        detailsHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        receiptPanel.add(detailsHeader);
        receiptPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Service details
        addDetailRow(receiptPanel, "Customer Name:", customerName, true);
        addDetailRow(receiptPanel, "Service:", service.getName(), false);
        addDetailRow(receiptPanel, "Department:", service.getDepartment(), false);
        addDetailRow(receiptPanel, "Duration:", service.getDuration() + " minutes", false);
        addDetailRow(receiptPanel, "Price:", String.format("PKR %.2f", service.getPrice()), true);
        receiptPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Timing details
        addDetailRow(receiptPanel, "Booking Time:", formatMillisToDateTime(bookingTimestamp), false);
        addDetailRow(receiptPanel, "Scheduled Start:", formatMinutesToTime(bookedTime), false);
        addDetailRow(receiptPanel, "Estimated Completion:", formatMinutesToTime(estimatedCompletionTime), false);
        receiptPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Another elegant separator
        JSeparator separator2 = new JSeparator(SwingConstants.HORIZONTAL);
        separator2.setForeground(new Color(186, 85, 211));
        separator2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        receiptPanel.add(separator2);
        receiptPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Thank You Section with purple background
        JPanel thankYouPanel = new JPanel();
        thankYouPanel.setLayout(new BoxLayout(thankYouPanel, BoxLayout.Y_AXIS));
        thankYouPanel.setOpaque(true);
        thankYouPanel.setBackground(new Color(230, 230, 250)); // Lavender
        thankYouPanel.setBorder(new CompoundBorder(
            new LineBorder(new Color(147, 112, 219), 1, true),
            new EmptyBorder(15, 20, 15, 20)
        ));
        thankYouPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel thankYouLabel = new JLabel("Thank you for choosing Belleve Salon!", JLabel.CENTER);
        thankYouLabel.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 15));
        thankYouLabel.setForeground(new Color(75, 0, 130));
        thankYouLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        thankYouPanel.add(thankYouLabel);

        JLabel serviceLabel = new JLabel("We look forward to serving you!", JLabel.CENTER);
        serviceLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        serviceLabel.setForeground(new Color(123, 104, 238));
        serviceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        thankYouPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        thankYouPanel.add(serviceLabel);

        receiptPanel.add(thankYouPanel);
        receiptPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Stylish OK Button with purple theme
        RoundedButton okButton = new RoundedButton("OK");
        okButton.setBackgroundColor(new Color(138, 43, 226)); // Blue violet
        okButton.setForeground(Color.WHITE);
        okButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        okButton.setPreferredSize(new Dimension(120, 40));
        okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        okButton.addActionListener(e -> dispose());
        
        // Add hover effect
        okButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                okButton.setBackgroundColor(new Color(148, 0, 211)); // Dark violet
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                okButton.setBackgroundColor(new Color(138, 43, 226));
            }
        });
        
        receiptPanel.add(okButton);

        mainContainer.add(receiptPanel, BorderLayout.CENTER);
        add(mainContainer, BorderLayout.CENTER);

        pack(); // Adjusts window size to fit contents
        setLocationRelativeTo(parent); // Center relative to parent frame
        setVisible(true);
    }

    private void addDetailRow(JPanel parentPanel, String label, String value, boolean highlight) {
        JPanel rowPanel = new JPanel(new BorderLayout());
        rowPanel.setOpaque(false);
        rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));

        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("SansSerif", Font.BOLD, 13));
        labelComp.setForeground(new Color(75, 0, 130)); // Indigo
        rowPanel.add(labelComp, BorderLayout.WEST);

        JLabel valueComp = new JLabel(value);
        valueComp.setFont(new Font("SansSerif", highlight ? Font.BOLD : Font.PLAIN, 13));
        valueComp.setForeground(highlight ? new Color(138, 43, 226) : new Color(60, 60, 60)); // Blue violet for highlights
        rowPanel.add(valueComp, BorderLayout.EAST);

        // Add subtle background for highlighted rows
        if (highlight) {
            rowPanel.setOpaque(true);
            rowPanel.setBackground(new Color(248, 245, 255));
            rowPanel.setBorder(new EmptyBorder(3, 8, 3, 8));
        }

        parentPanel.add(rowPanel);
        parentPanel.add(Box.createRigidArea(new Dimension(0, 6)));
    }

    private String formatMinutesToTime(int totalMinutes) {
        int hour = totalMinutes / 60;
        int minute = totalMinutes % 60;
        String ampm = "AM";
        if (hour >= 12) {
            ampm = "PM";
            if (hour > 12) hour -= 12;
        }
        if (hour == 0) hour = 12; // 00:xx becomes 12:xx AM
        return String.format("%02d:%02d %s", hour, minute, ampm);
    }

    private String formatMillisToDateTime(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a");
        return sdf.format(new Date(millis));
    }
}