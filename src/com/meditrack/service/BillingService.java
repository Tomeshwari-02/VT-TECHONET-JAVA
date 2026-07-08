package com.meditrack.service;

import com.meditrack.model.Bill;
import com.meditrack.model.Medicine;
import com.meditrack.util.FileStore;
import com.meditrack.util.IdGenerator;
import com.meditrack.util.InputUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BillingService {
    private static final String FILE_NAME = "bills.csv";
    private final PatientService patientService;
    private final InventoryService inventoryService;
    private final List<Bill> bills = new ArrayList<>();

    public BillingService(PatientService patientService, InventoryService inventoryService) {
        this.patientService = patientService;
        this.inventoryService = inventoryService;
        loadBills();
    }

    public void createBill(InputUtil input) {
        String patientId = input.readText("Patient ID: ");
        if (patientService.findById(patientId).isEmpty()) {
            System.out.println("Patient not found.");
            return;
        }

        double consultationFee = input.readDouble("Consultation fee: ");
        double medicineAmount = 0;
        boolean addingMedicines = true;

        while (addingMedicines) {
            String medicineId = input.readText("Medicine ID, or press Enter to finish: ");
            if (medicineId.isBlank()) {
                addingMedicines = false;
                continue;
            }

            Optional<Medicine> medicine = inventoryService.findById(medicineId);
            if (medicine.isEmpty()) {
                System.out.println("Medicine not found.");
                continue;
            }

            int quantity = input.readInt("Quantity: ");
            if (inventoryService.sellMedicine(medicineId, quantity)) {
                medicineAmount += medicine.get().getPrice() * quantity;
            }
        }

        String id = IdGenerator.nextId("B", bills.size(), 9000);
        double totalAmount = consultationFee + medicineAmount;
        Bill bill = new Bill(id, patientId, consultationFee, medicineAmount, totalAmount, LocalDate.now().toString());
        bills.add(bill);
        FileStore.appendLine(FILE_NAME, bill.toCsv());

        System.out.println("Bill created successfully.");
        System.out.printf("Bill ID: %s | Total amount: Rs.%.2f%n", id, totalAmount);
    }

    public void showBills() {
        if (bills.isEmpty()) {
            System.out.println("No bills found.");
            return;
        }

        System.out.printf("%-8s %-10s %-14s %-14s %-12s %-12s%n",
                "ID", "Patient", "Consultation", "Medicines", "Total", "Date");
        for (Bill bill : bills) {
            System.out.printf("%-8s %-10s Rs.%-11.2f Rs.%-11.2f Rs.%-9.2f %-12s%n",
                    bill.getId(), bill.getPatientId(), bill.getConsultationFee(),
                    bill.getMedicineAmount(), bill.getTotalAmount(), bill.getDate());
        }
    }

    public double totalRevenue() {
        double total = 0;
        for (Bill bill : bills) {
            total += bill.getTotalAmount();
        }
        return total;
    }

    public int count() {
        return bills.size();
    }

    private void loadBills() {
        for (String line : FileStore.readLines(FILE_NAME)) {
            if (!line.isBlank()) {
                bills.add(Bill.fromCsv(line));
            }
        }
    }
}
