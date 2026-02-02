/*
PROGRAMMER IDENTIFIER:
Full Name: RICH ANDREI CANTAROS
Student ID: 22-1867-386
*/

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class StudentRecordApp extends JFrame {

    private DefaultTableModel model;
    private JTable table;

    private JTextField idField;
    private JTextField nameField;
    private JTextField gradeField;

    public StudentRecordApp() {
        // REQUIRED: identifier in JFrame title
        this.setTitle("Records - YOUR NAME HERE YOUR STUDENT ID HERE");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 450);
        setLocationRelativeTo(null);

        initUI();
        loadCsvIntoTable("class_records.csv"); // expects file in same folder when run
    }

    private void initUI() {
        model = new DefaultTableModel(new Object[]{"ID", "Name", "Grade"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // keeps it simple; CRUD is via Add/Delete buttons
            }
        };

        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);

        // Inputs
        idField = new JTextField(10);
        nameField = new JTextField(18);
        gradeField = new JTextField(8);

        JButton addBtn = new JButton("Add");
        JButton deleteBtn = new JButton("Delete");

        addBtn.addActionListener(e -> addRecord());
        deleteBtn.addActionListener(e -> deleteSelectedRecord());

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0; c.gridy = 0;
        form.add(new JLabel("ID:"), c);
        c.gridx = 1;
        form.add(idField, c);

        c.gridx = 0; c.gridy = 1;
        form.add(new JLabel("Name:"), c);
        c.gridx = 1;
        form.add(nameField, c);

        c.gridx = 0; c.gridy = 2;
        form.add(new JLabel("Grade:"), c);
        c.gridx = 1;
        form.add(gradeField, c);

        c.gridx = 0; c.gridy = 3;
        form.add(addBtn, c);
        c.gridx = 1;
        form.add(deleteBtn, c);

        JPanel root = new JPanel(new BorderLayout());
        root.add(scrollPane, BorderLayout.CENTER);
        root.add(form, BorderLayout.EAST);

        setContentPane(root);
    }

    private void loadCsvIntoTable(String filePath) {
        // REQUIRED: try-catch for file-reading errors
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean maybeHeader = true;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // split CSV line (simple split per assignment)
                String[] parts = line.split(",");

                // If first row looks like a header, skip it
                if (maybeHeader) {
                    maybeHeader = false;
                    if (parts.length >= 3) {
                        String a = parts[0].trim().toLowerCase();
                        String b = parts[1].trim().toLowerCase();
                        String c = parts[2].trim().toLowerCase();
                        if (a.contains("id") && b.contains("name") && c.contains("grade")) {
                            continue;
                        }
                    }
                }

                if (parts.length < 3) continue; // ignore malformed rows

                String id = parts[0].trim();
                String name = parts[1].trim();
                String grade = parts[2].trim();

                model.addRow(new Object[]{id, name, grade});
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error reading CSV file: " + ex.getMessage(),
                    "File Read Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void addRecord() {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String grade = gradeField.getText().trim();

        if (id.isEmpty() || name.isEmpty() || grade.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in ID, Name, and Grade.");
            return;
        }

        model.addRow(new Object[]{id, name, grade});

        idField.setText("");
        nameField.setText("");
        gradeField.setText("");
        idField.requestFocus();
    }

    private void deleteSelectedRecord() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to delete.");
            return;
        }
        model.removeRow(row);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentRecordApp().setVisible(true));
    }
}