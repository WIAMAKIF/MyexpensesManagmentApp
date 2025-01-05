package com.example.myapplicationmanageexpenses;

// Category.java
public class Category {
    private String name;
    private String description;
    private  String id;

    // Default constructor
    public Category() {
    }

    // Constructor
    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id=id;
    }
}

