package ui;

import service.DataService;
import model.Participant;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Participant Panel providing student registration listings and attendance tracking.
 */
public class ParticipantPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;

    public ParticipantPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(UIUtils.COLOR_BG);
        setBorder(new EmptyBorder(20, 25, 20, 25));

        // 1. Header
        add(UIUtils.createHeaderPanel(
                "Participant Management",
                "View and filter registered students across all college events."
        ), BorderLayout.NORTH);

        // 2. Card Layout Container
        JPanel centerCard = UIUtils.createCardPanel();
        centerCard.setLayout(new BorderLayout(12, 12));
        centerCard.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        toolbar.setOpaque(false);

        JLabel lblSearch = new JLabel("Search Student:");
        lblSearch.setFont(UIUtils.FONT_BOLD);
        txtSearch = new JTextField(15);

        JButton btnSearch = UIUtils.createStyledButton("Search", UIUtils.COLOR_PRIMARY, Color.WHITE);
        btnSearch.addActionListener(e -> filterTable());

        JButton btnReset = UIUtils.createStyledButton("Show All", new Color(226, 232, 240), UIUtils.COLOR_TEXT_DARK);
        btnReset.addActionListener(e -> {
            txtSearch.setText("");
            populateTable(DataService.getInstance().getParticipants());
        });

        toolbar.add(lblSearch);
        toolbar.add(txtSearch);
        toolbar.add(btnSearch);
        toolbar.add(btnReset);

        centerCard.add(toolbar, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Name", "Email", "Department", "Registered Event", "Reg. Date", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        UIUtils.styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        centerCard.add(scrollPane, BorderLayout.CENTER);

        add(centerCard, BorderLayout.CENTER);

        populateTable(DataService.getInstance().getParticipants());
    }

    private void populateTable(List<Participant> list) {
        tableModel.setRowCount(0);
        for (Participant p : list) {
            tableModel.addRow(new Object[]{
                    p.getId(),
                    p.getName(),
                    p.getEmail(),
                    p.getDepartment(),
                    p.getRegisteredEvent(),
                    p.getRegistrationDate(),
                    p.getStatus()
            });
        }
    }

    private void filterTable() {
        String key = txtSearch.getText().trim().toLowerCase();
        if (key.isEmpty()) {
            populateTable(DataService.getInstance().getParticipants());
            return;
        }
        tableModel.setRowCount(0);
        for (Participant p : DataService.getInstance().getParticipants()) {
            if (p.getName().toLowerCase().contains(key) ||
                p.getId().toLowerCase().contains(key) ||
                p.getDepartment().toLowerCase().contains(key) ||
                p.getRegisteredEvent().toLowerCase().contains(key)) {
                tableModel.addRow(new Object[]{
                        p.getId(), p.getName(), p.getEmail(), p.getDepartment(),
                        p.getRegisteredEvent(), p.getRegistrationDate(), p.getStatus()
                });
            }
        }
    }
}
