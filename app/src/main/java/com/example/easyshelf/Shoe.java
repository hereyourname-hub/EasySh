package com.example.easyshelf;

public class Shoe {
    private String shoeNumber;
    private String shoeName;

    public Shoe() {
        // Пустой конструктор для Firebase
    }

    public Shoe(String shoeNumber, String shoeName) {
        this.shoeNumber = shoeNumber;
        this.shoeName = shoeName;
    }

    public String getShoeNumber() {
        return shoeNumber;
    }

    public String getShoeName() {
        return shoeName;
    }
}
