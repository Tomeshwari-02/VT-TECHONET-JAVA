package com.meditrack.model;

public class Bill {
    private final String id;
    private final String patientId;
    private final double consultationFee;
    private final double medicineAmount;
    private final double totalAmount;
    private final String date;

    public Bill(String id, String patientId, double consultationFee, double medicineAmount, double totalAmount, String date) {
        this.id = id;
        this.patientId = patientId;
        this.consultationFee = consultationFee;
        this.medicineAmount = medicineAmount;
        this.totalAmount = totalAmount;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getPatientId() {
        return patientId;
    }

    public double getConsultationFee() {
        return consultationFee;
    }

    public double getMedicineAmount() {
        return medicineAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getDate() {
        return date;
    }

    public String toCsv() {
        return String.join(",",
                id,
                patientId,
                String.format("%.2f", consultationFee),
                String.format("%.2f", medicineAmount),
                String.format("%.2f", totalAmount),
                date);
    }

    public static Bill fromCsv(String line) {
        String[] values = line.split(",", -1);
        return new Bill(
                values[0],
                values[1],
                Double.parseDouble(values[2]),
                Double.parseDouble(values[3]),
                Double.parseDouble(values[4]),
                values[5]);
    }
}
