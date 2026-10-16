package ui;

import algorithms.m4.BipartiteMatching;
import algorithms.m4.BipartiteMatching.MatchingResult;
import algorithms.m4.Dinic;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import model.Activity;
import model.Event;
import model.Venue;
import model.Volunteer;
import service.DataService;

/**
 * M4 - Resource Allocation & Volunteer Assignment UI Panel.
 * SECTION 1: Resource Capacity Allocation using Dinic's Maximum Flow Algorithm.
 * SECTION 2: Volunteer <-> Activity Assignment using Maximum Bipartite Matching Algorithm.
 */
public class M4ResourcePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    // Section 1: Dinic Max Flow Views
    private final JComboBox<String> cbDemandEvent;
    private final JTextField txtDemandCap;
    private final JTable tableDinic;
    private final DefaultTableModel modelDinic;
    private final JLabel lblDinicStatus;
    private final JLabel lblDinicRequested;
    private final JLabel lblDinicAllocated;
    private final JLabel lblDinicRemaining;
    private final JLabel lblDinicExecution;

    // Section 2: Bipartite Matching Views
    private final JTable tableBipartite;
    private final DefaultTableModel modelBipartite;
    private final JLabel lblBipartiteStatus;
    private final JLabel lblBipartiteVolunteers;
    private final JLabel lblBipartiteMatched;
    private final JLabel lblBipartiteUnmatched;
    private final JLabel lblBipartiteDetails;
    private final JPanel pnlDinicSummary;
    private final JPanel pnlBipartiteSummary;

    public M4ResourcePanel() {
        setLayout(new BorderLayout());
        setBackground(UIUtils.COLOR_BG);
        setBorder(new EmptyBorder(20, 25, 20, 25));

        List<Event> events = DataService.getInstance().getEvents();
        String[] eventOptions = new String[events.size()];
        for (int i = 0; i < events.size(); i++) {
            Event ev = events.get(i);
            eventOptions[i] = ev.getId() + ": " + ev.getTitle();
        }
        cbDemandEvent = new JComboBox<>(eventOptions);
        cbDemandEvent.setFont(UIUtils.FONT_REGULAR);
        cbDemandEvent.setPreferredSize(new Dimension(300, 34));
        cbDemandEvent.addActionListener(e -> updateDemandCapFromEvent());

        JPanel eventRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        eventRow.setOpaque(false);
        JLabel lblEvent = new JLabel("Target Event:");
        lblEvent.setFont(UIUtils.FONT_BOLD);
        eventRow.add(lblEvent);
        eventRow.add(cbDemandEvent);

        JPanel topContainer = new JPanel(new BorderLayout(0, 10));
        topContainer.setOpaque(false);
        topContainer.setBorder(new EmptyBorder(0, 0, 10, 0));
        topContainer.add(eventRow, BorderLayout.WEST);

        add(topContainer, BorderLayout.NORTH);

        JPanel sectionStack = new ScrollableSectionStack();
        sectionStack.setOpaque(false);

        JPanel cardDinic = UIUtils.createCardPanel();
        cardDinic.setLayout(new BorderLayout(0, 14));
        cardDinic.setBorder(new EmptyBorder(18, 20, 18, 20));
        cardDinic.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel dinicContent = new JPanel();
        dinicContent.setLayout(new BoxLayout(dinicContent, BoxLayout.Y_AXIS));
        dinicContent.setOpaque(false);

        JPanel dinicHeader = new JPanel();
        dinicHeader.setLayout(new BoxLayout(dinicHeader, BoxLayout.Y_AXIS));
        dinicHeader.setOpaque(false);
        JPanel sectionHeading = createSectionHeading("RESOURCE ALLOCATION", "Dinic Maximum Flow", UIUtils.COLOR_AMBER);
        sectionHeading.setAlignmentX(Component.LEFT_ALIGNMENT);
        dinicHeader.add(sectionHeading);
        cardDinic.add(dinicHeader, BorderLayout.NORTH);

        JPanel demandRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        demandRow.setOpaque(false);
        demandRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblDemand = new JLabel("Requested Capacity:");
        lblDemand.setFont(UIUtils.FONT_BOLD);
        txtDemandCap = new JTextField("300", 10);
        txtDemandCap.setFont(UIUtils.FONT_REGULAR);
        txtDemandCap.setPreferredSize(new Dimension(130, 34));
        txtDemandCap.addActionListener(e -> runDinicAllocation());

        JButton btnRunDinic = UIUtils.createStyledButton("Allocate Capacity", UIUtils.COLOR_AMBER, Color.WHITE);
        btnRunDinic.setPreferredSize(new Dimension(180, 34));
        btnRunDinic.addActionListener(e -> runDinicAllocation());
        
        demandRow.add(lblDemand);
        demandRow.add(txtDemandCap);
        demandRow.add(Box.createRigidArea(new Dimension(10, 0)));
        demandRow.add(btnRunDinic);

        dinicContent.add(demandRow);
        dinicContent.add(Box.createRigidArea(new Dimension(0, 12)));

        String[] colsDinic = {"Venue", "Capacity", "Allocated", "Remaining", "Status"};
        modelDinic = new DefaultTableModel(colsDinic, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tableDinic = new JTable(modelDinic);
        configureTable(tableDinic, new int[]{245, 147, 147, 147, 294});
        JScrollPane spDinic = createTableScrollPane(tableDinic, 148);
        JPanel dinicTableContainer = createTableContainer(spDinic);
        dinicContent.add(dinicTableContainer);
        dinicContent.add(Box.createRigidArea(new Dimension(0, 12)));

        lblDinicStatus = createStatusLabel("Allocation ready.", UIUtils.COLOR_TEXT_DARK);
        lblDinicRequested = createMetricValue("0");
        lblDinicAllocated = createMetricValue("0");
        lblDinicRemaining = createMetricValue("0");
        lblDinicExecution = createDetailLabel("Execution time: 0 ns");
        pnlDinicSummary = createResultSummary(
            "Allocation Result", UIUtils.COLOR_AMBER, lblDinicStatus, lblDinicExecution,
            new String[]{"Requested", "Allocated", "Remaining"},
            new JLabel[]{lblDinicRequested, lblDinicAllocated, lblDinicRemaining}
        );
        JPanel dinicSummaryContainer = createTableContainer(pnlDinicSummary);
        dinicContent.add(dinicSummaryContainer);
        cardDinic.add(dinicContent, BorderLayout.CENTER);

        JPanel cardBipartite = UIUtils.createCardPanel();
        cardBipartite.setLayout(new BorderLayout(0, 14));
        cardBipartite.setBorder(new EmptyBorder(18, 20, 18, 20));
        cardBipartite.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel bipartiteHeader = new JPanel(new BorderLayout());
        bipartiteHeader.setOpaque(false);
        bipartiteHeader.add(createSectionHeading("VOLUNTEER ASSIGNMENT", "Bipartite Matching", UIUtils.COLOR_PRIMARY), BorderLayout.WEST);

        JButton btnRunBipartite = UIUtils.createStyledButton("Perform Bipartite Matching", UIUtils.COLOR_PRIMARY, Color.WHITE);
        btnRunBipartite.setPreferredSize(new Dimension(220, 38));
        btnRunBipartite.addActionListener(e -> runBipartiteMatching());
        bipartiteHeader.add(btnRunBipartite, BorderLayout.EAST);
        
        cardBipartite.add(bipartiteHeader, BorderLayout.NORTH);

        JPanel bipartiteContent = new JPanel();
        bipartiteContent.setLayout(new BoxLayout(bipartiteContent, BoxLayout.Y_AXIS));
        bipartiteContent.setOpaque(false);
        bipartiteContent.add(Box.createRigidArea(new Dimension(0, 12)));

        String[] colsBipartite = {"Volunteer", "Role", "Assigned Activity", "Status"};
        modelBipartite = new DefaultTableModel(colsBipartite, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tableBipartite = new JTable(modelBipartite);
        configureTable(tableBipartite, new int[]{196, 216, 343, 225});
        JScrollPane spBipartite = createTableScrollPane(tableBipartite, 148);
        JPanel bipartiteTableContainer = createTableContainer(spBipartite);
        bipartiteContent.add(bipartiteTableContainer);
        bipartiteContent.add(Box.createRigidArea(new Dimension(0, 12)));

        lblBipartiteStatus = createStatusLabel("Matching ready.", UIUtils.COLOR_PRIMARY);
        lblBipartiteVolunteers = createMetricValue("0");
        lblBipartiteMatched = createMetricValue("0");
        lblBipartiteUnmatched = createMetricValue("0");
        lblBipartiteDetails = createDetailLabel("Unmatched activities: 0 | Execution time: 0 ns");
        pnlBipartiteSummary = createResultSummary(
            "Matching Result", UIUtils.COLOR_PRIMARY, lblBipartiteStatus, lblBipartiteDetails,
            new String[]{"Volunteers", "Matched", "Unmatched"},
            new JLabel[]{lblBipartiteVolunteers, lblBipartiteMatched, lblBipartiteUnmatched}
        );
        JPanel bipartiteSummaryContainer = createTableContainer(pnlBipartiteSummary);
        bipartiteContent.add(bipartiteSummaryContainer);
        cardBipartite.add(bipartiteContent, BorderLayout.CENTER);

        sectionStack.add(cardDinic);
        sectionStack.add(Box.createRigidArea(new Dimension(0, 18)));
        sectionStack.add(cardBipartite);

        JScrollPane pageScroll = new JScrollPane(sectionStack);
        pageScroll.setBorder(BorderFactory.createEmptyBorder());
        pageScroll.setOpaque(false);
        pageScroll.getViewport().setOpaque(false);
        pageScroll.getVerticalScrollBar().setUnitIncrement(16);
        add(pageScroll, BorderLayout.CENTER);

        // Initialize text field from event selection and run initial calculations
        updateDemandCapFromEvent();
        runDinicAllocation();
        runBipartiteMatching();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent event) {
                int tableWidth = Math.max(0, Math.min(980, getWidth() - 140));
                setTableWidth(spDinic, dinicTableContainer, tableWidth);
                setTableWidth(spBipartite, bipartiteTableContainer, tableWidth);
                setComponentWidth(dinicSummaryContainer, tableWidth, 120);
                setComponentWidth(bipartiteSummaryContainer, tableWidth, 120);
                sectionStack.revalidate();
            }
        });
    }

    private JPanel createSectionHeading(String title, String subtitle, Color accent) {
        JPanel heading = new JPanel();
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        heading.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UIUtils.FONT_SMALL);
        titleLabel.setForeground(accent);

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(UIUtils.FONT_SECTION);
        subtitleLabel.setForeground(UIUtils.COLOR_TEXT_DARK);

        heading.add(titleLabel);
        heading.add(Box.createRigidArea(new Dimension(0, 3)));
        heading.add(subtitleLabel);
        return heading;
    }

    private void configureTable(JTable table, int[] columnWidths) {
        UIUtils.styleTable(table);
        table.setFont(UIUtils.FONT_SMALL);
        table.setRowHeight(32);
        table.setFillsViewportHeight(false);
        for (int column = 0; column < columnWidths.length; column++) {
            table.getColumnModel().getColumn(column).setPreferredWidth(columnWidths[column]);
        }
    }

    private JScrollPane createTableScrollPane(JTable table, int height) {
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIUtils.COLOR_BORDER));
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setPreferredSize(new Dimension(980, height));
        scrollPane.setMinimumSize(new Dimension(0, height));
        scrollPane.setMaximumSize(new Dimension(980, height));
        return scrollPane;
    }

    private JPanel createTableContainer(JComponent component) {
        JPanel container = new JPanel(new GridBagLayout());
        container.setOpaque(false);
        container.setAlignmentX(Component.CENTER_ALIGNMENT);
        Dimension size = component.getPreferredSize();
        container.setPreferredSize(size);
        container.setMinimumSize(new Dimension(0, size.height));
        container.setMaximumSize(size);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.CENTER;
        container.add(component, constraints);
        return container;
    }

    private void setTableWidth(JScrollPane scrollPane, JPanel container, int width) {
        setComponentWidth(container, width, 148);
        Dimension size = new Dimension(width, 148);
        scrollPane.setPreferredSize(size);
        scrollPane.setMinimumSize(size);
        scrollPane.setMaximumSize(size);
    }
    
    private void setComponentWidth(JPanel container, int width, int height) {
        Dimension size = new Dimension(width, height);
        container.setPreferredSize(size);
        container.setMinimumSize(size);
        container.setMaximumSize(size);
    }

    private JLabel createMetricValue(String value) {
        JLabel label = new JLabel(value);
        label.setFont(UIUtils.FONT_SECTION);
        label.setForeground(UIUtils.COLOR_TEXT_DARK);
        return label;
    }

    private JLabel createStatusLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(UIUtils.FONT_BOLD);
        label.setForeground(color);
        return label;
    }

    private JLabel createDetailLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UIUtils.FONT_SMALL);
        label.setForeground(UIUtils.COLOR_TEXT_MUTED);
        return label;
    }

    private JPanel createResultSummary(String title, Color accent, JLabel status, JLabel details,
                                       String[] metricNames, JLabel[] metricValues) {
        JPanel summary = new JPanel();
        summary.setLayout(new BoxLayout(summary, BoxLayout.Y_AXIS));
        summary.setBackground(new Color(248, 250, 252));
        summary.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIUtils.COLOR_BORDER),
                new EmptyBorder(10, 12, 10, 12)
        ));
        summary.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UIUtils.FONT_BOLD);
        titleLabel.setForeground(accent);
        summary.add(titleLabel);
        summary.add(Box.createRigidArea(new Dimension(0, 8)));

        JPanel metrics = new JPanel(new GridLayout(1, metricNames.length, 16, 0));
        metrics.setOpaque(false);
        for (int index = 0; index < metricNames.length; index++) {
            JPanel metric = new JPanel();
            metric.setLayout(new BoxLayout(metric, BoxLayout.Y_AXIS));
            metric.setOpaque(false);
            JLabel name = new JLabel(metricNames[index]);
            name.setFont(UIUtils.FONT_SMALL);
            name.setForeground(UIUtils.COLOR_TEXT_MUTED);
            metric.add(name);
            metric.add(metricValues[index]);
            metrics.add(metric);
        }
        summary.add(metrics);
        summary.add(Box.createRigidArea(new Dimension(0, 6)));
        summary.add(details);
        summary.add(Box.createRigidArea(new Dimension(0, 4)));
        summary.add(status);
        summary.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        return summary;
    }

    private static class ScrollableSectionStack extends JPanel implements Scrollable {
        private static final long serialVersionUID = 1L;
        private ScrollableSectionStack() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 16;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return Math.max(16, visibleRect.height - 16);
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }
    }

    private void updateDemandCapFromEvent() {
        int idx = cbDemandEvent.getSelectedIndex();
        List<Event> events = DataService.getInstance().getEvents();
        if (idx >= 0 && idx < events.size()) {
            txtDemandCap.setText(String.valueOf(events.get(idx).getCapacity()));
        }
    }

    private void runDinicAllocation() {
        String demandStr = txtDemandCap.getText().trim();
        int requestedCapacity;
        try {
            requestedCapacity = Integer.parseInt(demandStr);
            if (requestedCapacity <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            lblDinicStatus.setForeground(new Color(225, 29, 72));
            lblDinicStatus.setText("Input Error: Please enter a valid positive integer for Requested Capacity.");
            lblDinicRequested.setText("0");
            lblDinicAllocated.setText("0");
            lblDinicRemaining.setText("0");
            lblDinicExecution.setText("Execution time: 0 ns");
            modelDinic.setRowCount(0);
            return;
        }

        List<Venue> venues = DataService.getInstance().getVenues();
        int numVenues = venues.size();

        // Build Dinic Network:
        // Source = 0
        // Event Demand Node = 1
        // Venue Nodes = 2 to (numVenues + 1)
        // Sink = numVenues + 2
        int source = 0;
        int demandNode = 1;
        int sink = numVenues + 2;
        int numNodes = numVenues + 3;

        Dinic dinic = new Dinic(numNodes);

        // 1. Source -> Demand Node (capacity = requestedCapacity)
        dinic.addEdge(source, demandNode, requestedCapacity);

        // 2. Demand Node -> Venue Nodes (capacity = venue max capacity)
        for (int i = 0; i < numVenues; i++) {
            Venue v = venues.get(i);
            int venueNode = i + 2;
            dinic.addEdge(demandNode, venueNode, v.getCapacity());
        }

        // 3. Venue Nodes -> Sink (capacity = venue max capacity)
        for (int i = 0; i < numVenues; i++) {
            Venue v = venues.get(i);
            int venueNode = i + 2;
            dinic.addEdge(venueNode, sink, v.getCapacity());
        }

        long startTime = System.nanoTime();

        int maxFlow = dinic.maxFlow(source, sink);

        long endTime = System.nanoTime();
        long elapsedNs = endTime - startTime;
        double elapsedMs = elapsedNs / 1_000_000.0;

        modelDinic.setRowCount(0);

        // Retrieve flows allocated to each venue from graph edges
        List<List<Dinic.Edge>> graph = dinic.getGraph();
        for (int i = 0; i < numVenues; i++) {
            Venue v = venues.get(i);
            int venueNode = i + 2;

            int flowAllocated = 0;
            for (Dinic.Edge e : graph.get(demandNode)) {
                if (e.to == venueNode) {
                    flowAllocated = e.flow;
                    break;
                }
            }

            int remainingCap = v.getCapacity() - flowAllocated;
            String status = (flowAllocated == v.getCapacity()) ? "FULL CAPACITY (100%)"
                    : (flowAllocated > 0) ? "PARTIAL FLOW (" + (flowAllocated * 100 / v.getCapacity()) + "%)"
                    : "UNALLOCATED";

            modelDinic.addRow(new Object[]{
                    v.getName(),
                    v.getCapacity() + " seats",
                    flowAllocated + " seats",
                    remainingCap + " seats",
                    status
            });
        }

        lblDinicStatus.setForeground(new Color(217, 119, 6)); // Amber
        lblDinicStatus.setText("Allocation complete.");
        lblDinicRequested.setText(String.format("%,d", requestedCapacity));
        lblDinicAllocated.setText(String.format("%,d", maxFlow));
        lblDinicRemaining.setText(String.format("%,d", requestedCapacity - maxFlow));
        lblDinicExecution.setText(String.format("Execution time: %,d ns (%.3f ms)", elapsedNs, elapsedMs));
    }

    private void runBipartiteMatching() {
        List<Volunteer> volunteers = DataService.getInstance().getVolunteers();
        List<Activity> activities = DataService.getInstance().getActivities();

        int numV = volunteers.size();
        int numA = activities.size();

        if (numV == 0 || numA == 0) {
            lblBipartiteStatus.setForeground(new Color(225, 29, 72));
            lblBipartiteStatus.setText("No Data: Volunteers or Activities list is empty.");
            lblBipartiteVolunteers.setText("0");
            lblBipartiteMatched.setText("0");
            lblBipartiteUnmatched.setText("0");
            lblBipartiteDetails.setText("Unmatched activities: 0 | Execution time: 0 ns");
            modelBipartite.setRowCount(0);
            return;
        }

        BipartiteMatching bm = new BipartiteMatching(numV, numA);

        // Build edges: Edge between Volunteer v and Activity a if
        // Volunteer's assigned event matches Activity's eventId OR role compatibility
        for (int i = 0; i < numV; i++) {
            Volunteer vol = volunteers.get(i);
            for (int j = 0; j < numA; j++) {
                Activity act = activities.get(j);
                if (vol.getAssignedEvent() != null && vol.getAssignedEvent().equalsIgnoreCase(act.getEventId())) {
                    bm.addEdge(i, j);
                } else if (vol.getRole() != null && (vol.getRole().contains("Lead") || vol.getRole().contains("Coordinator") || vol.getRole().contains("General"))) {
                    bm.addEdge(i, j);
                }
            }
        }

        long startTime = System.nanoTime();

        MatchingResult result = bm.computeMatching();

        long endTime = System.nanoTime();
        long elapsedNs = endTime - startTime;
        double elapsedMs = elapsedNs / 1_000_000.0;

        modelBipartite.setRowCount(0);

        Map<Integer, Integer> v2a = result.volunteerToActivityMap;

        for (int i = 0; i < numV; i++) {
            Volunteer vol = volunteers.get(i);
            if (v2a.containsKey(i)) {
                int actIdx = v2a.get(i);
                Activity act = activities.get(actIdx);
                modelBipartite.addRow(new Object[]{
                        vol.getName(),
                        vol.getRole(),
                        act.getTitle(),
                        "MATCHED (Bipartite Optimal)"
                });
            } else {
                modelBipartite.addRow(new Object[]{
                        vol.getName(),
                        vol.getRole(),
                        "Unassigned",
                        "UNMATCHED (No Capacity / Conflict)"
                });
            }
        }

        lblBipartiteStatus.setForeground(new Color(16, 185, 129)); // Emerald Green
    lblBipartiteStatus.setText("Matching complete.");
    lblBipartiteVolunteers.setText(String.format("%,d", numV));
    lblBipartiteMatched.setText(String.format("%,d", result.maxMatches));
    lblBipartiteUnmatched.setText(String.format("%,d", result.unmatchedVolunteers.size()));
    lblBipartiteDetails.setText(String.format("Unmatched activities: %,d | Execution time: %,d ns (%.3f ms)",
        result.unmatchedActivities.size(), elapsedNs, elapsedMs));
    }
}
