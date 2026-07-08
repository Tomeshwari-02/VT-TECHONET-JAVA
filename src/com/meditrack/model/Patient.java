package com.meditrack.model;

public class Patient {
    private final String id;
    private final String name;
    private final int age;
    private final String gender;
    private final String phone;
    private final String symptoms;

    public Patient(String id, String name, int age, String gender, String phone, String symptoms) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
        this.symptoms = symptoms;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public String toCsv() {
        return String.join(",", id, name, String.valueOf(age), gender, phone, symptoms);
    }

    public static Patient fromCsv(String line) {
        String[] values = line.split(",", -1);
        return new Patient(values[0], values[1], Integer.parseInt(values[2]), values[3], values[4], values[5]);
    }
}
