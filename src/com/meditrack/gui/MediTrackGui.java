package com.meditrack.gui;

import com.meditrack.model.Appointment;
import com.meditrack.model.Bill;
import com.meditrack.model.Medicine;
import com.meditrack.model.Patient;
import com.meditrack.model.Prescription;
import com.meditrack.util.FileStore;
import com.meditrack.util.IdGenerator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.RenderingHints;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MediTrackGui extends JFrame {
    private static final String PATIENTS_FILE = "patients.csv";
    private static final String MEDICINES_FILE = "medicines.csv";
    private static final String APPOINTMENTS_FILE = "appointments.csv";
    private static final String BILLS_FILE = "bills.csv";
    private static final String PRESCRIPTIONS_FILE = "prescriptions.csv";
    private static final Color NAVY = new Color(15, 35, 65);
    private static final Color BLUE = new Color(34, 102, 246);
    private static final Color CYAN = new Color(24, 190, 207);
    private static final Color SURFACE = new Color(242, 246, 252);
    private static final Color TEXT = new Color(33, 43, 54);

    private final List<Patient> patients = new ArrayList<>();
    private final List<Medicine> medicines = new ArrayList<>();
    private final List<Appointment> appointments = new ArrayList<>();
    private final List<Bill> bills = new ArrayList<>();
    private final List<Prescription> prescriptions = new ArrayList<>();

    private final DefaultTableModel patientTableModel = new DefaultTableModel(new String[]{"ID", "Name", "Age", "Gender", "Phone", "Symptoms"}, 0);
    private final DefaultTableModel medicineTableModel = new DefaultTableModel(new String[]{"ID", "Name", "Price", "Stock"}, 0);
    private final DefaultTableModel appointmentTableModel = new DefaultTableModel(new String[]{"ID", "Patient", "Department", "Doctor", "Date", "Time", "Status"}, 0);
    private final DefaultTableModel billTableModel = new DefaultTableModel(new String[]{"ID", "Patient", "Consultation", "Medicines", "Total", "Date"}, 0);
    private final DefaultTableModel prescriptionTableModel = new DefaultTableModel(
            new String[]{"ID", "Patient", "Doctor", "Diagnosis", "Prescription", "Suggested Tests", "Follow-up", "Created"}, 0);

    private final JTextField patientNameField = new JTextField(16);
    private final JTextField patientAgeField = new JTextField(8);
    private final JTextField patientGenderField = new JTextField(10);
    private final JTextField patientPhoneField = new JTextField(12);
    private final JTextField patientSymptomsField = new JTextField(20);
    private final JTextField patientSearchField = new JTextField(18);

    private final JTextField medicineNameField = new JTextField(16);
    private final JTextField medicinePriceField = new JTextField(8);
    private final JTextField medicineQuantityField = new JTextField(8);

    private final JComboBox<String> appointmentPatientCombo = new JComboBox<>();
    private final JTextField appointmentDepartmentField = new JTextField(16);
    private final JTextField appointmentDoctorField = new JTextField(14);
    private final JTextField appointmentDateField = new JTextField(10);
    private final JTextField appointmentTimeField = new JTextField(8);

    private final JComboBox<String> billPatientCombo = new JComboBox<>();
    private final JComboBox<String> billMedicineCombo = new JComboBox<>();
    private final JTextField consultationFeeField = new JTextField(8);
    private final JTextField billQuantityField = new JTextField(8);
    private final JTextArea reportArea = new JTextArea(9, 34);
    private final JComboBox<String> prescriptionPatientCombo = new JComboBox<>();
    private final JTextField prescriptionDoctorField = new JTextField(16);
    private final JTextField diagnosisField = new JTextField(18);
    private final JTextArea prescriptionTextArea = new JTextArea(4, 20);
    private final JTextArea suggestedTestsArea = new JTextArea(3, 20);
    private final JTextField followUpDateField = new JTextField(12);

    public MediTrackGui() {
        super("MediTrack Java - Clinic Management System");
        loadData();
        buildInterface();
        refreshAllViews();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ignored) {
                // Default Swing look is still fine for this project.
            }
            new MediTrackGui().setVisible(true);
        });
    }

    private void buildInterface() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1180, 760));
        getContentPane().setBackground(SURFACE);
        setLocationRelativeTo(null);

        JPanel header = new GradientPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(22, 30, 20, 30));

        JLabel title = new JLabel("MediTrack  |  Clinical Workspace");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        header.add(title, BorderLayout.WEST);

        JLabel subtitle = new JLabel("Clinic records, appointments, stock, billing, and reports");
        subtitle.setForeground(new Color(220, 235, 248));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        header.add(subtitle, BorderLayout.SOUTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabs.setBackground(Color.WHITE);
        tabs.setForeground(TEXT);
        tabs.addTab("Patients", createPatientsPanel());
        tabs.addTab("Clinical Notes", createPrescriptionsPanel());
        tabs.addTab("Medicines", createMedicinesPanel());
        tabs.addTab("Appointments", createAppointmentsPanel());
        tabs.addTab("Billing", createBillingPanel());
        tabs.addTab("Reports", createReportsPanel());

        add(header, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
        styleComponents(this);
        pack();
    }

    private JPanel createPatientsPanel() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Register Patient"));
        addFormRow(form, 0, "Name", patientNameField);
        addFormRow(form, 1, "Age", patientAgeField);
        addFormRow(form, 2, "Gender", patientGenderField);
        addFormRow(form, 3, "Phone", patientPhoneField);
        addFormRow(form, 4, "Symptoms", patientSymptomsField);

        JButton saveButton = new JButton("Register Patient");
        saveButton.addActionListener(event -> registerPatient());
        addButtonRow(form, 5, saveButton);

        JPanel tablePanel = new JPanel(new BorderLayout(8, 8));
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search"));
        searchPanel.add(patientSearchField);
        JButton searchButton = new JButton("Find");
        searchButton.addActionListener(event -> refreshPatientTable(patientSearchField.getText()));
        JButton resetButton = new JButton("Show All");
        resetButton.addActionListener(event -> {
            patientSearchField.setText("");
            refreshPatientTable("");
        });
        searchPanel.add(searchButton);
        searchPanel.add(resetButton);

        JTable table = createTable(patientTableModel);
        tablePanel.add(searchPanel, BorderLayout.NORTH);
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);

        panel.add(form, BorderLayout.WEST);
        panel.add(tablePanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createMedicinesPanel() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Add Or Update Medicine"));
        addFormRow(form, 0, "Name", medicineNameField);
        addFormRow(form, 1, "Price", medicinePriceField);
        addFormRow(form, 2, "Quantity", medicineQuantityField);

        JButton saveButton = new JButton("Save Medicine");
        saveButton.addActionListener(event -> saveMedicine());
        addButtonRow(form, 3, saveButton);

        panel.add(form, BorderLayout.WEST);
        panel.add(new JScrollPane(createTable(medicineTableModel)), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createPrescriptionsPanel() {
        JPanel panel = new JPanel(new BorderLayout(16, 16));
        panel.setBackground(SURFACE);
        panel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        JPanel form = new ShadowPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(8, 8, 10, 12),
                BorderFactory.createTitledBorder("Doctor's Prescription & Diagnostic Plan")));
        addFormRow(form, 0, "Patient", prescriptionPatientCombo);
        addFormRow(form, 1, "Doctor", prescriptionDoctorField);
        addFormRow(form, 2, "Diagnosis", diagnosisField);

        prescriptionTextArea.setLineWrap(true);
        prescriptionTextArea.setWrapStyleWord(true);
        suggestedTestsArea.setLineWrap(true);
        suggestedTestsArea.setWrapStyleWord(true);
        addFormRow(form, 3, "Medicines / Instructions", new JScrollPane(prescriptionTextArea));
        addFormRow(form, 4, "Suggested Tests", new JScrollPane(suggestedTestsArea));
        addFormRow(form, 5, "Follow-up (YYYY-MM-DD)", followUpDateField);

        JButton saveButton = new JButton("Save Clinical Note");
        saveButton.addActionListener(event -> savePrescription());
        addButtonRow(form, 6, saveButton);

        JPanel history = new ShadowPanel(new BorderLayout(8, 8));
        history.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(8, 8, 10, 12),
                BorderFactory.createTitledBorder("Patient Clinical History")));
        history.add(new JScrollPane(createTable(prescriptionTableModel)), BorderLayout.CENTER);

        panel.add(form, BorderLayout.WEST);
        panel.add(history, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createAppointmentsPanel() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Schedule Appointment"));
        addFormRow(form, 0, "Patient", appointmentPatientCombo);
        addFormRow(form, 1, "Department", appointmentDepartmentField);
        addFormRow(form, 2, "Doctor", appointmentDoctorField);
        addFormRow(form, 3, "Date", appointmentDateField);
        addFormRow(form, 4, "Time", appointmentTimeField);

        JButton saveButton = new JButton("Schedule");
        saveButton.addActionListener(event -> scheduleAppointment());
        addButtonRow(form, 5, saveButton);

        panel.add(form, BorderLayout.WEST);
        panel.add(new JScrollPane(createTable(appointmentTableModel)), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createBillingPanel() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Create Bill"));
        addFormRow(form, 0, "Patient", billPatientCombo);
        addFormRow(form, 1, "Consultation Fee", consultationFeeField);
        addFormRow(form, 2, "Medicine", billMedicineCombo);
        addFormRow(form, 3, "Quantity", billQuantityField);

        JButton saveButton = new JButton("Create Bill");
        saveButton.addActionListener(event -> createBill());
        addButtonRow(form, 4, saveButton);

        panel.add(form, BorderLayout.WEST);
        panel.add(new JScrollPane(createTable(billTableModel)), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        reportArea.setEditable(false);
        reportArea.setFont(new Font("Consolas", Font.PLAIN, 18));
        reportArea.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        JButton refreshButton = new JButton("Refresh Report");
        refreshButton.addActionListener(event -> refreshReport());

        panel.add(new JScrollPane(reportArea), BorderLayout.CENTER);
        panel.add(refreshButton, BorderLayout.SOUTH);
        return panel;
    }

    private JTable createTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setRowHeight(34);
        table.setAutoCreateRowSorter(true);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(226, 232, 240));
        table.setSelectionBackground(new Color(219, 234, 254));
        table.setSelectionForeground(TEXT);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(NAVY);
        table.getTableHeader().setForeground(Color.WHITE);
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        table.setDefaultRenderer(Object.class, renderer);
        return table;
    }

    private void addFormRow(JPanel panel, int row, String label, java.awt.Component field) {
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = row;
        labelConstraints.anchor = GridBagConstraints.WEST;
        labelConstraints.insets = new Insets(7, 7, 7, 7);
        panel.add(new JLabel(label), labelConstraints);

        GridBagConstraints fieldConstraints = new GridBagConstraints();
        fieldConstraints.gridx = 1;
        fieldConstraints.gridy = row;
        fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        fieldConstraints.weightx = 1;
        fieldConstraints.insets = new Insets(7, 7, 7, 7);
        panel.add(field, fieldConstraints);
    }

    private void addButtonRow(JPanel panel, int row, JButton button) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(12, 7, 7, 7);
        panel.add(button, constraints);
    }

    private void registerPatient() {
        try {
            String id = IdGenerator.nextId("P", patients.size(), 1000);
            Patient patient = new Patient(
                    id,
                    requireText(patientNameField, "Patient name"),
                    Integer.parseInt(requireText(patientAgeField, "Age")),
                    requireText(patientGenderField, "Gender"),
                    requireText(patientPhoneField, "Phone"),
                    requireText(patientSymptomsField, "Symptoms"));

            patients.add(patient);
            FileStore.appendLine(PATIENTS_FILE, patient.toCsv());
            clear(patientNameField, patientAgeField, patientGenderField, patientPhoneField, patientSymptomsField);
            refreshAllViews();
            showMessage("Patient registered with ID " + id);
        } catch (NumberFormatException error) {
            showError("Age must be a valid number.");
        } catch (IllegalArgumentException error) {
            showError(error.getMessage());
        }
    }

    private void saveMedicine() {
        try {
            String name = requireText(medicineNameField, "Medicine name");
            double price = Double.parseDouble(requireText(medicinePriceField, "Price"));
            int quantity = Integer.parseInt(requireText(medicineQuantityField, "Quantity"));

            Optional<Medicine> existing = medicines.stream()
                    .filter(medicine -> medicine.getName().equalsIgnoreCase(name))
                    .findFirst();

            if (existing.isPresent()) {
                existing.get().addQuantity(quantity);
            } else {
                medicines.add(new Medicine(IdGenerator.nextId("M", medicines.size(), 100), name, price, quantity));
            }

            saveMedicines();
            clear(medicineNameField, medicinePriceField, medicineQuantityField);
            refreshAllViews();
            showMessage("Medicine stock saved.");
        } catch (NumberFormatException error) {
            showError("Price and quantity must be valid numbers.");
        } catch (IllegalArgumentException error) {
            showError(error.getMessage());
        }
    }

    private void scheduleAppointment() {
        try {
            String patientId = selectedId(appointmentPatientCombo);
            Appointment appointment = new Appointment(
                    IdGenerator.nextId("A", appointments.size(), 5000),
                    patientId,
                    requireText(appointmentDepartmentField, "Department"),
                    requireText(appointmentDoctorField, "Doctor"),
                    requireText(appointmentDateField, "Date"),
                    requireText(appointmentTimeField, "Time"),
                    "Scheduled");

            appointments.add(appointment);
            FileStore.appendLine(APPOINTMENTS_FILE, appointment.toCsv());
            clear(appointmentDepartmentField, appointmentDoctorField, appointmentDateField, appointmentTimeField);
            refreshAllViews();
            showMessage("Appointment scheduled.");
        } catch (IllegalArgumentException error) {
            showError(error.getMessage());
        }
    }

    private void createBill() {
        try {
            String patientId = selectedId(billPatientCombo);
            double consultationFee = Double.parseDouble(requireText(consultationFeeField, "Consultation fee"));
            int quantity = Integer.parseInt(requireText(billQuantityField, "Quantity"));
            Medicine medicine = findMedicineById(selectedId(billMedicineCombo))
                    .orElseThrow(() -> new IllegalArgumentException("Please select a medicine."));

            if (!medicine.reduceQuantity(quantity)) {
                showError("Not enough medicine stock available.");
                return;
            }

            double medicineAmount = medicine.getPrice() * quantity;
            double total = consultationFee + medicineAmount;
            Bill bill = new Bill(
                    IdGenerator.nextId("B", bills.size(), 9000),
                    patientId,
                    consultationFee,
                    medicineAmount,
                    total,
                    LocalDate.now().toString());

            bills.add(bill);
            saveMedicines();
            FileStore.appendLine(BILLS_FILE, bill.toCsv());
            clear(consultationFeeField, billQuantityField);
            refreshAllViews();
            showMessage(String.format("Bill created. Total amount: Rs.%.2f", total));
        } catch (NumberFormatException error) {
            showError("Fee and quantity must be valid numbers.");
        } catch (IllegalArgumentException error) {
            showError(error.getMessage());
        }
    }

    private void savePrescription() {
        try {
            String followUp = requireText(followUpDateField, "Follow-up date");
            LocalDate.parse(followUp);
            String medicinesText = prescriptionTextArea.getText().trim();
            if (medicinesText.isBlank()) throw new IllegalArgumentException("Prescription instructions are required.");
            String testsText = suggestedTestsArea.getText().trim();
            if (testsText.isBlank()) testsText = "No tests suggested";

            Prescription prescription = new Prescription(
                    IdGenerator.nextId("RX", prescriptions.size(), 10000),
                    selectedId(prescriptionPatientCombo),
                    requireText(prescriptionDoctorField, "Doctor name"),
                    requireText(diagnosisField, "Diagnosis"),
                    medicinesText, testsText, followUp, LocalDate.now().toString());
            prescriptions.add(prescription);
            FileStore.appendLine(PRESCRIPTIONS_FILE, prescription.toCsv());
            clear(prescriptionDoctorField, diagnosisField, followUpDateField);
            prescriptionTextArea.setText("");
            suggestedTestsArea.setText("");
            refreshAllViews();
            showMessage("Prescription and suggested tests saved to the patient's clinical history.");
        } catch (java.time.format.DateTimeParseException error) {
            showError("Follow-up date must use YYYY-MM-DD format.");
        } catch (IllegalArgumentException error) {
            showError(error.getMessage());
        }
    }

    private void loadData() {
        for (String line : FileStore.readLines(PATIENTS_FILE)) {
            if (!line.isBlank()) {
                patients.add(Patient.fromCsv(line));
            }
        }
        for (String line : FileStore.readLines(MEDICINES_FILE)) {
            if (!line.isBlank()) {
                medicines.add(Medicine.fromCsv(line));
            }
        }
        for (String line : FileStore.readLines(APPOINTMENTS_FILE)) {
            if (!line.isBlank()) {
                appointments.add(Appointment.fromCsv(line));
            }
        }
        for (String line : FileStore.readLines(BILLS_FILE)) {
            if (!line.isBlank()) {
                bills.add(Bill.fromCsv(line));
            }
        }
        for (String line : FileStore.readLines(PRESCRIPTIONS_FILE)) {
            if (!line.isBlank()) {
                try {
                    prescriptions.add(Prescription.fromCsv(line));
                } catch (IllegalArgumentException error) {
                    System.out.println("Skipped invalid prescription record: " + error.getMessage());
                }
            }
        }
    }

    private void refreshAllViews() {
        refreshPatientTable(patientSearchField.getText());
        refreshMedicineTable();
        refreshAppointmentTable();
        refreshBillTable();
        refreshPrescriptionTable();
        refreshCombos();
        refreshReport();
    }

    private void refreshPatientTable(String keyword) {
        patientTableModel.setRowCount(0);
        String search = keyword == null ? "" : keyword.trim().toLowerCase();
        for (Patient patient : patients) {
            if (search.isBlank()
                    || patient.getId().toLowerCase().contains(search)
                    || patient.getName().toLowerCase().contains(search)
                    || patient.getPhone().contains(search)) {
                patientTableModel.addRow(new Object[]{
                        patient.getId(), patient.getName(), patient.getAge(), patient.getGender(),
                        patient.getPhone(), patient.getSymptoms()
                });
            }
        }
    }

    private void refreshMedicineTable() {
        medicineTableModel.setRowCount(0);
        for (Medicine medicine : medicines) {
            medicineTableModel.addRow(new Object[]{
                    medicine.getId(), medicine.getName(), String.format("Rs.%.2f", medicine.getPrice()), medicine.getQuantity()
            });
        }
    }

    private void refreshAppointmentTable() {
        appointmentTableModel.setRowCount(0);
        for (Appointment appointment : appointments) {
            appointmentTableModel.addRow(new Object[]{
                    appointment.getId(), appointment.getPatientId(), appointment.getDepartment(), appointment.getDoctor(),
                    appointment.getDate(), appointment.getTime(), appointment.getStatus()
            });
        }
    }

    private void refreshBillTable() {
        billTableModel.setRowCount(0);
        for (Bill bill : bills) {
            billTableModel.addRow(new Object[]{
                    bill.getId(), bill.getPatientId(), String.format("Rs.%.2f", bill.getConsultationFee()),
                    String.format("Rs.%.2f", bill.getMedicineAmount()), String.format("Rs.%.2f", bill.getTotalAmount()),
                    bill.getDate()
            });
        }
    }

    private void refreshPrescriptionTable() {
        prescriptionTableModel.setRowCount(0);
        for (Prescription prescription : prescriptions) {
            prescriptionTableModel.addRow(new Object[]{prescription.getId(), patientDisplayName(prescription.getPatientId()),
                    prescription.getDoctor(), prescription.getDiagnosis(), prescription.getMedicines(),
                    prescription.getSuggestedTests(), prescription.getFollowUpDate(), prescription.getCreatedDate()});
        }
    }

    private void refreshCombos() {
        appointmentPatientCombo.removeAllItems();
        billPatientCombo.removeAllItems();
        prescriptionPatientCombo.removeAllItems();
        for (Patient patient : patients) {
            String label = patient.getId() + " - " + patient.getName();
            appointmentPatientCombo.addItem(label);
            billPatientCombo.addItem(label);
            prescriptionPatientCombo.addItem(label);
        }

        billMedicineCombo.removeAllItems();
        for (Medicine medicine : medicines) {
            billMedicineCombo.addItem(medicine.getId() + " - " + medicine.getName() + " (" + medicine.getQuantity() + " left)");
        }
    }

    private void refreshReport() {
        double revenue = 0;
        for (Bill bill : bills) {
            revenue += bill.getTotalAmount();
        }

        int lowStock = 0;
        for (Medicine medicine : medicines) {
            if (medicine.getQuantity() < 20) {
                lowStock++;
            }
        }

        reportArea.setText(String.format("""
                ========== Clinic Report ==========

                Total patients      : %d
                Total appointments  : %d
                Total bills         : %d
                Clinical notes      : %d
                Total revenue       : Rs.%.2f
                Low-stock medicines : %d

                Data is saved permanently in CSV files.
                """, patients.size(), appointments.size(), bills.size(), prescriptions.size(), revenue, lowStock));
    }

    private String patientDisplayName(String patientId) {
        return patients.stream().filter(p -> p.getId().equals(patientId))
                .map(p -> p.getId() + " - " + p.getName()).findFirst().orElse(patientId);
    }

    private void saveMedicines() {
        List<String> lines = new ArrayList<>();
        for (Medicine medicine : medicines) {
            lines.add(medicine.toCsv());
        }
        FileStore.writeLines(MEDICINES_FILE, lines);
    }

    private Optional<Medicine> findMedicineById(String id) {
        return medicines.stream()
                .filter(medicine -> medicine.getId().equalsIgnoreCase(id))
                .findFirst();
    }

    private String selectedId(JComboBox<String> comboBox) {
        Object selected = comboBox.getSelectedItem();
        if (selected == null) {
            throw new IllegalArgumentException("Please select a record first.");
        }
        return selected.toString().split(" - ", 2)[0];
    }

    private String requireText(JTextField field, String label) {
        String value = field.getText().trim();
        if (value.isBlank()) {
            throw new IllegalArgumentException(label + " is required.");
        }
        return value;
    }

    private void clear(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "MediTrack", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Input Error", JOptionPane.ERROR_MESSAGE);
    }

    private void styleComponents(java.awt.Container container) {
        for (java.awt.Component component : container.getComponents()) {
            if (component instanceof JButton button) {
                button.setBackground(BLUE);
                button.setForeground(Color.WHITE);
                button.setFont(new Font("Segoe UI", Font.BOLD, 13));
                button.setFocusPainted(false);
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(24, 80, 200), 1, true),
                        BorderFactory.createEmptyBorder(9, 15, 9, 15)));
                button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            } else if (component instanceof JTextField field) {
                field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(203, 213, 225), 1, true),
                        BorderFactory.createEmptyBorder(7, 9, 7, 9)));
            } else if (component instanceof JComboBox<?> combo) {
                combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                combo.setBackground(Color.WHITE);
            } else if (component instanceof JLabel label && label.getForeground().equals(Color.BLACK)) {
                label.setForeground(TEXT);
                label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            } else if (component instanceof JPanel panel && !(panel instanceof GradientPanel) && !(panel instanceof ShadowPanel)) {
                panel.setBackground(SURFACE);
            }
            if (component instanceof java.awt.Container child) styleComponents(child);
        }
    }

    private static class GradientPanel extends JPanel {
        GradientPanel(java.awt.LayoutManager layout) { super(layout); setOpaque(false); }

        @Override protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g.setPaint(new GradientPaint(0, 0, NAVY, getWidth(), getHeight(), new Color(25, 104, 171)));
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(new Color(CYAN.getRed(), CYAN.getGreen(), CYAN.getBlue(), 45));
            g.fillOval(getWidth() - 220, -100, 310, 240);
            g.dispose();
            super.paintComponent(graphics);
        }
    }

    private static class ShadowPanel extends JPanel {
        ShadowPanel(java.awt.LayoutManager layout) {
            super(layout);
            setOpaque(false);
        }

        @Override protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(new Color(15, 23, 42, 25));
            g.fillRoundRect(6, 7, getWidth() - 9, getHeight() - 10, 20, 20);
            g.setColor(Color.WHITE);
            g.fillRoundRect(1, 1, getWidth() - 9, getHeight() - 10, 20, 20);
            g.dispose();
            super.paintComponent(graphics);
        }
    }
}
