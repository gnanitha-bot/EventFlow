package ui;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * Utility class for managing UI theme colors, fonts, headers, stat cards, and table styling.
 * Ensures a modern, clean, and consistent aesthetic across all EventFlow panels.
 */
public class UIUtils {
    // Theme Palette
    public static final Color COLOR_SIDEBAR = new Color(20, 26, 38);
    public static final Color COLOR_SIDEBAR_HOVER = new Color(35, 45, 63);
    public static final Color COLOR_SIDEBAR_ACTIVE = new Color(79, 70, 229);
    public static final Color COLOR_BG = new Color(245, 247, 250);
    public static final Color COLOR_CARD_BG = Color.WHITE;
    public static final Color COLOR_PRIMARY = new Color(79, 70, 229);
    public static final Color COLOR_PRIMARY_HOVER = new Color(67, 56, 202);
    public static final Color COLOR_TEXT_DARK = new Color(15, 23, 42);
    public static final Color COLOR_TEXT_MUTED = new Color(100, 116, 139);
    public static final Color COLOR_BORDER = new Color(226, 232, 240);

    // Module Accent Colors
    public static final Color COLOR_BLUE = new Color(37, 99, 235);
    public static final Color COLOR_PURPLE = new Color(147, 51, 234);
    public static final Color COLOR_EMERALD = new Color(16, 185, 129);
    public static final Color COLOR_AMBER = new Color(245, 158, 11);
    public static final Color COLOR_ROSE = new Color(244, 63, 94);
    public static final Color COLOR_CYAN = new Color(6, 182, 212);

    // Modern Fonts
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_SUBHEADER = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SECTION = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_STAT_VAL = new Font("Segoe UI", Font.BOLD, 28);
    public static final Font FONT_STAT_LBL = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);

    /**
     * Creates a standard page header with title and descriptive subtitle.
     */
    public static JPanel createHeaderPanel(String title, String subtitle) {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 15, 0));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(FONT_HEADER);
        lblTitle.setForeground(COLOR_TEXT_DARK);

        JLabel lblSubtitle = new JLabel(subtitle);
        lblSubtitle.setFont(FONT_SUBHEADER);
        lblSubtitle.setForeground(COLOR_TEXT_MUTED);

        header.add(lblTitle, BorderLayout.NORTH);
        header.add(lblSubtitle, BorderLayout.SOUTH);

        return header;
    }

    /**
     * Creates a modern stat card displaying metrics.
     */
    public static JPanel createStatCard(String title, String mainValue, String subtext, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_CARD_BG);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.setColor(COLOR_BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);

                // Top accent bar
                g2.setColor(accentColor);
                g2.fillRoundRect(0, 0, getWidth() - 1, 5, 12, 12);
                g2.fillRect(0, 3, getWidth() - 1, 3);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(15, 18, 15, 18));
        card.setPreferredSize(new Dimension(220, 110));

        JPanel textContainer = new JPanel();
        textContainer.setLayout(new BoxLayout(textContainer, BoxLayout.Y_AXIS));
        textContainer.setOpaque(false);

        JLabel lblTitle = new JLabel(title.toUpperCase());
        lblTitle.setFont(FONT_STAT_LBL);
        lblTitle.setForeground(COLOR_TEXT_MUTED);

        JLabel lblValue = new JLabel(mainValue);
        lblValue.setFont(FONT_STAT_VAL);
        lblValue.setForeground(COLOR_TEXT_DARK);

        JLabel lblSub = new JLabel(subtext);
        lblSub.setFont(FONT_SMALL);
        lblSub.setForeground(COLOR_TEXT_MUTED);

        textContainer.add(lblTitle);
        textContainer.add(Box.createRigidArea(new Dimension(0, 4)));
        textContainer.add(lblValue);
        textContainer.add(Box.createRigidArea(new Dimension(0, 4)));
        textContainer.add(lblSub);

        card.add(textContainer, BorderLayout.CENTER);
        return card;
    }

    /**
     * Creates an informative module header banner explaining DSA algorithms to be implemented.
     */
    public static JPanel createModuleBanner(String moduleCode, String title, String description, String dsaConcept) {
        JPanel banner = new JPanel(new BorderLayout(15, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(238, 242, 255)); // Soft indigo highlight
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.setColor(new Color(199, 210, 254));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
            }
        };
        banner.setOpaque(false);
        banner.setBorder(new EmptyBorder(12, 16, 12, 16));

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);

        JLabel lblCode = new JLabel(moduleCode);
        lblCode.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblCode.setForeground(COLOR_PRIMARY);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(FONT_SECTION);
        lblTitle.setForeground(COLOR_TEXT_DARK);

        JLabel lblDesc = new JLabel(description);
        lblDesc.setFont(FONT_REGULAR);
        lblDesc.setForeground(COLOR_TEXT_MUTED);

        JPanel infoStack = new JPanel();
        infoStack.setLayout(new BoxLayout(infoStack, BoxLayout.Y_AXIS));
        infoStack.setOpaque(false);
        infoStack.add(lblTitle);
        infoStack.add(Box.createRigidArea(new Dimension(0, 2)));
        infoStack.add(lblDesc);

        leftPanel.add(lblCode, BorderLayout.WEST);
        leftPanel.add(Box.createRigidArea(new Dimension(15, 0)), BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);

        JLabel lblDsa = new JLabel("Target DSA: " + dsaConcept);
        lblDsa.setFont(FONT_BOLD);
        lblDsa.setForeground(new Color(67, 56, 202));
        lblDsa.setBorder(new CompoundBorder(
                new LineBorder(new Color(199, 210, 254), 1, true),
                new EmptyBorder(4, 10, 4, 10)
        ));
        lblDsa.setOpaque(true);
        lblDsa.setBackground(Color.WHITE);
        rightPanel.add(lblDsa);

        banner.add(lblCode, BorderLayout.WEST);
        banner.add(infoStack, BorderLayout.CENTER);
        banner.add(rightPanel, BorderLayout.EAST);

        return banner;
    }

    public static void styleTable(JTable table) {
        table.setRowHeight(44); // Increased row height for premium feel
        table.setFont(FONT_REGULAR);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(false); // We'll draw custom borders in the renderer
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setBackground(Color.WHITE);
        table.setSelectionBackground(new Color(238, 242, 255));
        table.setSelectionForeground(new Color(30, 58, 138));
        
        // Add hover effect tracking
        table.putClientProperty("hoveredRow", -1);
        table.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                Integer prev = (Integer) table.getClientProperty("hoveredRow");
                if (prev == null || prev != row) {
                    table.putClientProperty("hoveredRow", row);
                    table.repaint();
                }
            }
        });
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                table.putClientProperty("hoveredRow", -1);
                table.repaint();
            }
        });

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_BOLD);
        header.setBackground(Color.WHITE);
        header.setForeground(new Color(71, 85, 105)); // Slate 600
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 46));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(226, 232, 240))); // Modern bottom border

        // Custom renderer for hover and padding
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                // Padded cells with a subtle row divider
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(241, 245, 249)),
                        new EmptyBorder(0, 16, 0, 16)
                ));

                Integer hoveredRow = (Integer) table.getClientProperty("hoveredRow");
                boolean isHovered = (hoveredRow != null && hoveredRow == row);

                if (isSelected) {
                    c.setBackground(new Color(238, 242, 255)); // Indigo selection
                    c.setForeground(new Color(30, 58, 138));
                } else if (isHovered) {
                    c.setBackground(new Color(248, 250, 252)); // Slate-50 hover
                    c.setForeground(COLOR_TEXT_DARK);
                } else {
                    c.setBackground(Color.WHITE);
                    c.setForeground(COLOR_TEXT_DARK);
                }
                
                return c;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
    }

    /**
     * Creates a styled button with hover transition support.
     */
    public static JButton createStyledButton(String text, Color bg, Color fg) {
        JButton button = new JButton(text);
        button.setFont(FONT_BOLD);
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(8, 16, 8, 16));
        return button;
    }

    /**
     * Creates a card container with rounded corners and border.
     */
    public static JPanel createCardPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_CARD_BG);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.setColor(COLOR_BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        return panel;
    }
}
