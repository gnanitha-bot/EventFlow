package ui;

import algorithms.m6.RandomizedQuickSort;
import algorithms.m6.RandomizedQuickSort.SortRecord;
import algorithms.m6.ReservoirSampling;
import algorithms.m6.ReservoirSampling.SamplingResult;
import model.Event;
import model.Participant;
import service.DataService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * M6 - Event Analytics & Stream Sampling UI Panel.
 * SECTION 1: Event Ranking using Randomized QuickSort.
 * SECTION 2: Registration Stream Sampling using Reservoir Sampling (Algorithm R).
 */
public class M6AnalyticsPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    // Section 1: QuickSort
    private JComboBox<String> cbMetric;
    private JComboBox<String> cbOrder;
    private JTable tableSort;
    private DefaultTableModel modelSort;
    private JLabel lblSortStatus;
    private JLabel lblSortStats;

    // Section 2: Reservoir Sampling
    private JComboBox<String> cbStream;
    private JTextField txtReservoirK;
    private JTable tableSample;
    private DefaultTableModel modelSample;
    private JLabel lblSampleStatus;
    private JLabel lblSampleStats;

    public M6AnalyticsPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(UIUtils.COLOR_BG);
        setBorder(new EmptyBorder(20, 25, 20, 25));

        // Header & Banner
        JPanel topContainer = new JPanel(new BorderLayout(0, 10));
        topContainer.setOpaque(false);
        topContainer.add(UIUtils.createHeaderPanel(
                "Module M6: Event Analytics & Stream Sampling",
                "Ranks event records via Randomized QuickSort and draws uniform random samples via Reservoir Sampling."
        ), BorderLayout.NORTH);

        topContainer.add(UIUtils.createModuleBanner(
                "M6",
                "Randomized QuickSort & Reservoir Sampling Engine",
                "SECTION 1: Random-pivot QuickSort for event analytics ranking. SECTION 2: Algorithm-R Reservoir Sampling over registration streams.",
                "Randomized QuickSort / Reservoir Sampling (Algorithm R)"
        ), BorderLayout.SOUTH);

        add(topContainer, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 0, 15));
        centerPanel.setOpaque(false);

        // =========================================================================
        // SECTION 1: Event Ranking — Randomized QuickSort
        // =========================================================================
        JPanel cardSort = UIUtils.createCardPanel();
        cardSort.setLayout(new BorderLayout(10, 10));
        cardSort.setBorder(new EmptyBorder(14, 16, 14, 16));

        // Header: Heading on Row 1, Controls on Row 2
        JPanel sortHeader = new JPanel();
        sortHeader.setLayout(new BoxLayout(sortHeader, BoxLayout.Y_AXIS));
        sortHeader.setOpaque(false);

        JLabel lblSortTitle = new JLabel("SECTION 1: Event Analytics Ranking (Randomized QuickSort)");
        lblSortTitle.setFont(UIUtils.FONT_SECTION);
        lblSortTitle.setForeground(UIUtils.COLOR_CYAN);
        lblSortTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel sortControls = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        sortControls.setOpaque(false);
        sortControls.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblMetric = new JLabel("Sort Metric:");
        lblMetric.setFont(UIUtils.FONT_BOLD);

        cbMetric = new JComboBox<>(new String[]{
                "Registered Count",
                "Remaining Capacity",
                "Fill Rate (%)",
                "Event Capacity"
        });
        cbMetric.setFont(UIUtils.FONT_REGULAR);
        cbMetric.setPreferredSize(new Dimension(170, 34));

        JLabel lblOrder = new JLabel("Order:");
        lblOrder.setFont(UIUtils.FONT_BOLD);

        cbOrder = new JComboBox<>(new String[]{"Descending (High to Low)", "Ascending (Low to High)"});
        cbOrder.setFont(UIUtils.FONT_REGULAR);
        cbOrder.setPreferredSize(new Dimension(190, 34));

        JButton btnSort = UIUtils.createStyledButton("Sort Events (QuickSort)", UIUtils.COLOR_CYAN, Color.WHITE);
        btnSort.addActionListener(e -> runQuickSort());

        sortControls.add(lblMetric);
        sortControls.add(cbMetric);
        sortControls.add(Box.createRigidArea(new Dimension(6, 0)));
        sortControls.add(lblOrder);
        sortControls.add(cbOrder);
        sortControls.add(Box.createRigidArea(new Dimension(6, 0)));
        sortControls.add(btnSort);

        sortHeader.add(lblSortTitle);
        sortHeader.add(Box.createRigidArea(new Dimension(0, 8)));
        sortHeader.add(sortControls);

        cardSort.add(sortHeader, BorderLayout.NORTH);

        // Sort Table
        String[] colsSort = {"Rank", "Event ID", "Event Title", "Category", "Status", "Sort Metric Value", "QuickSort Decision"};
        modelSort = new DefaultTableModel(colsSort, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tableSort = new JTable(modelSort);
        JScrollPane spSort = new JScrollPane(tableSort);
        spSort.setBorder(BorderFactory.createLineBorder(UIUtils.COLOR_BORDER));
        spSort.getViewport().setBackground(Color.WHITE);
        configureTableColumns(tableSort, spSort, new int[]{70, 95, 250, 120, 100, 160, 220});

        cardSort.add(spSort, BorderLayout.CENTER);

        // Sort Footer
        JPanel sortFooter = new JPanel(new GridLayout(2, 1, 3, 3));
        sortFooter.setOpaque(false);
        sortFooter.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                new EmptyBorder(8, 10, 8, 10)
        ));

        lblSortStatus = new JLabel("Status: Select a metric and run Randomized QuickSort to rank events.");
        lblSortStatus.setFont(UIUtils.FONT_BOLD);
        lblSortStatus.setForeground(UIUtils.COLOR_TEXT_DARK);

        lblSortStats = new JLabel("Records Sorted: 0 | Metric: - | Order: - | Exec Time: 0 ns");
        lblSortStats.setFont(UIUtils.FONT_REGULAR);
        lblSortStats.setForeground(UIUtils.COLOR_TEXT_MUTED);

        sortFooter.add(lblSortStatus);
        sortFooter.add(lblSortStats);
        cardSort.add(sortFooter, BorderLayout.SOUTH);

        centerPanel.add(cardSort);

        // =========================================================================
        // SECTION 2: Registration Stream Sampling — Reservoir Sampling
        // =========================================================================
        JPanel cardSample = UIUtils.createCardPanel();
        cardSample.setLayout(new BorderLayout(10, 10));
        cardSample.setBorder(new EmptyBorder(14, 16, 14, 16));

        // Header: Heading on Row 1, Controls on Row 2
        JPanel sampleHeader = new JPanel();
        sampleHeader.setLayout(new BoxLayout(sampleHeader, BoxLayout.Y_AXIS));
        sampleHeader.setOpaque(false);

        JLabel lblSampleTitle = new JLabel("SECTION 2: Registration Stream Sampling (Reservoir Sampling)");
        lblSampleTitle.setFont(UIUtils.FONT_SECTION);
        lblSampleTitle.setForeground(UIUtils.COLOR_PRIMARY);
        lblSampleTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel sampleControls = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        sampleControls.setOpaque(false);
        sampleControls.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblStream = new JLabel("Stream Source:");
        lblStream.setFont(UIUtils.FONT_BOLD);

        cbStream = new JComboBox<>(new String[]{
                "Participant Registrations (" + DataService.getInstance().getParticipants().size() + " records)",
                "Event Title Stream (" + DataService.getInstance().getEvents().size() + " events)",
                "Combined Registration Feed (" + (DataService.getInstance().getParticipants().size() + DataService.getInstance().getEvents().size()) + " records)"
        });
        cbStream.setFont(UIUtils.FONT_REGULAR);
        cbStream.setPreferredSize(new Dimension(280, 34));

        JLabel lblK = new JLabel("Reservoir Size (k):");
        lblK.setFont(UIUtils.FONT_BOLD);

        txtReservoirK = new JTextField("5", 4);
        txtReservoirK.setFont(UIUtils.FONT_REGULAR);
        txtReservoirK.setPreferredSize(new Dimension(55, 34));
        txtReservoirK.addActionListener(e -> runReservoirSampling());

        JButton btnSample = UIUtils.createStyledButton("Draw Sample (Reservoir)", UIUtils.COLOR_PRIMARY, Color.WHITE);
        btnSample.addActionListener(e -> runReservoirSampling());

        sampleControls.add(lblStream);
        sampleControls.add(cbStream);
        sampleControls.add(Box.createRigidArea(new Dimension(6, 0)));
        sampleControls.add(lblK);
        sampleControls.add(txtReservoirK);
        sampleControls.add(Box.createRigidArea(new Dimension(6, 0)));
        sampleControls.add(btnSample);

        sampleHeader.add(lblSampleTitle);
        sampleHeader.add(Box.createRigidArea(new Dimension(0, 8)));
        sampleHeader.add(sampleControls);

        cardSample.add(sampleHeader, BorderLayout.NORTH);

        // Sample Table
        String[] colsSample = {"Sample #", "Record ID", "Record Label", "Stream Position", "Reservoir Status"};
        modelSample = new DefaultTableModel(colsSample, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tableSample = new JTable(modelSample);
        JScrollPane spSample = new JScrollPane(tableSample);
        spSample.setBorder(BorderFactory.createLineBorder(UIUtils.COLOR_BORDER));
        spSample.getViewport().setBackground(Color.WHITE);
        configureTableColumns(tableSample, spSample, new int[]{85, 105, 320, 130, 250});

        cardSample.add(spSample, BorderLayout.CENTER);

        // Sample Footer
        JPanel sampleFooter = new JPanel(new GridLayout(2, 1, 3, 3));
        sampleFooter.setOpaque(false);
        sampleFooter.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                new EmptyBorder(8, 10, 8, 10)
        ));

        lblSampleStatus = new JLabel("Status: Select a stream and reservoir size k, then draw a sample.");
        lblSampleStatus.setFont(UIUtils.FONT_BOLD);
        lblSampleStatus.setForeground(UIUtils.COLOR_PRIMARY);

        lblSampleStats = new JLabel("Stream Size: 0 | Reservoir k: 0 | Actual Sample: 0 | Exec Time: 0 ns");
        lblSampleStats.setFont(UIUtils.FONT_REGULAR);
        lblSampleStats.setForeground(UIUtils.COLOR_TEXT_MUTED);

        sampleFooter.add(lblSampleStatus);
        sampleFooter.add(lblSampleStats);
        cardSample.add(sampleFooter, BorderLayout.SOUTH);

        centerPanel.add(cardSample);

        JScrollPane pageScroll = new JScrollPane(centerPanel);
        pageScroll.setBorder(null);
        pageScroll.setOpaque(false);
        pageScroll.getViewport().setOpaque(false);
        pageScroll.getVerticalScrollBar().setUnitIncrement(16);
        pageScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(pageScroll, BorderLayout.CENTER);

        // Initial runs
        runQuickSort();
        runReservoirSampling();
    }

    private void configureTableColumns(JTable table, JScrollPane scrollPane, int[] colWidths) {
        UIUtils.styleTable(table);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        int totalWidth = 0;
        for (int i = 0; i < colWidths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(colWidths[i]);
            table.getColumnModel().getColumn(i).setMinWidth(Math.min(colWidths[i], 50));
            totalWidth += colWidths[i];
        }
        table.setPreferredScrollableViewportSize(new Dimension(totalWidth, 135));
        final int minWidth = totalWidth;
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().addChangeListener(e -> {
            int vpWidth = scrollPane.getViewport().getWidth();
            if (vpWidth > minWidth) {
                table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
            } else {
                table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            }
        });
    }

    // -------------------------------------------------------------------------
    // Section 1: Randomized QuickSort
    // -------------------------------------------------------------------------
    private void runQuickSort() {
        List<Event> events = DataService.getInstance().getEvents();
        int metricIdx = cbMetric.getSelectedIndex();
        boolean descending = cbOrder.getSelectedIndex() == 0;
        String metricName = (String) cbMetric.getSelectedItem();

        // Build SortRecord array from DataService events
        SortRecord[] records = new SortRecord[events.size()];
        for (int i = 0; i < events.size(); i++) {
            Event ev = events.get(i);
            double value;
            String unit;
            switch (metricIdx) {
                case 0: value = ev.getRegisteredCount(); unit = "registered"; break;
                case 1: value = ev.getCapacity() - ev.getRegisteredCount(); unit = "seats free"; break;
                case 2: value = ev.getCapacity() > 0
                        ? Math.round((ev.getRegisteredCount() * 100.0 / ev.getCapacity()) * 10.0) / 10.0 : 0;
                        unit = "%"; break;
                default: value = ev.getCapacity(); unit = "capacity"; break;
            }
            records[i] = new SortRecord(ev.getId(), ev.getTitle(), value, unit);
        }

        long startTime = System.nanoTime();

        // Randomized QuickSort (not Arrays.sort)
        RandomizedQuickSort.sort(records, 0, records.length - 1, !descending); // ascending=!descending

        long elapsedNs = System.nanoTime() - startTime;
        double elapsedMs = elapsedNs / 1_000_000.0;

        // Populate sorted rows
        modelSort.setRowCount(0);
        for (int i = 0; i < records.length; i++) {
            SortRecord r = records[i];
            String decision = (i == 0) ? "RANK #1 (Top Record)"
                    : (i == records.length - 1) ? "RANK #" + records.length + " (Bottom Record)"
                    : "RANK #" + (i + 1);
            modelSort.addRow(new Object[]{
                    "#" + (i + 1),
                    r.id,
                    r.label,
                    DataService.getInstance().getEvents().stream()
                            .filter(e -> e.getId().equals(r.id)).findFirst()
                            .map(Event::getCategory).orElse("-"),
                    DataService.getInstance().getEvents().stream()
                            .filter(e -> e.getId().equals(r.id)).findFirst()
                            .map(Event::getStatus).orElse("-"),
                    String.format("%.1f %s", r.value, r.unit),
                    decision
            });
        }

        lblSortStatus.setForeground(new Color(6, 182, 212)); // Cyan
        lblSortStatus.setText(String.format(
                "QuickSort Complete: %d events ranked by '%s' (%s).",
                records.length, metricName, descending ? "Descending" : "Ascending"));

        lblSortStats.setText(String.format(
                "Records Sorted: %d | Metric: %s | Order: %s | Exec Time: %,d ns (%.3f ms)",
                records.length, metricName, descending ? "Desc" : "Asc", elapsedNs, elapsedMs));
    }

    // -------------------------------------------------------------------------
    // Section 2: Reservoir Sampling
    // -------------------------------------------------------------------------
    private void runReservoirSampling() {
        String kStr = txtReservoirK.getText().trim();
        int k;
        try {
            k = Integer.parseInt(kStr);
        } catch (NumberFormatException ex) {
            lblSampleStatus.setForeground(new Color(225, 29, 72));
            lblSampleStatus.setText("Input Error: Reservoir size k must be a valid integer.");
            return;
        }

        int streamIdx = cbStream.getSelectedIndex();

        // Build stream
        List<String> stream = new ArrayList<>();
        List<Participant> participants = DataService.getInstance().getParticipants();
        List<Event> events = DataService.getInstance().getEvents();

        switch (streamIdx) {
            case 0:
                // Participant registrations
                for (Participant p : participants) {
                    stream.add(p.getId() + " | " + p.getName() + " -> " + p.getRegisteredEvent());
                }
                break;
            case 1:
                // Event titles
                for (Event ev : events) {
                    stream.add(ev.getId() + " | " + ev.getTitle() + " (" + ev.getStatus() + ")");
                }
                break;
            default:
                // Combined: participants + events
                for (Participant p : participants) {
                    stream.add("REG:" + p.getId() + " | " + p.getName() + " [" + p.getStatus() + "]");
                }
                for (Event ev : events) {
                    stream.add("EVT:" + ev.getId() + " | " + ev.getTitle());
                }
                break;
        }

        long startTime = System.nanoTime();

        SamplingResult result = ReservoirSampling.sample(stream, k);

        long elapsedNs = System.nanoTime() - startTime;
        double elapsedMs = elapsedNs / 1_000_000.0;

        modelSample.setRowCount(0);

        if (k <= 0) {
            lblSampleStatus.setForeground(new Color(225, 29, 72));
            lblSampleStatus.setText("Edge Case: k = " + k + " - Reservoir size must be > 0. Returning empty sample.");
            lblSampleStats.setText(String.format("Stream Size: %d | Reservoir k: %d | Actual Sample: 0 | Exec Time: %,d ns (%.3f ms)",
                    stream.size(), k, elapsedNs, elapsedMs));
            return;
        }

        for (int i = 0; i < result.sample.size(); i++) {
            String item = result.sample.get(i);
            int streamPos = stream.indexOf(item);
            String[] parts = item.split(" \\| ", 2);
            String recordId = parts.length > 0 ? parts[0] : "REC-" + (i + 1);
            String recordLabel = parts.length > 1 ? parts[1] : item;

            double prob = result.streamSize > 0 ? Math.min(1.0, (double) k / result.streamSize) * 100.0 : 0;

            modelSample.addRow(new Object[]{
                    "Sample #" + (i + 1),
                    recordId,
                    recordLabel,
                    (streamPos >= 0 ? "Stream Pos #" + (streamPos + 1) : "Slot #" + (i + 1)) + " / " + result.streamSize,
                    String.format("Selected (Probability: %d/%d = %.1f%%)", k, result.streamSize, prob)
            });
        }

        lblSampleStatus.setForeground(new Color(79, 70, 229)); // Indigo
        lblSampleStatus.setText(String.format(
                "Reservoir Sampling Complete: Drew k=%d items from stream of N=%d (Uniform Probability: %.1f%% per item).",
                result.sample.size(), result.streamSize,
                result.streamSize > 0 ? (double) k / result.streamSize * 100.0 : 0));

        lblSampleStats.setText(String.format(
                "Stream Size: %d | Reservoir k: %d | Items Sampled: %d | Exec Time: %,d ns (%.3f ms)",
                result.streamSize, k, result.sample.size(), elapsedNs, elapsedMs));
    }
}
