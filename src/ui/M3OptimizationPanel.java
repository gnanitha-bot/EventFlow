package ui;

import algorithms.m3.BitmaskDP;
import algorithms.m3.BitmaskDP.OptimizationResult;
import algorithms.m3.LevenshteinDistance;
import algorithms.m3.LevenshteinDistance.LevenshteinResult;
import model.Activity;
import service.DataService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * M3 - Activity Optimization & Text Correction UI Panel.
 * SECTION 1: Activity Optimization using Bitmask Dynamic Programming.
 * SECTION 2: Name/Text Correction using Levenshtein Edit-Distance DP.
 */
public class M3OptimizationPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    // Section 1: Bitmask DP Controls & Views
    private JTextField txtMaxTime;
    private JTable tableOpt;
    private DefaultTableModel modelOpt;
    private JLabel lblOptStatus;
    private JLabel lblOptStats;

    // Section 2: Levenshtein Controls & Views
    private JTextField txtRefText;
    private JTextField txtCandidateText;
    private JLabel lblLevStatus;
    private JLabel lblLevDetails;
    private JLabel lblLevExecTime;

    public M3OptimizationPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(UIUtils.COLOR_BG);
        setBorder(new EmptyBorder(20, 25, 20, 25));

        // 1. Header & Banner
        JPanel topContainer = new JPanel(new BorderLayout(0, 10));
        topContainer.setOpaque(false);
        topContainer.add(UIUtils.createHeaderPanel(
                "Module M3: Activity Optimization & Text Correction",
                "Applies Bitmask DP for optimal activity subset selection and Levenshtein Distance for name spelling correction."
        ), BorderLayout.NORTH);

        topContainer.add(UIUtils.createModuleBanner(
                "M3",
                "Bitmask DP & Levenshtein Distance Engine",
                "SECTION 1: Bitmask DP activity selection under time constraints. SECTION 2: Levenshtein text edit-distance matching.",
                "Bitmask Dynamic Programming / Levenshtein Edit Distance"
        ), BorderLayout.SOUTH);

        add(topContainer, BorderLayout.NORTH);

        // Main Center Panel holding Section 1 and Section 2
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 0, 15));
        centerPanel.setOpaque(false);

        // =========================================================================
        // SECTION 1: Activity Optimization (Bitmask DP)
        // =========================================================================
        JPanel cardOpt = UIUtils.createCardPanel();
        cardOpt.setLayout(new BorderLayout(10, 10));
        cardOpt.setBorder(new EmptyBorder(12, 15, 12, 15));

        // Title & Toolbar: Heading on Row 1, Controls on Row 2
        JPanel optHeader = new JPanel();
        optHeader.setLayout(new BoxLayout(optHeader, BoxLayout.Y_AXIS));
        optHeader.setOpaque(false);

        JLabel lblOptTitle = new JLabel("SECTION 1: Activity Schedule Optimization (Bitmask DP)");
        lblOptTitle.setFont(UIUtils.FONT_SECTION);
        lblOptTitle.setForeground(UIUtils.COLOR_PRIMARY);
        lblOptTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel optControls = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        optControls.setOpaque(false);
        optControls.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblMaxTime = new JLabel("Max Available Time (mins):");
        lblMaxTime.setFont(UIUtils.FONT_BOLD);

        txtMaxTime = new JTextField("180", 6);
        txtMaxTime.setFont(UIUtils.FONT_REGULAR);
        txtMaxTime.setPreferredSize(new Dimension(75, 34));
        txtMaxTime.addActionListener(e -> runBitmaskOptimization());

        JButton btnOptimize = UIUtils.createStyledButton("Optimize Schedule (Bitmask DP)", UIUtils.COLOR_EMERALD, Color.WHITE);
        btnOptimize.addActionListener(e -> runBitmaskOptimization());

        optControls.add(lblMaxTime);
        optControls.add(txtMaxTime);
        optControls.add(Box.createRigidArea(new Dimension(6, 0)));
        optControls.add(btnOptimize);

        optHeader.add(lblOptTitle);
        optHeader.add(Box.createRigidArea(new Dimension(0, 8)));
        optHeader.add(optControls);

        cardOpt.add(optHeader, BorderLayout.NORTH);

        // Table for Activities
        String[] colsOpt = {"Activity ID", "Session Title", "Duration (mins)", "Priority Value", "Speaker", "Room", "Bitmask DP Status"};
        modelOpt = new DefaultTableModel(colsOpt, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tableOpt = new JTable(modelOpt);
        UIUtils.styleTable(tableOpt);
        tableOpt.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        int[] optColWidths = {95, 230, 110, 100, 150, 110, 200};
        int optTotalWidth = 0;
        for (int i = 0; i < optColWidths.length; i++) {
            tableOpt.getColumnModel().getColumn(i).setPreferredWidth(optColWidths[i]);
            optTotalWidth += optColWidths[i];
        }
        final int minOptWidth = optTotalWidth;

        JScrollPane spOpt = new JScrollPane(tableOpt);
        spOpt.setBorder(BorderFactory.createLineBorder(UIUtils.COLOR_BORDER));
        spOpt.getViewport().setBackground(Color.WHITE);
        spOpt.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        spOpt.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        spOpt.getViewport().addChangeListener(e -> {
            int vpWidth = spOpt.getViewport().getWidth();
            if (vpWidth > minOptWidth) {
                tableOpt.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
            } else {
                tableOpt.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            }
        });

        cardOpt.add(spOpt, BorderLayout.CENTER);

        // Section 1 Status Footer
        JPanel optFooter = new JPanel(new GridLayout(2, 1, 4, 4));
        optFooter.setOpaque(false);
        optFooter.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                new EmptyBorder(8, 10, 8, 10)
        ));
        optFooter.setBackground(new Color(248, 250, 252));

        lblOptStatus = new JLabel("Status: Ready to run Bitmask DP optimization.");
        lblOptStatus.setFont(UIUtils.FONT_BOLD);
        lblOptStatus.setForeground(UIUtils.COLOR_TEXT_DARK);

        lblOptStats = new JLabel("Selected Activities: 0 | Total Duration: 0 mins | Total Priority: 0 pts | Exec Time: 0 ns");
        lblOptStats.setFont(UIUtils.FONT_REGULAR);
        lblOptStats.setForeground(UIUtils.COLOR_TEXT_MUTED);

        optFooter.add(lblOptStatus);
        optFooter.add(lblOptStats);

        cardOpt.add(optFooter, BorderLayout.SOUTH);

        centerPanel.add(cardOpt);

        // =========================================================================
        // SECTION 2: Name/Text Correction (Levenshtein Distance)
        // =========================================================================
        JPanel cardLev = UIUtils.createCardPanel();
        cardLev.setLayout(new BorderLayout(10, 10));
        cardLev.setBorder(new EmptyBorder(12, 15, 12, 15));

        // Title
        JLabel lblLevTitle = new JLabel("SECTION 2: Name / Text Correction (Levenshtein Distance DP)");
        lblLevTitle.setFont(UIUtils.FONT_SECTION);
        lblLevTitle.setForeground(UIUtils.COLOR_PURPLE);

        cardLev.add(lblLevTitle, BorderLayout.NORTH);

        // Input & Controls Grid
        JPanel levForm = new JPanel(new GridBagLayout());
        levForm.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblRef = new JLabel("Original / Reference Text:");
        lblRef.setFont(UIUtils.FONT_BOLD);
        txtRefText = new JTextField("seminar", 15);
        txtRefText.setFont(UIUtils.FONT_REGULAR);
        txtRefText.addActionListener(e -> runLevenshteinCalculation());

        JLabel lblCand = new JLabel("Misspelled / Candidate Text:");
        lblCand.setFont(UIUtils.FONT_BOLD);
        txtCandidateText = new JTextField("semnar", 15);
        txtCandidateText.setFont(UIUtils.FONT_REGULAR);
        txtCandidateText.addActionListener(e -> runLevenshteinCalculation());

        JButton btnCompare = UIUtils.createStyledButton("Calculate Edit Distance", UIUtils.COLOR_PURPLE, Color.WHITE);
        btnCompare.addActionListener(e -> runLevenshteinCalculation());

        // Presets combo box for quick testing
        JLabel lblPreset = new JLabel("Quick Preset Test:");
        lblPreset.setFont(UIUtils.FONT_BOLD);
        JComboBox<String> cbPreset = new JComboBox<>(new String[]{
                "Custom Input",
                "seminar vs semnar",
                "event vs evnt",
                "Hackathon vs Hackaton",
                "Turing vs Tring",
                "Conference vs Conferance"
        });
        cbPreset.setFont(UIUtils.FONT_REGULAR);
        cbPreset.addActionListener(e -> {
            String sel = (String) cbPreset.getSelectedItem();
            if (sel != null && sel.contains(" vs ")) {
                String[] parts = sel.split(" vs ");
                txtRefText.setText(parts[0]);
                txtCandidateText.setText(parts[1]);
                runLevenshteinCalculation();
            }
        });

        gbc.gridx = 0; gbc.gridy = 0; levForm.add(lblRef, gbc);
        gbc.gridx = 1; gbc.gridy = 0; levForm.add(txtRefText, gbc);
        gbc.gridx = 2; gbc.gridy = 0; levForm.add(lblCand, gbc);
        gbc.gridx = 3; gbc.gridy = 0; levForm.add(txtCandidateText, gbc);

        gbc.gridx = 0; gbc.gridy = 1; levForm.add(lblPreset, gbc);
        gbc.gridx = 1; gbc.gridy = 1; levForm.add(cbPreset, gbc);
        gbc.gridx = 2; gbc.gridy = 1; gbc.gridwidth = 2; levForm.add(btnCompare, gbc);

        cardLev.add(levForm, BorderLayout.CENTER);

        // Section 2 Results Output Box
        JPanel levResultBox = new JPanel(new GridLayout(3, 1, 4, 4));
        levResultBox.setOpaque(false);
        levResultBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                new EmptyBorder(8, 10, 8, 10)
        ));
        levResultBox.setBackground(new Color(248, 250, 252));

        lblLevStatus = new JLabel("Edit Distance Result: -");
        lblLevStatus.setFont(UIUtils.FONT_BOLD);
        lblLevStatus.setForeground(UIUtils.COLOR_PURPLE);

        lblLevDetails = new JLabel("Operations Breakdown: Insertions = 0 | Deletions = 0 | Substitutions = 0 | Similarity = 0.0%");
        lblLevDetails.setFont(UIUtils.FONT_REGULAR);
        lblLevDetails.setForeground(UIUtils.COLOR_TEXT_DARK);

        lblLevExecTime = new JLabel("Execution Time: 0 ns (0.000 ms)");
        lblLevExecTime.setFont(UIUtils.FONT_REGULAR);
        lblLevExecTime.setForeground(UIUtils.COLOR_TEXT_MUTED);

        levResultBox.add(lblLevStatus);
        levResultBox.add(lblLevDetails);
        levResultBox.add(lblLevExecTime);

        cardLev.add(levResultBox, BorderLayout.SOUTH);

        centerPanel.add(cardLev);

        add(centerPanel, BorderLayout.CENTER);

        // Run initial simulations on panel load
        runBitmaskOptimization();
        runLevenshteinCalculation();
    }

    private void runBitmaskOptimization() {
        String maxTimeStr = txtMaxTime.getText().trim();
        int maxTime;
        try {
            maxTime = Integer.parseInt(maxTimeStr);
            if (maxTime <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            lblOptStatus.setForeground(new Color(225, 29, 72)); // Rose Red
            lblOptStatus.setText("Input Error: Please enter a valid positive integer for Maximum Available Time.");
            lblOptStats.setText("Selected Activities: 0 | Total Duration: 0 mins | Total Priority: 0 pts | Exec Time: 0 ns");
            modelOpt.setRowCount(0);
            return;
        }

        List<Activity> allActivities = DataService.getInstance().getActivities();

        long startTime = System.nanoTime();

        OptimizationResult result = BitmaskDP.optimize(allActivities, maxTime);

        long endTime = System.nanoTime();
        long elapsedNs = endTime - startTime;
        double elapsedMs = elapsedNs / 1_000_000.0;

        modelOpt.setRowCount(0);
        for (Activity act : allActivities) {
            boolean isSelected = result.selectedActivities.contains(act);
            modelOpt.addRow(new Object[]{
                    act.getId(),
                    act.getTitle(),
                    act.getDurationMinutes() + " mins",
                    act.getValue() + " pts",
                    act.getSpeaker(),
                    act.getRoom(),
                    isSelected ? "SELECTED (Bitmask DP Optimal)" : "EXCLUDED (Time Limit Exceeded)"
            });
        }

        lblOptStatus.setForeground(new Color(16, 185, 129)); // Emerald Green
        lblOptStatus.setText("Bitmask DP Optimization Complete: Selected " + result.selectedActivities.size()
                + " activities out of " + allActivities.size() + " evaluated across " + result.totalStatesEvaluated + " DP subset states.");

        lblOptStats.setText(String.format("Selected Activities: %d | Total Duration: %d / %d mins | Total Priority: %d pts | Exec Time: %,d ns (%.3f ms)",
                result.selectedActivities.size(), result.totalDuration, maxTime, result.totalValue, elapsedNs, elapsedMs));
    }

    private void runLevenshteinCalculation() {
        String str1 = txtRefText.getText().trim();
        String str2 = txtCandidateText.getText().trim();

        if (str1.isEmpty() && str2.isEmpty()) {
            lblLevStatus.setForeground(new Color(225, 29, 72));
            lblLevStatus.setText("Input Error: Please enter text in both input fields to compute Levenshtein distance.");
            lblLevDetails.setText("Operations Breakdown: N/A");
            lblLevExecTime.setText("Execution Time: 0 ns (0.000 ms)");
            return;
        }

        long startTime = System.nanoTime();

        LevenshteinResult result = LevenshteinDistance.compute(str1, str2);

        long endTime = System.nanoTime();
        long elapsedNs = endTime - startTime;
        double elapsedMs = elapsedNs / 1_000_000.0;

        lblLevStatus.setForeground(new Color(124, 58, 237)); // Purple
        lblLevStatus.setText(String.format("Levenshtein Distance: %d edit operation(s) required (Similarity: %.1f%%)",
                result.distance, result.similarityPercentage));

        lblLevDetails.setText(String.format("Operations Breakdown: %d Insertion(s) | %d Deletion(s) | %d Substitution(s)",
                result.insertions, result.deletions, result.substitutions));

        lblLevExecTime.setText(String.format("Execution Time: %,d ns (%.3f ms)", elapsedNs, elapsedMs));
    }
}
