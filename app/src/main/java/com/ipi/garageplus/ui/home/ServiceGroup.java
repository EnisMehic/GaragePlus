package com.ipi.garageplus.ui.home;

import java.util.List;

public class ServiceGroup {
    private final String category;
    private final double total;
    private final List<SubcategoryTotal> items;

    public ServiceGroup(String category, double total, List<SubcategoryTotal> items) {
        this.category = category;
        this.total = total;
        this.items = items;
    }

    public String getCategory() {
        return category;
    }

    public double getTotal() {
        return total;
    }

    public List<SubcategoryTotal> getItems() {
        return items;
    }
}