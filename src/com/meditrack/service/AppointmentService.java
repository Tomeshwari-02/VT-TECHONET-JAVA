package com.meditrack.service;

import com.meditrack.model.Appointment;
import com.meditrack.util.FileStore;
import com.meditrack.util.IdGenerator;
import com.meditrack.util.InputUtil;

import java.util.ArrayList;
import java.util.List;

public class AppointmentService {
    private static final String FILE_NAME = "appointments.csv";
    private final PatientService patientService;
    private final List<Appointment> appointments = new ArrayList<>();

    public AppointmentService(PatientService patientService) {
        this.patientService = patientService;
        loadAppointments();
    }

    public void scheduleAppointment(InputUtil input) {
        String patientId = input.readText("Patient ID: ");
        if (patientService.findById(patientId).isEmpty()) {
            System.out.println("Patient not found. Register the patient first.");
            return;
        }

        String id = IdGenerator.nextId("A", appointments.size(), 5000);
        String department = input.readText("Department: ");
        String doctor = input.readText("Doctor name: ");
        String date = input.readText("Date (YYYY-MM-DD): ");
        String time = input.readText("Time (HH:MM): ");

        Appointment appointment = new Appointment(id, patientId, department, doctor, date, time, "Scheduled");
        appointments.add(appointment);
        FileStore.appendLine(FILE_NAME, appointment.toCsv());

        System.out.println("Appointment scheduled successfully. Appointment ID: " + id);
    }

    public void showAppointments() {
        if (appointments.isEmpty()) {
            System.out.println("No appointments found.");
            return;
        }

        System.out.printf("%-8s %-10s %-20s %-15s %-12s %-8s %-12s%n",
                "ID", "Patient", "Department", "Doctor", "Date", "Time", "Status");
        for (Appointment appointment : appointments) {
            System.out.printf("%-8s %-10s %-20s %-15s %-12s %-8s %-12s%n",
                    appointment.getId(), appointment.getPatientId(), appointment.getDepartment(),
                    appointment.getDoctor(), appointment.getDate(), appointment.getTime(), appointment.getStatus());
        }
    }

    public int count() {
        return appointments.size();
    }

    private void loadAppointments() {
        for (String line : FileStore.readLines(FILE_NAME)) {
            if (!line.isBlank()) {
                appointments.add(Appointment.fromCsv(line));
            }
        }
    }
}
