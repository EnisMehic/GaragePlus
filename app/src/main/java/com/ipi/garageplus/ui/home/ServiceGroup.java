package com.ipi.garageplus.ui.home;

import java.util.List;

public class ServiceGroup {
    private final String category;
    private final List<SubcategoryTotal> items;

    public ServiceGroup(String category, List<SubcategoryTotal> items) {
        this.category = category;
        this.items = items;
    }

    public String getCategory() {
        return category;
    }

    public List<SubcategoryTotal> getItems() {
        return items;
    }
}