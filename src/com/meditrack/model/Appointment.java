package com.meditrack.model;

public class Appointment {
    private final String id;
    private final String patientId;
    private final String department;
    private final String doctor;
    private final String date;
    private final String time;
    private final String status;

    public Appointment(String id, String patientId, String department, String doctor, String date, String time, String status) {
        this.id = id;
        this.patientId = patientId;
        this.department = department;
        this.doctor = doctor;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getDepartment() {
        return department;
    }

    public String getDoctor() {
        return doctor;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }

    public String toCsv() {
        return String.join(",", id, patientId, department, doctor, date, time, status);
    }

    public static Appointment fromCsv(String line) {
        String[] values = line.split(",", -1);
        return new Appointment(values[0], values[1], values[2], values[3], values[4], values[5], values[6]);
    }
}
