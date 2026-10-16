package ui;

import service.DataService;
import model.Venue;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Venue Panel managing campus auditoriums, halls, and lab availability.
 */
public class VenuePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private JTable table;
    private DefaultTableModel tableModel;

    public VenuePanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(UIUtils.COLOR_BG);
        setBorder(new EmptyBorder(20, 25, 20, 25));

        // Header
        add(UIUtils.createHeaderPanel(
                "Campus Venue Directory",
                "Monitor auditorium, lab, and outdoor space availability and seating capacities."
        ), BorderLayout.NORTH);

        // Center Card
        JPanel centerCard = UIUtils.createCardPanel();
        centerCard.setLayout(new BorderLayout(12, 12));
        centerCard.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        toolbar.setOpaque(false);

        JButton btnAddVenue = UIUtils.createStyledButton("+ Add New Venue", UIUtils.COLOR_PRIMARY, Color.WHITE);
        btnAddVenue.addActionListener(e -> JOptionPane.showMessageDialog(this, "Venue registration modal placeholder.", "Add Venue", JOptionPane.INFORMATION_MESSAGE));

        toolbar.add(btnAddVenue);
        centerCard.add(toolbar, BorderLayout.NORTH);

        // JTable
        String[] columns = {"Venue ID", "Name", "Location", "Capacity", "Facilities", "Availability"};
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

        populateTable();
    }

    private void populateTable() {
        tableModel.setRowCount(0);
        for (Venue v : DataService.getInstance().getVenues()) {
            tableModel.addRow(new Object[]{
                    v.getId(),
                    v.getName(),
                    v.getLocation(),
                    v.getCapacity() + " seats",
                    v.getFacilities(),
                    v.isAvailable() ? "Available" : "Booked"
            });
        }
    }
}
