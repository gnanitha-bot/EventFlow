package ui;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import model.Event;
import service.DataService;

/**
 * Dashboard Panel displaying high-level system statistics, active events table,
 * and DSA module overview cards.
 */
public class DashboardPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    public DashboardPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(UIUtils.COLOR_BG);
        setBorder(new EmptyBorder(20, 25, 20, 25));

        DataService dataService = DataService.getInstance();

        // 1. Header Section
        JPanel headerPanel = UIUtils.createHeaderPanel(
                "System Overview & Analytics",
                "Real-time summary of events, participants, and DSA processing modules."
        );
        add(headerPanel, BorderLayout.NORTH);

        // 2. Main Scrollable Content Container
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setOpaque(false);

        // 2A. Stat Cards Row (Calculated dynamically from DataService dataset)
        int totalEvents = dataService.getEvents().size();
        int totalParticipants = dataService.getParticipants().size();
        int totalRegistrations = dataService.getRegistrations().size();
        int activeEvents = 0;
        for (Event event : dataService.getEvents()) {
            if ("Active".equalsIgnoreCase(event.getStatus())) {
                activeEvents++;
            }
        }

        JPanel statsRow = new JPanel(new GridLayout(1, 4, 15, 0));
        statsRow.setOpaque(false);

        statsRow.add(UIUtils.createStatCard("Total Events", String.valueOf(totalEvents), "All registered events", UIUtils.COLOR_BLUE));
        statsRow.add(UIUtils.createStatCard("Participants", String.valueOf(totalParticipants), "Across all departments", UIUtils.COLOR_PURPLE));
        statsRow.add(UIUtils.createStatCard("Registrations", String.valueOf(totalRegistrations), "Total event signups", UIUtils.COLOR_EMERALD));
        statsRow.add(UIUtils.createStatCard("Active Events", String.valueOf(activeEvents), "Currently ongoing", UIUtils.COLOR_AMBER));

        mainContent.add(statsRow);
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));

        // 2B. Middle Section: Recent Active Events JTable
        JPanel activeEventsCard = UIUtils.createCardPanel();
        activeEventsCard.setLayout(new BorderLayout(10, 10));
        activeEventsCard.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblTableTitle = new JLabel("Currently Active Events");
        lblTableTitle.setFont(UIUtils.FONT_SECTION);
        lblTableTitle.setForeground(UIUtils.COLOR_TEXT_DARK);
        activeEventsCard.add(lblTableTitle, BorderLayout.NORTH);

        String[] columns = {"Event ID", "Title", "Category", "Date & Time", "Venue", "Capacity", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        List<Event> eventList = dataService.getEvents();
        int activeCount = 0;
        for (Event event : eventList) {
            if ("Active".equalsIgnoreCase(event.getStatus()) && activeCount < 6) {
                model.addRow(new Object[]{
                        event.getId(),
                        event.getTitle(),
                        event.getCategory(),
                        event.getDate() + " " + event.getTime(),
                        event.getVenue(),
                        event.getRegisteredCount() + " / " + event.getCapacity(),
                        event.getStatus()
                });
                activeCount++;
            }
        }

        JTable table = new JTable(model);
        UIUtils.styleTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        activeEventsCard.add(scrollPane, BorderLayout.CENTER);

        mainContent.add(activeEventsCard);
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));

        // 2C. DSA Modules Status Overview Grid
        JPanel dsaGridHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        dsaGridHeader.setOpaque(false);
        JLabel lblDsaTitle = new JLabel("DSA Algorithm Modules Architecture");
        lblDsaTitle.setFont(UIUtils.FONT_SECTION);
        lblDsaTitle.setForeground(UIUtils.COLOR_TEXT_DARK);
        dsaGridHeader.add(lblDsaTitle);

        mainContent.add(dsaGridHeader);
        mainContent.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel dsaGrid = new JPanel(new GridLayout(2, 3, 12, 12));
        dsaGrid.setOpaque(false);

        dsaGrid.add(createMiniModuleCard("M1 - Event Search", "String Matching (KMP / Rabin-Karp)", UIUtils.COLOR_BLUE));
        dsaGrid.add(createMiniModuleCard("M2 - Document Algorithms", "Suffix Array & LCP (Kasai)", UIUtils.COLOR_PURPLE));
        dsaGrid.add(createMiniModuleCard("M3 - Optimization", "Bitmask DP & Levenshtein Distance", UIUtils.COLOR_EMERALD));
        dsaGrid.add(createMiniModuleCard("M4 - Resource Flow", "Dinic Max Flow & Bipartite Matching", UIUtils.COLOR_AMBER));
        dsaGrid.add(createMiniModuleCard("M5 - Conflict Manager", "3-SAT & Vertex Cover 2-Approximation", UIUtils.COLOR_ROSE));
        dsaGrid.add(createMiniModuleCard("M6 - Event Analytics", "Randomized QuickSort & Reservoir Sampling", UIUtils.COLOR_CYAN));

        mainContent.add(dsaGrid);

        JScrollPane containerScroll = new JScrollPane(mainContent);
        containerScroll.setBorder(null);
        containerScroll.setOpaque(false);
        containerScroll.getViewport().setOpaque(false);
        containerScroll.getVerticalScrollBar().setUnitIncrement(12);

        add(containerScroll, BorderLayout.CENTER);
    }

    private JPanel createMiniModuleCard(String name, String algo, Color accent) {
        JPanel card = UIUtils.createCardPanel();
        card.setLayout(new BorderLayout(8, 6));
        card.setBorder(new EmptyBorder(12, 14, 12, 14));

        JLabel lblName = new JLabel(name);
        lblName.setFont(UIUtils.FONT_BOLD);
        lblName.setForeground(UIUtils.COLOR_TEXT_DARK);

        JLabel lblAlgo = new JLabel(algo);
        lblAlgo.setFont(UIUtils.FONT_SMALL);
        lblAlgo.setForeground(UIUtils.COLOR_TEXT_MUTED);

        JLabel lblStatus = new JLabel("Structure Ready");
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblStatus.setForeground(accent);

        card.add(lblName, BorderLayout.NORTH);
        card.add(lblAlgo, BorderLayout.CENTER);
        card.add(lblStatus, BorderLayout.SOUTH);

        return card;
    }
}
