package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Main Window Frame for EventFlow application.
 * Features a modern dark sidebar navigation and CardLayout content switching.
 */
public class MainFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private CardLayout cardLayout;
    private JPanel contentPanel;

    private JButton activeButton;
    private final JButton[] navButtons = new JButton[10];

    public MainFrame() {
        setTitle("EventFlow - College Event Management Platform");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 800);
        setMinimumSize(new Dimension(1100, 700));
        setLocationRelativeTo(null);

        // Layout
        setLayout(new BorderLayout());

        // 1. Sidebar Construction
        JPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);

        // 2. Main CardLayout Content Area
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(UIUtils.COLOR_BG);

        // Register Panels
        contentPanel.add(new DashboardPanel(), "DASHBOARD");
        contentPanel.add(new EventPanel(), "EVENTS");
        contentPanel.add(new ParticipantPanel(), "PARTICIPANTS");
        contentPanel.add(new VenuePanel(), "VENUES");
        contentPanel.add(new M1SearchPanel(), "M1");
        contentPanel.add(new M2DocumentPanel(), "M2");
        contentPanel.add(new M3OptimizationPanel(), "M3");
        contentPanel.add(new M4ResourcePanel(), "M4");
        contentPanel.add(new M5ConflictPanel(), "M5");
        contentPanel.add(new M6AnalyticsPanel(), "M6");

        add(contentPanel, BorderLayout.CENTER);

        // Select default tab
        setActiveTab(navButtons[0], "DASHBOARD");
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(260, 0));
        sidebar.setBackground(UIUtils.COLOR_SIDEBAR);

        // Top Brand Header
        JPanel brandPanel = new JPanel();
        brandPanel.setLayout(new BoxLayout(brandPanel, BoxLayout.Y_AXIS));
        brandPanel.setOpaque(false);
        brandPanel.setBorder(new EmptyBorder(22, 20, 20, 20));

        JLabel lblLogo = new JLabel("EventFlow");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblLogo.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("DSA Management Platform");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(148, 163, 184));

        brandPanel.add(lblLogo);
        brandPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        brandPanel.add(lblSub);

        sidebar.add(brandPanel, BorderLayout.NORTH);

        // Navigation Buttons Stack
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setOpaque(false);
        navPanel.setBorder(new EmptyBorder(10, 12, 10, 12));

        String[] navLabels = {
                "Dashboard",
                "Events",
                "Participants",
                "Venues",
                "M1 - Event Search",
                "M2 - Document Similarity",
                "M3 - Activity Optimization",
                "M4 - Resource Allocation",
                "M5 - Conflict Management",
                "M6 - Event Analytics"
        };

        String[] cardKeys = {
                "DASHBOARD", "EVENTS", "PARTICIPANTS", "VENUES",
                "M1", "M2", "M3", "M4", "M5", "M6"
        };

        for (int i = 0; i < navLabels.length; i++) {
            final String cardKey = cardKeys[i];
            JButton btn = createNavButton(navLabels[i]);
            navButtons[i] = btn;
            btn.addActionListener(e -> setActiveTab(btn, cardKey));

            navPanel.add(btn);
            navPanel.add(Box.createRigidArea(new Dimension(0, 5)));

            // Add separator before DSA modules
            if (i == 3) {
                navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                JLabel lblSection = new JLabel("  DSA MODULES");
                lblSection.setFont(new Font("Segoe UI", Font.BOLD, 10));
                lblSection.setForeground(new Color(100, 116, 139));
                navPanel.add(lblSection);
                navPanel.add(Box.createRigidArea(new Dimension(0, 8)));
            }
        }

        JScrollPane navScroll = new JScrollPane(navPanel);
        navScroll.setBorder(null);
        navScroll.setOpaque(false);
        navScroll.getViewport().setOpaque(false);
        navScroll.getVerticalScrollBar().setUnitIncrement(10);

        sidebar.add(navScroll, BorderLayout.CENTER);

        // Footer info
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(10, 10, 15, 10));

        JLabel lblFooter = new JLabel("College DSA Project • Swing GUI");
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblFooter.setForeground(new Color(100, 116, 139));
        footer.add(lblFooter);

        sidebar.add(footer, BorderLayout.SOUTH);

        return sidebar;
    }

    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(new Color(203, 213, 225));
        btn.setBackground(UIUtils.COLOR_SIDEBAR);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(236, 38));
        btn.setPreferredSize(new Dimension(236, 38));
        btn.setBorder(new EmptyBorder(0, 15, 0, 15));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (btn != activeButton) {
                    btn.setBackground(UIUtils.COLOR_SIDEBAR_HOVER);
                    btn.setForeground(Color.WHITE);
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (btn != activeButton) {
                    btn.setBackground(UIUtils.COLOR_SIDEBAR);
                    btn.setForeground(new Color(203, 213, 225));
                }
            }
        });

        return btn;
    }

    private void setActiveTab(JButton btn, String cardKey) {
        if (activeButton != null) {
            activeButton.setBackground(UIUtils.COLOR_SIDEBAR);
            activeButton.setForeground(new Color(203, 213, 225));
            activeButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        }

        activeButton = btn;
        activeButton.setBackground(UIUtils.COLOR_SIDEBAR_ACTIVE);
        activeButton.setForeground(Color.WHITE);
        activeButton.setFont(new Font("Segoe UI", Font.BOLD, 13));

        cardLayout.show(contentPanel, cardKey);
    }
}
