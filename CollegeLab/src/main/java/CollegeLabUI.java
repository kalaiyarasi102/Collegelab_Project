
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalTime;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class CollegeLabUI extends JFrame {

    private JComboBox<String> department, labType, day, labName;

    private JTextField capacity, studentGroup;
    private JTextField subject, studentCount, staffName;
    private JTextField startTime, endTime;

    private DefaultTableModel tableModel;
    private JTable table;

    public CollegeLabUI() {
        setTitle("College Lab Allocation System");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel heading = new JLabel(
                "COLLEGE LAB ALLOCATION SYSTEM",
                SwingConstants.CENTER);

        heading.setFont(new Font("Arial", Font.BOLD, 23));
        heading.setOpaque(true);
        heading.setBackground(new Color(30, 60, 100));
        heading.setForeground(Color.WHITE);
        heading.setBorder(
                BorderFactory.createEmptyBorder(20, 10, 20, 10));

        add(heading, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(0, 4, 10, 10));
        form.setBorder(BorderFactory.createTitledBorder(
                "Lab Allocation Details"));

        department = new JComboBox<>(
                new String[]{"CSE", "IT", "ECE", "EEE", "MECH"});

        labType = new JComboBox<>(
                new String[]{"Computer", "Electronics",
                        "Electrical", "Mechanical", "Other"});

        day = new JComboBox<>(
                new String[]{"MONDAY", "TUESDAY", "WEDNESDAY",
                        "THURSDAY", "FRIDAY"});

        labName = new JComboBox<>();
        labName.setEditable(true);

        updateLabSuggestions();
        department.addActionListener(
                e -> updateLabSuggestions());

        capacity = new JTextField();
        studentGroup = new JTextField();
        subject = new JTextField();
        studentCount = new JTextField();
        staffName = new JTextField();
        startTime = new JTextField("09:00");
        endTime = new JTextField("10:00");

        addField(form, "Department", department);
        addField(form, "Laboratory Name", labName);
        addField(form, "Lab Type", labType);
        addField(form, "Lab Capacity", capacity);
        addField(form, "Student Group", studentGroup);
        addField(form, "Subject", subject);
        addField(form, "Student Count", studentCount);
        addField(form, "Staff Name", staffName);
        addField(form, "Day", day);
        addField(form, "Start Time (HH:mm)", startTime);
        addField(form, "End Time (HH:mm)", endTime);

        JButton save = new JButton("Add Allocation");
        JButton clear = new JButton("Clear Form");
        JButton delete = new JButton("Remove Selected");

        JPanel buttons = new JPanel();
        buttons.add(save);
        buttons.add(clear);
        buttons.add(delete);

        JPanel top = new JPanel(new BorderLayout(8, 8));
        top.add(form, BorderLayout.CENTER);
        top.add(buttons, BorderLayout.SOUTH);

        String[] columns = {
                "Department", "Lab Name", "Subject",
                "Group", "Day", "Start", "End", "Staff"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(24);

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setPreferredSize(new Dimension(900, 250));

        add(top, BorderLayout.CENTER);
        add(tableScroll, BorderLayout.SOUTH);

        save.addActionListener(e -> addAllocation());
        clear.addActionListener(e -> clearForm());

        delete.addActionListener(e -> {
            int row = table.getSelectedRow();

            if (row >= 0) {
                tableModel.removeRow(row);
            } else {
                JOptionPane.showMessageDialog(
                        this, "Select a row first.");
            }
        });
    }

    private void updateLabSuggestions() {
        String selectedDept =
                department.getSelectedItem().toString();

        String[] labs;

        switch (selectedDept) {
            case "CSE":
                labs = new String[]{
                    "OOPS Lab", "DBMS Lab",
                    "Programming Lab", "Computer Networks Lab",
                    "AI Lab"
                };
                break;

            case "IT":
                labs = new String[]{
                    "Programming Lab", "DBMS Lab",
                    "AI Lab", "Computer Networks Lab"
                };
                break;

            case "ECE":
                labs = new String[]{
                    "Electronics Lab", "Digital Electronics Lab",
                    "Communication Lab"
                };
                break;

            case "EEE":
                labs = new String[]{
                    "Electrical Lab", "Power Electronics Lab",
                    "Electrical Machines Lab"
                };
                break;

            case "MECH":
                labs = new String[]{
                    "Mechanical Lab", "CAD Lab",
                    "Thermal Engineering Lab"
                };
                break;

            default:
                labs = new String[]{};
        }

        labName.removeAllItems();

        for (String lab : labs) {
            labName.addItem(lab);
        }

        if (labName.getItemCount() > 0) {
            labName.setSelectedIndex(0);
        }
    }

    private void addField(
            JPanel panel, String label, Component field) {
        panel.add(new JLabel(label));
        panel.add(field);
    }

    private void addAllocation() {
        try {
            Object selectedLab = labName.getEditor().getItem();

            String lab = selectedLab == null
                    ? ""
                    : selectedLab.toString().trim();

            String capText = capacity.getText().trim();
            String group = studentGroup.getText().trim();
            String sub = subject.getText().trim();
            String countText = studentCount.getText().trim();
            String staff = staffName.getText().trim();
            String start = startTime.getText().trim();
            String end = endTime.getText().trim();

            if (lab.isEmpty() || capText.isEmpty()
                    || group.isEmpty() || sub.isEmpty()
                    || countText.isEmpty() || staff.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this, "Please fill in all fields.");
                return;
            }

            int cap = Integer.parseInt(capText);
            int count = Integer.parseInt(countText);

            LocalTime startValue = LocalTime.parse(start);
            LocalTime endValue = LocalTime.parse(end);

            if (cap <= 0 || count <= 0 || count > cap) {
                JOptionPane.showMessageDialog(
                        this, "Check lab capacity and student count.");
                return;
            }

            if (!endValue.isAfter(startValue)) {
                JOptionPane.showMessageDialog(
                        this, "End time must be after start time.");
                return;
            }

            tableModel.addRow(new Object[]{
                    department.getSelectedItem(),
                    lab,
                    sub,
                    group,
                    day.getSelectedItem(),
                    start,
                    end,
                    staff
            });

            JOptionPane.showMessageDialog(
                    this, "Allocation added to the UI table.");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this, "Capacity and student count must be numbers.");
        } catch (java.time.format.DateTimeParseException ex) {
            JOptionPane.showMessageDialog(
                    this, "Enter time as HH:mm, e.g. 09:30.");
        }
    }

    private void clearForm() {
        department.setSelectedIndex(0);
        labType.setSelectedIndex(0);
        day.setSelectedIndex(0);

        if (labName.getItemCount() > 0) {
            labName.setSelectedIndex(0);
        }

        capacity.setText("");
        studentGroup.setText("");
        subject.setText("");
        studentCount.setText("");
        staffName.setText("");
        startTime.setText("09:00");
        endTime.setText("10:00");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new CollegeLabUI().setVisible(true));
    }
}
