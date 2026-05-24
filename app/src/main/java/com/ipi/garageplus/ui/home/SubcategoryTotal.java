package com.ipi.garageplus.ui.home;

public class SubcategoryTotal {
    private final String name;
    private final double total;

    public SubcategoryTotal(String name, double total) {
        this.name = name;
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public double getTotal() {
        return total;
    }
}