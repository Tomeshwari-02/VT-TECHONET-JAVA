package com.meditrack.model;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Prescription {
    private final String id, patientId, doctor, diagnosis, medicines, suggestedTests, followUpDate, createdDate;

    public Prescription(String id, String patientId, String doctor, String diagnosis,
                        String medicines, String suggestedTests, String followUpDate, String createdDate) {
        this.id = id; this.patientId = patientId; this.doctor = doctor; this.diagnosis = diagnosis;
        this.medicines = medicines; this.suggestedTests = suggestedTests;
        this.followUpDate = followUpDate; this.createdDate = createdDate;
    }

    public String getId() { return id; }
    public String getPatientId() { return patientId; }
    public String getDoctor() { return doctor; }
    public String getDiagnosis() { return diagnosis; }
    public String getMedicines() { return medicines; }
    public String getSuggestedTests() { return suggestedTests; }
    public String getFollowUpDate() { return followUpDate; }
    public String getCreatedDate() { return createdDate; }

    public String toCsv() {
        return String.join(",", id, patientId, encode(doctor), encode(diagnosis), encode(medicines),
                encode(suggestedTests), encode(followUpDate), createdDate);
    }

    public static Prescription fromCsv(String line) {
        String[] v = line.split(",", -1);
        if (v.length < 8) throw new IllegalArgumentException("Invalid prescription record");
        return new Prescription(v[0], v[1], decode(v[2]), decode(v[3]), decode(v[4]), decode(v[5]), decode(v[6]), v[7]);
    }

    private static String encode(String value) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private static String decode(String value) {
        return value.isEmpty() ? "" : new String(Base64.getUrlDecoder().decode(value), StandardCharsets.UTF_8);
    }
}
