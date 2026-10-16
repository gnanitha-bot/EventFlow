package ui;

import algorithms.m1.KMP;
import algorithms.m1.RabinKarp;
import model.Event;
import service.DataService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * M1 - Event Search UI Panel (Pattern Matching Engine).
 * Integrates KMP (Knuth-Morris-Pratt) and Rabin-Karp DSA algorithms.
 */
public class M1SearchPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private JTextField txtQuery;
    private JComboBox<String> cbAlgorithm;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel lblStatus;
    private JLabel lblStats;

    public M1SearchPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(UIUtils.COLOR_BG);
        setBorder(new EmptyBorder(20, 25, 20, 25));

        // 1. Header & Banner
        JPanel topContainer = new JPanel(new BorderLayout(0, 10));
        topContainer.setOpaque(false);
        topContainer.add(UIUtils.createHeaderPanel(
                "Module M1: Event Search & String Matching",
                "High-performance DSA pattern matching across event titles and descriptions using KMP and Rabin-Karp."
        ), BorderLayout.NORTH);

        topContainer.add(UIUtils.createModuleBanner(
                "M1",
                "String Pattern Matching Engine",
                "Executes O(N+M) KMP exact pattern matching and Rabin-Karp rolling-hash searches over DataService event records.",
                "Knuth-Morris-Pratt (KMP) / Rabin-Karp Rolling Hash"
        ), BorderLayout.SOUTH);

        add(topContainer, BorderLayout.NORTH);

        // 2. Interactive Control Card
        JPanel card = UIUtils.createCardPanel();
        card.setLayout(new BorderLayout(12, 12));
        card.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel searchControls = new JPanel(new BorderLayout(0, 10));
        searchControls.setOpaque(false);

        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 5));
        searchBar.setOpaque(false);

        JLabel lblQuery = new JLabel("Search Text:");
        lblQuery.setFont(UIUtils.FONT_BOLD);
        txtQuery = new JTextField(18);
        txtQuery.setFont(UIUtils.FONT_REGULAR);
        txtQuery.setText("TechFest");
        txtQuery.addActionListener(e -> executeSearch());

        JLabel lblAlgo = new JLabel("Algorithm:");
        lblAlgo.setFont(UIUtils.FONT_BOLD);
        cbAlgorithm = new JComboBox<>(new String[]{
                "Knuth-Morris-Pratt (KMP)",
                "Rabin-Karp Rolling Hash"
        });
        cbAlgorithm.setFont(UIUtils.FONT_REGULAR);

        JButton btnSearch = UIUtils.createStyledButton("Execute Search", UIUtils.COLOR_PRIMARY, Color.WHITE);
        btnSearch.addActionListener(e -> executeSearch());

        searchBar.add(lblQuery);
        searchBar.add(txtQuery);
        searchBar.add(lblAlgo);
        searchBar.add(cbAlgorithm);
        searchBar.add(btnSearch);

        // Status & Statistics Feedback Bar
        JPanel feedbackPanel = new JPanel(new GridLayout(2, 1, 4, 4));
        feedbackPanel.setOpaque(false);
        feedbackPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                new EmptyBorder(10, 12, 10, 12)
        ));
        feedbackPanel.setBackground(new Color(248, 250, 252));

        lblStatus = new JLabel("Enter a search term and select an algorithm to begin.");
        lblStatus.setFont(UIUtils.FONT_BOLD);
        lblStatus.setForeground(UIUtils.COLOR_TEXT_DARK);

        lblStats = new JLabel("Events Matched: 0 | Total Occurrences: 0 | Execution Time: 0 ns");
        lblStats.setFont(UIUtils.FONT_REGULAR);
        lblStats.setForeground(UIUtils.COLOR_TEXT_MUTED);

        feedbackPanel.add(lblStatus);
        feedbackPanel.add(lblStats);

        searchControls.add(searchBar, BorderLayout.NORTH);
        searchControls.add(feedbackPanel, BorderLayout.SOUTH);

        card.add(searchControls, BorderLayout.NORTH);

        // 3. Output Results JTable
        String[] cols = {"Event ID", "Title", "Category", "Venue", "Matched Field", "Match Positions", "Occurrences"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel);
        UIUtils.styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        card.add(scrollPane, BorderLayout.CENTER);

        add(card, BorderLayout.CENTER);

        // Execute initial search on startup
        executeSearch();
    }

    private void executeSearch() {
        String query = txtQuery.getText().trim();

        if (query.isEmpty()) {
            tableModel.setRowCount(0);
            lblStatus.setForeground(new Color(225, 29, 72)); // Warning Red
            lblStatus.setText("Empty Search Input: Please enter a search keyword or pattern.");
            lblStats.setText("Events Matched: 0 | Total Occurrences: 0 | Execution Time: 0 ns (0.000 ms)");
            return;
        }

        String selectedAlgo = (String) cbAlgorithm.getSelectedItem();
        boolean isKMP = selectedAlgo != null && selectedAlgo.contains("KMP");
        String algoName = isKMP ? "Knuth-Morris-Pratt (KMP)" : "Rabin-Karp Rolling Hash";

        String patternLower = query.toLowerCase();
        List<Event> allEvents = DataService.getInstance().getEvents();

        tableModel.setRowCount(0);
        int matchingEventsCount = 0;
        int totalOccurrences = 0;

        long startTime = System.nanoTime();

        for (Event ev : allEvents) {
            String titleLower = ev.getTitle().toLowerCase();
            String descLower = (ev.getDescription() != null ? ev.getDescription() : "").toLowerCase();

            List<Integer> titleMatches = isKMP ? KMP.search(titleLower, patternLower)
                                               : RabinKarp.search(titleLower, patternLower);
            List<Integer> descMatches = isKMP ? KMP.search(descLower, patternLower)
                                              : RabinKarp.search(descLower, patternLower);

            if (!titleMatches.isEmpty() || !descMatches.isEmpty()) {
                matchingEventsCount++;
                int occurrencesCount = titleMatches.size() + descMatches.size();
                totalOccurrences += occurrencesCount;

                String matchedField;
                if (!titleMatches.isEmpty() && !descMatches.isEmpty()) {
                    matchedField = "Title & Description";
                } else if (!titleMatches.isEmpty()) {
                    matchedField = "Title";
                } else {
                    matchedField = "Description";
                }

                StringBuilder posSb = new StringBuilder();
                if (!titleMatches.isEmpty()) {
                    posSb.append("Title: ").append(titleMatches);
                }
                if (!descMatches.isEmpty()) {
                    if (posSb.length() > 0) posSb.append(" | ");
                    posSb.append("Desc: ").append(descMatches);
                }

                tableModel.addRow(new Object[]{
                        ev.getId(),
                        ev.getTitle(),
                        ev.getCategory(),
                        ev.getVenue(),
                        matchedField,
                        posSb.toString(),
                        occurrencesCount
                });
            }
        }

        long endTime = System.nanoTime();
        long elapsedNs = endTime - startTime;
        double elapsedMs = elapsedNs / 1_000_000.0;

        if (matchingEventsCount == 0) {
            lblStatus.setForeground(new Color(217, 119, 6)); // Warning Amber
            lblStatus.setText("No Matches: No events found matching pattern \"" + query + "\" using " + algoName + ".");
            lblStats.setText("Events Matched: 0 | Total Occurrences: 0 | Execution Time: "
                    + String.format("%,d ns (%.3f ms)", elapsedNs, elapsedMs));
        } else {
            lblStatus.setForeground(new Color(16, 185, 129)); // Success Emerald
            lblStatus.setText("Search Successful: Found " + matchingEventsCount + " matching event(s) with "
                    + totalOccurrences + " pattern occurrence(s) using " + algoName + ".");
            lblStats.setText("Events Matched: " + matchingEventsCount + " | Total Occurrences: " + totalOccurrences
                    + " | Execution Time: " + String.format("%,d ns (%.3f ms)", elapsedNs, elapsedMs));
        }
    }
}
