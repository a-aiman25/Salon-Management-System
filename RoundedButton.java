// RoundedButton.java - Purple Theme Version
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

class RoundedButton extends JButton {
    private Color backgroundColor = new Color(75, 0, 130); // Default to indigo purple
    private Color hoverColor;
    private Color pressedColor;
    private int cornerRadius = 15;
    private boolean isPressed = false;
    private boolean isHovered = false;
    
    public RoundedButton(String text) {
        super(text);
        
        // Initialize colors
        updateColors();
        
        // Remove default button styling
        setOpaque(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add mouse effects
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                isPressed = false;
                repaint();
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                isPressed = true;
                repaint();
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint();
            }
        });
    }
    
    private void updateColors() {
        // Create hover and pressed colors based on background color
        float[] hsb = Color.RGBtoHSB(backgroundColor.getRed(), backgroundColor.getGreen(), backgroundColor.getBlue(), null);
        
        // For purple theme, create lighter and darker variants
        hoverColor = Color.getHSBColor(hsb[0], Math.max(0.0f, hsb[1] - 0.1f), Math.min(1.0f, hsb[2] + 0.15f));
        pressedColor = Color.getHSBColor(hsb[0], Math.min(1.0f, hsb[1] + 0.1f), Math.max(0.0f, hsb[2] - 0.15f));
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        if (!isEnabled()) {
            // Handle disabled state - Gray theme
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw disabled button in grayscale
            g2d.setColor(new Color(100, 100, 100));
            g2d.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 3, cornerRadius, cornerRadius);
            
            g2d.setColor(new Color(70, 70, 70));
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawRoundRect(0, 0, getWidth() - 3, getHeight() - 3, cornerRadius, cornerRadius);
            
            g2d.dispose();
        } else {
            // Handle enabled state
            Graphics2D g2d = (Graphics2D) g.create();
            
            // Enable anti-aliasing for smooth rendering
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            
            // Determine current color based on state
            Color currentColor = backgroundColor;
            if (isPressed) {
                currentColor = pressedColor;
            } else if (isHovered) {
                currentColor = hoverColor;
            }
            
            // Draw shadow for depth - Black shadow
            g2d.setColor(new Color(0, 0, 0, 40));
            g2d.fillRoundRect(3, 3, getWidth() - 3, getHeight() - 3, cornerRadius, cornerRadius);
            
            // Draw button background
            g2d.setColor(currentColor);
            g2d.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 3, cornerRadius, cornerRadius);
            
            // Draw subtle inner highlight with white
            g2d.setColor(new Color(255, 255, 255, 30));
            g2d.fillRoundRect(1, 1, getWidth() - 5, (getHeight() - 5) / 2, cornerRadius - 1, cornerRadius - 1);
            
            // Draw border - Darker version of current color
            g2d.setColor(currentColor.darker());
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawRoundRect(0, 0, getWidth() - 3, getHeight() - 3, cornerRadius, cornerRadius);
            
            g2d.dispose();
        }
        
        // Paint text
        super.paintComponent(g);
    }
    
    @Override
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        // Add padding for shadow
        return new Dimension(size.width + 3, size.height + 3);
    }
    
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        updateColors();
        repaint();
    }
    
    public Color getBackgroundColor() {
        return backgroundColor;
    }
    
    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
        repaint();
    }
    
    public int getCornerRadius() {
        return cornerRadius;
    }
    
    // Override setEnabled to handle disabled state
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        setCursor(enabled ? new Cursor(Cursor.HAND_CURSOR) : new Cursor(Cursor.DEFAULT_CURSOR));
        repaint();
    }
}