package com.meditrack.model;

public class Medicine {
    private final String id;
    private final String name;
    private final double price;
    private int quantity;

    public Medicine(String id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void addQuantity(int amount) {
        quantity += amount;
    }

    public boolean reduceQuantity(int amount) {
        if (amount > quantity) {
            return false;
        }
        quantity -= amount;
        return true;
    }

    public String toCsv() {
        return String.join(",", id, name, String.format("%.2f", price), String.valueOf(quantity));
    }

    public static Medicine fromCsv(String line) {
        String[] values = line.split(",", -1);
        return new Medicine(values[0], values[1], Double.parseDouble(values[2]), Integer.parseInt(values[3]));
    }
}
