package ui;

import service.DataService;
import model.Event;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Event Panel providing complete event management listing with category filters and search.
 */
public class EventPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JComboBox<String> cbCategory;
    private JComboBox<String> cbStatus;

    public EventPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(UIUtils.COLOR_BG);
        setBorder(new EmptyBorder(20, 25, 20, 25));

        // 1. Header
        add(UIUtils.createHeaderPanel(
                "Event Directory",
                "Explore, manage, and schedule college platform events."
        ), BorderLayout.NORTH);

        // 2. Center Container
        JPanel centerCard = UIUtils.createCardPanel();
        centerCard.setLayout(new BorderLayout(12, 12));
        centerCard.setBorder(new EmptyBorder(15, 15, 15, 15));

        // 2A. Filter Bar
        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterBar.setOpaque(false);

        JLabel lblSearch = new JLabel("Search:");
        lblSearch.setFont(UIUtils.FONT_BOLD);
        txtSearch = new JTextField(15);
        txtSearch.setFont(UIUtils.FONT_REGULAR);

        JLabel lblCategory = new JLabel("Category:");
        lblCategory.setFont(UIUtils.FONT_BOLD);
        cbCategory = new JComboBox<>(new String[]{"All Categories", "Technology", "Academic", "Cultural", "Sports", "Workshop", "Gaming"});
        cbCategory.setFont(UIUtils.FONT_REGULAR);

        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setFont(UIUtils.FONT_BOLD);
        cbStatus = new JComboBox<>(new String[]{"All Statuses", "Active", "Upcoming", "Completed"});
        cbStatus.setFont(UIUtils.FONT_REGULAR);

        JButton btnFilter = UIUtils.createStyledButton("Apply Filter", UIUtils.COLOR_PRIMARY, Color.WHITE);
        btnFilter.addActionListener(e -> applyFilter());

        JButton btnReset = UIUtils.createStyledButton("Reset", new Color(226, 232, 240), UIUtils.COLOR_TEXT_DARK);
        btnReset.addActionListener(e -> {
            txtSearch.setText("");
            cbCategory.setSelectedIndex(0);
            cbStatus.setSelectedIndex(0);
            populateTable(DataService.getInstance().getEvents());
        });

        JButton btnAddEvent = UIUtils.createStyledButton("+ Add New Event", UIUtils.COLOR_EMERALD, Color.WHITE);
        btnAddEvent.addActionListener(e -> showAddEventDialog());

        filterBar.add(lblSearch);
        filterBar.add(txtSearch);
        filterBar.add(lblCategory);
        filterBar.add(cbCategory);
        filterBar.add(lblStatus);
        filterBar.add(cbStatus);
        filterBar.add(btnFilter);
        filterBar.add(btnReset);

        JPanel topToolbar = new JPanel(new BorderLayout());
        topToolbar.setOpaque(false);
        topToolbar.add(filterBar, BorderLayout.WEST);
        topToolbar.add(btnAddEvent, BorderLayout.EAST);

        centerCard.add(topToolbar, BorderLayout.NORTH);

        // 2B. JTable
        String[] columns = {"ID", "Title", "Category", "Date", "Time", "Venue", "Capacity", "Registered", "Status"};
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

        // Load initial data
        populateTable(DataService.getInstance().getEvents());
    }

    private void populateTable(List<Event> events) {
        tableModel.setRowCount(0);
        for (Event ev : events) {
            tableModel.addRow(new Object[]{
                    ev.getId(),
                    ev.getTitle(),
                    ev.getCategory(),
                    ev.getDate(),
                    ev.getTime(),
                    ev.getVenue(),
                    ev.getCapacity(),
                    ev.getRegisteredCount(),
                    ev.getStatus()
            });
        }
    }

    private void applyFilter() {
        String keyword = txtSearch.getText().trim().toLowerCase();
        String selectedCat = (String) cbCategory.getSelectedItem();
        String selectedStat = (String) cbStatus.getSelectedItem();

        List<Event> all = DataService.getInstance().getEvents();
        tableModel.setRowCount(0);

        for (Event ev : all) {
            boolean matchesKey = keyword.isEmpty() || ev.getTitle().toLowerCase().contains(keyword) || ev.getId().toLowerCase().contains(keyword);
            boolean matchesCat = "All Categories".equals(selectedCat) || ev.getCategory().equalsIgnoreCase(selectedCat);
            boolean matchesStat = "All Statuses".equals(selectedStat) || ev.getStatus().equalsIgnoreCase(selectedStat);

            if (matchesKey && matchesCat && matchesStat) {
                tableModel.addRow(new Object[]{
                        ev.getId(),
                        ev.getTitle(),
                        ev.getCategory(),
                        ev.getDate(),
                        ev.getTime(),
                        ev.getVenue(),
                        ev.getCapacity(),
                        ev.getRegisteredCount(),
                        ev.getStatus()
                });
            }
        }
    }

    private void showAddEventDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add New Event", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(420, 480);
        dialog.setLocationRelativeTo(this);

        JPanel form = new JPanel(new GridLayout(8, 2, 10, 10));
        form.setBorder(new EmptyBorder(20, 20, 20, 20));

        JTextField fId = new JTextField("EVT-" + (DataService.getInstance().getEvents().size() + 101));
        JTextField fTitle = new JTextField();
        JComboBox<String> fCategory = new JComboBox<>(new String[]{"Technology", "Academic", "Cultural", "Sports", "Workshop", "Gaming"});
        JTextField fDate = new JTextField("2026-11-20");
        JTextField fTime = new JTextField("10:00 AM");
        JTextField fVenue = new JTextField("Main Auditorium");
        JTextField fCap = new JTextField("200");

        form.add(new JLabel("Event ID:")); form.add(fId);
        form.add(new JLabel("Title:")); form.add(fTitle);
        form.add(new JLabel("Category:")); form.add(fCategory);
        form.add(new JLabel("Date (YYYY-MM-DD):")); form.add(fDate);
        form.add(new JLabel("Time:")); form.add(fTime);
        form.add(new JLabel("Venue:")); form.add(fVenue);
        form.add(new JLabel("Capacity:")); form.add(fCap);

        JButton btnSave = UIUtils.createStyledButton("Save Event", UIUtils.COLOR_PRIMARY, Color.WHITE);
        btnSave.addActionListener(e -> {
            if (fTitle.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please enter event title.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int cap = 100;
            try { cap = Integer.parseInt(fCap.getText().trim()); } catch (Exception ignored) {}

            Event newEv = new Event(
                    fId.getText().trim(),
                    fTitle.getText().trim(),
                    (String) fCategory.getSelectedItem(),
                    fDate.getText().trim(),
                    fTime.getText().trim(),
                    fVenue.getText().trim(),
                    cap,
                    0,
                    "Upcoming"
            );
            DataService.getInstance().getEvents().add(0, newEv);
            populateTable(DataService.getInstance().getEvents());
            dialog.dispose();
            JOptionPane.showMessageDialog(this, "New event added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        dialog.add(form, BorderLayout.CENTER);
        dialog.add(btnSave, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}
