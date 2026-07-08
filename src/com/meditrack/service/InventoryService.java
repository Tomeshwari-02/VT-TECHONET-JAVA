package com.meditrack.service;

import com.meditrack.model.Medicine;
import com.meditrack.util.FileStore;
import com.meditrack.util.IdGenerator;
import com.meditrack.util.InputUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InventoryService {
    private static final String FILE_NAME = "medicines.csv";
    private final List<Medicine> medicines = new ArrayList<>();

    public InventoryService() {
        loadMedicines();
    }

    public void addMedicine(InputUtil input) {
        String name = input.readText("Medicine name: ");
        Optional<Medicine> existing = medicines.stream()
                .filter(medicine -> medicine.getName().equalsIgnoreCase(name))
                .findFirst();

        if (existing.isPresent()) {
            int quantity = input.readInt("Quantity to add: ");
            existing.get().addQuantity(quantity);
            saveAll();
            System.out.println("Existing medicine stock updated.");
            return;
        }

        String id = IdGenerator.nextId("M", medicines.size(), 100);
        double price = input.readDouble("Price per unit: ");
        int quantity = input.readInt("Quantity: ");
        Medicine medicine = new Medicine(id, name, price, quantity);
        medicines.add(medicine);
        FileStore.appendLine(FILE_NAME, medicine.toCsv());
        System.out.println("Medicine added successfully. Medicine ID: " + id);
    }

    public void showMedicines() {
        if (medicines.isEmpty()) {
            System.out.println("No medicines found.");
            return;
        }

        System.out.printf("%-8s %-25s %-10s %-10s%n", "ID", "Name", "Price", "Stock");
        for (Medicine medicine : medicines) {
            System.out.printf("%-8s %-25s Rs.%-7.2f %-10d%n",
                    medicine.getId(), medicine.getName(), medicine.getPrice(), medicine.getQuantity());
        }
    }

    public Optional<Medicine> findById(String id) {
        return medicines.stream()
                .filter(medicine -> medicine.getId().equalsIgnoreCase(id))
                .findFirst();
    }

    public boolean sellMedicine(String medicineId, int quantity) {
        Optional<Medicine> medicine = findById(medicineId);
        if (medicine.isEmpty()) {
            System.out.println("Medicine not found.");
            return false;
        }
        if (!medicine.get().reduceQuantity(quantity)) {
            System.out.println("Not enough stock available.");
            return false;
        }
        saveAll();
        return true;
    }

    public int countLowStock() {
        int count = 0;
        for (Medicine medicine : medicines) {
            if (medicine.getQuantity() < 20) {
                count++;
            }
        }
        return count;
    }

    private void loadMedicines() {
        for (String line : FileStore.readLines(FILE_NAME)) {
            if (!line.isBlank()) {
                medicines.add(Medicine.fromCsv(line));
            }
        }
    }

    private void saveAll() {
        List<String> lines = new ArrayList<>();
        for (Medicine medicine : medicines) {
            lines.add(medicine.toCsv());
        }
        FileStore.writeLines(FILE_NAME, lines);
    }
}
