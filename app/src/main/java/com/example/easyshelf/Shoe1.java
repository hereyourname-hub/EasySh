package com.example.easyshelf;

public class Shoe1 {
    private String shoeNumber;
    private String shoeName;

    public Shoe1() {
        // Пустой конструктор для Firebase
    }

    public Shoe1(String shoeNumber, String shoeName) {
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
