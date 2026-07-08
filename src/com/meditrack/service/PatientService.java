package com.meditrack.service;

import com.meditrack.model.Patient;
import com.meditrack.util.FileStore;
import com.meditrack.util.IdGenerator;
import com.meditrack.util.InputUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PatientService {
    private static final String FILE_NAME = "patients.csv";
    private final List<Patient> patients = new ArrayList<>();

    public PatientService() {
        loadPatients();
    }

    public void registerPatient(InputUtil input) {
        String id = IdGenerator.nextId("P", patients.size(), 1000);
        String name = input.readText("Patient name: ");
        int age = input.readInt("Age: ");
        String gender = input.readText("Gender: ");
        String phone = input.readText("Phone: ");
        String symptoms = input.readText("Symptoms: ");

        Patient patient = new Patient(id, name, age, gender, phone, symptoms);
        patients.add(patient);
        FileStore.appendLine(FILE_NAME, patient.toCsv());

        System.out.println("Patient registered successfully. Patient ID: " + id);
    }

    public void showAllPatients() {
        if (patients.isEmpty()) {
            System.out.println("No patients found.");
            return;
        }

        System.out.printf("%-8s %-22s %-5s %-10s %-14s %-30s%n", "ID", "Name", "Age", "Gender", "Phone", "Symptoms");
        for (Patient patient : patients) {
            System.out.printf("%-8s %-22s %-5d %-10s %-14s %-30s%n",
                    patient.getId(), patient.getName(), patient.getAge(), patient.getGender(),
                    patient.getPhone(), patient.getSymptoms());
        }
    }

    public void searchPatient(InputUtil input) {
        String keyword = input.readText("Enter patient ID, name, or phone: ").toLowerCase();
        boolean found = false;

        for (Patient patient : patients) {
            if (patient.getId().toLowerCase().contains(keyword)
                    || patient.getName().toLowerCase().contains(keyword)
                    || patient.getPhone().contains(keyword)) {
                System.out.println(patient.getId() + " | " + patient.getName() + " | " + patient.getPhone() + " | " + patient.getSymptoms());
                found = true;
            }
        }

        if (!found) {
            System.out.println("No matching patient found.");
        }
    }

    public Optional<Patient> findById(String id) {
        return patients.stream()
                .filter(patient -> patient.getId().equalsIgnoreCase(id))
                .findFirst();
    }

    public int count() {
        return patients.size();
    }

    private void loadPatients() {
        for (String line : FileStore.readLines(FILE_NAME)) {
            if (!line.isBlank()) {
                patients.add(Patient.fromCsv(line));
            }
        }
    }
}
