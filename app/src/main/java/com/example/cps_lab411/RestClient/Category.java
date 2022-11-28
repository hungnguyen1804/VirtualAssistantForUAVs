package com.example.cps_lab411.RestClient;

public class Category {
    private String name;
    private String selected;

    public Category(String name) {
        this.name = name;
    }

    public Category(String name, String selected) {
        this.name = name;
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }
}