package ui;

import algorithms.m2.LCP;
import algorithms.m2.LCP.CommonSubstringResult;
import model.Event;
import service.DataService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * M2 - Document Similarity UI Panel.
 * Uses Suffix Array Construction and Kasai's LCP Algorithm to identify common substrings across event proposals.
 */
public class M2DocumentPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private JComboBox<String> cbEventA;
    private JComboBox<String> cbEventB;
    private JTextArea txtDocA;
    private JTextArea txtDocB;
    private JLabel lblStatusResult;
    private JLabel lblLcsText;
    private JLabel lblLcsLength;
    private JLabel lblLcpStats;
    private JLabel lblExecTime;

    public M2DocumentPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(UIUtils.COLOR_BG);
        setBorder(new EmptyBorder(20, 25, 20, 25));

        // 1. Header & Banner
        JPanel topContainer = new JPanel(new BorderLayout(0, 10));
        topContainer.setOpaque(false);
        topContainer.add(UIUtils.createHeaderPanel(
                "Module M2: Document Substring Analysis & Plagiarism Detection",
                "Identifies common substrings and shared text structures using Suffix Arrays and Kasai's LCP Algorithm."
        ), BorderLayout.NORTH);

        topContainer.add(UIUtils.createModuleBanner(
                "M2",
                "Suffix Array & Kasai LCP Substring Engine",
                "Constructs prefix-doubled Suffix Arrays and linear-time Kasai LCP arrays over event descriptions.",
                "Suffix Array / Kasai's LCP Algorithm"
        ), BorderLayout.SOUTH);

        add(topContainer, BorderLayout.NORTH);

        // 2. Interactive Card
        JPanel card = UIUtils.createCardPanel();
        card.setLayout(new BorderLayout(12, 12));
        card.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Controls Bar
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 5));
        controls.setOpaque(false);

        JLabel lblEventA = new JLabel("Document 1:");
        lblEventA.setFont(UIUtils.FONT_BOLD);

        JLabel lblEventB = new JLabel("Document 2:");
        lblEventB.setFont(UIUtils.FONT_BOLD);

        List<Event> events = DataService.getInstance().getEvents();
        String[] eventItems = new String[events.size()];
        for (int i = 0; i < events.size(); i++) {
            Event ev = events.get(i);
            eventItems[i] = ev.getId() + ": " + ev.getTitle();
        }

        cbEventA = new JComboBox<>(eventItems);
        cbEventA.setFont(UIUtils.FONT_REGULAR);
        if (events.size() > 0) cbEventA.setSelectedIndex(0);

        cbEventB = new JComboBox<>(eventItems);
        cbEventB.setFont(UIUtils.FONT_REGULAR);
        if (events.size() > 1) cbEventB.setSelectedIndex(1);

        cbEventA.addActionListener(e -> updateDocAText());
        cbEventB.addActionListener(e -> updateDocBText());

        JButton btnAnalyze = UIUtils.createStyledButton("Analyze Substrings (SA + LCP)", UIUtils.COLOR_PURPLE, Color.WHITE);
        btnAnalyze.addActionListener(e -> runSubstringAnalysis());

        controls.add(lblEventA);
        controls.add(cbEventA);
        controls.add(lblEventB);
        controls.add(cbEventB);
        controls.add(btnAnalyze);

        card.add(controls, BorderLayout.NORTH);

        // Text comparison split view
        JPanel textSplit = new JPanel(new GridLayout(1, 2, 15, 0));
        textSplit.setOpaque(false);

        txtDocA = new JTextArea();
        txtDocA.setFont(UIUtils.FONT_REGULAR);
        txtDocA.setLineWrap(true);
        txtDocA.setWrapStyleWord(true);
        JScrollPane spA = new JScrollPane(txtDocA);
        spA.setBorder(BorderFactory.createTitledBorder("Event Description / Proposal A"));

        txtDocB = new JTextArea();
        txtDocB.setFont(UIUtils.FONT_REGULAR);
        txtDocB.setLineWrap(true);
        txtDocB.setWrapStyleWord(true);
        JScrollPane spB = new JScrollPane(txtDocB);
        spB.setBorder(BorderFactory.createTitledBorder("Event Description / Proposal B"));

        textSplit.add(spA);
        textSplit.add(spB);

        card.add(textSplit, BorderLayout.CENTER);

        // Result Card at Bottom
        JPanel resultPanel = new JPanel(new BorderLayout(8, 8));
        resultPanel.setBackground(new Color(248, 250, 252));
        resultPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIUtils.COLOR_BORDER),
                new EmptyBorder(12, 15, 12, 15)
        ));

        lblStatusResult = new JLabel("Click 'Analyze Substrings' to compute Suffix Array and LCP metrics.");
        lblStatusResult.setFont(UIUtils.FONT_BOLD);
        lblStatusResult.setForeground(UIUtils.COLOR_PURPLE);

        JPanel detailsGrid = new JPanel(new GridLayout(2, 2, 10, 6));
        detailsGrid.setOpaque(false);

        lblLcsText = new JLabel("Most Significant Substring: -");
        lblLcsText.setFont(UIUtils.FONT_REGULAR);

        lblLcsLength = new JLabel("Substring Length: 0 chars");
        lblLcsLength.setFont(UIUtils.FONT_REGULAR);

        lblLcpStats = new JLabel("LCP Info: Max LCP = 0 | Avg LCP = 0.00 | Common Pairs = 0");
        lblLcpStats.setFont(UIUtils.FONT_REGULAR);

        lblExecTime = new JLabel("Execution Time: 0 ns (0.000 ms)");
        lblExecTime.setFont(UIUtils.FONT_REGULAR);

        detailsGrid.add(lblLcsText);
        detailsGrid.add(lblLcsLength);
        detailsGrid.add(lblLcpStats);
        detailsGrid.add(lblExecTime);

        resultPanel.add(lblStatusResult, BorderLayout.NORTH);
        resultPanel.add(detailsGrid, BorderLayout.SOUTH);

        card.add(resultPanel, BorderLayout.SOUTH);

        add(card, BorderLayout.CENTER);

        // Populate initial text fields from combobox selections
        updateDocAText();
        updateDocBText();

        // Run initial analysis
        runSubstringAnalysis();
    }

    private void updateDocAText() {
        int idx = cbEventA.getSelectedIndex();
        List<Event> events = DataService.getInstance().getEvents();
        if (idx >= 0 && idx < events.size()) {
            txtDocA.setText(events.get(idx).getDescription());
        }
    }

    private void updateDocBText() {
        int idx = cbEventB.getSelectedIndex();
        List<Event> events = DataService.getInstance().getEvents();
        if (idx >= 0 && idx < events.size()) {
            txtDocB.setText(events.get(idx).getDescription());
        }
    }

    private void runSubstringAnalysis() {
        String docA = txtDocA.getText().trim();
        String docB = txtDocB.getText().trim();

        if (docA.isEmpty() || docB.isEmpty()) {
            lblStatusResult.setForeground(new Color(225, 29, 72)); // Rose Warning
            lblStatusResult.setText("Empty Input Error: Please select or enter non-empty text for both Document A and Document B.");
            lblLcsText.setText("Most Significant Substring: N/A");
            lblLcsLength.setText("Substring Length: 0 chars");
            lblLcpStats.setText("LCP Info: N/A");
            lblExecTime.setText("Execution Time: 0 ns (0.000 ms)");
            return;
        }

        long startTime = System.nanoTime();

        CommonSubstringResult result = LCP.findLongestCommonSubstring(docA, docB);

        long endTime = System.nanoTime();
        long elapsedNs = endTime - startTime;
        double elapsedMs = elapsedNs / 1_000_000.0;

        if (result.length == 0) {
            lblStatusResult.setForeground(new Color(217, 119, 6)); // Amber
            lblStatusResult.setText("No Matches Found: No common substrings were found between Document A and Document B.");
            lblLcsText.setText("Most Significant Substring: None");
            lblLcsLength.setText("Substring Length: 0 chars");
            lblLcpStats.setText("LCP Info: Max LCP = 0 | Avg LCP = 0.00 | Common Pairs = 0");
        } else {
            lblStatusResult.setForeground(new Color(16, 185, 129)); // Emerald Success
            lblStatusResult.setText("Analysis Successful: Identified Longest Common Substring using Suffix Array & Kasai's LCP.");
            lblLcsText.setText("Most Significant Substring: \"" + result.longestCommonSubstring + "\"");
            lblLcsLength.setText("Substring Length: " + result.length + " characters");
            lblLcpStats.setText(String.format("LCP Info: Max LCP = %d | Avg LCP = %.2f | Common Pairs = %d",
                    result.maxLcpValue, result.averageLcp, result.totalLcpMatches));
        }

        lblExecTime.setText(String.format("Execution Time: %,d ns (%.3f ms)", elapsedNs, elapsedMs));
    }
}
